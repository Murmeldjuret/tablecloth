package tablecloth.plaza.campaign

import grails.compiler.GrailsCompileStatic
import grails.plugin.springsecurity.annotation.Secured
import tablecloth.commands.AddCampaignCommand
import tablecloth.exceptions.TableclothAccessException
import tablecloth.exceptions.TableclothDomainException
import tablecloth.security.UserService

@GrailsCompileStatic
@Secured('ROLE_USER')
class CampaignController {

    CampaignService campaignService
    UserService userService

    def index() {
        def user = userService.currentUserViewmodel
        def campaigns = campaignService.campaigns
        render view: 'campaigns', model: [campaigns: campaigns, user: user]
    }

    def create(AddCampaignCommand cmd) {
        if (!cmd.validate()) {
            flash.message = "Campaign creation failed due to error $cmd.errors"
            redirect action: 'index'
            return
        }
        campaignService.newCampaign(cmd)
        flash.message = "Campaign successfully created!"
        redirect action: 'index'
    }

    def inviteUser(long campId, String username) {
        try {
            campaignService.addPlayerToCampaign(campId, username)
            flash.message = "User Successfully invited"
        } catch (TableclothDomainException ex) {
            flash.message = "Error with input: $ex.message"
        } catch (TableclothAccessException ex) {
            flash.message = "Error with permissions: $ex.message"
        }
        redirect action: 'index'
    }

    def removeParticipant(long campId, String username) {
        try {
            campaignService.removeParticipant(campId, username)
        } catch (TableclothDomainException ex) {
            flash.message = "Error with input: $ex.message"
        } catch (TableclothAccessException ex) {
            flash.message = "Error with permissions: $ex.message"
        }
        redirect action: 'index'
    }

    def delete(long campId) {
        try {
            campaignService.removeCampaign(campId)
            flash.message = "Campaign deleted successfully!"
        } catch (TableclothDomainException ex) {
            flash.message = "Error with input: $ex.message"
        } catch (TableclothAccessException ex) {
            flash.message = "Error with permissions: $ex.message"
        }
        redirect action: 'index'
    }
}
