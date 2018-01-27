package tablecloth

import grails.compiler.GrailsCompileStatic
import grails.gorm.transactions.Transactional
import org.grails.datastore.gorm.GormEntity

@GrailsCompileStatic
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
