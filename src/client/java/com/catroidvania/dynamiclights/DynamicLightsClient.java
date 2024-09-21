package com.catroidvania.dynamiclights;

import com.fox2code.foxloader.loader.ClientMod;


public class DynamicLightsClient extends DynamicLights implements ClientMod {

    public static final DynamicLightsUpdater lightHandler = new DynamicLightsUpdater();

    @Override
    public void onInit() {
        System.out.println("DYNAMIC LIGHTS CLIENT INITIALISED");
    }

    @Override
    public void onTick() {
        lightHandler.updateDynamicLights();
    }
}
