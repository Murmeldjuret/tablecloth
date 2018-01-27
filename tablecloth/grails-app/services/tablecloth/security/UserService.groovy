package tablecloth.security

import grails.gorm.transactions.Transactional
import tablecloth.exceptions.TableclothDomainException
import tablecloth.model.domain.creatures.PlayerCharacter
import tablecloth.model.domain.messages.Inbox
import tablecloth.model.domain.users.Role
import tablecloth.model.domain.users.User
import tablecloth.model.domain.users.UserRole
import tablecloth.viewmodel.UserViewmodel

class UserService {

    SecurityService securityService

    List<UserViewmodel> getUsers() {
        User currentUser = securityService.user
        return User.getAll().collect { User user ->
            createViewmodel(user, currentUser)
        }
    }

    UserViewmodel getUser(String username) {
        User user = User.findByUsername(username)
        return getUser(user as User)
    }

    UserViewmodel getCurrentUser() {
        return getUser(securityService.user)
    }

    UserViewmodel getUser(User user) {
        if (!user) {
            return null
        }
        return createViewmodel(user, null)
    }

    @Transactional
    void addUser(String name, String pw) {
        User user = User.findByUsername(name)
        if (user) {
            throw new TableclothDomainException("A user with the name $name already exists.")
        }
        User newUser = new User(username: name, password: pw)
        Inbox inbox = new Inbox(
            owner: newUser,
            messages: [].toSet()
        )
        newUser.inbox = inbox
        newUser.save()
        UserRole.create(newUser, Role.findByAuthority('ROLE_USER') as Role, true)
        log.info("Added new $newUser")
    }

    @Transactional
    void removeUser(String name) {
        User user = User.findByUsername(name)
        if (!user) {
            throw new TableclothDomainException("No user with the name: $name exists.")
        }
        UserRole.removeAll(user)
        user.delete(flush: true)
        log.info("Deleted $user")
    }

    private UserViewmodel createViewmodel(User user, User currentUser) {
        int pcCount = PlayerCharacter.countByOwner(user)
        boolean isAdmin = securityService.isAdmin(user)
        boolean isCurrentUser = currentUser ? user == currentUser : false
        return new UserViewmodel(
            name: user.username,
            isAdmin: isAdmin,
            pcCount: pcCount,
            isCurrentUser: isCurrentUser,
        )
    }

}
