package tablecloth.gen

import grails.plugin.springsecurity.annotation.Secured
import org.springframework.http.HttpStatus
import tablecloth.gen.model.domain.users.Role
import tablecloth.gen.model.domain.users.User
import tablecloth.gen.model.domain.users.UserRole

@Secured('ROLE_ADMIN')
class UserController { //TODO add as Transactional service

    def index() {
        List<User> users = User.getAll()
        String text = users.join("\n")
        render text: text, encoding: 'UTF-8'
    }

    def delete(String name) {
        User user = User.findByUsername(name)
        if (!user) {
            render text: "USER DOES NOT EXIST"
        } else {
            UserRole.removeAll(user)
            user.delete(flush: true)
            log.info("Deleted $user")
            render status: HttpStatus.OK, text: 'User deleted!'
        }
    }

    def create(String name, String pw) { //SUPER SAFE GUARANTEED
        if (User.findByUsername(name)) {
            render text: "USER ALREADY EXISTS"
        } else {
            User newUser = new User(username: name, password: pw).save()
            UserRole.create(newUser, Role.findByAuthority('ROLE_USER') as Role, true)
            log.info("Added new $newUser")
            render status: HttpStatus.OK, text: 'User added!'
        }
    }
}
