package tablecloth.model.domain.messages

import tablecloth.model.domain.users.User

import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

class Inbox {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id

    static hasMany = [
        messages: Message
    ]

    static belongsTo = [
        owner: User
    ]

    static mapping = {
        messages cascade: "all-delete-orphan"
    }

    static constraints = {
    }

    boolean isAnyMessageDirty() {
        return messages.any { it.dirty }
    }

    void receiveAllMessages(Date receiveTime) {
        messages.findAll { it.received == null }.each { it.received == receiveTime }
    }
}
