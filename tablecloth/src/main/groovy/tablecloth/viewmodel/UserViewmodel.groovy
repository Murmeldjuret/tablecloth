package tablecloth.viewmodel

import grails.compiler.GrailsCompileStatic

@GrailsCompileStatic
class UserViewmodel {

    String name
    int pcCount
    boolean isAdmin
    boolean isCurrentUser

}
