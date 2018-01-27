package tablecloth.plaza.campaign

import grails.compiler.GrailsCompileStatic
import grails.gorm.transactions.Transactional
import tablecloth.DatabaseService
import tablecloth.commands.AddCampaignCommand
import tablecloth.exceptions.TableclothAccessException
import tablecloth.exceptions.TableclothDomainException
import tablecloth.plaza.messages.MessageService
import tablecloth.model.domain.campaign.Campaign
import tablecloth.model.domain.campaign.Participant
import tablecloth.model.domain.users.User
import tablecloth.modelData.CampaignPermission
import tablecloth.modelData.ParticipantStatus
import tablecloth.security.SecurityService
import tablecloth.viewmodel.CampaignViewmodel

@Transactional
@GrailsCompileStatic
class CampaignService {

    DatabaseService databaseService
    SecurityService securityService
    MessageService messageService

    List<CampaignViewmodel> getCampaigns() {
        User user = securityService.user
        Set<Campaign> campaigns = (user.getCampaigns() ?: [].toSet()) as Set<Campaign>
        return campaigns.collect { Campaign c ->
            CampaignViewmodel.fromDomain(c)
        }
    }

    void newCampaign(AddCampaignCommand cmd) {
        Campaign camp = new Campaign(
            name: cmd.name,
            description: cmd.description,
            defaultPermissions: CampaignPermission.defaultPermissions(),
            participants: [].toSet(),
            party: [].toSet(),
        )
        User user = securityService.user
        Participant participant = new Participant(
            user: user,
            status: ParticipantStatus.OWNER,
            permissions: CampaignPermission.masterPermissions(),
        )
        camp.participants.add(participant)
        user.campaigns.add(camp)
        databaseService.save(user)
    }

    void addPlayerToCampaign(long id, String username) {
        Campaign camp = fetchCampaign(id)
        User user = User.findByUsername(username)
        if (!user) {
            throw new TableclothDomainException("User with username $username not found")
        }
        assertOwnerIsCurrentUser(camp, "Only owner can add players to campaign!")
        if (!camp.defaultPermissions) {
            log.error("No permissions assigned to campaign $camp.name")
        }
        Participant participant = new Participant(
            user: user,
            status: ParticipantStatus.PENDING_INVITATION,
            permissions: camp.defaultPermissions.collect()
        )
        camp.participants.add(participant)
        messageService.sendInvitationToUser(camp, user)
        databaseService.save(camp, user)
    }

    void removeParticipant(long id, String username) {
        Campaign camp = fetchCampaign(id)
        assertOwnerIsCurrentUser(camp, "Only owner can add players to campaign!")
        Participant participant = camp.participants.find { it.user.username == username }
        if (!participant) {
            throw new TableclothDomainException("User with $username is not participating in campaign with id $id")
        }
        camp.removeFromParticipants(participant)
        messageService.deleteAllAssociatedInvitations(id)
        databaseService.save(camp)
    }

    void removeCampaign(long id) {
        Campaign camp = fetchCampaign(id)
        assertOwnerIsCurrentUser(camp, "Only owner may remove their campaign.")
        messageService.deleteAllAssociatedInvitations(id)
        List<User> participants = camp.participants*.user
        participants.each { User user ->
            user.removeFromCampaigns(camp)
        }
        participants.each { User user ->
            databaseService.save(user)
        }
        databaseService.delete(camp)
    }

    void handleInvitation(boolean accepted, long id, User user) {
        Campaign camp = fetchCampaign(id)
        Participant participant = camp.participants.find { it.user == user }
        if (!participant) {
            throw new TableclothDomainException("User with $user.username is not participating in campaign with id $id")
        }
        if (accepted) {
            user.addToCampaigns(camp)
            participant.status = ParticipantStatus.ACCEPTED_INVITATION
        } else {
            camp.participants.remove(participant)
        }
        databaseService.save(user, camp)
    }

    private Campaign fetchCampaign(long id) {
        Campaign camp = Campaign.get(id)
        if (!camp) {
            throw new TableclothDomainException("Campaign with id $id not found")
        }
        return camp
    }

    private void assertOwnerIsCurrentUser(Campaign camp, String msg) {
        User current = securityService.user
        if (!camp.participants.find {it.user.username == current.username && it.owner}) {
            throw new TableclothAccessException(msg)
        }
    }
}