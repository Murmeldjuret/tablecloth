package tablecloth.gen

import com.fasterxml.jackson.databind.ObjectMapper
import grails.gorm.transactions.Transactional
import tablecloth.gen.country.CountryConfig
import tablecloth.gen.country.GovConfig

import javax.annotation.PostConstruct

@Transactional
class ConfigService {

    private static String govPath = "gendata/govcfg.json"
    private static String countryPath = "gendata/countrycfg.json"

    private static GovConfig govCfgDefault
    private static CountryConfig countryCfgDefault

    @PostConstruct
    init() {
        govCfgDefault = readConfig(govPath, GovConfig)
        countryCfgDefault = readConfig(countryPath, CountryConfig)
    }

    CountryConfig getCountry() {
        return countryCfgDefault
    }

    GovConfig getGovernment() {
        return govCfgDefault
    }

    private <T extends ConfigHolder> T readConfig(String path, Class<T> type) {
        InputStream read = this.class.classLoader.getResourceAsStream(path)
        ConfigHolder cfg = new ObjectMapper().readValue(read, type)
        return cfg
    }
}
