/*
 * Copyright (c) 2008, Swedish Institute of Computer Science.
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

package org.contikios.cooja.contikimote.interfaces;

import org.contikios.cooja.mote.memory.SectionMoteMemory;
import java.awt.*;
import java.util.*;
import javax.swing.JPanel;
import org.apache.log4j.Logger;

import org.contikios.cooja.*;
import org.contikios.cooja.contikimote.ContikiMoteInterface;
import org.contikios.cooja.interfaces.LEDInterface;
import org.contikios.cooja.interfaces.PolledAfterActiveTicks;

/**
 * LEDInterfaces mote interface.
 *
 * Contiki variables:
 * <ul>
 * <li>char simLedsValue
 * </ul>
 * <p>
 *
 * Core interface:
 * <ul>
 * <li>leds_interface
 * </ul>
 * <p>

 This observable notifies when any LEDInterface changes.
 *
 * @author Fredrik Osterlind
 */
public class ContikiLED extends LEDInterface implements ContikiMoteInterface, PolledAfterActiveTicks {
  private static Logger logger = Logger.getLogger(ContikiLED.class);

  private Mote mote = null;
  private SectionMoteMemory moteMem = null;
  private byte currentLedValue = 0;

  /**
   * Creates an interface to LEDInterfaces at mote.
   *
   * @param mote Mote
   *
   * @see Mote
   * @see org.contikios.cooja.MoteInterfaceHandler
   */
  public ContikiLED(Mote mote) {
    this.mote = mote;
    this.moteMem = (SectionMoteMemory) mote.getMemory();
  }

  public static String[] getCoreInterfaceDependencies() {
    return new String[]{"leds_interface"};
  }

  LED[] leds = new LED[] {
    new LED(Color.GREEN),
    new LED(Color.YELLOW),
    new LED(Color.RED)
  };

  @Override
  public LED[] getLEDs() {
    return leds;
  }

  @Override
  public LED getLED(int idx) {
    return leds[idx];
  }

  public boolean isAnyOn() {
    return currentLedValue > 0;
  }

  public void doActionsAfterTick() {
    boolean ledChanged;

    byte newLedsValue = moteMem.getByteValueOf("simLedsValue");
    ledChanged = newLedsValue != currentLedValue;

    currentLedValue = newLedsValue;
    if (ledChanged) {
      for (int idx = 0; idx < leds.length; idx++) {
        leds[idx].on = (currentLedValue & (1 << idx)) > 0;
      }
      this.setChanged();
      this.notifyObservers(mote);
    }
  }

  public void releaseInterfaceVisualizer(JPanel panel) {
    Observer observer = (Observer) panel.getClientProperty("intf_obs");
    if (observer == null) {
      logger.fatal("Error when releasing panel, observer is null");
      return;
    }

    this.deleteObserver(observer);
  }

}
