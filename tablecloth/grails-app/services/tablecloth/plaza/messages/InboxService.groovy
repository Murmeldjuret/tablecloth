package tablecloth.plaza.messages

import grails.compiler.GrailsCompileStatic
import grails.gorm.transactions.Transactional
import tablecloth.DatabaseService
import tablecloth.exceptions.TableclothAccessException
import tablecloth.exceptions.TableclothDomainReferenceException
import tablecloth.model.domain.messages.Inbox
import tablecloth.model.domain.messages.Message
import tablecloth.model.domain.users.User
import tablecloth.model.MessageType
import tablecloth.plaza.campaign.CampaignService
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
        User currentUser = securityService.loggedInUser
        Inbox inbox = currentUser.inbox
        if (inbox == null) {
            currentUser.inbox = new Inbox(
                owner: currentUser,
                messages: [].toSet()
            )
            databaseService.save(currentUser)
        } else {
            inbox.receiveAllMessages(timeService.now)
            if (inbox.anyMessageDirty) {
                databaseService.save(inbox)
            }
        }
        return InboxViewmodel.fromDomain(inbox)
    }

    void flagMessageAsRead(long messageId) {
        Message message = getMessageAsserted(messageId)
        getLoggedInUserAssertOwner(message)
        message.read = true
        if (message.dirty) {
            databaseService.save(message)
        }
    }

    void acceptInvitation(long messageId) {
        Message message = getMessageAsserted(messageId)
        User currentUser = getLoggedInUserAssertOwner(message)
        message.read = true
        campaignService.acceptInvitation(message.invitationId, currentUser)
        message.messageType = MessageType.ACCEPTED_INVITATION
        databaseService.save(message)
    }

    void rejectInvitation(long messageId) {
        Message message = getMessageAsserted(messageId)
        User currentUser = getLoggedInUserAssertOwner(message)
        message.read = true
        campaignService.rejectInvitation(message.invitationId, currentUser)
        message.messageType = MessageType.REJECTED_INVITATION
        databaseService.save(message)
    }

    void deleteMessage(long messageId) {
        Message message = getMessageAsserted(messageId)
        User currentUser = getLoggedInUserAssertOwner(message)
        currentUser.inbox.removeFromMessages(message)
        if (message.messageType == MessageType.INVITATION) {
            campaignService.rejectInvitation(message.invitationId, currentUser)
        }
        databaseService.save(currentUser.inbox)
    }

    private static Message getMessageAsserted(long messageId) {
        Message message = Message.get(messageId)
        if (message == null) {
            throw new TableclothDomainReferenceException("Message with id $messageId does not exist")
        }
        return message
    }

    private User getLoggedInUserAssertOwner(Message message) {
        User currentUser = securityService.loggedInUser
        if (message.inbox.owner != currentUser) {
            throw new TableclothAccessException("Attempted to access wrong inbox")
        }
        return currentUser
    }
}
