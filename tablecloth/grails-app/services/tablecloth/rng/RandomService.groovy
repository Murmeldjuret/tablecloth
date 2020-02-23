package tablecloth.rng

import groovy.transform.CompileStatic

import java.security.SecureRandom

@CompileStatic
class RandomService {

    private static final SecureRandom rnd = new SecureRandom()

    int getInt(int roof) {
        return rnd.nextInt(roof)
    }

    double noise(int iterations = 25) {
        double factor = 1.0
        (1..iterations).each {
            factor += rnd.nextDouble()
        }
        factor *= 2 / iterations
        return factor
    }

    boolean rollPercent(double val) {
        if (val >= 1) {
            return true
        }
        if (val <= 0) {
            return false
        }
        return rnd.nextDouble() <= val
    }

    Object chooseBucket(Map<? extends Object, Double> buckets) {
        Double total = buckets.values().sum() as Double
        Double jump = rnd.nextDouble() * total
        Double totalJumped = 0.0d
        def selected = buckets.find { Object cand, Double weight ->
            totalJumped += weight
            if (totalJumped > jump) {
                return true
            }
            return false
        }
        return selected.key
    }
}
