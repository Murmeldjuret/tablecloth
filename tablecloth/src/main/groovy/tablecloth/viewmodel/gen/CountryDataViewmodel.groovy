package tablecloth.viewmodel.gen

class CountryDataViewmodel {

    Long totalSize
    Long totalWealth
    Long totalUrban
    Long totalMil
    Long totalFood

    static CountryDataViewmodel build(Collection<ClassListViewmodel> list) {
        list.sort(true) { ClassListViewmodel cls -> 0 - cls.wealth }
        CountryDataViewmodel ret = new CountryDataViewmodel()
        ret.totalSize = list.sum { ClassListViewmodel cls -> cls.size } as Long
        ret.totalWealth = list.sum { ClassListViewmodel cls -> cls.wealth } as Long
        ret.totalUrban = list.sum { ClassListViewmodel cls -> cls.urban } as Long
        ret.totalMil = list.sum { ClassListViewmodel cls -> cls.militarization } as Long
        ret.totalFood = list.sum { ClassListViewmodel cls -> cls.food.round() } as Long
        return ret
    }
}
