package tablecloth.plaza.messages

import grails.compiler.GrailsCompileStatic
import grails.plugin.springsecurity.annotation.Secured
import grails.util.Holders
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
        def user = userService.currentUser
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
                flash.message = 'Error, username not found'
            }
        }
        redirect action: 'index'
    }

    @Secured('ROLE_ADMIN')
    def broadcast(String msg) {
        if (!msg) {
            flash.message = "Invalid message"
        } else if (msg.length() > MAX_MSG_LENGTH) {
            flash.message = "Message too long"
        } else {
            messageService.broadcastMessageToAllUsers(msg)
            flash.message = 'Broadcast sent!'
        }
        redirect action: 'index'
    }

    def delete(long id) {
        inboxService.deleteMessage(id)
        redirect action: 'index'
    }

    def handleInvitation(boolean accepted, long id) {
        inboxService.handleInvitation(accepted, id)
        redirect action: 'index'
    }

}
