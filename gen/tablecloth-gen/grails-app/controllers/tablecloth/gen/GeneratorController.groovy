package tablecloth.gen

import grails.plugin.springsecurity.annotation.Secured
import grails.web.Action

@Secured('ROLE_USER')
class GeneratorController {

    static defaultAction = "person"

    GeneratorService generatorService

    @Action
    void person() {

        render(text: "<xml>some xml</xml>", contentType: "text/xml", encoding: "UTF-8")
    }
}
