package tablecloth.gen.security

import grails.plugin.springsecurity.annotation.Secured
import org.springframework.http.HttpStatus

@Secured('ROLE_ADMIN')
class UserController {

    UserService userService

    static String defaultView = '../users/users'

    def index() {
        def users = userService.getUsers()
        render view: defaultView, model: [users: users]
    }

    def delete(String name) {
        if (name && name != '') {
            try {
                userService.removeUser(name)
                flash.message = 'User removed!'
                redirect controller: 'user', action: 'index'
            } catch (e) {
                render text: "User not removed, error reason: $e.message"
            }
        } else {
            render status: HttpStatus.BAD_REQUEST, text: "User not removed, supplied name is not valid."
        }
    }

    def create(String name, String pw) {
        if (name && pw && name != '' && pw != '') {
            try {
                userService.addUser(name, pw)
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