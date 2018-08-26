package tablecloth.gen.country

import com.fasterxml.jackson.annotation.JsonProperty
import grails.validation.Validateable

class GovConfig implements Validateable {

    @JsonProperty
    Integer version

    @JsonProperty
    List<GovData> data

    static constraints = {
        version nullable: false
        data nullable: false, empty: false
    }
}
