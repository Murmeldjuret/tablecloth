package tablecloth.gen

import tablecloth.gen.model.domain.users.Role
import tablecloth.gen.model.domain.users.User
import tablecloth.gen.model.domain.users.UserRole

class BootStrap {

    def init = { servletContext ->
        //*
        if (User.count() == 0) {
            def adminRole = new Role(authority: 'ROLE_ADMIN').save()
            def userRole = new Role(authority: 'ROLE_USER').save()

            def user = new User(username: 'user', password: 'pw').save()
            def admin = new User(username: 'admin', password: 'pw2').save()

            UserRole.create admin, adminRole, true
            UserRole.create admin, userRole, true
            UserRole.create user, userRole, true

            assert User.count() == 2
            assert Role.count() == 2
            assert UserRole.count() == 3
        }//*/
    }
    def destroy = {
    }
}
