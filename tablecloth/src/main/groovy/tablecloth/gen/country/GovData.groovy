package tablecloth.gen.country

import com.fasterxml.jackson.annotation.JsonProperty

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
    Integer baseweight

    @Override
    String toString() {
        return code
    }

}
