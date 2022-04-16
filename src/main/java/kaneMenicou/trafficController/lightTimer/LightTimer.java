package kaneMenicou.trafficController.lightTimer;

import kaneMenicou.trafficController.lightSystem.TrafficLight;

public interface LightTimer {
    void tick();

    boolean hasExpired();

    TrafficLight getLight();

    boolean isApplicable();
}
