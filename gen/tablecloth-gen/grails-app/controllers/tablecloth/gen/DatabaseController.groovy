package tablecloth.gen

import grails.plugin.springsecurity.annotation.Secured
import grails.web.Action

@Secured(['ROLE_ADMIN'])
class DatabaseController {

    static defaultAction = "reload"

    DatabaseService databaseService

    @Action
    void reload() {


        render "Successfully reloaded database!"
    }


}
