package tablecloth.gen

import grails.compiler.GrailsCompileStatic
import grails.plugin.springsecurity.annotation.Secured
import tablecloth.commands.PersonNameCommand
import tablecloth.security.SecurityService
import tablecloth.utils.ValidatableResponseUtil
import tablecloth.viewmodel.gen.ClassListViewmodel
import tablecloth.viewmodel.gen.CountryDataViewmodel
import tablecloth.viewmodel.gen.TagChoicesViewmodel

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
        Collection<String> chosen = buildChosen()
        Collection<ClassListViewmodel> list = generatorService.generateCountry(chosen)
        list.sort(true) { ClassListViewmodel cls -> 0 - cls.wealth }
        TagChoicesViewmodel tagChoices = TagChoicesViewmodel.build(configService.tags)
        CountryDataViewmodel countryData = CountryDataViewmodel.build(list)
        render view: '/country/country', model: [
            classes      : list,
            availableTags: tagChoices,
            chosenTags   : chosen,
            countryData  : countryData
        ]
    }

    private Collection<String> buildChosen() {
        Collection<String> tags = params.list('tags').toList()
        tags.add(params.environs as String)
        tags.add(params.ages as String)
        tags.add(params.fortunes as String)
        tags.add(params.sizes as String)
        return tags.grep()
    }
}
