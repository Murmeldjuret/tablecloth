package tablecloth.gen.campaign

import grails.testing.gorm.DataTest
import grails.testing.services.ServiceUnitTest
import spock.lang.Specification
import tablecloth.gen.DatabaseService
import tablecloth.gen.MockObjects
import tablecloth.gen.commands.AddCampaignCommand
import tablecloth.gen.model.domain.campaign.Campaign
import tablecloth.gen.model.domain.users.User
import tablecloth.gen.modelData.CampaignPermission
import tablecloth.gen.modelData.ParticipantStatus
import tablecloth.gen.security.SecurityService

class CampaignServiceSpec extends Specification implements ServiceUnitTest<CampaignService>, DataTest {

    void setupSpec() {
        mockDomain User
        mockDomain Campaign
    }

    void setup() {
        service.securityService = Mock(SecurityService)
        service.databaseService = Mock(DatabaseService)
    }

    void "test add new campaign to User"() {
        given:
        User user = MockObjects.genericUser()
        AddCampaignCommand cmd = new AddCampaignCommand(
            name: 'Middle Earth 2.0',
            description: 'Not stolen from Tolkien',
        )

        when:
        boolean result = service.newCampaign(cmd)

        then:
        1 * service.securityService.user >> user
        result
        User.findAll().first().campaigns.count { it.name == 'Middle Earth 2.0' } == 1
        User.findAll().first().campaigns.first().participants.each {
            it.status == ParticipantStatus.OWNER &&
                it.user == user &&
                it.permissions.contains(CampaignPermission.EDIT_CAMPAIGN)
        }
    }

    void "test add new player to campaign"() {
        given:
        User user = MockObjects.genericUser()
        Campaign camp = MockObjects.genericCampaign()

        when:
        boolean result = service.addPlayerToCampaign(camp, user)

        then:
        1 * service.securityService.user >> User.findByUsername('owner')
        result
        User.findAll().first().campaigns.count { it.name == 'Middle Earth 2.0' } == 1
        Campaign.findAll().first().participants.find {
            it.user.username == 'user' &&
                it.status == ParticipantStatus.PENDING_INVITATION &&
                it.permissions.contains(CampaignPermission.VIEW)
        }
    }
}
