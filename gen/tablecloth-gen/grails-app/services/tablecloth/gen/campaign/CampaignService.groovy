package tablecloth.gen.campaign

import grails.gorm.transactions.Transactional
import tablecloth.gen.DatabaseService
import tablecloth.gen.commands.AddCampaignCommand
import tablecloth.gen.exceptions.TableclothAccessException
import tablecloth.gen.exceptions.TableclothDomainException
import tablecloth.gen.messages.MessageService
import tablecloth.gen.model.domain.campaign.Campaign
import tablecloth.gen.model.domain.campaign.Participant
import tablecloth.gen.model.domain.users.User
import tablecloth.gen.modelData.CampaignPermission
import tablecloth.gen.modelData.ParticipantStatus
import tablecloth.gen.security.SecurityService
import tablecloth.gen.viewmodel.CampaignViewmodel

@Transactional
class CampaignService {

    DatabaseService databaseService
    SecurityService securityService
    MessageService messageService

    List<CampaignViewmodel> getCampaigns() {
        User user = securityService.user
        List<Campaign> campaigns = user.getCampaigns().toList() ?: []
        return campaigns.collect {
            CampaignViewmodel.fromDomain(it)
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
        camp.owner = user
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

    void removeCampaign(long id) {
        Campaign camp = fetchCampaign(id)
        assertOwnerIsCurrentUser(camp, "Only owner may remove their campaign.")
        camp.owner.removeFromCampaigns(camp)
        databaseService.save(camp.owner)
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
        if (camp.owner != securityService.user) {
            throw new TableclothAccessException(msg)
        }
    }
}