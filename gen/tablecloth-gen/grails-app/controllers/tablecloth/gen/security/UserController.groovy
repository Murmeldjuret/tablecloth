package tablecloth.gen.security

import grails.plugin.springsecurity.annotation.Secured
import org.springframework.http.HttpStatus
import tablecloth.gen.commands.AddUserCommand
import tablecloth.gen.commands.GetUserCommand

@Secured('ROLE_ADMIN')
class UserController {

    UserService userService

    static String defaultView = '../users/users'

    def index() {
        def users = userService.getUsers()
        render view: defaultView, model: [users: users]
    }

    def delete(GetUserCommand cmd) {
        if (cmd.validate()) {
            try {
                userService.removeUser(cmd.name)
                flash.message = 'User removed!'
                redirect controller: 'user', action: 'index'
            } catch (e) {
                render text: "User not removed, error reason: $e.message"
            }
        } else {
            render status: HttpStatus.BAD_REQUEST, text: "User not removed, supplied name is not valid."
        }
    }

    def create(AddUserCommand cmd) {
        if (cmd.validate()) {
            try {
                userService.addUser(cmd.name, cmd.pw)
                flash.message = 'User added!'
                redirect controller: 'user', action: 'index'
            } catch (e) {
                render text: "User not added, error reason: $e.message"
            }
        } else {
            render status: HttpStatus.BAD_REQUEST, text: "User not removed, supplied name and password are not valid."
        }
    }
}