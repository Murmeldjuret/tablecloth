package tablecloth.viewmodel.gen

import grails.compiler.GrailsCompileStatic

@GrailsCompileStatic
class CountryDataViewmodel {

    Long totalHouseholds
    Long totalPop
    Long totalWealth
    Long totalUrban
    Double urbanPercent
    Long totalMil
    Long totalFood

    GovDataViewmodel gov
    Collection<ClassListViewmodel> clsList

    static CountryDataViewmodel build(Collection<ClassListViewmodel> list, GovDataViewmodel gov) {
        CountryDataViewmodel ret = new CountryDataViewmodel()
        ret.clsList = list
        ret.totalHouseholds = list.sum { ClassListViewmodel cls -> cls.households } as Long
        ret.totalPop = list.sum { ClassListViewmodel cls -> cls.population } as Long
        ret.totalWealth = list.sum { ClassListViewmodel cls -> cls.wealth } as Long
        ret.totalUrban = list.sum { ClassListViewmodel cls -> cls.urban } as Long
        ret.urbanPercent = (ret.totalUrban / ret.totalPop).toDouble()
        ret.totalMil = list.sum { ClassListViewmodel cls -> cls.militarization } as Long
        ret.totalFood = (list.sum { ClassListViewmodel cls -> cls.food } as Double).round() as Long
        ret.gov = gov
        return ret
    }
}
