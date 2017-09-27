package tablecloth.gen.auth

import grails.web.Action

class UserController {

    static defaultAction = "login"

    @Action
    def login() {
        session.user = "Bob"
        redirect "/generator/person"
        /* TODO implement actual logic
        if (request.get) {
            return // render the login view
        }

        def u = tablecloth.gen.model.domain.User.findByLogin(params.login)
        if (u) {
            if (u.password == params.password) {
                session.user = u
                redirect(action: "home")
            }
            else {
                render(view: "login", model: [message: "Password incorrect"])
            }
        }
        else {
            render(view: "login", model: [message: "tablecloth.gen.model.domain.User not found"])
        }*/
    }
}
