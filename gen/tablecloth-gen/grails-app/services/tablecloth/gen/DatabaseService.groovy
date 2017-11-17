package tablecloth.gen

import grails.gorm.transactions.Transactional
import org.grails.datastore.gorm.GormEntity

class DatabaseService {

    @Transactional
    def save(GormEntity... objs) {
        log.info("Saving ${objs.size()} objects to database.")
        objs.each {
            log.debug("Saving $it to database.")
            it.save(flush: true)
        }
    }

    @Transactional
    def delete(GormEntity... objs) {
        log.info("Deleting ${objs.size()} objects from database.")
        objs.each {
            log.debug("Deleting $it from database.")
            it.delete(flush: true)
        }
    }
}
