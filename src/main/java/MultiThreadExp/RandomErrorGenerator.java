package MultiThreadExp;

import java.util.Random;

public class RandomErrorGenerator {
    private final Random rand = new Random();
    private final double poss;

    public RandomErrorGenerator(double possibility) {
        this.poss = possibility;
    }

    public boolean getError() {
        return rand.nextDouble() < poss;
    }
}
