package tablecloth.gen.country

import tablecloth.gen.ConfigHolder

class CountryConfig extends ConfigHolder<CountryData> {

    static constraints = {
        version nullable: false
        tags nullable: false, empty: false
        data empty: false, validator: { val, obj ->
            val.every { govData ->
                govData.tags.each {
                    obj.tags.containsKey(it)
                }
            }
        }
    }
}
