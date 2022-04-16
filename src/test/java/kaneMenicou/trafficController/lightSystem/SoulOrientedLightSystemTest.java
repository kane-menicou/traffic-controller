package kaneMenicou.trafficController.lightSystem;

import kaneMenicou.trafficController.interactor.Car;
import kaneMenicou.trafficController.interactor.Pedestrian;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

class SoulOrientedLightSystemTest {

    @Test
    public void itWillSetAllLightsToWaitIfNoInteractors() {
        ArrayList<TrafficLight> lights = new ArrayList<>();

        TrafficLight north = new TrafficLight(new ArrayList<>(), TrafficLight.Types.Vehicle);
        lights.add(north);

        TrafficLight south = new TrafficLight(new ArrayList<>(), TrafficLight.Types.Vehicle);
        lights.add(south);

        ArrayList<TrafficLight> conflicts = new ArrayList<>();
        conflicts.add(north);
        conflicts.add(south);

        TrafficLight east = new TrafficLight(conflicts, TrafficLight.Types.Pedestrian);
        lights.add(east);

        TrafficLight west = new TrafficLight(conflicts, TrafficLight.Types.Pedestrian);
        lights.add(west);

        SoulOrientedLightSystem system = new SoulOrientedLightSystem(lights);

        Car car = new Car(north);
        system.addInteractor(car);

        system.tick();

        Assertions.assertSame(north.getState(), TrafficLight.State.Go);
        Assertions.assertSame(south.getState(), TrafficLight.State.Go);

        Assertions.assertSame(east.getState(), TrafficLight.State.Wait);
        Assertions.assertSame(west.getState(), TrafficLight.State.Wait);


        system.removeInteractor(car);
        system.tick();

        Assertions.assertSame(north.getState(), TrafficLight.State.Wait);
        Assertions.assertSame(south.getState(), TrafficLight.State.Wait);

        Assertions.assertSame(east.getState(), TrafficLight.State.Wait);
        Assertions.assertSame(west.getState(), TrafficLight.State.Wait);
    }

    @Test
    public void itWillAllowAVehicleAlone() {
        ArrayList<TrafficLight> lights = new ArrayList<>();

        TrafficLight north = new TrafficLight(new ArrayList<>(), TrafficLight.Types.Vehicle);
        lights.add(north);

        TrafficLight south = new TrafficLight(new ArrayList<>(), TrafficLight.Types.Vehicle);
        lights.add(south);

        ArrayList<TrafficLight> conflicts = new ArrayList<>();
        conflicts.add(north);
        conflicts.add(south);

        TrafficLight east = new TrafficLight(conflicts, TrafficLight.Types.Pedestrian);
        lights.add(east);

        TrafficLight west = new TrafficLight(conflicts, TrafficLight.Types.Pedestrian);
        lights.add(west);

        SoulOrientedLightSystem system = new SoulOrientedLightSystem(lights);

        system.addInteractor(new Car(north));

        system.tick();

        Assertions.assertSame(north.getState(), TrafficLight.State.Go);
        Assertions.assertSame(south.getState(), TrafficLight.State.Go);

        Assertions.assertSame(east.getState(), TrafficLight.State.Wait);
        Assertions.assertSame(west.getState(), TrafficLight.State.Wait);
    }

    @Test
    public void itWillAllowPedestriansToCrossIfNoVehicles() {
        ArrayList<TrafficLight> lights = new ArrayList<>();

        TrafficLight north = new TrafficLight(new ArrayList<>(), TrafficLight.Types.Vehicle);
        lights.add(north);

        TrafficLight south = new TrafficLight(new ArrayList<>(), TrafficLight.Types.Vehicle);
        lights.add(south);

        ArrayList<TrafficLight> conflicts = new ArrayList<>();
        conflicts.add(north);
        conflicts.add(south);

        TrafficLight east = new TrafficLight(conflicts, TrafficLight.Types.Pedestrian);
        lights.add(east);

        TrafficLight west = new TrafficLight(conflicts, TrafficLight.Types.Pedestrian);
        lights.add(west);

        SoulOrientedLightSystem system = new SoulOrientedLightSystem(lights);

        system.addInteractor(new Pedestrian(east));

        system.tick();

        Assertions.assertSame(north.getState(), TrafficLight.State.Wait);
        Assertions.assertSame(south.getState(), TrafficLight.State.Wait);

        Assertions.assertSame(east.getState(), TrafficLight.State.Go);
        Assertions.assertSame(west.getState(), TrafficLight.State.Go);
    }

