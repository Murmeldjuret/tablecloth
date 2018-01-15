package tablecloth.gen.viewmodel

import tablecloth.gen.model.domain.messages.Message
import tablecloth.gen.modelData.MessageType

import static tablecloth.gen.modelData.MessageType.*

class MessageViewmodel {

    Long id
    String sender
    Date sent, received
    boolean read
    String body
    Long invitationId
    MessageType type

    static MessageViewmodel fromDomain(Message msg) {
        return new MessageViewmodel(
            id: msg.id,
            sender: msg.sender.username,
            sent: msg.sent,
            received: msg.received,
            read: msg.read ?: false,
            body: msg.body,
            invitationId: msg.invitationId,
            type: msg.messageType,
        )
    }

    String getInvitationStatus() {
        switch (type) {
            case ACCEPTED_INVITATION:
                return 'Accepted'
            case REJECTED_INVITATION:
                return 'Rejected'
            case DELETED_INVITATION:
                return 'Invitation deleted by campaign owner.'
            case INVITATION:
            case PRIVATE_MESSAGE:
            case SERVER_MESSAGE:
            default:
                return ''
        }
    }

    boolean isPending() {
        type == INVITATION
    }
}
