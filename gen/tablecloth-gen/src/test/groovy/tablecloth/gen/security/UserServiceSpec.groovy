package tablecloth.gen.security

import grails.test.hibernate.HibernateSpec
import grails.testing.services.ServiceUnitTest
import spock.lang.Shared
import tablecloth.gen.exceptions.TableclothDomainException
import tablecloth.gen.model.domain.messages.Inbox
import tablecloth.gen.model.domain.users.Role
import tablecloth.gen.model.domain.users.User
import tablecloth.gen.model.domain.users.UserRole
import tablecloth.gen.viewmodel.UserViewmodel

class UserServiceSpec extends HibernateSpec implements ServiceUnitTest<UserService> {

    List<Class> getDomainClasses() { [User, Inbox, Role, UserRole] }

    @Shared
    static Map saveParams = [failOnError: true, flush: true]

    void setup() {
        service.securityService = Mock(SecurityService)
        service.securityService.isAdmin(_) >> { args -> args[0].authorities.find { it.authority == 'ROLE_ADMIN' } }
        saveDummyUsers()
    }

    void "test getUsers"() {
        when:
        List<UserViewmodel> users = service.getUsers()

        then:
        1 * service.securityService.getUser() >> new User(username: 'user1')
        users.any { UserViewmodel viewmodel ->
            viewmodel.name == 'admin' &&
                !viewmodel.isCurrentUser &&
                viewmodel.isAdmin &&
                viewmodel.pcCount == 0
        }
        users.any { UserViewmodel viewmodel ->
            viewmodel.name == 'user1' &&
                viewmodel.isCurrentUser &&
                !viewmodel.isAdmin &&
                viewmodel.pcCount == 0
        }
        users.any { UserViewmodel viewmodel ->
            viewmodel.name == 'user2' &&
                !viewmodel.isCurrentUser &&
                !viewmodel.isAdmin &&
                viewmodel.pcCount == 0
        }
    }

    void "test getUser"() {
        when:
        UserViewmodel viewmodel = service.getUser('user1')

        then:
        viewmodel.name == 'user1'
        !viewmodel.isAdmin
        viewmodel.pcCount == 0
    }

    void "test getUser missing"() {
        when:
        UserViewmodel viewmodel = service.getUser('notfound')

        then:
        viewmodel == null
    }

    void "test getUser1"() {
        when:
        UserViewmodel viewmodel = service.getUser(User.findByUsername('user1'))

        then:
        viewmodel.name == 'user1'
        !viewmodel.isAdmin
        viewmodel.pcCount == 0
    }

    void "test addUser"() {
        when:
        service.addUser('user3', '123456')

        then:
        UserViewmodel viewmodel = service.getUser('user3')
        viewmodel.name == 'user3'
        !viewmodel.isAdmin
        viewmodel.pcCount == 0
    }

    void "test addUser not unique"() {
        when:
        service.addUser('user2', '123456')

        then:
        thrown TableclothDomainException
    }

    void "test removeUser"() {
        when:
        assert service.getUser('user2')
        service.removeUser('user2')

        then:
        service.getUser('user2') == null
    }

    void "test removeUser missing"() {
        when:
        assert !service.getUser('user3')
        service.removeUser('user3')

        then:
        thrown TableclothDomainException
    }

    private static void saveDummyUsers() {
        Role adminRole = new Role(
            authority: 'ROLE_ADMIN'
        ).save(saveParams)
        Role userRole = new Role(
            authority: 'ROLE_USER'
        ).save(saveParams)
        User admin = new User(
            username: 'admin',
            password: 'supersecure101'
        )
        admin.inbox = new Inbox(
            owner: admin,
            messages: [].toSet()
        )
        admin.save(saveParams)
        User user1 = new User(
            username: 'user1',
            password: 'supersecure101'
        )
        user1.inbox = new Inbox(
            owner: user1,
            messages: [].toSet()
        )
        user1.save(saveParams)
        User user2 = new User(
            username: 'user2',
            password: 'supersecure101'
        )
        user2.inbox = new Inbox(
            owner: user2,
            messages: [].toSet()
        )
        user2.save(saveParams)

        UserRole.create admin, adminRole, true
        UserRole.create admin, userRole, true
        UserRole.create user1, userRole, true
        UserRole.create user2, userRole, true
    }
}
