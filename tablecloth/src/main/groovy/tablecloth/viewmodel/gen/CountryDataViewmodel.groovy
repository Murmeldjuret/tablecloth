package tablecloth.viewmodel.gen

class CountryDataViewmodel {

    Long totalHouseholds
    Long totalPop
    Long totalWealth
    Long totalUrban
    Long totalMil
    Long totalFood

    static CountryDataViewmodel build(Collection<ClassListViewmodel> list) {
        list.sort(true) { ClassListViewmodel cls -> 0 - cls.wealth }
        CountryDataViewmodel ret = new CountryDataViewmodel()
        ret.totalHouseholds = list.sum { ClassListViewmodel cls -> cls.households } as Long
        ret.totalPop = list.sum { ClassListViewmodel cls -> cls.population } as Long
        ret.totalWealth = list.sum { ClassListViewmodel cls -> cls.wealth } as Long
        ret.totalUrban = list.sum { ClassListViewmodel cls -> cls.urban } as Long
        ret.totalMil = list.sum { ClassListViewmodel cls -> cls.militarization } as Long
        ret.totalFood = list.sum { ClassListViewmodel cls -> cls.food.round() } as Long
        return ret
    }
}
