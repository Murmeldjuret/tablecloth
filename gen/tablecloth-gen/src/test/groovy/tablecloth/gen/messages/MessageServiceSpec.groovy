package tablecloth.gen.messages

import grails.test.hibernate.HibernateSpec
import grails.testing.services.ServiceUnitTest
import tablecloth.gen.DatabaseService
import tablecloth.gen.MockObjects
import tablecloth.gen.model.domain.campaign.Campaign
import tablecloth.gen.model.domain.messages.Inbox
import tablecloth.gen.model.domain.messages.Message
import tablecloth.gen.model.domain.users.User
import tablecloth.gen.modelData.MessageType
import tablecloth.gen.security.SecurityService
import tablecloth.gen.utils.TimeService

class MessageServiceSpec extends HibernateSpec implements ServiceUnitTest<MessageService> {

    List<Class> getDomainClasses() { [User, Message, Inbox, Campaign] }

    def setup() {
        service.timeService = Mock(TimeService)
        service.timeService.now >> new Date()
        service.databaseService = new DatabaseService()
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
        1 * service.securityService.user >> sender
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
        1 * service.securityService.user >> admin
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
        User.findByUsername(user.username).inbox.messages.any {
            it.body == msgBody &&
                it.messageType == MessageType.INVITATION &&
                it.sender.username == camp.owner.username &&
                it.invitationId == camp.id
        }
    }
}
