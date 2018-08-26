package tablecloth.gen.country

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty

class TagConfig {

    @JsonProperty
    Integer version

    @JsonProperty
    Map<String, Double> environment
    @JsonProperty
    Map<String, Double> ages
    @JsonProperty
    Map<String, Double> size
    @JsonProperty
    Map<String, Double> generic

    @JsonIgnore
    Map<String, Double> getTags() {
        return ages + size + environment + generic
    }

}
