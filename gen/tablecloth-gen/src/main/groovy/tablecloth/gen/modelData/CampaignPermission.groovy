package tablecloth.gen.modelData

enum CampaignPermission {

    EDIT_CAMPAIGN('Edit campaign'),
    EDIT_PC('Edit player character'),
    EDIT_PARTY_PROPERTIES('Edit party properties'),
    EDIT_JOURNAL('Edit player journal'),
    ACCEPTED_INVITATION('Player'),
    FORMER_PLAYER('Former player'),
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