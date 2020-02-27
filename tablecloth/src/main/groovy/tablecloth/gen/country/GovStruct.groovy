package tablecloth.gen.country

import org.codehaus.jackson.annotate.JsonProperty

class GovStruct implements TagAppraiserWithNew {

    @JsonProperty
    String code
    @JsonProperty
    String name

    @JsonProperty
    List<GovType> has

    @Override
    String toString() {
        return code
    }

}
