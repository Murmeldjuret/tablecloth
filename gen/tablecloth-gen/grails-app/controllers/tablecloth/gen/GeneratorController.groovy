package tablecloth.gen

import grails.web.Action

class GeneratorController {

    static defaultAction = "person"

    GeneratorService generatorService

    @Action
    void person() {

        render(text: "<xml>some xml</xml>", contentType: "text/xml", encoding: "UTF-8")
    }
}
