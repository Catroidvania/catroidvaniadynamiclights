package com.catroidvania.dynamiclights;

import com.fox2code.foxloader.config.ConfigEntry;
import com.fox2code.foxloader.loader.Mod;

import java.io.*;

public class DynamicLights extends Mod {

    public static final DynamicLightsConfig CONFIG = new DynamicLightsConfig();
    public static File configDir;
    public static File configFile;

    @Override
    public void onPreInit() {
        setConfigObject(CONFIG);
        configDir = getConfigFolder();
        configFile = new File(configDir, "catroidvaniadynamiclights.txt");
        CONFIG.readConfig();
        System.out.println("dynamic lights initialised");
    }

    public static class DynamicLightsConfig {

        public enum LightsDistance {
            OFF,
            SHORT,
            MEDIUM,
            FAR,
            UNLIMITED
        }

        public enum UpdatesPerSecond {
            MINIMAL,
            DECREASED,
            SMOOTH,
            SMOOTHEST
        }

        @ConfigEntry(configName = "dropped item lights", handlerName = "writeConfig")
        public boolean itemLights = true;

        @ConfigEntry(configName = "entity lights", handlerName = "writeConfig")
        public boolean entityLights = true;

        @ConfigEntry(configName = "handheld lights", handlerName = "writeConfig")
        public boolean handheldLights = true;

        // hostile mobs burning during the day causes so much lag
        @ConfigEntry(configName = "on fire lights", handlerName = "writeConfig")
        public boolean onFireLights = true;

        @ConfigEntry(configName = "always lit underwater", handlerName = "writeConfig")
        public boolean alwaysLitUnderwater = false;

        @ConfigEntry(configName =  "max entity distance", handlerName = "writeConfig")
        public LightsDistance maxEntityDistance = LightsDistance.MEDIUM;

        @ConfigEntry(configName =  "light updates", handlerName = "writeConfig")
        public UpdatesPerSecond updateSpeed = UpdatesPerSecond.SMOOTH;

        public void writeConfig() {
            try {
                PrintWriter pwriter = new PrintWriter(new FileWriter(configFile));
                pwriter.println("itemLights:" + CONFIG.itemLights);
                pwriter.println("entityLights:" + CONFIG.entityLights);
                pwriter.println("handheldLights:" + CONFIG.handheldLights);
                pwriter.println("onFireLights:" + CONFIG.onFireLights);
                pwriter.println("alwaysLitUnderwater:" + CONFIG.alwaysLitUnderwater);
                pwriter.println("maxEntityDistance:" + CONFIG.maxEntityDistance);
                pwriter.println("updateSpeed:" + CONFIG.updateSpeed);
                pwriter.close();
            } catch (Exception exception) {
                System.out.println("could not write to config file!");
                exception.printStackTrace();
            }
        }

        public void readConfig() {
            try {
                if (configFile.exists()) {
                    BufferedReader breader = new BufferedReader(new FileReader(configFile));
                    String line = "";

                    while ((line = breader.readLine()) != null) {
                        try {
                            String[] strings = line.split(":");
                            if (strings[0].equals("itemLights")) {
                                CONFIG.itemLights = strings[1].equals("true");
                            }
                            if (strings[0].equals("entityLights")) {
                                CONFIG.entityLights = strings[1].equals("true");
                            }
                            if (strings[0].equals("handheldLights")) {
                                CONFIG.handheldLights = strings[1].equals("true");
                            }
                            if (strings[0].equals("onFireLights")) {
                                CONFIG.onFireLights = strings[1].equals("true");
                            }
                            if (strings[0].equals("alwaysLitUnderwater")) {
                                CONFIG.alwaysLitUnderwater = strings[1].equals("true");
                            }
                            if (strings[0].equals("maxEntityDistance")) {
                                CONFIG.maxEntityDistance = LightsDistance.valueOf(strings[1]);
                            }
                            if (strings[0].equals("updateSpeed")) {
                                CONFIG.updateSpeed = UpdatesPerSecond.valueOf(strings[1]);
                            }
                        } catch (Exception exception) {
                            System.out.println("failed to parse option " + line);
                        }
                    }
                    breader.close();
                }
            } catch (Exception exception) {
                System.out.println("could not read config file!");
                exception.printStackTrace();
            }
        }

        public float getMaxDistanceValue(LightsDistance ld) {
            switch (ld) {
                case SHORT:
                    return 16.0f;
                case MEDIUM:
                    return 32.0f;
                case FAR:
                    return 64.0f;
                case UNLIMITED:
                    return 1024.0f;
            }
            return 0;
        }

        public int getTicksPerUpdate(UpdatesPerSecond ups) {
            switch (ups) {
                case MINIMAL:
                    return 10;
                case DECREASED:
                    return 4;
                case SMOOTH:
                    return 2;
                case SMOOTHEST:
                    return 1;
            }
            return 1;
        }
    }
}
