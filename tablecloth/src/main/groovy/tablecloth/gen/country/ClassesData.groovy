package tablecloth.gen.country

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty

class ClassesData {

    @JsonProperty
    CountryType type
    @JsonProperty
    String name
    @JsonProperty
    String code
    @JsonProperty
    String desc
    @JsonProperty
    Double baseweight
    @JsonProperty
    Integer basesize
    @JsonProperty
    Double basechance
    @JsonProperty
    Boolean mandatory
    @JsonProperty
    Double urbanization
    @JsonProperty
    Double popPerHousehold
    @JsonProperty
    Double militarization
    @JsonProperty
    Double food

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
    List<String> blockerTags

    @JsonIgnore
    List<String> getTags() {
        return likesTags + hatesTags + requiresTags + dislikesTags + lovesTags + blockerTags
    }

    @Override
    String toString() {
        return code
    }

}