    @Test
    public void itWillAllowAVehicleToPassBeforeAPedestrianCrosses() {
        ArrayList<TrafficLight> lights = new ArrayList<>();

        TrafficLight north = new TrafficLight(new ArrayList<>(), TrafficLight.Types.Vehicle);
        lights.add(north);

        TrafficLight south = new TrafficLight(new ArrayList<>(), TrafficLight.Types.Vehicle);
        lights.add(south);

        ArrayList<TrafficLight> conflicts = new ArrayList<>();
        conflicts.add(north);
        conflicts.add(south);

        TrafficLight east = new TrafficLight(conflicts, TrafficLight.Types.Pedestrian);
        lights.add(east);

        TrafficLight west = new TrafficLight(conflicts, TrafficLight.Types.Pedestrian);
        lights.add(west);

        SoulOrientedLightSystem system = new SoulOrientedLightSystem(lights);

        Pedestrian pedestrian = new Pedestrian(east);
        system.addInteractor(pedestrian);
        Car car = new Car(north);
        system.addInteractor(car);

        system.tick();

        Assertions.assertSame(north.getState(), TrafficLight.State.Go);
        Assertions.assertSame(south.getState(), TrafficLight.State.Go);

        Assertions.assertSame(east.getState(), TrafficLight.State.Wait);
        Assertions.assertSame(west.getState(), TrafficLight.State.Wait);

        system.removeInteractor(car);
        system.tick();

        Assertions.assertSame(north.getState(), TrafficLight.State.Wait);
        Assertions.assertSame(south.getState(), TrafficLight.State.Wait);

        Assertions.assertSame(east.getState(), TrafficLight.State.Go);
        Assertions.assertSame(west.getState(), TrafficLight.State.Go);
    }

    @Test
    public void itWillAllowMultiplePedestriansToCrossBeforeOneCarFromSameLights() {
        ArrayList<TrafficLight> lights = new ArrayList<>();

        TrafficLight north = new TrafficLight(new ArrayList<>(), TrafficLight.Types.Vehicle);
        lights.add(north);

        TrafficLight south = new TrafficLight(new ArrayList<>(), TrafficLight.Types.Vehicle);
        lights.add(south);

        ArrayList<TrafficLight> conflicts = new ArrayList<>();
        conflicts.add(north);
        conflicts.add(south);

        TrafficLight east = new TrafficLight(conflicts, TrafficLight.Types.Pedestrian);
        lights.add(east);

        TrafficLight west = new TrafficLight(conflicts, TrafficLight.Types.Pedestrian);
        lights.add(west);

        SoulOrientedLightSystem system = new SoulOrientedLightSystem(lights);

        Pedestrian pedestrian1 = new Pedestrian(east);
        system.addInteractor(pedestrian1);
        Pedestrian pedestrian2 = new Pedestrian(east);
        system.addInteractor(pedestrian2);
        Car car = new Car(north);
        system.addInteractor(car);

        system.tick();

        Assertions.assertSame(north.getState(), TrafficLight.State.Wait);
        Assertions.assertSame(south.getState(), TrafficLight.State.Wait);

        Assertions.assertSame(east.getState(), TrafficLight.State.Go);
        Assertions.assertSame(west.getState(), TrafficLight.State.Go);

        system.tick();

        Assertions.assertSame(north.getState(), TrafficLight.State.Wait);
        Assertions.assertSame(south.getState(), TrafficLight.State.Wait);

        Assertions.assertSame(east.getState(), TrafficLight.State.Go);
        Assertions.assertSame(west.getState(), TrafficLight.State.Go);

        system.removeInteractor(pedestrian1);
        system.removeInteractor(pedestrian2);

        system.tick();

        Assertions.assertSame(north.getState(), TrafficLight.State.Go);
        Assertions.assertSame(south.getState(), TrafficLight.State.Go);

        Assertions.assertSame(east.getState(), TrafficLight.State.Wait);
        Assertions.assertSame(west.getState(), TrafficLight.State.Wait);
    }

    @Test
    public void itWillAllowThreePedestriansToCrossBeforeOneCarFromDifferentLightsOnSamePhase() {
        ArrayList<TrafficLight> lights = new ArrayList<>();

        TrafficLight north = new TrafficLight(new ArrayList<>(), TrafficLight.Types.Vehicle);
        lights.add(north);

        TrafficLight south = new TrafficLight(new ArrayList<>(), TrafficLight.Types.Vehicle);
        lights.add(south);

        ArrayList<TrafficLight> conflicts = new ArrayList<>();
        conflicts.add(north);
        conflicts.add(south);

        TrafficLight east = new TrafficLight(conflicts, TrafficLight.Types.Pedestrian);
        lights.add(east);

        TrafficLight west = new TrafficLight(conflicts, TrafficLight.Types.Pedestrian);
        lights.add(west);

        SoulOrientedLightSystem system = new SoulOrientedLightSystem(lights);

        Pedestrian pedestrian1 = new Pedestrian(east);
        system.addInteractor(pedestrian1);
        Pedestrian pedestrian2 = new Pedestrian(west);
        system.addInteractor(pedestrian2);
        Pedestrian pedestrian3 = new Pedestrian(west);
        system.addInteractor(pedestrian3);
        Car car = new Car(north);
        system.addInteractor(car);

        system.tick();

        Assertions.assertSame(north.getState(), TrafficLight.State.Wait);
        Assertions.assertSame(south.getState(), TrafficLight.State.Wait);

        Assertions.assertSame(east.getState(), TrafficLight.State.Go);
        Assertions.assertSame(west.getState(), TrafficLight.State.Go);

        system.tick();

        Assertions.assertSame(north.getState(), TrafficLight.State.Wait);
        Assertions.assertSame(south.getState(), TrafficLight.State.Wait);

        Assertions.assertSame(east.getState(), TrafficLight.State.Go);
        Assertions.assertSame(west.getState(), TrafficLight.State.Go);

        system.removeInteractor(pedestrian1);
        system.removeInteractor(pedestrian2);

        system.tick();

        Assertions.assertSame(north.getState(), TrafficLight.State.Go);
        Assertions.assertSame(south.getState(), TrafficLight.State.Go);

        Assertions.assertSame(east.getState(), TrafficLight.State.Wait);
        Assertions.assertSame(west.getState(), TrafficLight.State.Wait);
    }

