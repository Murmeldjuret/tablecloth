package tablecloth.gen.country

import com.fasterxml.jackson.annotation.JsonProperty
import org.codehaus.jackson.annotate.JsonIgnore

class GovData implements TagAppraiser {

    @JsonProperty
    GovType type
    @JsonProperty
    String name
    @JsonProperty
    String code
    @JsonProperty
    String desc

    @JsonProperty
    List<String> newTags = [] as List<String>

    @JsonProperty
    Integer baseweight

    @Override
    @JsonIgnore
    Double getFactor() {
        return baseweight
    }

    @Override
    String toString() {
        return code
    }

}
