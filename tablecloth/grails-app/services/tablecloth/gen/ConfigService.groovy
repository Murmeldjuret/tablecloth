package tablecloth.gen

import com.fasterxml.jackson.databind.ObjectMapper
import grails.gorm.transactions.Transactional
import tablecloth.gen.country.ClassesConfig
import tablecloth.gen.country.GovConfig
import tablecloth.gen.country.TagConfig

import javax.annotation.PostConstruct

@Transactional
class ConfigService {

    private static final String govPath = "gendata/govcfg.json"
    private static final String countryPath = "gendata/classes.json"
    private static final String tagsPath = "gendata/tags.json"

    private static GovConfig govCfgDefault
    private static ClassesConfig classesCfgDefault
    private static TagConfig tagsCfgDefault

    @PostConstruct
    init() {
        govCfgDefault = readConfig(govPath, GovConfig)
        classesCfgDefault = readConfig(countryPath, ClassesConfig)
        tagsCfgDefault = readConfig(tagsPath, TagConfig)
    }

    ClassesConfig getCountry() {
        return classesCfgDefault
    }

    GovConfig getGovernment() {
        return govCfgDefault
    }

    TagConfig getTags() {
        return tagsCfgDefault
    }

    private <T> T readConfig(String path, Class<T> type) {
        InputStream read = this.class.classLoader.getResourceAsStream(path)
        return new ObjectMapper().readValue(read, type)
    }
}
