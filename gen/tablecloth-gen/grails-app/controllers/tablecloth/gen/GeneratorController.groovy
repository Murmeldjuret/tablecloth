package tablecloth.gen

import grails.plugin.springsecurity.annotation.Secured
import tablecloth.gen.security.SecurityService

@Secured('ROLE_USER')
class GeneratorController {

    static defaultAction = "person"

    GeneratorService generatorService
    SecurityService securityService

    def person(String name) {
        generatorService.generatePerson(name ?: 'bob', securityService.user)
        redirect controller: 'characters'
    }
}
