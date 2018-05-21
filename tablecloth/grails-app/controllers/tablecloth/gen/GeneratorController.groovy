package tablecloth.gen

import grails.compiler.GrailsCompileStatic
import grails.plugin.springsecurity.annotation.Secured
import tablecloth.commands.PersonNameCommand
import tablecloth.security.SecurityService
import tablecloth.utils.ValidatableResponseUtil

@GrailsCompileStatic
@Secured('ROLE_USER')
class GeneratorController {

    GeneratorService generatorService
    SecurityService securityService

    def index() {
        redirect controller: 'characters'
    }

    def person(PersonNameCommand cmd) {
        if (!cmd?.validate()) {
            flash.message = "Could not generate character, input ${ValidatableResponseUtil.errorcount(cmd)}"
            redirect controller: 'characters'
            return
        }
        boolean success = generatorService.generatePerson(cmd, securityService.loggedInUser)
        if (success) {
            flash.message = "Character $cmd.name successfully created!"
        } else {
            flash.message = "Failed to create character $cmd.name"
        }
        redirect controller: 'characters'
    }

    def country() {
        String text = generatorService.generateCountry()
        render text: text
    }
}
