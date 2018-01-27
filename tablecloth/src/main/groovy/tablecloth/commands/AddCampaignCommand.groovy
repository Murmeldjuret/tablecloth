package tablecloth.commands

import grails.validation.Validateable

class AddCampaignCommand implements Validateable {

    String name
    String description

    static constraints = {
        name nullable: false, blank: false
        description nullable: true, blank: true
    }
}
