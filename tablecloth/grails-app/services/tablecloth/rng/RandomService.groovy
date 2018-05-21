package tablecloth.rng

import groovy.transform.CompileStatic

import java.security.SecureRandom

@CompileStatic
class RandomService {

    private static final SecureRandom rnd = new SecureRandom()

    int getInt(int roof) {
        return rnd.nextInt(roof)
    }

    boolean rollPercent(double val) {
        if(val >= 1) {
            return true
        }
        if(val <= 0) {
            return false
        }
        return rnd.nextDouble() <= val
    }

}
