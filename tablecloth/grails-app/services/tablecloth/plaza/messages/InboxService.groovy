package tablecloth.plaza.messages

import grails.compiler.GrailsCompileStatic
import grails.gorm.transactions.Transactional
import tablecloth.DatabaseService
import tablecloth.plaza.campaign.CampaignService
import tablecloth.exceptions.TableclothAccessException
import tablecloth.exceptions.TableclothDomainException
import tablecloth.model.domain.messages.Inbox
import tablecloth.model.domain.messages.Message
import tablecloth.model.domain.users.User
import tablecloth.modelData.MessageType
import tablecloth.security.SecurityService
import tablecloth.utils.TimeService
import tablecloth.viewmodel.InboxViewmodel

@Transactional
@GrailsCompileStatic
class InboxService {

    SecurityService securityService
    DatabaseService databaseService
    CampaignService campaignService
    TimeService timeService

    InboxViewmodel getCurrentInbox() {
        User currentUser = securityService.user
        if (!currentUser) {
            throw new TableclothAccessException("Attempted to access wrong inbox")
        }
        Inbox inbox = currentUser.inbox
        if(inbox == null) {
            inbox = new Inbox(
                owner: currentUser,
                messages: [].toSet()
            )
            databaseService.save(currentUser)
        } else {
            inbox.messages.findAll { it.received == null }.each { it.received == timeService.now }
            if (inbox.messages.any { it.dirty }) {
                databaseService.save(inbox)
            }
        }
        return InboxViewmodel.fromDomain(inbox)
    }

    void flagMessageAsRead(long id) {
        Message message = fetchMessage(id)
        assert fetchUser(message)
        message.read = true
        databaseService.save(message)
    }

    void handleInvitation(boolean accepted, long id) {
        Message message = fetchMessage(id)
        User currentUser = fetchUser(message)
        message.read = true
        message.messageType = accepted ? MessageType.ACCEPTED_INVITATION : MessageType.REJECTED_INVITATION
        campaignService.handleInvitation(accepted, message.invitationId, currentUser)
        databaseService.save(message)
    }

    void deleteMessage(long id) {
        Message message = fetchMessage(id)
        assert fetchUser(message)
        message.inbox.messages.removeAll { it.id == id }
        databaseService.save(message.inbox)
    }

    private Message fetchMessage(long id) {
        Message message = Message.get(id)
        if (!message) {
            throw new TableclothDomainException("Message with id $id does not exist")
        }
        return message
    }

    private User fetchUser(Message message) {
        User currentUser = securityService.user
        if (message.inbox.owner != currentUser) {
            throw new TableclothAccessException("Attempted to access wrong inbox")
        }
        return currentUser
    }
}
