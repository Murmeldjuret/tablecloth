package tablecloth.gen.country

import grails.compiler.GrailsCompileStatic
import grails.gorm.transactions.Transactional
import tablecloth.gen.ConfigService
import tablecloth.rng.RandomService
import tablecloth.viewmodel.gen.ClassListViewmodel

@Transactional
@GrailsCompileStatic
class CountryGeneratorService {

    private static List<String> sizeIncreaseTags = ["LARGE_STATE", "EMPIRE"]
    private static List<String> sizeDecreaseTags = ["SMALL_STATE"]

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
        addFoodEfficiency(response, country, startTags, tagData)
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
        double size = calculateSize(chosen, data, factor, available)
        return new ClassListViewmodel(
            name: data.name,
            desc: data.desc,
            size: size.round(),
            wealth: (size * data.baseweight * randomService.noise()).round(),
            urban: (size * data.urbanization * randomService.noise()).round(),
            militarization: (size * data.militarization * randomService.noise()).round(),
            food: (size * data.food),
        )
    }

    private double calculateSize(Collection<String> chosen, ClassesData data, double factor, Map<String, Double> available) {
        double size = (data.basesize * factor * randomService.noise()).round()
        chosen.findAll { String tag ->
            if (sizeDecreaseTags.contains(tag)) {
                size /= available[tag]
            }
            if (sizeIncreaseTags.contains(tag)) {
                size *= available[tag] * 10
            }
        }
        if (size < 1) {
            size = 1d
        }
        return size
    }

    private ClassListViewmodel buildMarginalViewmodel(Collection<String> chosen, ClassesData data, Map<String, Double> available) {
        ClassListViewmodel model = buildViewmodel(chosen, data, available)
        model.wealth = (model.wealth * 0.05d).round()
        model.size = (model.size * 0.1d).round()
        model.food = (model.size * 0.1d).round()
        if (model.size < 1) {
            model.size = 1L
        }
        return model
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
            size: 0L,
            wealth: 0L,
            urban: 0L,
            militarization: 0L,
            food: 0.0d,
        )
    }

    static
    private void addFoodEfficiency(Collection<ClassListViewmodel> cls, CountryConfig cfg, Collection<String> tags, TagConfig tagData) {
        String age = tags.find { String tag ->
            tagData.ages.containsKey(tag)
        }
        String fortunes = tags.find { String tag ->
            tagData.fortunes.containsKey(tag)
        }
        Double factor = cfg.getFoodEfficiency(age, fortunes)
        cls.each { ClassListViewmodel model ->
            if(model != null && model.size > 0) {
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
