package tablecloth.gen

import com.fasterxml.jackson.annotation.JsonProperty
import grails.validation.Validateable

abstract class ConfigHolder<TData> implements Validateable {

    @JsonProperty
    Integer version

    @JsonProperty
    Map<String, Integer> tags

    @JsonProperty
    List<TData> data

}
