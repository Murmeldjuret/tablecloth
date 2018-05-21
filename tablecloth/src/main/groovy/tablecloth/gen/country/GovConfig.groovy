package tablecloth.gen.country

import tablecloth.gen.ConfigHolder

class GovConfig extends ConfigHolder<GovData> {

    static constraints = {
        version nullable: false
        tags nullable: false, empty: false
        data empty: false, validator: { val, obj ->
            val.every { GovData govData ->
                govData.tags.each {
                    obj.tags.containsKey(it)
                }
            }
        }
    }
}
