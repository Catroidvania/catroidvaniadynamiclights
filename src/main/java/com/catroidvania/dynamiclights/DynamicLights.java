package com.catroidvania.dynamiclights;

import com.fox2code.foxloader.config.ConfigEntry;
import com.fox2code.foxloader.loader.Mod;

public class DynamicLights extends Mod {

    public static final DynamicLightsConfig CONFIG = new DynamicLightsConfig();

    @Override
    public void onPreInit() {
        setConfigObject(CONFIG);
        System.out.println("CATS DYNAMIC LIGHTS INITIALISED");
    }

    public static class DynamicLightsConfig {

        @ConfigEntry(configName = "dropped item lights")
        public boolean itemLights = true;

        @ConfigEntry(configName = "entity lights")
        public boolean entityLights = true;

        @ConfigEntry(configName = "handheld lights")
        public boolean handheldLights = true;

        // false by default since hostile mobs burning during the day causes so much lag
        @ConfigEntry(configName = "on fire lights")
        public boolean onFireLights = false;
    }
}
