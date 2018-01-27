package tablecloth.commands

import grails.compiler.GrailsCompileStatic
import grails.validation.Validateable

@GrailsCompileStatic
class AddCampaignCommand implements Validateable {

    String name
    String description

    static constraints = {
        name nullable: false, blank: false
        description nullable: true, blank: true
    }
}
