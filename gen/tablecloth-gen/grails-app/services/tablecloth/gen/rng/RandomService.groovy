package tablecloth.gen.rng

import groovy.transform.CompileStatic

@CompileStatic
class RandomService {

    private static final Random rnd = new Random()

    int getInt(int roof) {
        return rnd.nextInt(roof)
    }

}
