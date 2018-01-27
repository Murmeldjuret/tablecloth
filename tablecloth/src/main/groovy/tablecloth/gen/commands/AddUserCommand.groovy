package tablecloth.gen.commands

import grails.validation.Validateable

class AddUserCommand implements Validateable {

    String name
    String pw

    static constraints = {
        name nullable: false, blank: false
        pw nullable: false, blank: false
    }
}
