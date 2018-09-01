package tablecloth.gen.country

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty

class TagConfig {

    @JsonProperty
    Integer version

    @JsonProperty
    Map<String, Double> environment
    @JsonProperty
    Map<String, Double> ages
    @JsonProperty
    Map<String, Double> fortunes
    @JsonProperty
    Map<String, Double> size
    @JsonProperty
    Map<String, Double> generic

    @JsonIgnore
    Map<String, Double> getTags() {
        return ages + fortunes + size + environment + generic
    }

    @JsonIgnore
    List<String> getEnvironmentList() {
        return environment?.keySet()?.toList() ?: []
    }

    @JsonIgnore
    List<String> getSizeList() {
        return size?.keySet()?.toList() ?: []
    }

    @JsonIgnore
    List<String> getFortunesList() {
        return fortunes?.keySet()?.toList() ?: []
    }

    @JsonIgnore
    List<String> getAgesList() {
        return ages?.keySet()?.toList() ?: []
    }

    @JsonIgnore
    List<String> getGenericList() {
        return generic?.keySet()?.toList() ?: []
    }

}
