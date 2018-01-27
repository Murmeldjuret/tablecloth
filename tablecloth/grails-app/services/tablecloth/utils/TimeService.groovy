package tablecloth.utils

import grails.gorm.transactions.Transactional

@Transactional
class TimeService {

    def getNow() {
        return new Date()
    }
}
