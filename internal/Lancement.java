package internal;

import internal.missions.missions;
import internal.rocket.rocket;
import java.time.LocalDateTime;

public class Lancement {
    private final rocket fusee;
    private final missions mission;
    private final LocalDateTime date;
    private final boolean succes;
    private final String raison;
    private final int coutTotal;

    public Lancement(rocket fusee, missions mission, LocalDateTime date, boolean succes, String raison, int coutTotal) {
        if (fusee == null) {
            throw new IllegalArgumentException("fusee ne peut pas être null");
        }
        if (mission == null) {
            throw new IllegalArgumentException("mission ne peut pas être null");
        }
        if (date == null) {
            throw new IllegalArgumentException("date ne peut pas être null");
        }

        this.fusee = fusee;
        this.mission = mission;
        this.date = date;
        this.succes = succes;
        this.raison = raison == null ? "" : raison;
        this.coutTotal = coutTotal;
    }

    public static Lancement fromSimulation(rocket fusee, missions mission, boolean succes, String raison) {
        int cout = fusee.getTotalCost();
        return new Lancement(fusee, mission, LocalDateTime.now(), succes, raison, cout);
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
        return "Lancement{" +
                "mission=" + mission.getClass().getSimpleName() +
                ", date=" + date +
                ", succes=" + succes +
                ", raison='" + raison + '\'' +
                ", coutTotal=" + coutTotal +
                '}';
    }
}
