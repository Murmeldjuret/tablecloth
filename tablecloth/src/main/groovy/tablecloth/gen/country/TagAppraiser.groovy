package tablecloth.gen.country

import com.fasterxml.jackson.annotation.JsonProperty

trait TagAppraiser {

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

}