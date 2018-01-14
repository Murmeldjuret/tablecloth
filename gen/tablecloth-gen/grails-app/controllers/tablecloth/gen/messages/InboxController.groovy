package tablecloth.gen.messages

import grails.plugin.springsecurity.annotation.Secured
import grails.util.Holders
import tablecloth.gen.commands.SendMessageCommand
import tablecloth.gen.exceptions.TableclothDomainException
import tablecloth.gen.security.UserService

@Secured('ROLE_USER')
class InboxController {

    private static int MAX_MSG_LENGTH = Holders.config.tablecloth?.messaging?.messagelength ?: 1024

    UserService userService
    InboxService inboxService
    MessageService messageService

    def index() {
        def user = userService.currentUser
        def inbox = inboxService.getCurrentInbox()
        render view: "../messages/inbox", model: [inbox: inbox, user: user]
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
