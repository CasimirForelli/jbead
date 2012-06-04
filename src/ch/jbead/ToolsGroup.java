/** jbead - http://www.brunoldsoftware.ch
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

package ch.jbead;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ButtonGroup;

public class ToolsGroup {

    private ButtonGroup menugroup = new ButtonGroup();
    private ButtonGroup toolbargroup = new ButtonGroup();

    private List<ToolButton> buttons = new ArrayList<ToolButton>();
    private List<ToolMenuItem> items = new ArrayList<ToolMenuItem>();

    public ToolButton addTool(ToolButton button) {
        buttons.add(button);
        toolbargroup.add(button);
        return button;
    }

    public ToolMenuItem addTool(ToolMenuItem item) {
        items.add(item);
        menugroup.add(item);
        return item;
    }

    public void selectTool(int index) {
        buttons.get(index).setSelected(true);
        items.get(index).setSelected(true);
    }

    public boolean isSelected(int index) {
        return buttons.get(index).isSelected();
    }

}
