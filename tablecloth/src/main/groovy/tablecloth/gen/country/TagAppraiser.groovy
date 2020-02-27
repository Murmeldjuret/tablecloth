package tablecloth.gen.country

import com.fasterxml.jackson.annotation.JsonProperty
import org.codehaus.jackson.annotate.JsonIgnore

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

    @JsonIgnore
    Double getFactor() {
        return 1.0d
    }

    @JsonIgnore
    Double appreciate(Generator cfg) {
        Double factor = getFactor()
        factor *= getValueOfTags(cfg.currentTags, likesTags, cfg.allAvailableTags)
        factor /= getValueOfTags(cfg.currentTags, dislikesTags, cfg.allAvailableTags)
        factor *= Math.pow(getValueOfTags(cfg.currentTags, lovesTags, cfg.allAvailableTags), 2)
        factor /= Math.pow(getValueOfTags(cfg.currentTags, hatesTags, cfg.allAvailableTags), 2)
        return factor as Double
    }

    @JsonIgnore
    Double appreciateExtremes(Generator cfg) {
        Double factor = getFactor()
        factor *= getValueOfTags(cfg.currentTags, lovesTags, cfg.allAvailableTags)
        factor /= getValueOfTags(cfg.currentTags, hatesTags, cfg.allAvailableTags)
        return factor as Double
    }

    @JsonIgnore
    Boolean isCompatible(Collection<String> tags) {
        return requiresTags.every {
            it in tags
        } && blockerTags.every {
            !(it in tags)
        }
    }

    static
    private double getValueOfTags(Collection<String> selected, Collection<String> options, Map<String, Double> values) {
        double factor = 1.0
        selected.each { String key ->
            if (options.contains(key) && values.containsKey(key)) {
                factor *= values.get(key)
            }
        }
        return factor
    }

}