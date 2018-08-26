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
        CountryConfig cont = configService.country
        GovConfig gov = configService.government
        return generateCountry(startTags, cont)
    }

    private Collection<ClassListViewmodel> generateCountry(Collection<String> startTags, CountryConfig cfg) {
        Collection<String> tags = startTags
        Collection<CountryData> classes = cfg.data.findAll { it.type == CountryType.CLASSES }
        Collection<ClassListViewmodel> response = []
        classes.each { CountryData data ->
            if (shouldIncludeClass(tags, data, cfg)) {
                response += buildViewmodel(tags, data, cfg)
            } else if (data.mandatory) {
                response += buildMarginalViewmodel(tags, data, cfg)
            } else {
                response += emptyViewmodel(data)
            }
        }
        return response
    }

    private boolean shouldIncludeClass(Collection<String> tags, CountryData data, CountryConfig cfg) {
        if (!requiredTagsFulfilled(data.requiresTags, tags)) {
            return false
        }
        if (blockerTagsFulfilled(data.blockerTags, tags)) {
            return false
        }
        double chance = data.basechance
        chance *= getValueOfTags(tags, data.lovesTags, cfg.tags)
        chance /= getValueOfTags(tags, data.hatesTags, cfg.tags)
        return randomService.rollPercent(chance)
    }

    private ClassListViewmodel buildViewmodel(Collection<String> tags, CountryData data, CountryConfig cfg) {
        double factor = 1.0
        factor *= getValueOfTags(tags, data.likesTags, cfg.tags)
        factor /= getValueOfTags(tags, data.dislikesTags, cfg.tags)
        factor *= Math.pow(getValueOfTags(tags, data.lovesTags, cfg.tags), 2)
        factor /= Math.pow(getValueOfTags(tags, data.hatesTags, cfg.tags), 2)
        double size = calculateSize(tags, data, factor, cfg)
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

    private double calculateSize(Collection<String> tags, CountryData data, double factor, CountryConfig cfg) {
        double size = (data.basesize * factor * randomService.noise()).round()
        tags.findAll { String tag ->
            if (sizeDecreaseTags.contains(tag)) {
                size /= cfg.tags[tag]
            }
            if (sizeIncreaseTags.contains(tag)) {
                size *= cfg.tags[tag] * 10
            }
        }
        if (size < 1) {
            size = 1d
        }
        return size
    }

    private ClassListViewmodel buildMarginalViewmodel(Collection<String> tags, CountryData data, CountryConfig cfg) {
        ClassListViewmodel model = buildViewmodel(tags, data, cfg)
        model.wealth = (model.wealth * 0.05d).round()
        model.size = (model.size * 0.1d).round()
        if (model.size < 1) {
            model.size = 1L
        }
        return model
    }

    private static boolean requiredTagsFulfilled(Collection<String> requirements, Collection<String> tags) {
        requirements.every { String tag -> tags.contains(tag) }
    }

    private static boolean blockerTagsFulfilled(Collection<String> blockers, Collection<String> tags) {
        blockers.any { String tag -> tags.contains(tag) }
    }

    static private ClassListViewmodel emptyViewmodel(CountryData data) {
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
    private double getValueOfTags(Collection<String> selected, Collection<String> options, Map<String, Double> values) {
        double factor = 1.0
        selected.each { String key ->
            if (options.contains(key)) {
                factor *= values.get(key)
            }
        }
        return factor
    }

}
