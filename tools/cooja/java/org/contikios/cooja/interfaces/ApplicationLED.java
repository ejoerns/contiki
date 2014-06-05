package org.contikios.cooja.interfaces;
import java.awt.Color;

import org.apache.log4j.Logger;

import org.contikios.cooja.*;
import org.contikios.cooja.contikimote.interfaces.ContikiLED;

public class ApplicationLED extends LEDInterface {
    private static Logger logger = Logger.getLogger(ContikiLED.class);

    private Mote mote = null;
    private byte currentLedValue = 0;
    
    private final LED[] ledMap = new LED[] {
        new LED(Color.GREEN),
        new LED(Color.YELLOW),
        new LED(Color.RED)
    };

    private static final int GREEN_IDX = 0;
    private static final int YELLOW_IDX = 1;
    private static final int RED_IDX = 2;

     public ApplicationLED(Mote mote) {
       this.mote = mote;
     }

     public static String[] getCoreInterfaceDependencies() {
       return new String[]{"leds_interface"};
     }

     public boolean isAnyOn() {
       return ledMap[GREEN_IDX].on || ledMap[YELLOW_IDX].on || ledMap[RED_IDX].on;
     }

     public void setLED(int led) {
       boolean ledChanged;
       ledChanged = led != currentLedValue;

       currentLedValue = (byte) led;
       if (ledChanged) {
         this.setChanged();
         this.notifyObservers(mote);
       }
     }

    @Override
    public LED[] getLEDs() {
        return ledMap;
    }

    @Override
    public LED getLED(int idx) {
        return ledMap[idx];
    }
}
