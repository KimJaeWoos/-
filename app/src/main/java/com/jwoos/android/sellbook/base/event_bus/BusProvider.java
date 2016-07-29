package com.jwoos.android.sellbook.base.event_bus;

import com.squareup.otto.Bus;

/**
 * Created by Jwoo on 2016-05-30.
 */
public final class BusProvider {
    private static final Bus BUS = new Bus();

    public static Bus getInstance(){
        return BUS;
    }

    private BusProvider(){

    }
}
