package tablecloth.gen.commands

import grails.validation.Validateable

class GenerateCharacterCommand implements Validateable {

    String name

    static constraints = {
        name nullable: false, blank: false
    }

}
