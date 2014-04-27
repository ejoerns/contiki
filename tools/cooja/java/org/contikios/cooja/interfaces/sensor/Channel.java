/*
 * Copyright (c) 2014, TU Braunschweig
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

package org.contikios.cooja.interfaces.sensor;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Enrico Joerns
 */
public class Channel {
  public String name;
  public String unit;
  public DataModel model;
  public double default_ = 0.0;
  private AbstractSensorFeeder feeder = null;
  private AbstractSensorFeeder.FeederParameter param = null;

  public Channel(String name, String unit, DataModel model) {
    this.name = name;
    this.unit = unit;
    this.model = model;
  }

  public Channel(String name) {
    this(name, null, new DataModel.EmptyDataModel());
  }

  public DataModel getDataModel() {
    return model;
  }

  /* to be set by the feeder */
  public void setFeeder(AbstractSensorFeeder feeder, AbstractSensorFeeder.FeederParameter param) {
    this.feeder = feeder;
    this.param = param;
    notifyFeederUpdate();
  }

  public AbstractSensorFeeder getFeeder() {
    return this.feeder;
  }

  public AbstractSensorFeeder.FeederParameter getFeederParameter() {
    return param;
  }

  private final List<Listener> listeners = new LinkedList<>();

  private void notifyFeederUpdate() {
    for (Listener l : listeners) {
      System.out.println(l);
      l.onFeederUpdated();
    }
  }

  public void addChannelListener(Listener l) {
    if (!listeners.contains(l)) {
      listeners.add(l);
    }
  }

  public void removeChannelListener(Listener l) {
    if (listeners.contains(l)) {
      listeners.remove(l);
    }
  }

  public static interface Listener {

    void onFeederUpdated();
  }
  
}
