package tablecloth.viewmodel

import grails.compiler.GrailsCompileStatic
import tablecloth.model.domain.messages.Inbox

@GrailsCompileStatic
class InboxViewmodel {

    String username

    List<MessageViewmodel> messages

    static InboxViewmodel fromDomain(Inbox inbox) {
        return new InboxViewmodel(
            username: inbox.owner.username,
            messages: inbox.messages.collect { MessageViewmodel.fromDomain(it) }
        )
    }
}
