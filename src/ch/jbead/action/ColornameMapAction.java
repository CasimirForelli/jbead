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

package ch.jbead.action;

import java.awt.event.ActionEvent;

import ch.jbead.JBeadFrame;
import ch.jbead.dialog.ColornameMapDialog;
import ch.jbead.dialog.PatternHeightDialog;
import ch.jbead.dialog.TalkingDialog;

public class ColornameMapAction extends BaseAction {

    private static final long serialVersionUID = 1L;

    private static final String NAME = "pattern.colornamemap";

    public ColornameMapAction(JBeadFrame frame) {
        super(NAME, frame);
        putValue(SHORT_DESCRIPTION, localization.getString("action.pattern.colornamemap.description"));
        putValue(MNEMONIC_KEY, localization.getMnemonic("action.pattern.colornamemap.mnemonic"));
    }

    public void actionPerformed(ActionEvent e) {

        ColornameMapDialog dialog = new ColornameMapDialog(frame);

        dialog.setVisible(true);

    }

}
