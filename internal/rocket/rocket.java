package internal.rocket;

import internal.SpaceLauncher.spacelauncher;
import internal.boosters.Booster;
import internal.capsules.capsule;
import internal.missions.missions;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class rocket {
    public static final int KEROSENE_PRICE_PER_TON = 1200;

    private final spacelauncher launcher;
    private final capsule capsule;
    private final List<Booster> boosters;

    public rocket(spacelauncher launcher, capsule capsule, List<? extends Booster> boosters) {
        if (launcher == null) {
            throw new IllegalArgumentException("launcher ne peut pas être null");
        }
        if (capsule == null) {
            throw new IllegalArgumentException("capsule ne peut pas être null");
        }

        this.launcher = launcher;
        this.capsule = capsule;
        this.boosters = new ArrayList<>();
        if (boosters != null) {
            this.boosters.addAll(boosters);
        }
    }

    public rocket(spacelauncher launcher, capsule capsule) {
        this(launcher, capsule, List.of());
    }

    public spacelauncher getLauncher() {
        return launcher;
    }

    public capsule getCapsule() {
        return capsule;
    }

    public List<Booster> getBoosters() {
        return Collections.unmodifiableList(boosters);
    }

    public void addBooster(Booster booster) {
        if (booster == null) {
            throw new IllegalArgumentException("booster ne peut pas être null");
        }
        boosters.add(booster);
    }

    public int getBoosterCount() {
        return boosters.size();
    }

    public int getRocketPrice() {
        int total = launcher.price + capsule.price;
        for (Booster booster : boosters) {
            total += booster.getPrice();
        }
        return total;
    }

    public double getTotalMass() {
        double total = capsule.Weight;
        for (Booster booster : boosters) {
            total += booster.getWeight();
        }
        return total;
    }

    public double getRequiredFuel(missions mission) {
        if (mission == null) {
            throw new IllegalArgumentException("mission cannot be null");
        }
        return (getTotalMass() * mission.distance * mission.coef) / 1000.0;
    }

    public int getLaunchCost(missions mission) {
        double fuel = getRequiredFuel(mission);
        double cost = getRocketPrice() + (fuel * KEROSENE_PRICE_PER_TON);
        return (int) Math.round(cost);
    }
}
