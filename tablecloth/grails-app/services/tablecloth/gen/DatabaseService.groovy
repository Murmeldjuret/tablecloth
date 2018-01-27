package tablecloth.gen

import grails.gorm.transactions.Transactional
import groovy.transform.CompileStatic
import org.grails.datastore.gorm.GormEntity

@CompileStatic
class DatabaseService {

    @Transactional
    def save(GormEntity... objs) {
        log.info("Saving ${objs.size()} objects to database.")
        objs.each { GormEntity entity ->
            log.debug("Saving $entity to database.")
            entity.save()
        }
    }

    @Transactional
    def delete(GormEntity... objs) {
        log.info("Deleting ${objs.size()} objects from database.")
        objs.each { GormEntity entity ->
            log.debug("Deleting $entity from database.")
            entity.delete()
        }
    }
}
