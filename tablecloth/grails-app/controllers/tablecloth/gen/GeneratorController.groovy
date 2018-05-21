package tablecloth.gen

import grails.compiler.GrailsCompileStatic
import grails.plugin.springsecurity.annotation.Secured
import tablecloth.commands.PersonNameCommand
import tablecloth.security.SecurityService
import tablecloth.utils.ValidatableResponseUtil
import tablecloth.viewmodel.gen.ClassListViewmodel

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
        Collection<String> tags = params.list('tags')
        Collection<ClassListViewmodel> list = generatorService.generateCountry(tags).sort(true) { ClassListViewmodel cls -> 0 - cls.wealth }
        Long totalSize = list.sum { ClassListViewmodel cls -> cls.size } as Long
        Long totalUrban = list.sum { ClassListViewmodel cls -> cls.urban } as Long
        render view: '/country/country', model: [classes: list, tags: tags, totalSize: totalSize, totalUrban: totalUrban]
    }
}
