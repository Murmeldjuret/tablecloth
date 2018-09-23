package tablecloth.viewmodel.gen

import tablecloth.gen.country.TagConfig

class TagChoicesViewmodel {
    List<String> environment = []
    List<String> ages = []
    List<String> fortunes = []
    List<String> size = []
    List<String> generic = []

    static TagChoicesViewmodel build(TagConfig cfg) {
        if (cfg == null) {
            return new TagChoicesViewmodel()
        }
        return new TagChoicesViewmodel(
            environment: cfg.environmentList,
            ages: cfg.agesList,
            fortunes: cfg.fortunesList,
            size: cfg.sizeList,
            generic: cfg.genericList,
        )
    }

    String getChosenEnviron(Collection<String> choices) {
        choices.find { it in environment } ?: 'SMALL_FARMS'
    }

    String getChosenAge(Collection<String> choices) {
        choices.find { it in ages } ?: 'MEDIEVAL'
    }

    String getChosenSize(Collection<String> choices) {
        choices.find { it in size } ?: 'MEDIUM_STATE'
    }

    String getChosenFortune(Collection<String> choices) {
        choices.find { it in fortunes } ?: 'POVERTY'
    }
}
