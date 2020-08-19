package tablecloth.gen.country

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import grails.validation.Validateable

class GovConfig implements Validateable {

    @JsonProperty
    Integer version

    @JsonProperty
    List<GovData> headOfState
    @JsonProperty
    List<GovData> investment
    @JsonProperty
    List<GovData> franchise

    @JsonIgnore
    List<GovData> getData() {
        return headOfState + investment + franchise
    }

    static constraints = {
        version nullable: false
        headOfState nullable: false, empty: false
        investment nullable: false, empty: false
        franchise nullable: false, empty: false
    }
}
