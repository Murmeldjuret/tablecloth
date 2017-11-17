package tablecloth.gen.commands

import grails.validation.Validateable

class AddPersonCommand implements Validateable {

    String characterName

    static constraints = {
        characterName nullable: false
    }

}
