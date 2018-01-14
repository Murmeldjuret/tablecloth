package tablecloth.gen.viewmodel

import tablecloth.gen.model.domain.messages.Inbox

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
