package co.com.autolagos.rtaxi.local.driver.model.events;

import com.squareup.otto.Bus;

public class StationBus {

    private static Bus bus = new Bus();
    public static Bus getBus() {
        return bus;
    }

}

