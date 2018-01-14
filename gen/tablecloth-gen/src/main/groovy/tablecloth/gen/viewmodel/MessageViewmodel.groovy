package tablecloth.gen.viewmodel

import tablecloth.gen.model.domain.messages.Message

class MessageViewmodel {

    Long id
    String sender
    Date sent, received
    boolean read
    String body
    Long invitationId

    static MessageViewmodel fromDomain(Message msg) {
        return new MessageViewmodel(
            id: msg.id,
            sender: msg.sender.username,
            sent: msg.sent,
            received: msg.received,
            read: msg.read,
            body: msg.body,
            invitationId: msg.invitationId,
        )
    }
}
