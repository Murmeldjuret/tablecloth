package tablecloth.gen.country

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty

class GovData {

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

    @JsonProperty
    List<String> newTags
    @JsonProperty
    List<String> likesTags
    @JsonProperty
    List<String> hatesTags
    @JsonProperty
    List<String> requiresTags

    @JsonProperty
    List<String> requiresChoices

    @JsonIgnore
    List<String> getTags() {
        return newTags + likesTags + hatesTags + requiresTags
    }

    @Override
    String toString() {
        return code
    }

}
