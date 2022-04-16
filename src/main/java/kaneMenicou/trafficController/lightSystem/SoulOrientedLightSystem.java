package kaneMenicou.trafficController.lightSystem;

import kaneMenicou.trafficController.interactor.Interactor;
import kaneMenicou.trafficController.lightTimer.LightTimer;
import kaneMenicou.trafficController.lightTimer.WaitingInteractorLightTimer;

import java.util.List;
import java.util.stream.Collectors;

final public class SoulOrientedLightSystem extends LightSystem {
    public SoulOrientedLightSystem(List<TrafficLight> lights) {
        super(lights);
    }

    protected void onUnexpiredTimerTick() {
        activatePhaseWithMostSouls(lights);
    }

    protected void onExpiredTimerTick(List<LightTimer> expiredTimers) {
        List<TrafficLight> lightsWithExpiredTimers = expiredTimers.stream().map(LightTimer::getLight).collect(Collectors.toList());

        activatePhaseWithMostSouls(lightsWithExpiredTimers);
    }

    private void activatePhaseWithMostSouls(List<TrafficLight> lights) {
        double maxSouls = 0;
        TrafficLight lightWithMostSouls = null;

        for (TrafficLight light : lights) {
            double soulsAtLight = getSoulsWaitingOnPhaseForLight(light);

            if (soulsAtLight > maxSouls) {
                maxSouls = soulsAtLight;
                lightWithMostSouls = light;
            }
        }

        if (lightWithMostSouls == null) {
            for (TrafficLight light : lights) {
                light.setStateWait();
            }

            return;
        }

        activatePhaseForLight(lightWithMostSouls);

        for (TrafficLight light : lights) {
            if (light.isStateWait()) {
                for (Interactor interactor : interactors) {
                    addTimer(new WaitingInteractorLightTimer(this, interactor));
                }
            }
        }
    }

    private double getSoulsWaitingAtLight(TrafficLight light) {
        double souls = 0;

        for (Interactor interactor : interactors) {
            if (interactor.getWaitingAtLight() == light) {
                souls = souls + interactor.getSouls();
            }
        }

        return souls;
    }

    private double getSoulsWaitingOnPhaseForLight(TrafficLight light) {
        double souls = 0;

        for (TrafficLight potentialCompatibleLight : lights) {
            if (!potentialCompatibleLight.doesConflict(light)) {
                souls = souls + getSoulsWaitingAtLight(potentialCompatibleLight);
            }
        }

        return souls;
    }

    private void activatePhaseForLight(TrafficLight light) {
        light.setStateGo();

        for (TrafficLight otherLight : lights) {
            if (otherLight.doesConflict(light)) {
                otherLight.setStateWait();
            } else {
                otherLight.setStateGo();
            }
        }
    }
}
