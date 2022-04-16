package kaneMenicou.trafficController.interactor;

import kaneMenicou.trafficController.lightSystem.TrafficLight;

public abstract class Interactor {
    private final TrafficLight waitingAtLight;

    abstract public double getSouls();

    public Interactor(TrafficLight waitingAtLight) {
        this.waitingAtLight = waitingAtLight;
    }

    public TrafficLight getWaitingAtLight() {
        return waitingAtLight;
    }
}
