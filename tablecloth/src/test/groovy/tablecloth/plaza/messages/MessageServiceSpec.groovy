package tablecloth.plaza.messages

import grails.test.hibernate.HibernateSpec
import grails.testing.services.ServiceUnitTest
import tablecloth.DatabaseService
import tablecloth.MockObjects
import tablecloth.model.domain.campaign.Campaign
import tablecloth.model.domain.messages.Inbox
import tablecloth.model.domain.messages.Message
import tablecloth.model.domain.users.User
import tablecloth.model.MessageType
import tablecloth.security.SecurityService
import tablecloth.utils.TimeService

class MessageServiceSpec extends HibernateSpec implements ServiceUnitTest<MessageService> {

    List<Class> getDomainClasses() { [User, Message, Inbox, Campaign] }

    def setup() {
        service.timeService = Mock(TimeService)
        service.timeService.now >> new Date()
        service.databaseService = Mock(DatabaseService)
        service.databaseService.save(_) >> { args -> args[0].each { it.save(failOnError: true, flush: true) } }
        service.databaseService.delete(_) >> { args -> args[0].each { it.delete(failOnError: true, flush: true) } }
        service.securityService = Mock(SecurityService)
        service.securityService.isAdmin(_) >> { args -> args[0].username == 'admin' }
    }

    void "test send private message"() {
        given:
        User sender = MockObjects.genericUser()
        User receiver = MockObjects.genericOtherUser()
        String msgBody = "Message body ä2501sa_25"

        when:
        service.sendMessageToUser(receiver.username, msgBody)

        then:
        1 * service.securityService.loggedInUser >> sender
        User.findByUsername(receiver.username).inbox.messages.any {
            it.body == msgBody &&
                it.messageType == MessageType.PRIVATE_MESSAGE &&
                it.sender.username == sender.username
        }
        User.findByUsername(receiver.username).inbox.messages.size() == 1
    }

    void "test send broadcast"() {
        given:
        User admin = MockObjects.genericAdmin()
        User user1 = MockObjects.genericUser()
        User user2 = MockObjects.genericOtherUser()
        String msgBody = "Message body ä2501sa_25"

        when:
        service.broadcastMessageToAllUsers(msgBody)

        then:
        1 * service.securityService.loggedInUser >> admin
        User.findByUsername(user1.username).inbox.messages.any {
            it.body == msgBody &&
                it.messageType == MessageType.SERVER_MESSAGE &&
                it.sender.username == admin.username
        }
        User.findByUsername(user1.username).inbox.messages.size() == 1
        User.findByUsername(user2.username).inbox.messages.any {
            it.body == msgBody &&
                it.messageType == MessageType.SERVER_MESSAGE &&
                it.sender.username == admin.username
        }
        User.findByUsername(user2.username).inbox.messages.size() == 1
        User.findByUsername(admin.username).inbox.messages.size() == 0
    }

    void "test send invitation to campaign"() {
        given:
        Campaign camp = MockObjects.genericCampaign()
        User user = MockObjects.genericOtherUser()
        String msgBody = "Message body ä2501sa_25"

        when:
        service.sendInvitationToUser(camp, user, msgBody)

        then:
        1 * service.securityService.loggedInUser >> User.findByUsername('owner')
        User.findByUsername(user.username).inbox.messages.any {
            it.body == msgBody &&
                it.messageType == MessageType.INVITATION &&
                it.sender.username == 'owner' &&
                it.invitationId == camp.id
        }
    }

    void "test delete associated invitations"() {
        given:
        Campaign camp = MockObjects.genericCampaign()
        User user = MockObjects.genericUser()
        user.inbox.addToMessages(
            new Message(
                sent: new Date().minus(1),
                sender: User.findByUsername('owner'),
                messageType: MessageType.INVITATION,
                body: 'invitation',
                inbox: user.inbox,
                read: false,
                invitationId: camp.id
            ))
        user.inbox.save(failOnError: true, flush: true)
        User user2 = MockObjects.genericOtherUser()
        user2.inbox.addToMessages(
            new Message(
                sent: new Date().minus(1),
                sender: User.findByUsername('owner'),
                messageType: MessageType.INVITATION,
                body: 'invitation',
                inbox: user2.inbox,
                read: false,
                invitationId: camp.id
            ))
        user2.inbox.save(failOnError: true, flush: true)

        when:
        service.deleteAllInvitationsToCampaign(camp.id)

        then:
        User.findByUsername(user.username).inbox.messages.size() == 1
        User.findByUsername(user.username).inbox.messages.first().messageType == MessageType.DELETED_INVITATION
        User.findByUsername(user2.username).inbox.messages.size() == 1
        User.findByUsername(user2.username).inbox.messages.first().messageType == MessageType.DELETED_INVITATION
    }
}
