package tablecloth.gen.rng

class RandomService {

    private static final Random rnd = new Random()

    int getInt(int roof) {
        return rnd.nextInt(roof)
    }

}
