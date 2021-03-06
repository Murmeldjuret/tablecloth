package tablecloth.commands

import grails.compiler.GrailsCompileStatic
import grails.validation.Validateable

@GrailsCompileStatic
class PersonNameCommand implements Validateable {

    String name

    static constraints = {
        name nullable: false, blank: false
    }

}
