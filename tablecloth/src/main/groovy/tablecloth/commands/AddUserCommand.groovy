package tablecloth.commands

import grails.compiler.GrailsCompileStatic
import grails.validation.Validateable

@GrailsCompileStatic
class AddUserCommand implements Validateable {

    String name
    String pw

    static constraints = {
        name nullable: false, blank: false
        pw nullable: false, blank: false
    }
}
