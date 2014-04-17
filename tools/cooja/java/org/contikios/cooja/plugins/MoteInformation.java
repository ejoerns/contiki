/*
 * Copyright (c) 2006, Swedish Institute of Computer Science.
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

package org.contikios.cooja.plugins;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.apache.log4j.Logger;

import org.contikios.cooja.ClassDescription;
import org.contikios.cooja.Cooja;
import org.contikios.cooja.Mote;
import org.contikios.cooja.MotePlugin;
import org.contikios.cooja.PluginType;
import org.contikios.cooja.Simulation;
import org.contikios.cooja.VisPlugin;
import org.contikios.cooja.motes.AbstractEmulatedMote;

/**
 * Mote information displays information about a given mote.
 *
 * @author Fredrik Osterlind
 */
@ClassDescription("Mote Information")
@PluginType(PluginType.MOTE_PLUGIN)
public class MoteInformation extends VisPlugin implements MotePlugin {
  private static final long serialVersionUID = 2359676837283723500L;
  private static final Logger logger = Logger.getLogger(MoteInformation.class);

  private Mote mote;
  private Simulation simulation;

  /**
   * Create a new mote information window.
   *
   * @param m Mote
   * @param s Simulation
   * @param gui Simulator
   */
  public MoteInformation(Mote m, Simulation s, Cooja gui) {
    super("Mote Information (" + m + ")", gui);
    this.mote = m;
    this.simulation = s;

    JLabel label;
    JButton button;
    
    JPanel mainPane = new JPanel(new GridBagLayout());
    mainPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

    /* Mote type */
    GridBagConstraints c = new GridBagConstraints();

    label = new JLabel("Mote type");
    label.setFont(label.getFont().deriveFont(Font.BOLD));
    c.gridx = 0;
    c.gridy = 0;
    c.ipadx = 5;
    c.ipady = 2;
    c.anchor = GridBagConstraints.WEST;
    mainPane.add(label, c);

    label = new JLabel(mote.getType().getDescription());
    c.gridx++;
    c.anchor = GridBagConstraints.EAST;
    mainPane.add(label, c);

    label = new JLabel(mote.getType().getIdentifier());
    c.gridy++;
    c.anchor = GridBagConstraints.EAST;
    mainPane.add(label, c);

    button = new JButton("Mote type information");
    c.gridy++;
    c.anchor = GridBagConstraints.EAST;
    c.fill = GridBagConstraints.HORIZONTAL;
    mainPane.add(button, c);

    button.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        simulation.getCooja().tryStartPlugin(MoteTypeInformation.class, simulation.getCooja(), simulation, mote);
      }
    });

    /* Mote interfaces */
    label = new JLabel("Mote interfaces");
    label.setFont(label.getFont().deriveFont(Font.BOLD));
    c.gridx = 0;
    c.gridy++;
    c.anchor = GridBagConstraints.WEST;
    c.fill = GridBagConstraints.NONE;
    mainPane.add(label, c);

    label = new JLabel(mote.getInterfaces().getInterfaces().size() + " interfaces");
    c.gridx++;
    c.anchor = GridBagConstraints.EAST;
    mainPane.add(label, c);

    button = new JButton("Mote interface viewer");
//    button.setPreferredSize(size);
    c.gridy++;
    c.anchor = GridBagConstraints.EAST;
    c.fill = GridBagConstraints.HORIZONTAL;
    mainPane.add(button, c);

    button.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        simulation.getCooja().tryStartPlugin(MoteInterfaceViewer.class, simulation.getCooja(), simulation, mote);
      }
    });

    /* CPU frequency */
    if (mote instanceof AbstractEmulatedMote) {
      AbstractEmulatedMote emulatedMote = (AbstractEmulatedMote) mote;
      label = new JLabel("CPU frequency");
      label.setFont(label.getFont().deriveFont(Font.BOLD));
      c.gridx = 0;
      c.gridy++;
      c.anchor = GridBagConstraints.WEST;
      c.fill = GridBagConstraints.NONE;
      mainPane.add(label, c);
      if (emulatedMote.getCPUFrequency() < 0) {
        label = new JLabel("[unknown]");
      } else {
        label = new JLabel(emulatedMote.getCPUFrequency() + " Hz");
      }
      c.gridx++;
      c.anchor = GridBagConstraints.EAST;
      mainPane.add(label, c);
    }

    /* Remove button */
//    label = new JLabel("Remove mote");
//    label.setFont(label.getFont().deriveFont(Font.BOLD));
//    c.gridx = 0;
//    c.gridy++;
//    c.anchor = GridBagConstraints.WEST;
//    mainPane.add(label, c);
//
//    button = new JButton("Remove");
//    c.gridx++;
//    c.anchor = GridBagConstraints.EAST;
//    c.fill = GridBagConstraints.HORIZONTAL;
//    mainPane.add(button, c);

    button.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        /* TODO In simulation event (if running) */
        simulation.removeMote(MoteInformation.this.mote);
      }
    });

    this.getContentPane().add(
            BorderLayout.CENTER,
            new JScrollPane(mainPane,
                            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                            JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED));
  }

  @Override
  public void packPlugin(JDesktopPane pane) {
    pack();
  }

  @Override
  public void closePlugin() {
  }

  @Override
  public Mote getMote() {
    return mote;
  }

}
