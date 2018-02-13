package tablecloth.plaza.messages

import grails.compiler.GrailsCompileStatic
import grails.gorm.transactions.Transactional
import tablecloth.DatabaseService
import tablecloth.exceptions.TableclothAccessException
import tablecloth.exceptions.TableclothDomainReferenceException
import tablecloth.model.domain.campaign.Campaign
import tablecloth.model.domain.messages.Message
import tablecloth.model.domain.users.User
import tablecloth.modelData.MessageType
import tablecloth.security.SecurityService
import tablecloth.utils.TimeService

@Transactional
@GrailsCompileStatic
class MessageService {

    TimeService timeService
    DatabaseService databaseService
    SecurityService securityService

    void sendMessageToUser(String username, String body) {
        User currentUser = securityService.loggedInUser
        User receiver = getUserByNameAsserted(username)
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
        User currentUser = securityService.loggedInUser
        if (currentUser == null || !securityService.isAdmin(currentUser)) {
            throw new TableclothAccessException("Must be logged in as admin to send broadcast!")
        }
        List<User> users = getAllUsersExcept(currentUser)
        users.each { User receiver ->
            Message message = new Message(
                sent: timeService.now,
                sender: currentUser,
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
            sender: securityService.loggedInUser,
            messageType: MessageType.INVITATION,
            body: body ?: "I would like you to join my campaign: $camp.name",
            invitationId: camp.id
        )
        user.inbox.addToMessages(message)
        databaseService.save(user.inbox)
    }

    void deleteAllInvitationsToCampaign(long campId) {
        List<Message> invites = Message.findAllByInvitationId(campId)
        invites.each { Message msg ->
            msg.messageType = MessageType.DELETED_INVITATION
            databaseService.save(msg)
        }
    }

    private static List<User> getAllUsersExcept(User user) {
        List<User> users = User.findAll()
        users.remove(user)
        return users
    }

    private static User getUserByNameAsserted(String username) {
        User user = User.findByUsername(username)
        if (!user) {
            throw new TableclothDomainReferenceException("Target username does not exist!")
        }
        return user
    }
}
