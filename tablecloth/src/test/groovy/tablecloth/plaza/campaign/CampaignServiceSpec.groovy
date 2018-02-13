package tablecloth.plaza.campaign

import grails.test.hibernate.HibernateSpec
import grails.testing.services.ServiceUnitTest
import tablecloth.DatabaseService
import tablecloth.MockObjects
import tablecloth.commands.AddCampaignCommand
import tablecloth.model.domain.campaign.Campaign
import tablecloth.model.domain.campaign.Participant
import tablecloth.model.domain.users.User
import tablecloth.modelData.CampaignPermission
import tablecloth.modelData.ParticipantStatus
import tablecloth.plaza.messages.MessageService
import tablecloth.security.SecurityService
import tablecloth.viewmodel.CampaignViewmodel

class CampaignServiceSpec extends HibernateSpec implements ServiceUnitTest<CampaignService> {

    List<Class> getDomainClasses() { [User, Campaign, Participant] }

    void setup() {
        service.securityService = Mock(SecurityService)
        service.databaseService = Mock(DatabaseService)
        service.messageService = Mock(MessageService)
        service.databaseService.save(_) >> { args -> args[0].each { it.save(failOnError: true, flush: true) } }
        service.databaseService.delete(_) >> { args -> args[0].each { it.delete(failOnError: true, flush: true) } }
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
        1 * service.securityService.loggedInUser >> user
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
        1 * service.securityService.loggedInUser >> User.findByUsername('owner')
        Campaign.findAll().first().participants.find {
            it.user.username == 'user' &&
                it.status == ParticipantStatus.PENDING_INVITATION &&
                it.permissions.contains(CampaignPermission.VIEW)
        }
    }

    void "test getCampaigns"() {
        given:
        MockObjects.genericCampaign()

        when:
        List<CampaignViewmodel> camps = service.getCampaigns()

        then:
        1 * service.securityService.loggedInUser >> User.findByUsername('owner')
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
        1 * service.securityService.loggedInUser >> User.findByUsername('owner')
    }

    void "test add user then accept invite then delete campaign"() {
        given:
        Campaign camp = MockObjects.genericCampaign()
        User user = MockObjects.genericUser()

        when:
        service.addPlayerToCampaign(camp.id, user.username)

        then:
        1 * service.securityService.loggedInUser >> User.findByUsername('owner')

        when:
        service.acceptInvitation(camp.id, user)

        then:
        Campaign.findAll().first().participants.size() == 2
        User.findAll().every { it.campaigns.size() == 1 }

        when:
        service.removeCampaign(camp.id)

        then:
        1 * service.securityService.loggedInUser >> User.findByUsername('owner')
        Campaign.findAll() == []
        User.findAll().every { it.campaigns.size() == 0 }
    }

    void "test accept invitation"() {
        given:
        Campaign camp = MockObjects.genericCampaign()
        User user = MockObjects.genericUser()
        camp.participants.add(
            new Participant(
                user: user,
                permissions: camp.defaultPermissions.collect(),
                status: ParticipantStatus.PENDING_INVITATION,
            )
        )

        when:
        service.acceptInvitation(camp.id, user)

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
        service.rejectInvitation(camp.id, user)

        then:
        camp.participants.size() == 1
    }
}
