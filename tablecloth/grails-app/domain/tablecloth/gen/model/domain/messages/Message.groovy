package tablecloth.gen.model.domain.messages

import tablecloth.gen.model.domain.users.User
import tablecloth.gen.modelData.MessageType

import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id

    // Header data
    Date sent
    User sender

    // Meta data
    Date received
    MessageType messageType
    Boolean read

    // Content
    String body
    Long invitationId

    static belongsTo = [
        inbox: Inbox
    ]

    static constraints = {
        received nullable: true
        read nullable: true
        invitationId nullable: true, validator: { val, obj ->
            if (obj.messageType.invitation) {
                return val != null
            }
            return val == null
        }
    }
}
