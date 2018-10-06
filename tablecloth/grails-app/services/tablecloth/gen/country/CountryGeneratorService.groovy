package tablecloth.gen.country

import grails.compiler.GrailsCompileStatic
import grails.gorm.transactions.Transactional
import tablecloth.gen.ConfigService
import tablecloth.rng.RandomService
import tablecloth.viewmodel.gen.ClassListViewmodel

@Transactional
@GrailsCompileStatic
class CountryGeneratorService {

    ConfigService configService
    RandomService randomService

    Collection<ClassListViewmodel> generate(Collection<String> startTags) {
        ClassesConfig cls = configService.cls
        TagConfig tagData = configService.tags
        GovConfig gov = configService.government
        CountryConfig contCfg = configService.country
        return generateCountry(startTags, cls, tagData, contCfg)
    }

    private Collection<ClassListViewmodel> generateCountry(Collection<String> startTags, ClassesConfig cls, TagConfig tagData, CountryConfig country) {
        Collection<String> tags = startTags
        Map<String, Double> availableTags = tagData.tags
        Collection<ClassesData> classes = cls.data.findAll { it.type == CountryType.CLASSES }
        Collection<ClassListViewmodel> response = []
        classes.each { ClassesData data ->
            if (shouldIncludeClass(tags, data, availableTags)) {
                response += buildViewmodel(tags, data, availableTags)
            } else if (data.mandatory) {
                response += buildMarginalViewmodel(tags, data, availableTags)
            } else {
                response += emptyViewmodel(data)
            }
        }
        addFoodEfficiency(response, country, startTags)
        addSizeModifier(response, country, startTags)
        ensurePopRequirement(response)
        return response
    }

    private boolean shouldIncludeClass(Collection<String> chosen, ClassesData data, Map<String, Double> available) {
        if (!requiredTagsFulfilled(data.requiresTags, chosen)) {
            return false
        }
        if (blockerTagsFulfilled(data.blockerTags, chosen)) {
            return false
        }
        double chance = data.basechance
        chance *= getValueOfTags(chosen, data.lovesTags, available)
        chance /= getValueOfTags(chosen, data.hatesTags, available)
        return randomService.rollPercent(chance)
    }

    private ClassListViewmodel buildViewmodel(Collection<String> chosen, ClassesData data, Map<String, Double> available) {
        double factor = 1.0
        factor *= getValueOfTags(chosen, data.likesTags, available)
        factor /= getValueOfTags(chosen, data.dislikesTags, available)
        factor *= Math.pow(getValueOfTags(chosen, data.lovesTags, available), 2)
        factor /= Math.pow(getValueOfTags(chosen, data.hatesTags, available), 2)
        double size = calculateSize(data, factor)
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

    private double calculateSize(ClassesData data, double factor) {
        double size = (data.basesize * factor * randomService.noise()).round()
        if (size < 1) {
            size = 1d
        }
        return size
    }

    private ClassListViewmodel buildMarginalViewmodel(Collection<String> chosen, ClassesData data, Map<String, Double> available) {
        ClassListViewmodel model = buildViewmodel(chosen, data, available)
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

    private void addSizeModifier(Collection<ClassListViewmodel> cls, CountryConfig cfg, Collection<String> tags) {
        cls.each { ClassListViewmodel model ->
            Double factor = cfg.getSizeModifiers(tags) * randomService.noise()
            if(model != null && model.households > 0 && model.name != 'Royalty') {
                model.households = (model.households * factor).toLong()
                model.population = (model.population * factor).toLong()
                model.militarization = (model.militarization * factor).toLong()
                model.urban = (model.urban * factor).toLong()
                model.food = (model.food * factor).toLong()
            }
        }
    }

    static
    private void ensurePopRequirement(Collection<ClassListViewmodel> classes) {
        classes.each { ClassListViewmodel cls ->
            if(cls.population < cls.households) {
                cls.population = cls.households
            }
        }
    }

    static
    private boolean requiredTagsFulfilled(Collection<String> requirements, Collection<String> tags) {
        requirements.every { String tag -> tags.contains(tag) }
    }

    static
    private boolean blockerTagsFulfilled(Collection<String> blockers, Collection<String> tags) {
        blockers.any { String tag -> tags.contains(tag) }
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
    private void addFoodEfficiency(Collection<ClassListViewmodel> cls, CountryConfig cfg, Collection<String> tags) {
        Double factor = cfg.getFoodEfficiency(tags)
        cls.each { ClassListViewmodel model ->
            if(model != null && model.households > 0) {
                model.food *= factor
            }
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
