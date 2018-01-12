package tablecloth.gen.campaign

import grails.gorm.transactions.Transactional
import tablecloth.gen.DatabaseService
import tablecloth.gen.commands.AddCampaignCommand
import tablecloth.gen.exceptions.TableclothAccessException
import tablecloth.gen.model.domain.campaign.Campaign
import tablecloth.gen.model.domain.campaign.Participant
import tablecloth.gen.model.domain.users.User
import tablecloth.gen.modelData.CampaignPermission
import tablecloth.gen.modelData.ParticipantStatus
import tablecloth.gen.security.SecurityService

@Transactional
class CampaignService {

    DatabaseService databaseService
    SecurityService securityService

    boolean newCampaign(AddCampaignCommand cmd) {
        if (!cmd.validate()) {
            return false
        }
        Campaign camp = new Campaign(
            name: cmd.name,
            description: cmd.description,
            defaultPermissions: CampaignPermission.defaultPermissions(),
            participants: [].toSet(),
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
        return true
    }

    boolean addPlayerToCampaign(Campaign camp, User user) {
        if (!camp || !user || user == camp.owner) {
            log.error("Invalid input to addPlayerToCampaign for campaign $camp and user $user")
            return false
        }
        if (camp.owner != securityService.user) {
            throw new TableclothAccessException("Only owner can add players to campaign!")
        }
        if (!camp.defaultPermissions) {
            log.error("No permissions assigned to campaign $camp")
            return false
        } else {
            Participant participant = new Participant(
                user: user,
                status: ParticipantStatus.PENDING_INVITATION,
                permissions: camp.defaultPermissions
            )
            user.campaigns.add(camp)
            camp.participants.add(participant)
            databaseService.save(camp, user)
            return true
        }
    }
}