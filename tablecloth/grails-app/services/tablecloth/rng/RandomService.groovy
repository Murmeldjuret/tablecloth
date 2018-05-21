package tablecloth.rng

import groovy.transform.CompileStatic

import java.security.SecureRandom

@CompileStatic
class RandomService {

    private static final SecureRandom rnd = new SecureRandom()

    int getInt(int roof) {
        return rnd.nextInt(roof)
    }

}
