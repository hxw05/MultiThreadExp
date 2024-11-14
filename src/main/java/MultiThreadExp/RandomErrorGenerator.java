package MultiThreadExp;

import java.util.Random;

public class RandomErrorGenerator {
    private final Random rand = new Random();
    private final double poss;

    public RandomErrorGenerator(double possibility) {
        this.poss = possibility;
    }

    public boolean getError() {
        var r = rand.nextDouble();
        System.out.println("LOG >> 模拟错误随机值=" + r);
        return r < poss;
    }
}
