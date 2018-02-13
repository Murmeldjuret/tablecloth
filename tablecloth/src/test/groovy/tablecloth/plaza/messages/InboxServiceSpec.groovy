package tablecloth.plaza.messages

import grails.test.hibernate.HibernateSpec
import grails.testing.services.ServiceUnitTest
import spock.lang.Unroll
import tablecloth.DatabaseService
import tablecloth.MockObjects
import tablecloth.model.domain.messages.Inbox
import tablecloth.model.domain.messages.Message
import tablecloth.model.domain.users.User
import tablecloth.modelData.MessageType
import tablecloth.plaza.campaign.CampaignService
import tablecloth.security.SecurityService
import tablecloth.utils.TimeService
import tablecloth.viewmodel.InboxViewmodel

class InboxServiceSpec extends HibernateSpec implements ServiceUnitTest<InboxService> {

    List<Class> getDomainClasses() { [User, Message, Inbox] }

    void setup() {
        service.securityService = Mock(SecurityService)
        service.databaseService = Mock(DatabaseService)
        service.timeService = Mock(TimeService)
        service.timeService.now >> new Date()
        service.campaignService = Mock(CampaignService)
        service.databaseService.save(_) >> { args -> args[0].each { it.save(failOnError: true, flush: true) } }
        service.databaseService.delete(_) >> { args -> args[0].each { it.delete(failOnError: true, flush: true) } }
    }

    void "test flagMessageAsRead"() {
        given:
        Inbox inbox = MockObjects.genericInbox()
        long id = Message.findByBody('testname').id

        when:
        service.flagMessageAsRead(id)

        then:
        1 * service.securityService.loggedInUser >> inbox.owner
        Message.findByRead(true).body == 'testname'
        !Message.findAllByRead(false).body.contains('testname')
    }

    void "test deleteMessage"() {
        given:
        Inbox inbox = MockObjects.genericInbox()

        when:
        service.deleteMessage(Message.findByBody('testname').id)

        then:
        1 * service.securityService.loggedInUser >> inbox.owner
        Message.count == 2
        Inbox.findAll().first().messages.size() == 2
        !Inbox.findAll().first().messages.find { it.body == 'testname' }
        !Inbox.findAll().first().messages.find { it.body == 'othermessage' }.read
    }

    void "test acceptInvitation"() {
        given:
        Inbox inbox = MockObjects.genericInbox()

        when:
        service.acceptInvitation(Message.findByBody('invitation').id)

        then:
        1 * service.securityService.loggedInUser >> inbox.owner
        1 * service.campaignService.acceptInvitation(66, _)
        Inbox.findAll().first().messages.size() == 3
        Inbox.findAll().first().messages.any { it.body == 'invitation' && it.messageType == MessageType.ACCEPTED_INVITATION }
    }

    void "test rejectInvitation"() {
        given:
        Inbox inbox = MockObjects.genericInbox()

        when:
        service.rejectInvitation(Message.findByBody('invitation').id)

        then:
        1 * service.securityService.loggedInUser >> inbox.owner
        1 * service.campaignService.rejectInvitation(66, _)
        Inbox.findAll().first().messages.size() == 3
        Inbox.findAll().first().messages.any { it.body == 'invitation' && it.messageType == MessageType.REJECTED_INVITATION }
    }

    void "test getInbox"() {
        given:
        Inbox inbox = MockObjects.genericInbox()
        assert inbox.messages.count { it.received != null } == 0

        when:
        InboxViewmodel viewmodel = service.getCurrentInbox()

        then:
        1 * service.securityService.loggedInUser >> inbox.owner
        viewmodel.messages.size() == 3
        viewmodel.username == inbox.owner.username
        Inbox.findAll().first().messages.each { it.received != null }
    }
}
