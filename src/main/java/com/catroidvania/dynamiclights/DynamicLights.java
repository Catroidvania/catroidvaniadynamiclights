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
            TINY,
            SHORT,
            NORMAL,
            FAR,
            ULTRA,
            MAX
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

        @ConfigEntry(configName =  "max entity distance", handlerName = "writeConfig")
        public LightsDistance maxEntityDistance = LightsDistance.NORMAL;

        public void writeConfig() {
            try {
                PrintWriter pwriter = new PrintWriter(new FileWriter(configFile));
                pwriter.println("itemLights:" + CONFIG.itemLights);
                pwriter.println("entityLights:" + CONFIG.entityLights);
                pwriter.println("handheldLights:" + CONFIG.handheldLights);
                pwriter.println("onFireLights:" + CONFIG.onFireLights);
                pwriter.println("maxEntityDistance:" + CONFIG.maxEntityDistance);
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
                            if (strings[0].equals("maxEntityDistance")) {
                                CONFIG.maxEntityDistance = LightsDistance.valueOf(strings[1]);
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
                case TINY:
                    return 8.0f;
                case SHORT:
                    return 16.0f;
                case NORMAL:
                    return 32.0f;
                case FAR:
                    return 64.0f;
                case ULTRA:
                    return 128.0f;
                case MAX:
                    return 512.0f;
            }
            return 0;
        }
    }
}
