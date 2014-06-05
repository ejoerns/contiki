/*
 * Copyright (c) 2011, Swedish Institute of Computer Science.
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

import org.apache.log4j.Logger;

import org.contikios.cooja.ClassDescription;
import org.contikios.cooja.Mote;
import org.contikios.cooja.interfaces.LEDInterface;
import org.contikios.cooja.mspmote.TyndallMote;
import se.sics.mspsim.core.IOPort;
import se.sics.mspsim.core.IOUnit;
import se.sics.mspsim.core.PortListener;
import se.sics.mspsim.platform.tyndall.TyndallNode;

/**
 * @author Fredrik Osterlind
 */
@ClassDescription("Tyndall LEDs")
public class TyndallLED extends LEDInterface {
  private static Logger logger = Logger.getLogger(TyndallLED.class);

  private TyndallMote mspMote;

  private LED[] ledMap = new LED[] {
    new LED(Color.RED),
    new LED(Color.GREEN)
  };

  private static final int RED_IDX = 0;
  private static final int GREEN_IDX = 1;

  public TyndallLED(Mote mote) {
    mspMote = (TyndallMote) mote;

    IOUnit unit = mspMote.getCPU().getIOUnit("P7");
    if (unit instanceof IOPort) {
      ((IOPort) unit).addPortListener(new PortListener() {
        public void portWrite(IOPort source, int data) {
          ledMap[RED_IDX].on = (data & TyndallNode.LEDS_CONF_RED) == 0;
          setChanged();
          notifyObservers();
        }
      });
    }
    unit = mspMote.getCPU().getIOUnit("P8");
    if (unit instanceof IOPort) {
      ((IOPort) unit).addPortListener(new PortListener() {
        public void portWrite(IOPort source, int data) {
          ledMap[GREEN_IDX].on = (data & TyndallNode.LEDS_CONF_GREEN) == 0;
          setChanged();
          notifyObservers();
        }
      });
    }
  }

  public boolean isAnyOn() {
    return ledMap[RED_IDX].on || ledMap[GREEN_IDX].on;
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
