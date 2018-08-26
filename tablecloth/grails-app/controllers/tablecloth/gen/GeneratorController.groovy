package tablecloth.gen

import grails.compiler.GrailsCompileStatic
import grails.plugin.springsecurity.annotation.Secured
import tablecloth.commands.PersonNameCommand
import tablecloth.security.SecurityService
import tablecloth.utils.ValidatableResponseUtil
import tablecloth.viewmodel.gen.ClassListViewmodel
import tablecloth.viewmodel.gen.CountryDataViewmodel

@GrailsCompileStatic
@Secured('ROLE_USER')
class GeneratorController {

    ConfigService configService
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
        Collection<String> chosen = params.list('tags')
        Collection<String> available = (configService.tags?.tags?.keySet()?.toList() ?: []) as List<String>
        Collection<ClassListViewmodel> list = generatorService.generateCountry(chosen)
        list.sort(true) { ClassListViewmodel cls -> 0 - cls.wealth }
        CountryDataViewmodel countryData = CountryDataViewmodel.build(list)
        render view: '/country/country', model: [
            classes      : list,
            availableTags: available,
            chosenTags   : chosen,
            countryData  : countryData
        ]
    }
}
