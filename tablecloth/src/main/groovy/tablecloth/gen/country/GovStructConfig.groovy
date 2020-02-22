package tablecloth.gen.country

import com.fasterxml.jackson.annotation.JsonProperty
import grails.validation.Validateable

class GovStructConfig implements Validateable {

    @JsonProperty
    Integer version

    @JsonProperty
    List<GovStruct> structs

    static constraints = {
        version nullable: false
        structs nullable: false, empty: false
    }
}
