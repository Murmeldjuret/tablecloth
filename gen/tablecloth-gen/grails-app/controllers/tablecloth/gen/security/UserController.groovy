package tablecloth.gen.security

import grails.plugin.springsecurity.annotation.Secured
import org.springframework.http.HttpStatus

@Secured('ROLE_ADMIN')
class UserController {

    UserService userService

    def index() {
        def users = userService.getUsers()
        render(view: '../users/users', model: [users: users])
    }

    def delete(String name) {
        if (name != '') {
            try {
                userService.removeUser(name)
                render status: HttpStatus.OK, text: 'User deleted!'
            } catch (e) {
                render text: "User not removed, error reason: $e.message"
            }
        } else {
            render status: HttpStatus.BAD_REQUEST, text: "User not removed, supplied name is not valid."
        }
    }

    def create(String name, String pw) { //SUPER SAFE GUARANTEED
        if (name && pw && name != '' && pw != '') {
            try {
                userService.addUser(name, pw)
                render status: HttpStatus.OK, text: 'User added!'
            } catch (e) {
                render text: "User not added, error reason: $e.message"
            }
        } else {
            render status: HttpStatus.BAD_REQUEST, text: "User not removed, supplied name and password are not valid."
        }
    }
}