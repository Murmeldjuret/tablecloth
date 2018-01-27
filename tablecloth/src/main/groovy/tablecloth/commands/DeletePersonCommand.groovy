package tablecloth.commands

import grails.validation.Validateable

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
