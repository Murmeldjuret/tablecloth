package tablecloth.gen

import grails.gorm.transactions.Transactional
import org.grails.datastore.gorm.GormEntity

class DatabaseService {

    @Transactional
    def save(GormEntity... objs) {
        objs.each {
           it.save(flush: true)
        }
    }
}
