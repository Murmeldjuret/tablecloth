package tablecloth.gen.country

import org.codehaus.jackson.annotate.JsonProperty

class GovStruct {

    @JsonProperty
    String code
    @JsonProperty
    String name

    @JsonProperty
    List<GovType> has

}
