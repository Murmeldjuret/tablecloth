package tablecloth.commands

import grails.compiler.GrailsCompileStatic
import grails.validation.Validateable

@GrailsCompileStatic
class DeletePersonCommand implements Validateable {

    String characterName
    String returntoUserAfterDeletion
    Long charId

    static constraints = {
        characterName nullable: true
        charId nullable: false
        returntoUserAfterDeletion nullable: true
    }

}
