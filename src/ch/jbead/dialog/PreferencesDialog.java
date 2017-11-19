/** jbead - http://www.jbead.ch
    Copyright (C) 2001-2012  Damian Brunold

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
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import ch.jbead.BeadSymbols;
import ch.jbead.Localization;
import ch.jbead.Model;
import ch.jbead.Settings;
import ch.jbead.View;
import ch.jbead.audio.TalkingManager;

public class PreferencesDialog extends JDialog {
    private static final long serialVersionUID = 1L;

    private JTextField author;
    private JTextField organization;
    private JTextField symbols;
    private JCheckBox disablestartcheck;

    //Audio setting fields
    private JTextField pathToTts; //"%USERHOME%\\tts\\tts.exe";
    private JTextField ttsParams; //"-f 3 -v 0 -o <out> <text>";
    private JTextField fileType; //  "wav";
    private JTextField pathToSpeachFiles; // "%TMP%\\jbead_audio_cache";



    public PreferencesDialog(Localization localization, final Model model, final View view) {
        setTitle(localization.getString("preferences.title"));
        JPanel main = new JPanel();
        main.setBorder(BorderFactory.createEmptyBorder(7, 7, 7, 7));
        setContentPane(main);
        setLayout(new BorderLayout());

        JPanel form = new JPanel();
        form.setLayout(new GridBagLayout());

        final Settings settings = new Settings();
        settings.setCategory("user");

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.ipadx = 3;
        form.add(new JLabel(localization.getString("preferences.author")), constraints);

        constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        form.add(author = new JTextField(settings.loadString("author")), constraints);
        author.setColumns(20);

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.ipadx = 3;
        constraints.anchor = GridBagConstraints.WEST;
        form.add(new JLabel(localization.getString("preferences.organization")), constraints);

        constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        form.add(organization = new JTextField(settings.loadString("organization")), constraints);
        organization.setColumns(20);

        settings.setCategory("view");

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.ipadx = 3;
        constraints.anchor = GridBagConstraints.WEST;
        form.add(new JLabel(localization.getString("preferences.symbols")), constraints);

        constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy = 2;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        form.add(symbols = new JTextField(settings.loadString("symbols", BeadSymbols.SYMBOLS)), constraints);
        symbols.setColumns(33);

        settings.setCategory("update");

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.gridwidth = 2;
        constraints.anchor = GridBagConstraints.WEST;
        form.add(disablestartcheck = new JCheckBox(localization.getString("preferences.disablestartcheck")), constraints);
        disablestartcheck.setSelected(!settings.loadBoolean("check_at_start", true));

        settings.setCategory("audio");

        // PathToTTS
        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 4;
        constraints.ipadx = 3;
        constraints.anchor = GridBagConstraints.WEST;
        form.add(new JLabel(localization.getString("preferences.pathToTts")), constraints);

        constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy = 4;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        form.add(pathToTts = new JTextField(settings.loadString("pathToTts", TalkingManager.DEFAULT_PATH_TO_TTS)), constraints);
        pathToTts.setColumns(30);

        // ttsParams
        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 5;
        constraints.ipadx = 3;
        constraints.anchor = GridBagConstraints.WEST;
        form.add(new JLabel(localization.getString("preferences.ttsParams")), constraints);

        constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy = 5;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        form.add(ttsParams = new JTextField(settings.loadString("ttsParams", TalkingManager.DEFAULT_TTS_PARAMS)), constraints);
        ttsParams.setColumns(30);

        // pathToSpeachFiles
        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 6;
        constraints.ipadx = 3;
        constraints.anchor = GridBagConstraints.WEST;
        form.add(new JLabel(localization.getString("preferences.pathToSpeachFiles")), constraints);

        constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy = 6;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        form.add(pathToSpeachFiles = new JTextField(settings.loadString("pathToSpeachFiles", TalkingManager.DEFAULT_PATH_TO_SPEACH_FILES)), constraints);
        pathToSpeachFiles.setColumns(30);

        add(form, BorderLayout.CENTER);

        JPanel buttons = new JPanel();
        buttons.setLayout(new FlowLayout());
        JButton ok = new JButton(localization.getString("ok"));
        buttons.add(ok);
        JButton cancel = new JButton(localization.getString("cancel"));
        buttons.add(cancel);
        add(buttons, BorderLayout.SOUTH);

        setModal(true);
        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        ok.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                settings.setCategory("user");
                settings.saveString("author", author.getText());
                settings.saveString("organization", organization.getText());
                settings.setCategory("view");
                if (symbols.getText().length() == 0) {
                    settings.remove("symbols");
                } else {
                    settings.saveString("symbols", symbols.getText());
                }
                if (!BeadSymbols.SYMBOLS.equals(symbols.getText())) {
                    BeadSymbols.reloadSymbols();
                    view.refresh();
                }
                settings.setCategory("update");
                settings.saveBoolean("check_at_start", !disablestartcheck.isSelected());

                settings.setCategory("audio");
                settings.saveString("pathToTts", pathToTts.getText());
                settings.saveString("ttsParams", ttsParams.getText());
                settings.saveString("pathToSpeachFiles", pathToSpeachFiles.getText());

                if (!model.getAuthor().equals(author.getText())) {
                    model.setModified();
                    model.setAuthor(author.getText());
                }
                if (!model.getOrganization().equals(organization.getText())) {
                    model.setModified();
                    model.setOrganization(organization.getText());
                }
                dispose();
            }
        });
        cancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }

}
