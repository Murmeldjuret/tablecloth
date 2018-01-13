package tablecloth.gen.campaign

import grails.gorm.transactions.Transactional
import tablecloth.gen.DatabaseService
import tablecloth.gen.commands.AddCampaignCommand
import tablecloth.gen.exceptions.TableclothAccessException
import tablecloth.gen.exceptions.TableclothDomainException
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

    List<CampaignViewmodel> getCampaigns() {
        User user = securityService.user
        List<Campaign> campaigns = Campaign.findAllByOwner(user) ?: []
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
        Campaign camp = Campaign.findById(id)
        if (!camp) {
            throw new TableclothDomainException("Campaign with id $id not found")
        }
        User user = User.findByUsername(username)
        if (!user) {
            throw new TableclothDomainException("User with username $username not found")
        }
        if (camp.owner != securityService.user) {
            throw new TableclothAccessException("Only owner can add players to campaign!")
        }
        if (!camp.defaultPermissions) {
            log.error("No permissions assigned to campaign $camp")
        }
        Participant participant = new Participant(
            user: user,
            status: ParticipantStatus.PENDING_INVITATION,
            permissions: camp.defaultPermissions
        )
        user.campaigns.add(camp)
        camp.participants.add(participant)
        databaseService.save(camp, user)
    }

    void removeCampaign(long id) {
        Campaign camp = Campaign.findById(id)
        if (!camp) {
            throw new TableclothDomainException("Campaign with id $id not found")
        }
        if (camp.owner != securityService.user) {
            throw new TableclothAccessException("Only owner may remove their campaign.")
        }
        databaseService.delete(camp)
    }
}