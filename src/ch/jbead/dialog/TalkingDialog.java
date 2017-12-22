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
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;

import ch.jbead.BeadCounts;
import ch.jbead.BeadList;
import ch.jbead.BeadRun;
import ch.jbead.JBeadFrame;
import ch.jbead.audio.TalkingManager;
import ch.jbead.view.BeadRunViewPanel;

/**
 *
 */
public class TalkingDialog extends JDialog {

    private static final long serialVersionUID = 1L;

    private JButton btnNext = null;
    private JButton btnBack = null;
    private JButton btnRepeat = null;
    private JButton btnExit = null;
    private JButton btnSaveState = null;
    private JButton btnRestart = null;

    BeadRunViewPanel beadRunView = null;

    private BeadList beadList;
    private BeadCounts beadCounts;

    private Map<Byte, String> colorNameMap;

    private TalkingManager talkingManager;

    private BeadRun currentBeadRun;
    private int currentBeadRunIdx = -1;

    private JBeadFrame parentFrame;

    private JProgressBar beadRunProgressBar;
    private JTextArea beadRunProgressTextArea;

    /**
     *
     * @param frame
     */
    public TalkingDialog(JBeadFrame frame) {

        parentFrame = frame;

        // Empty model => return
        if (frame.getModel().getRepeat() == 0) return;

        this.beadList = new BeadList(frame.getModel());
        this.beadCounts = new BeadCounts(frame.getModel());
        this.colorNameMap = frame.getModel().getColorNamesMap();

        this.currentBeadRunIdx = frame.getModel().getLastReadingPos();

        this.talkingManager = new TalkingManager();

        setTitle(frame.getString("talkingdialog.title"));
        setSize(400, 400);
        setResizable(false);
        JPanel main = new JPanel();
        main.setBorder(BorderFactory.createEmptyBorder(7, 7, 7, 7));
        setContentPane(main);
        setLayout(new BorderLayout(5, 5));

        JPanel form = new JPanel();
        form.setLayout(new GridBagLayout());

        JProgressBar progressBar = new JProgressBar(0, beadList.size());
        progressBar.setString(frame.getString("talkingdialog.progress.running"));
        progressBar.setStringPainted(true);
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;

        form.add(progressBar, constraints);

        add(form, BorderLayout.PAGE_START);

        form = new JPanel();
        form.setLayout(new GridBagLayout());

        beadRunView = new BeadRunViewPanel(beadList, parentFrame.getModel(), parentFrame);
        beadRunView.currentBeadRunIdx = currentBeadRunIdx;

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        form.add(beadRunView, constraints);

        add(form, BorderLayout.CENTER);

        form = new JPanel();
        form.setLayout(new GridBagLayout());

        btnSaveState = new JButton("Save");

        btnNext = new JButton(frame.getString("talkingdialog.btnNext"));
        btnNext.setMnemonic(frame.getMnemonic("talkingdialog.btnNext.mnemonic"));
        getRootPane().setDefaultButton(btnNext);
        btnNext.requestFocusInWindow();
        constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy = 0;
        form.add(btnNext, constraints);

        btnBack = new JButton(frame.getString("talkingdialog.btnBack"));
        btnBack.setMnemonic(frame.getMnemonic("talkingdialog.btnBack.mnemonic"));
        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 1;
        form.add(btnBack, constraints);

        btnRepeat = new JButton(frame.getString("talkingdialog.btnRepeat"));
        btnRepeat.setMnemonic(frame.getMnemonic("talkingdialog.btnRepeat.mnemonic"));
        constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy = 1;
        form.add(btnRepeat, constraints);

        btnExit = new JButton(frame.getString("talkingdialog.btnExit"));
        btnExit.setMnemonic(frame.getMnemonic("talkingdialog.btnExit.mnemonic"));
        constraints = new GridBagConstraints();
        constraints.gridx = 2;
        constraints.gridy = 1;
        form.add(btnExit, constraints);

        btnRestart = new JButton("Neustart");
        btnRestart.setMnemonic(frame.getMnemonic("talkingdialog.btnRestart.mnemonic"));
        constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy = 2;
        form.add(btnRestart, constraints);

        btnNext.setEnabled(false);
        btnRepeat.setEnabled(false);
        btnBack.setEnabled(false);
        btnRestart.setEnabled(false);

        add(form, BorderLayout.PAGE_END);

        // Progress
        form = new JPanel();
        form.setLayout(new BorderLayout(5, 5));
        form.add(beadRunProgressBar = new JProgressBar(JProgressBar.VERTICAL, 0, beadList.size()), BorderLayout.WEST);
        beadRunProgressBar.setValue(beadList.size());
        Color bg = beadRunProgressBar.getBackground();
        beadRunProgressBar.setBackground(beadRunProgressBar.getForeground());
        beadRunProgressBar.setForeground(bg);

        form.add(beadRunProgressTextArea = new JTextArea("Restfarben:", 15, 25));
        beadRunProgressTextArea.setEnabled(false);
        beadRunProgressTextArea.setDisabledTextColor(Color.BLACK);
        beadRunProgressTextArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        beadRunProgressTextArea.setBackground(form.getBackground());
        beadRunProgressTextArea.setLineWrap(true);
        add(form, BorderLayout.EAST);

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
                        TalkingDialog.this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                        TalkingDialog.this.repaint();
                        talkingManager.createBeadListAudios(TalkingDialog.this.beadList, progressBar, TalkingDialog.this.colorNameMap, parentFrame);
                        btnNext.setEnabled(true);
                        btnNext.requestFocusInWindow();
                        btnRepeat.setEnabled(true);
                        btnBack.setEnabled(true);
                        btnRestart.setEnabled(true);
                        progressBar.setString(frame.getString("talkingdialog.progress.complete"));
                        updateProgress();
                        TalkingDialog.this.setCursor(null);

                    }
                });
                thread1.start();

            }

        });

        btnExit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                parentFrame.getModel().setLastReadingPos(currentBeadRunIdx);
                parentFrame.getModel().setModified();
                parentFrame.fileSaveClick(parentFrame.getModel().isSaved(), parentFrame.getModel().getFile());
                dispose();
            }
        });

        btnSaveState.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                parentFrame.getModel().setLastReadingPos(currentBeadRunIdx);
                parentFrame.getModel().setModified();
                parentFrame.fileSaveClick(parentFrame.getModel().isSaved(), parentFrame.getModel().getFile());
                if (btnNext != null) btnNext.requestFocusInWindow();

            }
        });

        btnRepeat.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Thread thread1 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        talkingManager.speak(currentBeadRun, colorNameMap);
                    }
                });
                thread1.start();
                if (btnNext != null) btnNext.requestFocusInWindow();

            }
        });

        btnBack.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                if (currentBeadRunIdx <= 0) {
                    return;
                }
                currentBeadRunIdx--;
                currentBeadRun = beadList.get(currentBeadRunIdx);
                beadRunView.currentBeadRunIdx = currentBeadRunIdx;
                beadRunView.repaint();
                updateProgress();

                Thread thread1 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        talkingManager.speak(currentBeadRun, colorNameMap);
                    }
                });
                thread1.start();
                parentFrame.getModel().setLastReadingPos(currentBeadRunIdx);
                parentFrame.getModel().setModified();
                parentFrame.fileSaveClick(parentFrame.getModel().isSaved(), parentFrame.getModel().getFile());
                if (btnNext != null) btnNext.requestFocusInWindow();

            }

        });

        btnRestart.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                currentBeadRunIdx = -1;
                currentBeadRun = null;
                beadRunView.currentBeadRunIdx = currentBeadRunIdx;
                beadRunView.repaint();
                updateProgress();

                if (btnNext != null) btnNext.requestFocusInWindow();

            }

        });

        btnNext.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                if (currentBeadRunIdx + 1 == beadList.size()) {
                    talkingManager.speakText("talkingdialog.pattern.end");
                    return;
                }

                currentBeadRunIdx++;
                currentBeadRun = beadList.get(currentBeadRunIdx);
                beadRunView.currentBeadRunIdx = currentBeadRunIdx;
                beadRunView.repaint();
                updateProgress();

                Thread thread1 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        talkingManager.speak(currentBeadRun, colorNameMap);
                    }
                });
                thread1.start();
                parentFrame.getModel().setLastReadingPos(currentBeadRunIdx);
                parentFrame.getModel().setModified();
                parentFrame.fileSaveClick(parentFrame.getModel().isSaved(), parentFrame.getModel().getFile());

                if (btnNext != null) btnNext.requestFocusInWindow();

            }

        });

    }

    /**
     *
     */
    private void updateProgress() {

        beadRunProgressBar.setValue(beadList.size() - currentBeadRunIdx);
        String text = calculateRest();
        beadRunProgressTextArea.setText(text);

    }

    private String calculateRest() {

        StringBuilder restString = new StringBuilder("Restfarben:\n");

        Map<Byte, Integer> counts = new HashMap<Byte, Integer>();
        int i = 0;
        // count rest
        for (BeadRun beadRun : beadList) {
            Integer count = counts.get(beadRun.getColor());
            if (count == null) {
                counts.put(beadRun.getColor(), count = new Integer(0));
            }
            i++;
            if (i > currentBeadRunIdx) {
                counts.put(beadRun.getColor(), count + beadRun.getCount());
            }
        }
        // prepare text
        for (Iterator<Byte> iterator = counts.keySet().iterator(); iterator.hasNext();) {
            Byte c = iterator.next();
            Integer anz = counts.get(c);
            if (anz > 0) {
                String cname = this.colorNameMap.get(c);

                String t = "%4d x %s\n";
                String t2 = String.format(t, anz, cname);
                restString.append(t2);
            }
        }

        return restString.toString();
    }

}
