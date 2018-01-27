package tablecloth.commands

import grails.validation.Validateable

class SingleStringCommand implements Validateable {

    String name

    static constraints = {
        name nullable: false, blank: false
    }

}
