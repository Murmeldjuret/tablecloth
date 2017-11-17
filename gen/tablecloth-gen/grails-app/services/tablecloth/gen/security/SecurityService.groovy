package tablecloth.gen.security

import org.springframework.security.core.context.SecurityContextHolder
import tablecloth.gen.model.domain.users.User

class SecurityService {

    User getUser() {
        String username = SecurityContextHolder?.getContext()?.getAuthentication()?.getPrincipal()?.username ?: ''
        return User.findByUsername(username)
    }
}