package kaneMenicou.trafficController.lightSystem;

import java.util.List;

final public class TrafficLight {
    public enum Types {
        Pedestrian, Vehicle,
    }

    public enum State {
        Go, Wait
    }

    private final List<TrafficLight> conflicts;
    private final Types type;
    private State state;

    public TrafficLight(List<TrafficLight> conflicts, Types type) {
        this.conflicts = conflicts;
        this.type = type;
        state = State.Wait;

        for (TrafficLight conflict : conflicts) {
            conflict.conflicts.add(this);
        }
    }

    public boolean doesConflict(TrafficLight light) {
        return conflicts.contains(light);
    }

    public void setStateGo() {
        state = State.Go;
    }

    public void setStateWait() {
        state = State.Wait;
    }

    public State getState() {
        return state;
    }

    public boolean isStateWait() {
        return state == State.Wait;
    }
}
