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

/**
 * @author Fredrik Osterlind
 */
@ClassDescription("Trxeb LEDs")
public class TrxebLEDs extends LEDInterface {
	private static Logger logger = Logger.getLogger(TrxebLEDs.class);

	private Exp5438Mote mspMote;

  private LED[] ledMap = new LED[] {
    new LED(Color.RED),
    new LED(Color.YELLOW),
    new LED(Color.GREEN),
    new LED(Color.BLUE)
  };

  private static final int RED_IDX = 0;
  private static final int YELLOW_IDX = 1;
  private static final int GREEN_IDX = 2;
  private static final int BLUE_IDX = 3;

	public TrxebLEDs(Mote mote) {
		mspMote = (Exp5438Mote) mote;

		IOUnit unit = mspMote.getCPU().getIOUnit("P4");
		if (unit instanceof IOPort) {
			((IOPort) unit).addPortListener(new PortListener() {
				public void portWrite(IOPort source, int data) {
					ledMap[RED_IDX].on = (data & (1<<0)) == 0;
					ledMap[YELLOW_IDX].on = (data & (1<<1)) == 0;
					ledMap[GREEN_IDX].on = (data & (1<<2)) == 0;
					ledMap[BLUE_IDX].on = (data & (1<<3)) == 0;
					setChanged();
					notifyObservers();
				}
			});
		}
	}

  public boolean isAnyOn() {
    return ledMap[RED_IDX].on || ledMap[YELLOW_IDX].on || ledMap[GREEN_IDX].on || ledMap[BLUE_IDX].on;
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
