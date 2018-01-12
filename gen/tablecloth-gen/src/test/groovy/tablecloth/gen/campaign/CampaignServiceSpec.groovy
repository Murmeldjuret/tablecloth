package tablecloth.gen.campaign

import grails.testing.gorm.DataTest
import grails.testing.services.ServiceUnitTest
import spock.lang.Specification
import tablecloth.gen.DatabaseService
import tablecloth.gen.DummyObjects
import tablecloth.gen.commands.AddCampaignCommand
import tablecloth.gen.model.domain.campaign.Campaign
import tablecloth.gen.model.domain.users.User
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
        User user = DummyObjects.genericUser()
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
    }

    void "test add new player to campaign"() {
        given:
        User user = DummyObjects.genericUser()
        Campaign camp = DummyObjects.genericCampaign()

        when:
        boolean result = service.addPlayerToCampaign(camp, user)

        then:
        1 * service.securityService.user >> User.findByUsername('owner')
        result
        User.findAll().first().campaigns.count { it.name == 'Middle Earth 2.0' } == 1
        Campaign.findAll().first().participants.first().user == user
    }
}
