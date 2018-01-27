package tablecloth.commands

import grails.compiler.GrailsCompileStatic
import grails.validation.Validateable

@GrailsCompileStatic
class DeletePersonCommand implements Validateable {

    String characterName
    String returntoUser
    Integer charId

    static constraints = {
        characterName nullable: true
        charId nullable: false
        returntoUser nullable: true
    }

}
