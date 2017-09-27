package tablecloth.gen.utils.random

class RandomHelper {

    private static Random random = new Random()

    static int getInt(int roof) {
        return random.nextInt(roof)
    }

}
