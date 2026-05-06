package internal.boosters;

public class Booster {
    private final int boostGain;
    private final int weight;
    private final int price;

    public Booster(int boostGain, int weight, int price) {
        this.boostGain = boostGain;
        this.weight = weight;
        this.price = price;
    }

    public int getBoostGain() {
        return boostGain;
    }

    public int getWeight() {
        return weight;
    }

    public int getPrice() {
        return price;
    }
}
