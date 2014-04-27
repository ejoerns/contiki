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
import org.apache.log4j.Logger;
import org.contikios.cooja.Mote;

/**
 *
 * @author Enrico Jorns
 */
public class Sensor {

  private final Mote mote;
  private final SensorAdapter sensor;
  private static final Logger logger = Logger.getLogger(Sensor.class);

  public Sensor(Mote mote, SensorAdapter sensor) {
    this.mote = mote;
    this.sensor = sensor;
    for (Channel c : sensor.getChannels()) {
      addForwarder(c);
    }
  }
  
  private void addForwarder(final Channel c) {
    c.addChannelListener(new Channel.Listener() {

      @Override
      public void onFeederUpdated() {
        notifyFeederChanged(c);
      }
    });
  }

  /**
   * Returns the mote this sensor belongs to.
   *
   * @return Mote that owns this sensor.
   */
  public Mote getMote() {
    return mote;
  }

  /**
   * Returns name of this sensor.
   *
   * @return Name
   */
  public String getName() {
    return sensor.getName();
  }

  /**
   * Set value for a sensor channel
   *
   * @param channel Channel to set value for.
   * @param value Value to set
   */
  public final void setValue(int channel, double value) {
    logger.info("Validating with model: " + getChannel(channel).getDataModel().getClass().getSimpleName());
    /* validate and forward to SensorAdapter */
    double retval = getChannel(channel).getDataModel().validate(value);
    sensor.setValue(channel, retval);
    notifyNewData(getChannel(channel));
  }

  /**
   * Returns all channels of this sensor.
   *
   * @return All Channels of this sensor
   */
  public Channel[] getChannels() {
    return sensor.getChannels();
  }

  /**
   * Returns channel.
   *
   * @param channel index of channel to return
   * @return Channel at index
   */
  public Channel getChannel(int channel) {
    return sensor.getChannels()[channel];
  }
  
  /**
   * Returns number of channels for this sensor.
   *
   * @return Number of chanels
   */
  public int numChannels() {
    return sensor.getChannels().length;
  }

  private List<SensorListener> sensorListeners = new LinkedList<>();
  
  /**
   * Interface for clients that want to listen for sensor updates have to implement.
   */
  public interface SensorListener {

    void onFeederUpdated(Channel ch);
    void onDataUpdated(Channel ch);
    void onModelViolation(Channel ch);
  }
  
  /**
   * Add listener to be notified about sensor updates.
   * @param listener Listener to add
   */
  public void addSensorListener(SensorListener listener) {
    if (!sensorListeners.contains(listener)) {
      sensorListeners.add(listener);
    }
  }
  
  /**
   * Remove listener to be notified about sensor updates.
   * @param listener Listener to remove
   */
  public void removeSensorListener(SensorListener listener) {
    if (sensorListeners.contains(listener)) {
      sensorListeners.remove(listener);
    }
  }
  
  private void notifyFeederChanged(Channel ch) {
    for (SensorListener l : sensorListeners) {
      l.onFeederUpdated(ch);
    }
  }
  
  // XXX check if required
  private void notifyModelViolation(Channel ch) {
    throw new UnsupportedOperationException();
  }
  
  // XXX check if required
  private void notifyNewData(Channel ch) {
    for (SensorListener l : sensorListeners) {
      l.onDataUpdated(ch);
    }
  }

  /* XXX Configuration interface? */
}
