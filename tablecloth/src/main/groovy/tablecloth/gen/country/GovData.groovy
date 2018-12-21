package tablecloth.gen.country

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
    List<String> lovesTags
    @JsonProperty
    List<String> likesTags
    @JsonProperty
    List<String> dislikesTags
    @JsonProperty
    List<String> hatesTags
    @JsonProperty
    List<String> blockerTags
    @JsonProperty
    List<String> requiresTags

    @Override
    String toString() {
        return code
    }

}
