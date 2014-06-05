/*
 * Copyright (c) 2012, Swedish Institute of Computer Science.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. Neither the name of the Institute nor the names of its contributors
 *    may be used to endorse or promote products derived from this software
 *    without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE INSTITUTE AND CONTRIBUTORS ``AS IS'' AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED.  IN NO EVENT SHALL THE INSTITUTE OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
 * OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 */

package org.contikios.cooja.mspmote.interfaces;
import java.awt.Color;
import org.contikios.cooja.ClassDescription;
import org.contikios.cooja.Mote;
import org.contikios.cooja.interfaces.LEDInterface;
import org.contikios.cooja.mspmote.MspMote;
import se.sics.mspsim.chip.Leds;
import se.sics.mspsim.core.StateChangeListener;

/**
 * @author Fredrik Osterlind, Niclas Finne
 */
@ClassDescription("Leds")
public class MspLED extends LEDInterface {

    private final Leds leds;

    private LED[] ledMap;

    public MspLED(Mote mote) {
        final MspMote mspMote = (MspMote) mote;
        leds = mspMote.getCPU().getChip(Leds.class);
        if (leds == null) {
            throw new IllegalStateException("Mote is not equipped with leds");
        }
        ledMap = new LED[leds.getLedsCount()];
        for (int i = 0; i < leds.getLedsCount(); i++) {
            ledMap[i] = new LED(new Color(leds.getLedsColor(i)));
        }
        leds.addStateChangeListener(new StateChangeListener() {

            public void stateChanged(Object source, int oldState, int newState) {

                for (int i = 0; i < leds.getLedsCount(); i++) {
                    ledMap[i].on = leds.isLedOn(i);
                }
                setChanged();
                notifyObservers();
            }

        });
    }

    @Override
    public boolean isAnyOn() {
        for (LED l : ledMap) {
          if (l.on) return true;
        }
        return false;
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
