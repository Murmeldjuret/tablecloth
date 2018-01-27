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
            throw new TableclothAccessException("Must be logged in to send messaging!")
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

    void sendInvitationToUser(Campaign camp, User user, String body = null) {
        Message message = new Message(
            sent: timeService.now,
            sender: securityService.user,
            messageType: MessageType.INVITATION,
            body: body ?: "I would like you to join my campaign: $camp.name",
            invitationId: camp.id
        )
        user.inbox.addToMessages(message)
        databaseService.save(user.inbox)
    }

    void deleteAllAssociatedInvitations(long id) {
        List<Message> invites = Message.findAllByInvitationId(id)
        invites.each { Message msg ->
            msg.messageType = MessageType.DELETED_INVITATION
            databaseService.save(msg)
        }
    }

    void deleteAssociatedInvitations(long id, User user) {
        List<Message> invites = user.inbox.messages.findAll { it.invitationId == id }
        invites.each {
            user.inbox.removeFromMessages(invites)
        }
        databaseService.save(user.inbox)
    }

    private User fetchReceiver(String username) {
        User receiver = User.findByUsername(username)
        if (!receiver) {
            throw new TableclothDomainException("Target username does not exist!")
        }
        return receiver
    }
}
