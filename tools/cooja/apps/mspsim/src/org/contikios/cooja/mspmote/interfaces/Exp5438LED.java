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
import org.contikios.cooja.mspmote.Exp5438Mote;
import se.sics.mspsim.core.IOPort;
import se.sics.mspsim.core.IOUnit;
import se.sics.mspsim.core.PortListener;
import se.sics.mspsim.platform.ti.Exp5438Node;

/**
 * @author Fredrik Osterlind
 */
@ClassDescription("Exp5438 LEDs")
public class Exp5438LED extends LEDInterface {
  private static Logger logger = Logger.getLogger(Exp5438LED.class);

  private Exp5438Mote mspMote;

  private LED[] ledMap = new LED[] {
    new LED(Color.RED),
    new LED(Color.YELLOW)
  };

  private static final int RED_IDX = 0;
  private static final int YELLOW_IDX = 1;

  public Exp5438LED(Mote mote) {
    mspMote = (Exp5438Mote) mote;

    IOUnit unit = mspMote.getCPU().getIOUnit("P1");
    if (unit instanceof IOPort) {
      ((IOPort) unit).addPortListener(new PortListener() {
        public void portWrite(IOPort source, int data) {
          ledMap[RED_IDX].on = (data & Exp5438Node.LEDS_CONF_RED) != 0;
          ledMap[YELLOW_IDX].on = (data & Exp5438Node.LEDS_CONF_YELLOW) != 0;
          setChanged();
          notifyObservers();
        }
      });
    }
  }

  public boolean isAnyOn() {
    return ledMap[RED_IDX].on || ledMap[YELLOW_IDX].on;
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
