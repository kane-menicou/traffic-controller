package kaneMenicou.trafficController.lightSystem;

import kaneMenicou.trafficController.interactor.Interactor;
import kaneMenicou.trafficController.lightTimer.LightTimer;

import java.util.ArrayList;
import java.util.List;

abstract public class LightSystem {
    protected final List<TrafficLight> lights;
    protected final List<Interactor> interactors = new ArrayList<>();

    private final List<LightTimer> timers = new ArrayList<>();

    abstract protected void onUnexpiredTimerTick();

    abstract protected void onExpiredTimerTick(List<LightTimer> timers);

    public LightSystem(List<TrafficLight> lights) {
        this.lights = lights;
    }

    final public void tick() {
        List<LightTimer> expiredTimers = new ArrayList<>();
        List<LightTimer> inapplicableTimers = new ArrayList<>();

        for (LightTimer timer : timers) {
            timer.tick();

            if (timer.hasExpired() && timer.isApplicable()) {
                expiredTimers.add(timer);
            }

            if (!timer.isApplicable()) {
                inapplicableTimers.add(timer);
            }
        }

        for (LightTimer inapplicableTimer : inapplicableTimers) {
            timers.remove(inapplicableTimer);
        }

        if (!expiredTimers.isEmpty()) {
            onExpiredTimerTick(expiredTimers);

            return;
        }

        onUnexpiredTimerTick();
    }

    final public void addInteractor(Interactor interactor) {
        interactors.add(interactor);
    }

    final public void removeInteractor(Interactor interactor) {
        interactors.remove(interactor);
    }

    final public boolean hasInteractor(Interactor interactor) {
        return interactors.contains(interactor);
    }

    final protected void addTimer(LightTimer timer) {
        timers.add(timer);
    }
}
