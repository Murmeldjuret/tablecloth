package tablecloth.plaza.campaign

import grails.compiler.GrailsCompileStatic
import grails.gorm.transactions.Transactional
import tablecloth.DatabaseService
import tablecloth.commands.AddCampaignCommand
import tablecloth.exceptions.TableclothAccessException
import tablecloth.exceptions.TableclothDomainReferenceException
import tablecloth.model.domain.campaign.Campaign
import tablecloth.model.domain.campaign.Participant
import tablecloth.model.domain.users.User
import tablecloth.model.ParticipantStatus
import tablecloth.plaza.messages.MessageService
import tablecloth.security.SecurityService
import tablecloth.viewmodel.CampaignViewmodel

@Transactional
@GrailsCompileStatic
class CampaignService {

    DatabaseService databaseService
    SecurityService securityService
    MessageService messageService

    List<CampaignViewmodel> getCampaigns() {
        User user = securityService.loggedInUser
        Set<Campaign> campaigns = user.campaigns ?: [].toSet() as Set<Campaign>
        return campaigns.collect { Campaign camp ->
            CampaignViewmodel.fromDomain(camp)
        }
    }

    void newCampaign(AddCampaignCommand cmd) {
        Campaign camp = Campaign.fromAddCommand(cmd)
        User user = securityService.loggedInUser
        Participant participant = Participant.fromMasterUser(user)
        camp.addToParticipants(participant)
        user.addToCampaigns(camp)
        databaseService.save(user)
    }

    void addPlayerToCampaign(long campId, String username) {
        Campaign camp = getCampaignAsserted(campId)
        User user = User.getUserByNameAssertExists(username)
        assertOwnerIsCurrentUser(camp)
        camp.addUserAsParticipant(user)
        messageService.sendInvitationToUser(camp, user)
        databaseService.save(camp, user)
    }

    void removeParticipant(long campId, String username) {
        Campaign camp = getCampaignAsserted(campId)
        assertOwnerIsCurrentUser(camp)
        camp.removeParticipantByNameAsserted(username)
        messageService.deleteAllInvitationsToCampaign(campId)
        databaseService.save(camp)
    }

    void removeCampaign(long campId) {
        Campaign camp = getCampaignAsserted(campId)
        assertOwnerIsCurrentUser(camp)
        messageService.deleteAllInvitationsToCampaign(campId)
        removeAllParticipantsFromCampaign(camp)
        databaseService.delete(camp)
    }

    void acceptInvitation(long campId, User user) {
        Campaign camp = getCampaignAsserted(campId)
        Participant participant = camp.getParticipantByUsernameAsserted(user.username)
        user.addToCampaigns(camp)
        participant.status = ParticipantStatus.ACCEPTED_INVITATION
        databaseService.save(user, camp)
    }

    void rejectInvitation(long campId, User user) {
        Campaign camp = getCampaignAsserted(campId)
        Participant participant = camp.getParticipantByUsernameAsserted(user.username)
        camp.removeFromParticipants(participant)
        databaseService.save(user, camp)
    }

    private void removeAllParticipantsFromCampaign(Campaign camp) {
        List<User> users = camp.participants*.user
        users.each { User user ->
            user.removeFromCampaigns(camp)
        }
        users.each { User user ->
            databaseService.save(user)
        }
    }

    private static Campaign getCampaignAsserted(long campId) {
        Campaign camp = Campaign.get(campId)
        if (!camp) {
            throw new TableclothDomainReferenceException("Campaign with id $campId not found")
        }
        return camp
    }

    private void assertOwnerIsCurrentUser(Campaign camp) {
        User current = securityService.loggedInUser
        if (!camp.participants.find { it.user.username == current.username && it.owner }) {
            throw new TableclothAccessException("Only owner may edit a campaign.")
        }
    }
}