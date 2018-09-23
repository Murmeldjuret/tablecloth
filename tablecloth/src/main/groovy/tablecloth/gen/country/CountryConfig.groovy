package tablecloth.gen.country

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty

class CountryConfig {

    @JsonProperty
    Integer version

    @JsonProperty
    Map<String, Double> baseFoodEfficiency
    @JsonProperty
    Map<String, Double> baseSizeModifiers

    @JsonIgnore
    Double getFoodEfficiency(Collection<String> tags) {
        double factor = 1.0d
        baseFoodEfficiency.findAll { tags.contains(it.key) }.each { String tag, Double d ->
            factor *= d
        }
        return factor
    }

    @JsonIgnore
    Double getSizeModifiers(Collection<String> tags) {
        double factor = 1.0d
        baseSizeModifiers.findAll { tags.contains(it.key) }.each { String tag, Double d ->
            factor *= d
        }
        return factor
    }
}
