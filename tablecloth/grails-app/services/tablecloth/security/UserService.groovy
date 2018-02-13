package tablecloth.security

import grails.compiler.GrailsCompileStatic
import grails.gorm.transactions.Transactional
import tablecloth.exceptions.TableclothDomainValidationException
import tablecloth.model.domain.creatures.PlayerCharacter
import tablecloth.model.domain.messages.Inbox
import tablecloth.model.domain.users.Role
import tablecloth.model.domain.users.User
import tablecloth.model.domain.users.UserRole
import tablecloth.viewmodel.UserViewmodel

@GrailsCompileStatic
class UserService {

    SecurityService securityService

    List<UserViewmodel> getUsers() {
        User currentUser = securityService.loggedInUser
        return User.getAll().collect { User user ->
            createViewmodel(user, user == currentUser)
        }
    }

    UserViewmodel getCurrentUserViewmodel(String username) {
        User user = User.findByUsername(username)
        return getCurrentUserViewmodel(user as User)
    }

    UserViewmodel getCurrentUserViewmodel() {
        return getCurrentUserViewmodel(securityService.loggedInUser)
    }

    UserViewmodel getCurrentUserViewmodel(User user) {
        if (!user) {
            return null
        }
        return createViewmodel(user, true)
    }

    @Transactional
    void addUser(String username, String pw) {
        assertUserDoesNotAlreadyExist(username)
        User newUser = new User(username: username, password: pw)
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
    void removeUser(String username) {
        User user = User.getUserByNameAssertExists(username)
        UserRole.removeAll(user)
        user.delete(flush: true)
        log.info("Deleted $user")
    }

    private static void assertUserDoesNotAlreadyExist(String username) {
        User user = User.findByUsername(username)
        if (user) {
            throw new TableclothDomainValidationException("A user with the name $username already exists.")
        }
    }

    private UserViewmodel createViewmodel(User user, boolean isCurrentUser) {
        int pcCount = PlayerCharacter.countByOwner(user)
        boolean isAdmin = securityService.isAdmin(user)
        return new UserViewmodel(
            name: user.username,
            isAdmin: isAdmin,
            pcCount: pcCount,
            isCurrentUser: isCurrentUser,
        )
    }

}
