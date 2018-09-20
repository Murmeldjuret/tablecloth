package tablecloth.gen.country

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty

class CountryConfig {

    @JsonProperty
    Integer version

    @JsonProperty
    Map<String, Double> baseFoodEfficiency

    @JsonIgnore
    Double getFoodEfficiency(String age, String prosperity) {
        Double ageFactor = baseFoodEfficiency.get(age) ?: 1.0d
        Double prosperityFactor = baseFoodEfficiency.get(prosperity) ?: 1.0d
        return ageFactor * prosperityFactor
    }
}
