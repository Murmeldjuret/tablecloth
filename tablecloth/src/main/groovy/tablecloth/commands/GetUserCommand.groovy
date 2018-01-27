package tablecloth.commands

import grails.validation.Validateable

class GetUserCommand implements Validateable {

    String name

    static constraints = {
        name nullable: false, blank: false
    }
}
