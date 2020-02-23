package tablecloth.gen.country

class Generator {

    Collection<String> currentTags = []

    GovStructConfig govStructs

    GovConfig govCats

    ClassesConfig classes

    TagConfig tags

    CountryConfig countryConfig

    void addNewTags(Collection<String> newTags) {
        currentTags.addAll(newTags)
        currentTags.unique(true)
    }

    List<ClassesData> getClassDataFor(CountryType target) {
        return classes.data.findAll { it.type == target }
    }

    List<GovData> getGovCatsByType(GovType target) {
        return govCats.data.findAll { it.type == target }
    }

    Map<String, Double> getAllAvailableTags() {
        return tags.tags
    }

}
