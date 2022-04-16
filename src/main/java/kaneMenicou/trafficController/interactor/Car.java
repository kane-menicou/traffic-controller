package kaneMenicou.trafficController.interactor;

import kaneMenicou.trafficController.lightSystem.TrafficLight;

final public class Car extends Interactor {
    public Car(TrafficLight waitingAtLight) {
        super(waitingAtLight);
    }

    public double getSouls() {
        return 1.55;
    }
}
