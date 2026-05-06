package internal;

import internal.missions.missions;
import internal.rocket.rocket;
import java.time.LocalDateTime;

public class start {
    private final rocket fusee;
    private final missions mission;
    private final LocalDateTime date;
    private final boolean succes;
    private final String raison;
    private final int coutTotal;

    public start(rocket fusee, missions mission, LocalDateTime date, boolean succes, String raison, int coutTotal) {
        if (fusee == null) {
            throw new IllegalArgumentException("rocket cannot be null");
        }
        if (mission == null) {
            throw new IllegalArgumentException("mission cannot be null");
        }
        if (date == null) {
            throw new IllegalArgumentException("date cannot be null");
        }

        this.fusee = fusee;
        this.mission = mission;
        this.date = date;
        this.succes = succes;
        this.raison = raison == null ? "" : raison;
        this.coutTotal = coutTotal;
    }

    public static start fromSimulation(rocket fusee, missions mission, boolean succes, String raison) {
        double carburant = fusee.getRequiredFuel(mission);
        int cout = fusee.getLaunchCost(mission);
        System.out.println("Carburant nécessaire (tonnes): " + carburant);
        System.out.println("Coût total lancement (€): " + cout);
        return new start(fusee, mission, LocalDateTime.now(), succes, raison, cout);
    }

    public rocket getFusee() {
        return fusee;
    }

    public missions getMission() {
        return mission;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public boolean isSucces() {
        return succes;
    }

    public String getRaison() {
        return raison;
    }

    public int getCoutTotal() {
        return coutTotal;
    }

    @Override
    public String toString() {
        double carburant = fusee.getRequiredFuel(mission);
        return "Lancement{" +
                "mission=" + mission.getClass().getSimpleName() +
                ", date=" + date +
                ", succes=" + succes +
                ", raison='" + raison + '\'' +
                ", carburantNecessaire=" + carburant +
                ", coutTotal=" + coutTotal +
                '}';
    }
}