    @Test
    public void itWillAllowAPedestrianToCrossAConstantStreamOfCarsAfter5Ticks() {
        ArrayList<TrafficLight> lights = new ArrayList<>();

        TrafficLight north = new TrafficLight(new ArrayList<>(), TrafficLight.Types.Vehicle);
        lights.add(north);

        TrafficLight south = new TrafficLight(new ArrayList<>(), TrafficLight.Types.Vehicle);
        lights.add(south);

        ArrayList<TrafficLight> conflicts = new ArrayList<>();
        conflicts.add(north);
        conflicts.add(south);

        TrafficLight east = new TrafficLight(conflicts, TrafficLight.Types.Pedestrian);
        lights.add(east);

        TrafficLight west = new TrafficLight(conflicts, TrafficLight.Types.Pedestrian);
        lights.add(west);

        SoulOrientedLightSystem system = new SoulOrientedLightSystem(lights);

        Pedestrian pedestrian1 = new Pedestrian(east);
        system.addInteractor(pedestrian1);
        Car car1 = new Car(north);
        system.addInteractor(car1);
        Car car2 = new Car(south);
        system.addInteractor(car2);

        system.tick();

        Assertions.assertSame(north.getState(), TrafficLight.State.Go);
        Assertions.assertSame(south.getState(), TrafficLight.State.Go);

        Assertions.assertSame(east.getState(), TrafficLight.State.Wait);
        Assertions.assertSame(west.getState(), TrafficLight.State.Wait);

        system.removeInteractor(car1);
        system.removeInteractor(car2);

        Car car3 = new Car(north);
        system.addInteractor(car3);
        Car car4 = new Car(south);
        system.addInteractor(car4);

        system.tick();

        Assertions.assertSame(north.getState(), TrafficLight.State.Go);
        Assertions.assertSame(south.getState(), TrafficLight.State.Go);

        Assertions.assertSame(east.getState(), TrafficLight.State.Wait);
        Assertions.assertSame(west.getState(), TrafficLight.State.Wait);

        system.removeInteractor(car3);
        system.removeInteractor(car4);

        Car car5 = new Car(north);
        system.addInteractor(car5);
        Car car6 = new Car(south);
        system.addInteractor(car6);

        system.tick();

        Assertions.assertSame(north.getState(), TrafficLight.State.Go);
        Assertions.assertSame(south.getState(), TrafficLight.State.Go);

        Assertions.assertSame(east.getState(), TrafficLight.State.Wait);
        Assertions.assertSame(west.getState(), TrafficLight.State.Wait);

        system.removeInteractor(car5);
        system.removeInteractor(car6);

        Car car7 = new Car(north);
        system.addInteractor(car7);
        Car car8 = new Car(south);
        system.addInteractor(car8);

        system.tick();

        Assertions.assertSame(north.getState(), TrafficLight.State.Go);
        Assertions.assertSame(south.getState(), TrafficLight.State.Go);

        Assertions.assertSame(east.getState(), TrafficLight.State.Wait);
        Assertions.assertSame(west.getState(), TrafficLight.State.Wait);

        system.removeInteractor(car7);
        system.removeInteractor(car8);

        Car car9 = new Car(north);
        system.addInteractor(car9);
        Car car10 = new Car(south);
        system.addInteractor(car10);

        system.tick();

        Assertions.assertSame(north.getState(), TrafficLight.State.Go);
        Assertions.assertSame(south.getState(), TrafficLight.State.Go);

        Assertions.assertSame(east.getState(), TrafficLight.State.Wait);
        Assertions.assertSame(west.getState(), TrafficLight.State.Wait);

        system.removeInteractor(car9);
        system.removeInteractor(car10);

        Car car11 = new Car(north);
        system.addInteractor(car11);
        Car car12 = new Car(south);
        system.addInteractor(car12);

        system.tick();

        Assertions.assertSame(north.getState(), TrafficLight.State.Wait);
        Assertions.assertSame(south.getState(), TrafficLight.State.Wait);

        Assertions.assertSame(east.getState(), TrafficLight.State.Go);
        Assertions.assertSame(west.getState(), TrafficLight.State.Go);

        system.removeInteractor(pedestrian1);
        system.tick();

        Assertions.assertSame(north.getState(), TrafficLight.State.Go);
        Assertions.assertSame(south.getState(), TrafficLight.State.Go);

        Assertions.assertSame(east.getState(), TrafficLight.State.Wait);
        Assertions.assertSame(west.getState(), TrafficLight.State.Wait);
    }
}