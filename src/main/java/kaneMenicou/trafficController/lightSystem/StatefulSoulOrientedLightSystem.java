package kaneMenicou.trafficController.lightSystem;

import kaneMenicou.trafficController.interactor.Interactor;
import kaneMenicou.trafficController.lightTimer.LightTimer;
import kaneMenicou.trafficController.lightTimer.WaitingInteractorLightTimer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

final public class StatefulSoulOrientedLightSystem extends StatefulLightSystem {
    static final private class LightsPhase extends ArrayList<TrafficLight> {
        private LightsPhase(List<TrafficLight> list) {
            super(list);
        }

        private double getSoulsWaiting(List<Interactor> interactors) {
            double souls = 0;

            for (TrafficLight light : this) {
                souls = souls + getSoulsWaitingAtLight(light, interactors);
            }

            return souls;
        }

        private double getSoulsWaitingAtLight(TrafficLight light, List<Interactor> interactors) {
            double souls = 0;

            for (Interactor interactor : interactors) {
                if (interactor.getWaitingAtLight() == light) {
                    souls = souls + interactor.getSouls();
                }
            }

            return souls;
        }
    }

    public StatefulSoulOrientedLightSystem(List<TrafficLight> lights) {
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
        double maxPhaseSouls = 0;
        LightsPhase phaseWithMostSouls = null;

        for (LightsPhase phase: getPhases()) {
            if (phase.getSoulsWaiting(interactors) > maxPhaseSouls) {
                maxPhaseSouls = phase.getSoulsWaiting(interactors);
                phaseWithMostSouls = phase;
            }
        }

        if (phaseWithMostSouls == null) {
            for (TrafficLight light : lights) {
                light.setStateWait();
            }

            return;
        }

        activatePhase(phaseWithMostSouls);

        for (TrafficLight light : lights) {
            if (light.isStateWait()) {
                for (Interactor interactor : interactors) {
                    addTimer(new WaitingInteractorLightTimer(this, interactor));
                }
            }
        }
    }

    private boolean doAnyLightsConflict(List<TrafficLight> list, TrafficLight light) {
        for (TrafficLight allowedLight : list) {
            if (allowedLight.doesConflict(light)) {
                return true;
            }
        }

        return false;
    }

    private void activatePhase(LightsPhase phase) {
        for (TrafficLight light : lights) {
            if (phase.contains(light)) {
                light.setStateGo();
            }

            if (!phase.contains(light)) {
                light.setStateWait();
            }
        }
    }

    private List<LightsPhase> getPhases() {
        List<LightsPhase> phases = new ArrayList<>();

        for (TrafficLight light : lights) {
            phases.addAll(getPhasesForLights(light, new ArrayList<>()));
        }

        return phases;
    }

    private List<LightsPhase> getPhasesForLights(TrafficLight mainLight, List<TrafficLight> compatibleWith) {
        LightsPhase phase = new LightsPhase(compatibleWith);
        phase.add(mainLight);

        List<LightsPhase> phases = new ArrayList<>();
        phases.add(phase);

        for (TrafficLight light : lights) {
            if (mainLight.doesConflict(light) || phase.contains(light) || light == mainLight) {
                continue;
            }

            if (!doAnyLightsConflict(phase, light)) {
                phase.add(light);

                continue;
            }

            List<TrafficLight> innerCompatibleWith = new ArrayList<>(compatibleWith);
            innerCompatibleWith.add(light);

            phases.addAll(getPhasesForLights(mainLight, innerCompatibleWith));
        }

        return phases;
    }
}
