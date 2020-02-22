package tablecloth.gen.country

import com.fasterxml.jackson.annotation.JsonProperty

trait TagAppraiser {

    @JsonProperty
    List<String> lovesTags = [] as List<String>
    @JsonProperty
    List<String> likesTags = [] as List<String>
    @JsonProperty
    List<String> dislikesTags = [] as List<String>
    @JsonProperty
    List<String> hatesTags = [] as List<String>
    @JsonProperty
    List<String> requiresTags = [] as List<String>
    @JsonProperty
    List<String> blockerTags = [] as List<String>

}