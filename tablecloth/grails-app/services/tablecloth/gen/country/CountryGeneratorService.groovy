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
        double size = (data.basesize * factor * randomService.noise()).round()
        if (size < 1) {
            size = 1d
        }
        return new ClassListViewmodel(
            name: data.name,
            desc: data.desc,
            size: size.toLong(),
            wealth: (data.baseweight * factor * randomService.noise()).round(),
            urban: (data.basesize * factor * randomService.noise() * data.urbanization).round(),
        )
    }

    private ClassListViewmodel buildMarginalViewmodel(Collection<String> tags, CountryData data, CountryConfig cfg) {
        ClassListViewmodel model = buildViewmodel(tags, data, cfg)
        model.wealth = (model.wealth * 0.05d).round()
        model.size *= (model.size * 0.1d).round()
        if (model.size < 1) {
            model.size = 1L
        }
        return model
    }

    private static boolean requiredTagsFulfilled(Collection<String> requirements, Collection<String> tags) {
        requirements.every { String tag -> tags.contains(tag) }
    }

    static private ClassListViewmodel emptyViewmodel(CountryData data) {
        return new ClassListViewmodel(
            name: data.name,
            desc: data.desc,
            size: 0L,
            wealth: 0L,
            urban: 0L,
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
