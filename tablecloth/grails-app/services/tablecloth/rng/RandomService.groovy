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
        double factor = 0.0
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

    Double valueBetween(Double lower, Double upper) {
        return (rnd.nextDouble() * (upper - lower)) + lower
    }

    def <T> T chooseBucket(Map<T, Double> buckets) {
        Double total = buckets.values().sum() as Double
        Double jump = rnd.nextDouble() * total
        Double totalJumped = 0.0d
        Map.Entry<T, Double> selected = buckets.find { T cand, Double weight ->
            totalJumped += weight
            if (totalJumped > jump) {
                return true
            }
            return false
        }
        log.debug("Chose $selected.key with a chance of $selected.value / $total")
        return selected.key
    }
}
