package internal.rocket;

import internal.SpaceLauncher.spacelauncher;
import internal.boosters.boosters;
import internal.capsules.capsule;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class rocket {
    private final spacelauncher launcher;
    private final capsule capsule;
    private final List<boosters> boosters;

    public rocket(spacelauncher launcher, capsule capsule, List<? extends boosters> boosters) {
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

    public List<boosters> getBoosters() {
        return Collections.unmodifiableList(boosters);
    }

    public void addBooster(boosters booster) {
        if (booster == null) {
            throw new IllegalArgumentException("booster ne peut pas être null");
        }
        boosters.add(booster);
    }

    public int getBoosterCount() {
        return boosters.size();
    }

    public int getTotalCost() {
        int total = launcher.price + capsule.price;
        for (boosters b : boosters) {
            total += b.price;
        }
        return total;
    }

    /**
     * Poids total "simple" : capsule + boosters.
     * (Le lanceur n'est pas compté ici, à adapter selon ton modèle.)
     */
    public double getTotalPayloadWeight() {
        double total = capsule.Weight;
        for (boosters b : boosters) {
            total += b.Weight;
        }
        return total;
    }
}
