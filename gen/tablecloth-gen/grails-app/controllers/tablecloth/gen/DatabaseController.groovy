package tablecloth.gen

import grails.web.Action

class DatabaseController {

    static defaultAction = "reload"

    DatabaseService databaseService

    @Action
    void reload() {


        render "Successfully reloaded database!"
    }


}
