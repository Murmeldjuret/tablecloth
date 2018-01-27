package tablecloth.modelData

import grails.compiler.GrailsCompileStatic

@GrailsCompileStatic
enum CampaignPermission {

    EDIT_CAMPAIGN('Edit campaign'),
    EDIT_PC('Edit player character'),
    EDIT_PARTY_PROPERTIES('Edit party properties'),
    EDIT_JOURNAL('Edit player journal'),
    VIEW('View rights')

    String desc

    CampaignPermission(String desc) {
        this.desc = desc
    }

    static List<CampaignPermission> masterPermissions() {
        return [VIEW, EDIT_CAMPAIGN, EDIT_PARTY_PROPERTIES]
    }

    static List<CampaignPermission> defaultPermissions() {
        return [VIEW, EDIT_PC, EDIT_JOURNAL, EDIT_PARTY_PROPERTIES]
    }

}