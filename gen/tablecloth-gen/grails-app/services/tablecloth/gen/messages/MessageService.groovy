package tablecloth.gen.messages

import grails.gorm.transactions.Transactional
import tablecloth.gen.DatabaseService
import tablecloth.gen.exceptions.TableclothAccessException
import tablecloth.gen.exceptions.TableclothDomainException
import tablecloth.gen.model.domain.campaign.Campaign
import tablecloth.gen.model.domain.messages.Message
import tablecloth.gen.model.domain.users.User
import tablecloth.gen.modelData.MessageType
import tablecloth.gen.security.SecurityService
import tablecloth.gen.utils.TimeService

@Transactional
class MessageService {

    TimeService timeService
    DatabaseService databaseService
    SecurityService securityService

    void sendMessageToUser(String username, String body) {
        User currentUser = securityService.user
        if (!currentUser) {
            throw new TableclothAccessException("Must be logged in to send messages!")
        }
        User receiver = fetchReceiver(username)
        Message message = new Message(
            sent: timeService.now,
            sender: currentUser,
            messageType: MessageType.PRIVATE_MESSAGE,
            body: body,
        )
        receiver.inbox.addToMessages(message)
        databaseService.save(receiver.inbox)
    }

    void broadcastMessageToAllUsers(String body) {
        User user = securityService.user
        if (!user || !securityService.isAdmin(user)) {
            throw new TableclothAccessException("Must be logged in as admin to send broadcast!")
        }
        List<User> users = User.findAll()
        users.remove(user)
        users.each { User receiver ->
            Message message = new Message(
                sent: timeService.now,
                sender: user,
                messageType: MessageType.SERVER_MESSAGE,
                body: body,
            )
            receiver.inbox.addToMessages(message)
            databaseService.save(receiver.inbox)
        }
    }

    void sendInvitationToUser(Campaign camp, String username, String body = '') {
        User receiver = fetchReceiver(username)
        Message message = new Message(
            sent: timeService.now,
            sender: camp.owner,
            messageType: MessageType.INVITATION,
            body: body,
            invitationId: camp.id
        )
        receiver.inbox.addToMessages(message)
        databaseService.save(receiver.inbox)
    }

    private User fetchReceiver(String username) {
        User receiver = User.findByUsername(username)
        if (!receiver) {
            throw new TableclothDomainException("Target username does not exist!")
        }
        return receiver
    }
}
