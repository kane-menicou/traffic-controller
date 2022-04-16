package kaneMenicou.trafficController.lightTimer;

import kaneMenicou.trafficController.interactor.Interactor;
import kaneMenicou.trafficController.lightSystem.LightSystem;
import kaneMenicou.trafficController.lightSystem.TrafficLight;

final public class WaitingInteractorLightTimer implements LightTimer {
    private final LightSystem system;
    private final Interactor interactor;
    private int ticks = 0;

    public WaitingInteractorLightTimer(LightSystem system, Interactor interactor) {
        this.system = system;
        this.interactor = interactor;
    }

    public void tick() {
        ticks++;
    }

    public boolean hasExpired() {
        return ticks > 4;
    }

    public TrafficLight getLight() {
        return interactor.getWaitingAtLight();
    }

    @Override
    public boolean isApplicable() {
        return system.hasInteractor(interactor);
    }
}
