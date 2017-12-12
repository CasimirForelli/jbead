/** jbead - http://www.jbead.ch
    Copyright (C) 2017  Casimir Forelli

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package ch.jbead.dialog;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;

import ch.jbead.BeadCounts;
import ch.jbead.JBeadFrame;
import ch.jbead.Model;

/**
 *
 */
public class ColornameMapDialog extends JDialog {

    private static final long serialVersionUID = 1L;

    private JButton btnOk = null;
    private JButton btnCancel = null;

    private Map<Byte, String> colorNameMap;

    private JBeadFrame parentFrame;

    private JPanel colorMapForm;

    private List<ColorNamePair> colorNamePairComps = new ArrayList<ColorNamePair>();

    /**
     *
     * @param frame
     */
    public ColornameMapDialog(JBeadFrame frame) {

        parentFrame = frame;
        Model model = frame.getModel();
        BeadCounts counts = new BeadCounts(model);

        this.colorNameMap = frame.getModel().getColorNamesMap();

        setTitle(frame.getString("colornamemapdialog.title"));
        setSize(400, 400);
        setResizable(true);
        JPanel main = new JPanel();
        main.setBorder(BorderFactory.createEmptyBorder(7, 7, 7, 7));
        setContentPane(main);
        setLayout(new BorderLayout());

        GridBagConstraints constraints = new GridBagConstraints();
        JPanel form = new JPanel();
        form.setLayout(new GridBagLayout());


        add(form, BorderLayout.PAGE_START);

        form = new JPanel();
        form.setLayout(new GridBagLayout());


        int y = 0;
        for (byte color = 0; color < frame.getModel().getColorCount(); color++) {

            if(counts.getCount(color) == 0) continue;

            constraints = new GridBagConstraints();
            constraints.gridx = 0;
            constraints.gridy = y;
            constraints.insets = new Insets(2, 2, 2, 2);
            Color colorObject =  frame.getModel().getColor(color);
            ColorCanvas colorCanvas = new ColorCanvas(color, colorObject);
            form.add(colorCanvas , constraints);
            constraints = new GridBagConstraints();
            constraints.gridx = 1;
            constraints.gridy = y;
            constraints.insets = new Insets(2, 2, 2, 2);
            String colorName = frame.getModel().getColorNamesMap().get(color);
            JTextField colorTextbox = new JTextField(colorName);
            colorTextbox.setColumns(10);
            form.add(colorTextbox, constraints);
            colorNamePairComps.add(new ColorNamePair(colorCanvas, colorTextbox));
            y++;

        }
        colorMapForm = form;
        add(form, BorderLayout.CENTER);

        form = new JPanel();
        form.setLayout(new GridBagLayout());

        btnOk = new JButton("OK");

        getRootPane().setDefaultButton(btnOk);
        btnOk.requestFocusInWindow();
        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        form.add(btnOk, constraints);

        btnCancel = new JButton(frame.getString("cancel"));
        constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy = 0;
        form.add(btnCancel, constraints);

        add(form, BorderLayout.PAGE_END);

        setModal(true);
        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // prepare speech files after dialog opened
        this.addWindowListener(new WindowAdapter() {

            @Override
            public void windowOpened(WindowEvent e) {

                Thread thread1 = new Thread(new Runnable() {
                    @Override
                    public void run() {


                    }
                });
                thread1.start();

            }

        });

        btnOk.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // update colormap in model and save
                boolean changed = false;
                for (ColorNamePair cnp : colorNamePairComps) {
                    String colorName = cnp.text.getText();
                    Byte color = cnp.canvas.getColorIdx();
                    if(!colorName.equals(colorNameMap.get(color))) {
                        colorNameMap.put(color, colorName);
                        changed = true;
                    }
                }

                if(changed) {
                    parentFrame.getModel().setModified();
                    parentFrame.fileSaveClick(parentFrame.getModel().isSaved(), parentFrame.getModel().getFile());
                }
                dispose();
            }
        });

        btnCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

    }

    private class ColorNamePair {
        ColorCanvas canvas;
        JTextField text;
        /**
         * @param canvas
         * @param text
         */
        public ColorNamePair(ColorCanvas canvas, JTextField text) {
            super();
            this.canvas = canvas;
            this.text = text;
        }

    }
}
