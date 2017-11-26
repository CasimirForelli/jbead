/** jbead - http://www.jbead.ch
    Copyright (C) 2001-2017  Damian Brunold

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
import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;

import ch.jbead.BeadList;
import ch.jbead.BeadRun;
import ch.jbead.JBeadFrame;
import ch.jbead.audio.DefaultColorNameMap;
import ch.jbead.audio.TalkingManager;

/**
 *
 */
public class TalkingDialog extends JDialog {

    private static final long serialVersionUID = 1L;

    private JButton btnStart = null;
    private JButton btnNext = null;
    private JButton btnBack = null;
    private JButton btnRepeat = null;
    private JButton btnExit = null;

    JPanel beadRunView = null;

    private BeadList beadList;

    private Map<Byte, String> colorNameMap;

    private TalkingManager talkingManager;

    private BeadRun currentBeadRun;
    private int currentBeadRunIdx = -1;

    private JBeadFrame parentFrame;

    /**
     *
     * @param frame
     */
    public TalkingDialog(JBeadFrame frame) {

        parentFrame = frame;

        // Empty model => return
        if (frame.getModel().getRepeat() == 0) return;

        this.beadList = new BeadList(frame.getModel());
        this.colorNameMap = DefaultColorNameMap.create(Locale.GERMAN);
        this.talkingManager = new TalkingManager();

        setTitle(frame.getString("talkingdialog.title"));
        setSize(400, 400);
        setResizable(false);
        JPanel main = new JPanel();
        main.setBorder(BorderFactory.createEmptyBorder(7, 7, 7, 7));
        setContentPane(main);
        setLayout(new BorderLayout());

        JPanel form = new JPanel();
        form.setLayout(new GridBagLayout());

        JProgressBar progressBar = new JProgressBar(0, beadList.size());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;

        form.add(progressBar, constraints);

        btnStart = new JButton("Start");
        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 1;

        form.add(btnStart, constraints);

        add(form, BorderLayout.PAGE_START);


        form = new JPanel();
        form.setLayout(new GridBagLayout());

        beadRunView = new JPanel();
        beadRunView.setLayout(new BoxLayout(beadRunView, BoxLayout.Y_AXIS));
        beadRunView.add(createLabel("  -2"));
        beadRunView.add(createLabel("  -1"));
        beadRunView.add(createLabel("=> 0"));
        beadRunView.add(createLabel("  +1"));
        beadRunView.add(createLabel("  +2"));
//        beadRunView.add(new JLabel("  -2"));
//        beadRunView.add(new JLabel("  -1"));
//        beadRunView.add(new JLabel("=> 0"));
//        beadRunView.add(new JLabel("  +1"));
//        beadRunView.add(new JLabel("  +2"));

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        form.add(beadRunView, constraints);

        add(form, BorderLayout.CENTER);


        form = new JPanel();
        form.setLayout(new GridBagLayout());

        btnNext = new JButton("Weiter");
        getRootPane().setDefaultButton(btnNext);
        btnNext.requestFocusInWindow();
        constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy = 0;
        form.add(btnNext, constraints);

        btnBack = new JButton("Zurück");
        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 1;
        form.add(btnBack, constraints);

        btnRepeat = new JButton("Nochmal");
        constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy = 1;
        form.add(btnRepeat, constraints);

        btnExit = new JButton("Ende");
        constraints = new GridBagConstraints();
        constraints.gridx = 2;
        constraints.gridy = 1;
        form.add(btnExit, constraints);

        btnNext.setEnabled(false);
        btnRepeat.setEnabled(false);
        btnBack.setEnabled(false);
        btnStart.setEnabled(true);

        add(form, BorderLayout.PAGE_END);

        setModal(true);
        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        btnStart.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Thread thread1 = new Thread(new Runnable() {
                    @Override
                    public void run(){
                        talkingManager.createBeadListAudios(TalkingDialog.this.beadList, progressBar, TalkingDialog.this.colorNameMap, parentFrame);
                        btnNext.setEnabled(true);
                        btnRepeat.setEnabled(true);
                        btnBack.setEnabled(true);
                        btnStart.setEnabled(false);
                    }
                });

                thread1.start();

//                talkingManager.createBeadListAudios(TalkingDialog.this.beadList, progressBar, TalkingDialog.this.colorNameMap);
            }
        });

        btnExit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        btnRepeat.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Thread thread1 = new Thread(new Runnable() {
                    @Override
                    public void run(){
                        talkingManager.speak(currentBeadRun, colorNameMap);
                    }
                });
                thread1.start();

            }
        });

        btnBack.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                currentBeadRunIdx--;
                currentBeadRun = beadList.get(currentBeadRunIdx);
                drawBeadRunView();

                Thread thread1 = new Thread(new Runnable() {
                    @Override
                    public void run(){
                        talkingManager.speak(currentBeadRun, colorNameMap);
                    }
                });
                thread1.start();
            }

        });

        btnNext.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                if(currentBeadRunIdx == beadList.size()) {
                    talkingManager.speakText("talkingdialog.pattern.end");
                    return;
                }

                currentBeadRunIdx++;
                currentBeadRun = beadList.get(currentBeadRunIdx);
                drawBeadRunView();

                Thread thread1 = new Thread(new Runnable() {
                    @Override
                    public void run(){
                        talkingManager.speak(currentBeadRun, colorNameMap);
                    }
                });
                thread1.start();

            }

        });

    }

    /**
     * @param string
     * @return
     */
    private Component createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setHorizontalAlignment(SwingConstants.TRAILING );
        lbl.setBackground(Color.WHITE);
        return lbl;
    }

    /**
     *
     */
    private void drawBeadRunView() {

        for (int y = 0; y <= 4; y++) {
            JLabel lbl = (JLabel) beadRunView.getComponent(y);
            int idx = currentBeadRunIdx - 2 + y;
            if (idx < 0 || idx >= beadList.size()) {
                lbl.setText("-----------");
            } else {
                BeadRun br = beadList.get(idx);
                String prefix = "    ";
                if (y == 2) prefix = " => ";
                lbl.setText(idx + "." + prefix + colorNameMap.get(br.getColor())+ " " + br.getCount() + "x");
            }
        }

        btnNext.requestFocusInWindow();

    }

}
