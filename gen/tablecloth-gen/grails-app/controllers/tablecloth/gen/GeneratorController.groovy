package tablecloth.gen

import grails.plugin.springsecurity.annotation.Secured
import tablecloth.gen.commands.GenerateCharacterCommand
import tablecloth.gen.security.SecurityService
import tablecloth.gen.utils.ValidatableResponseUtil

@Secured('ROLE_USER')
class GeneratorController {

    GeneratorService generatorService
    SecurityService securityService

    def index() {
        redirect controller: 'characters'
    }

    def person(GenerateCharacterCommand cmd) {
        if (!cmd?.validate()) {
            flash.message = "Could not generate character, input ${ValidatableResponseUtil.errorcount(cmd)}"
            redirect controller: 'characters'
            return
        }
        boolean success = generatorService.generatePerson(cmd, securityService.user)
        if (success) {
            flash.message = "Character $cmd.name successfully created!"
        } else {
            flash.message = "Failed to create character $cmd.name"
        }
        redirect controller: 'characters'
    }
}
