package kaneMenicou.trafficController.interactor;

import kaneMenicou.trafficController.lightSystem.TrafficLight;

final public class Pedestrian extends Interactor {
    public Pedestrian(TrafficLight waitingAtLight) {
        super(waitingAtLight);
    }

    public double getSouls() {
        return 1;
    }
}
