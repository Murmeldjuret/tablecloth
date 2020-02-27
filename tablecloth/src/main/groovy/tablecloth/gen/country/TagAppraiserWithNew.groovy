package tablecloth.gen.country

import com.fasterxml.jackson.annotation.JsonProperty

trait TagAppraiserWithNew extends TagAppraiser {

    @JsonProperty
    List<String> newTags = [] as List<String>
}