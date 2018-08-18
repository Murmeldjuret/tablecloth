package tablecloth.gen.country

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty

class CountryData {

    @JsonProperty
    CountryType type
    @JsonProperty
    String name
    @JsonProperty
    String code
    @JsonProperty
    String desc
    @JsonProperty
    Integer baseweight
    @JsonProperty
    Integer basesize
    @JsonProperty
    Double basechance
    @JsonProperty
    Boolean mandatory
    @JsonProperty
    Double urbanization

    @JsonProperty
    List<String> newTags
    @JsonProperty
    List<String> lovesTags
    @JsonProperty
    List<String> likesTags
    @JsonProperty
    List<String> dislikesTags
    @JsonProperty
    List<String> hatesTags
    @JsonProperty
    List<String> requiresTags

    @JsonProperty
    List<String> requiresChoices

    @JsonIgnore
    List<String> getTags() {
        return newTags + likesTags + hatesTags + requiresTags + dislikesTags + lovesTags
    }

    @Override
    String toString() {
        return code
    }

}
