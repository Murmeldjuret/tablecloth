package tablecloth.gen.country

import grails.compiler.GrailsCompileStatic
import grails.gorm.transactions.Transactional
import tablecloth.gen.ConfigService
import tablecloth.rng.RandomService
import tablecloth.viewmodel.gen.ClassListViewmodel
import tablecloth.viewmodel.gen.CountryDataViewmodel
import tablecloth.viewmodel.gen.GovDataViewmodel

@Transactional
@GrailsCompileStatic
class CountryGeneratorService {

    ConfigService configService
    RandomService randomService

    CountryDataViewmodel generate(Collection<String> startTags) {
        Generator cfg = configService.createNewGenerator(startTags)
        GovDataViewmodel govData = generateGov(cfg)
        Collection<ClassListViewmodel> clsList = generateCountry(cfg)
        CountryDataViewmodel cdv = CountryDataViewmodel.build(clsList, govData)
        return cdv
    }

    private GovDataViewmodel generateGov(Generator cfg) {
        GovStruct struct = selectCandidate(cfg.govStructs.structs, cfg)
        List<GovData> chosen = [] as List<GovData>
        struct.has.each { GovType type ->
            List<GovData> dataCandidates = cfg.getGovCatsByType(type)
            GovData choice = selectCandidate(dataCandidates, cfg)
            chosen << choice
        }
        return buildGovViewmodel(struct, chosen)
    }

    private <T extends TagAppraiserWithNew> T selectCandidate(Collection<T> cand, Generator cfg) {
        Map<T, Double> buckets = buildBuckets(cand, cfg)
        T choice = randomService.chooseBucket(buckets)
        cfg.addNewTags(choice.newTags)
        return choice
    }

    private <T extends TagAppraiser> Map<T, Double> buildBuckets(Collection<T> cand, Generator cfg) {
        Map<T, Double> buckets = [:] as Map<T, Double>
        cand.each { T candidate ->
            if (candidate.isCompatible(cfg.currentTags)) {
                Double factor = candidate.appreciate(cfg)
                buckets[(candidate)] = factor
            }
        }
        return buckets
    }

    private Collection<ClassListViewmodel> generateCountry(Generator cfg) {
        Collection<ClassesData> classes = cfg.getClassDataFor(CountryType.CLASSES)
        Collection<ClassListViewmodel> clsList = [] as Collection<ClassListViewmodel>
        classes.each { ClassesData data ->
            if (shouldIncludeClass(cfg, data)) {
                clsList += buildViewmodel(cfg, data)
            } else if (data.mandatory) {
                clsList += buildMarginalViewmodel(cfg, data)
            } else {
                clsList += emptyViewmodel(data)
            }
        }
        addFoodEfficiency(clsList, cfg)
        addSizeModifier(clsList, cfg)
        ensurePopRequirement(clsList)
        clsList.sort(true) { ClassListViewmodel cls -> 0 - cls.wealth }
        return clsList
    }

    private boolean shouldIncludeClass(Generator cfg, ClassesData data) {
        if (!data.isCompatible(cfg.currentTags)) {
            return false
        }
        double chance = data.basechance
        chance *= data.appreciateExtremes(cfg)
        return randomService.rollPercent(chance)
    }

    private ClassListViewmodel buildViewmodel(Generator cfg, ClassesData data) {
        Double factor = data.appreciate(cfg)
        Double size = calculateSize(data, factor)
        return new ClassListViewmodel(
            name: data.name,
            desc: data.desc,
            households: size.round(),
            population: (size * data.popPerHousehold * randomService.noise()).round(),
            wealth: (size * data.baseweight * randomService.noise()).round(),
            urban: (size * data.popPerHousehold * data.urbanization * randomService.noise()).round(),
            militarization: (size * data.militarization * randomService.noise()).round(),
            food: (size * data.food * data.popPerHousehold),
        )
    }

    private GovDataViewmodel buildGovViewmodel(GovStruct struct, List<GovData> chosen) {
        return new GovDataViewmodel(
            name: struct.name,
            data: chosen.first().name,
        )
    }

    private double calculateSize(ClassesData data, Double factor) {
        double size = (data.basesize * factor * randomService.noise()).round()
        if (size < 1) {
            size = 1d
        }
        return size
    }

    private ClassListViewmodel buildMarginalViewmodel(Generator cfg, ClassesData data) {
        ClassListViewmodel model = buildViewmodel(cfg, data)
        model.wealth = (model.wealth * 0.01d).round()
        model.households = (model.households * 0.1d).round()
        model.population = (model.population * 0.1d).round()
        model.urban = (model.urban * 0.1d).round()
        model.food = (model.food * data.popPerHousehold * 0.1d).round()
        model.militarization = (model.households * data.militarization * 0.1d).round()
        if (model.households < 1) {
            model.households = 1L
        }
        return model
    }

    private void addSizeModifier(Collection<ClassListViewmodel> cls, Generator cfg) {
        cls.each { ClassListViewmodel model ->
            Double factor = cfg.countryConfig.getSizeModifiers(cfg.currentTags) * randomService.noise()
            if (model != null && model.households > 0 && model.name != 'Royalty') {
                model.households = (model.households * factor).toLong()
                model.population = (model.population * factor).toLong()
                model.militarization = (model.militarization * factor).toLong()
                model.urban = (model.urban * factor).toLong()
                model.food = (model.food * factor)
            }
        }
    }

    static
    private void ensurePopRequirement(Collection<ClassListViewmodel> classes) {
        classes.each { ClassListViewmodel cls ->
            if (cls.population < cls.households) {
                cls.population = cls.households
            }
        }
    }

    static
    private ClassListViewmodel emptyViewmodel(ClassesData data) {
        return new ClassListViewmodel(
            name: data.name,
            desc: data.desc,
            households: 0L,
            population: 0L,
            wealth: 0L,
            urban: 0L,
            militarization: 0L,
            food: 0.0d,
        )
    }

    static
    private void addFoodEfficiency(Collection<ClassListViewmodel> cls, Generator cfg) {
        Double factor = cfg.countryConfig.getFoodEfficiency(cfg.currentTags)
        cls.each { ClassListViewmodel model ->
            if (model != null && model.households > 0) {
                model.food *= factor
            }
        }
    }
}
