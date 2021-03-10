package com.itvrach.services;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by engineer on 3/3/2019.
 */

public class GlobalBus {
    private static EventBus sBus;
    public static EventBus getsBus(){
        if(sBus == null){
            sBus = EventBus.getDefault();
            return sBus;
        }
        return null;
    }
}
