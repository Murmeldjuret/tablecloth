package tablecloth.plaza.messages

import grails.compiler.GrailsCompileStatic
import grails.plugin.springsecurity.annotation.Secured
import tablecloth.commands.SendMessageCommand
import tablecloth.exceptions.TableclothDomainException
import tablecloth.security.UserService

@GrailsCompileStatic
@Secured('ROLE_USER')
class InboxController {

    private static int MAX_MSG_LENGTH = SendMessageCommand.MAX_MSG_LENGTH

    UserService userService
    InboxService inboxService
    MessageService messageService

    def index() {
        def user = userService.currentUserViewmodel
        def inbox = inboxService.getCurrentInbox()
        render view: "/inbox/inbox", model: [inbox: inbox, user: user]
    }

    def send(SendMessageCommand cmd) {
        if (!cmd.validate()) {
            flash.message = cmd.errors
        } else {
            try {
                messageService.sendMessageToUser(cmd.username, cmd.body)
                flash.message = 'Message sent!'
            } catch (TableclothDomainException ex) {
                flash.message = "Error, $ex.message"
            }
        }
        redirect action: 'index'
    }

    @Secured('ROLE_ADMIN')
    def broadcast(String messageText) {
        if (!messageText) {
            flash.message = "Invalid message"
        } else if (messageText.length() > MAX_MSG_LENGTH) {
            flash.message = "Message was too long, maximum of $MAX_MSG_LENGTH characters allowed."
        } else {
            messageService.broadcastMessageToAllUsers(messageText)
            flash.message = 'Broadcast sent!'
        }
        redirect action: 'index'
    }

    def delete(long messageId) {
        inboxService.deleteMessage(messageId)
        redirect action: 'index'
    }

    def rejectInvitation(long messageId) {
        inboxService.acceptInvitation(messageId)
        redirect action: 'index'
    }

    def acceptInvitation(long messageId) {
        inboxService.rejectInvitation(messageId)
        redirect action: 'index'
    }

}
