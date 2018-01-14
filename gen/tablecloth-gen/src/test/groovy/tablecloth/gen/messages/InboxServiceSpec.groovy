package tablecloth.gen.messages

import grails.test.hibernate.HibernateSpec
import grails.testing.services.ServiceUnitTest
import spock.lang.Unroll
import tablecloth.gen.DatabaseService
import tablecloth.gen.MockObjects
import tablecloth.gen.campaign.CampaignService
import tablecloth.gen.model.domain.messages.Inbox
import tablecloth.gen.model.domain.messages.Message
import tablecloth.gen.model.domain.users.User
import tablecloth.gen.modelData.MessageType
import tablecloth.gen.security.SecurityService
import tablecloth.gen.utils.TimeService
import tablecloth.gen.viewmodel.InboxViewmodel

class InboxServiceSpec extends HibernateSpec implements ServiceUnitTest<InboxService> {

    List<Class> getDomainClasses() { [User, Message, Inbox] }

    void setup() {
        service.securityService = Mock(SecurityService)
        service.databaseService = Mock(DatabaseService)
        service.timeService = Mock(TimeService)
        service.timeService.now >> new Date()
        service.campaignService = Mock(CampaignService)
        service.databaseService = new DatabaseService()
    }

    void "test flagMessageAsRead"() {
        given:
        Inbox inbox = MockObjects.genericInbox()
        long id = Message.findByBody('testname').id

        when:
        service.flagMessageAsRead(id)

        then:
        1 * service.securityService.user >> inbox.owner
        Message.findByRead(true).body == 'testname'
        !Message.findAllByRead(false).body.contains('testname')
    }

    void "test deleteMessage"() {
        given:
        Inbox inbox = MockObjects.genericInbox()

        when:
        service.deleteMessage(Message.findByBody('testname').id)

        then:
        1 * service.securityService.user >> inbox.owner
        Message.count == 2
        Inbox.findAll().first().messages.size() == 2
        !Inbox.findAll().first().messages.find { it.body == 'testname' }
        !Inbox.findAll().first().messages.find { it.body == 'othermessage' }.read
    }

    @Unroll
    void "test handleInvitation"() {
        given:
        Inbox inbox = MockObjects.genericInbox()

        when:
        service.handleInvitation(accepted, Message.findByBody('invitation').id)

        then:
        1 * service.securityService.user >> inbox.owner
        1 * service.campaignService.handleInvitation(accepted, 66, _)
        Inbox.findAll().first().messages.size() == 3
        Inbox.findAll().first().messages.any { it.body == 'invitation' && it.messageType == expectedType }

        where:
        accepted || expectedType
        true     || MessageType.ACCEPTED_INVITATION
        false    || MessageType.REJECTED_INVITATION
    }

    void "test getInbox"() {
        given:
        Inbox inbox = MockObjects.genericInbox()
        assert inbox.messages.count { it.received != null } == 0

        when:
        InboxViewmodel viewmodel = service.getCurrentInbox()

        then:
        1 * service.securityService.user >> inbox.owner
        viewmodel.messages.size() == 3
        viewmodel.username == inbox.owner.username
        Inbox.findAll().first().messages.each { it.received != null }
    }
}
