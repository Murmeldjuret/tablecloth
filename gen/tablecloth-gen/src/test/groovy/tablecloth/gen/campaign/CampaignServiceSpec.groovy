package tablecloth.gen.campaign

import grails.test.hibernate.HibernateSpec
import grails.testing.services.ServiceUnitTest
import tablecloth.gen.DatabaseService
import tablecloth.gen.MockObjects
import tablecloth.gen.commands.AddCampaignCommand
import tablecloth.gen.messages.MessageService
import tablecloth.gen.model.domain.campaign.Campaign
import tablecloth.gen.model.domain.campaign.Participant
import tablecloth.gen.model.domain.users.User
import tablecloth.gen.modelData.CampaignPermission
import tablecloth.gen.modelData.ParticipantStatus
import tablecloth.gen.security.SecurityService
import tablecloth.gen.viewmodel.CampaignViewmodel

class CampaignServiceSpec extends HibernateSpec implements ServiceUnitTest<CampaignService> {

    List<Class> getDomainClasses() { [User, Campaign, Participant] }

    void setup() {
        service.securityService = Mock(SecurityService)
        service.databaseService = Mock(DatabaseService)
        service.messageService = Mock(MessageService)
        service.databaseService.save(_) >> { args -> args[0].each { it.save(failOnError: true) } }
        service.databaseService.delete(_) >> { args -> args[0].each { it.delete(failOnError: true) } }
    }

    void "test add new campaign to User"() {
        given:
        User user = MockObjects.genericUser()
        AddCampaignCommand cmd = new AddCampaignCommand(
            name: 'Middle Earth 2.0',
            description: 'Not stolen from Tolkien',
        )

        when:
        service.newCampaign(cmd)

        then:
        1 * service.securityService.user >> user
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
        service.addPlayerToCampaign(camp.id, user.username)

        then:
        1 * service.securityService.user >> User.findByUsername('username')
        Campaign.findAll().first().participants.find {
            it.user.username == 'user' &&
                it.status == ParticipantStatus.PENDING_INVITATION &&
                it.permissions.contains(CampaignPermission.VIEW)
        }
    }

    void "test getCampaigns"() {
        given:
        Campaign camp = MockObjects.genericCampaign()

        when:
        List<CampaignViewmodel> camps = service.getCampaigns()

        then:
        1 * service.securityService.user >> camp.owner
        camps.size() == 1
        camps.first().name
        camps.first().participants.size() == 1
    }

    void "test delete campaign"() {
        given:
        Campaign camp = MockObjects.genericCampaign()

        when:
        service.removeCampaign(camp.id)

        then:
        1 * service.securityService.user >> camp.owner
    }

    void "test accept invitation"() {
        given:
        Campaign camp = MockObjects.genericCampaign()
        User user = MockObjects.genericUser()
        camp.participants.add(
            new Participant(
                user: user,
                permissions: camp.defaultPermissions,
                status: ParticipantStatus.PENDING_INVITATION,
            )
        )

        when:
        service.handleInvitation(true, camp.id, user)

        then:
        camp.participants.size() == 2
        User.findByUsername(user.username).campaigns.count { it.name == 'Middle Earth 2.0' } == 1
        camp.participants.any { it.status == ParticipantStatus.ACCEPTED_INVITATION }
    }

    void "test reject invitation"() {
        given:
        Campaign camp = MockObjects.genericCampaign()
        User user = MockObjects.genericUser()
        camp.participants.add(
            new Participant(
                user: user,
                permissions: camp.defaultPermissions,
                status: ParticipantStatus.PENDING_INVITATION,
            )
        )

        when:
        service.handleInvitation(false, camp.id, user)

        then:
        camp.participants.size() == 1
    }
}
