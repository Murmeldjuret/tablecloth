package tablecloth.gen.security

import grails.gorm.transactions.Transactional
import tablecloth.gen.exceptions.TableclothDomainException
import tablecloth.gen.model.domain.creatures.PlayerCharacter
import tablecloth.gen.model.domain.users.Role
import tablecloth.gen.model.domain.users.User
import tablecloth.gen.model.domain.users.UserRole
import tablecloth.gen.viewmodel.UserDisplayViewmodel

class UserService {

    SecurityService securityService

    List<UserDisplayViewmodel> getUsers() {
        User currentUser = securityService.getUser()
        return User.getAll().collect { User user ->
            int pcCount = PlayerCharacter.countByOwner(user) ?: 0
            boolean isAdmin = user.authorities.find { it.authority == 'ROLE_ADMIN' }
            boolean isCurrentUser = user == currentUser
            return new UserDisplayViewmodel(
                name: user.username,
                isAdmin: isAdmin,
                pcCount: pcCount,
                isCurrentUser: isCurrentUser,
            )
        }
    }

    @Transactional
    void addUser(String name, String pw) {
        User user = User.findByUsername(name)
        if (user) {
            throw new TableclothDomainException("A user with the name $name already exists.")
        }
        User newUser = new User(username: name, password: pw).save()
        UserRole.create(newUser, Role.findByAuthority('ROLE_USER') as Role, true)
        log.info("Added new $newUser")
    }

    @Transactional
    void removeUser(String name) {
        User user = User.findByUsername(name)
        if (!user) {
            throw new TableclothDomainException("No user with the name: $name exists.")
        } else {
            UserRole.removeAll(user)
            user.delete(flush: true)
            log.info("Deleted $user")
        }
    }

}
