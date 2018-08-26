package tablecloth.gen.country

import com.fasterxml.jackson.annotation.JsonProperty
import grails.validation.Validateable

class ClassesConfig implements Validateable {

    @JsonProperty
    Integer version

    @JsonProperty
    List<ClassesData> data

    static constraints = {
        version nullable: false
        data nullable: false, empty: false
    }
}
