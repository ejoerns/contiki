/*
 * Copyright (c) 2007, Swedish Institute of Computer Science.
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
 *
 */

package org.contikios.cooja.avrmote.interfaces;

import java.awt.Color;

import org.apache.log4j.Logger;

import org.contikios.cooja.ClassDescription;
import org.contikios.cooja.Mote;
import org.contikios.cooja.avrmote.AvroraMote;
import org.contikios.cooja.interfaces.LEDInterface;
import avrora.sim.FiniteStateMachine;

/**
 * @author Joakim Eriksson
 * @author David Kopf
 * @author Enrico Jorns
 */
@ClassDescription("LEDs")
public class AvroraLED extends LEDInterface {
  private static final Logger logger = Logger.getLogger(AvroraLED.class);

//  private avrora.sim.FiniteStateMachine.Probe[] ledProbes = {null, null, null, null};
  private avrora.sim.platform.LED.LEDGroup leds;

//  private boolean probesInserted = false;
  private LED[] ledMap;

  public AvroraLED(Mote mote) {
    leds = (avrora.sim.platform.LED.LEDGroup) ((AvroraMote)mote).getPlatform().getDevice("leds");
    ledMap = new LED[leds.leds.length];

    int index = 0;
    for ( avrora.sim.platform.LED led : leds.leds) {
      // Map Avrora color notation to awt Color
      switch (led.color) {
        case "Red":
          ledMap[index] = new LED(Color.RED);
          break;
        case "Green":
          ledMap[index] = new LED(Color.GREEN);
          break;
        case "Blue":
          ledMap[index] = new LED(Color.BLUE);
          break;
        case "Yellow":
          ledMap[index] = new LED(Color.YELLOW);
          break;
        case "Orange":
          ledMap[index] = new LED(Color.ORANGE);
          break;
        default:
          logger.warn("Color " + led.color + " not available");
          break;
      }
      
      final int idxParam = index;
      led.getFSM().insertProbe(new FiniteStateMachine.Probe() {
        @Override
        public void fireBeforeTransition(int beforeState, int afterState) {
        }

        @Override
        public void fireAfterTransition(int beforeState, int afterState) {
          ledMap[idxParam].on = afterState > 0;
          setChanged();
          notifyObservers();
        }
      });
      
      index++;
    }

//    probesInserted = true;

  }

  @Override
  public void removed() {
    super.removed();
    /* TODO Remove probes */
  }

  @Override
  public boolean isAnyOn() {
    for (int i=0; i<4; i++) if (ledMap[i].on) return true;
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

