package tablecloth.gen.commands

import grails.validation.Validateable

class DeletePersonCommand implements Validateable {

    String characterName
    String returntoUser
    int charId

    static constraints = {
        characterName nullable: true
        charId nullable: false
        returntoUser nullable: true
    }

}
