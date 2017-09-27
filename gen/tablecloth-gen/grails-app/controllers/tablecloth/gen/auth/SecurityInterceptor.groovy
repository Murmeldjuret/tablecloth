package tablecloth.gen.auth

class SecurityInterceptor {
    SecurityInterceptor() {
        matchAll()
                .except(controller:'user', action:'login')
    }

    boolean before() {
        if (!session.user && actionName != "login") {
            redirect(controller: "user", action: "login")
            return false
        }
        return true
    }
}
