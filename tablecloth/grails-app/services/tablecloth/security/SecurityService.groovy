package tablecloth.security

import org.springframework.security.core.context.SecurityContextHolder
import tablecloth.model.domain.users.User

class SecurityService {

    User getLoggedInUser() {
        String username = SecurityContextHolder?.getContext()?.getAuthentication()?.getPrincipal()?.username ?: ''
        return User.findByUsername(username)
    }

    boolean isAdmin(User user) {
        return user.authorities.find { it.authority == 'ROLE_ADMIN' }
    }
}
