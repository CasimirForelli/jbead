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

package ch.jbead.audio;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javax.swing.KeyStroke;

import ch.jbead.BeadList;
import ch.jbead.Localization;
import ch.jbead.Model;
import junit.framework.*;

/**
 *
 */
public class AudioTest extends TestCase implements Localization {

    BeadList beadList = null;

    private ResourceBundle bundle = ResourceBundle.getBundle("jbead");
    private Model model = new Model(this);

    private Map<Byte, String> colorNameMap;

    @Override
    protected void setUp() {
        model.setWidth(5);
        model.setHeight(10);
        model.set(4, (byte) 1);
        model.updateRepeat();
        model.set(5, (byte) 4);
        model.updateRepeat();
        model.set(10, (byte) 2);
        model.updateRepeat();
        model.set(11, (byte) 2);
        model.updateRepeat();
        model.set(15, (byte) 2);
        model.updateRepeat();

        beadList = new BeadList(model);

        colorNameMap = new HashMap<>();
        colorNameMap.put((byte) 0, "Weiß");
        colorNameMap.put((byte) 1, "Braun");
        colorNameMap.put((byte) 2, "Rot");
        colorNameMap.put((byte) 3, "Grün");
        colorNameMap.put((byte) 4, "Blau");

    }

    public void testAudioFileCreation() {
        long start = System.currentTimeMillis();
        TalkingManager m = new TalkingManager();
//        m.clearCache();
        m.createBeadListAudios(beadList, null, colorNameMap);
        System.out.println("Fertig nach "+ (System.currentTimeMillis()-start) + " ms");

    }



    public ResourceBundle getBundle() {
        return bundle;
    }

    public String getString(String key) {
        return bundle.getString(key);
    }

    public int getMnemonic(String key) {
        return bundle.getString(key).charAt(0);
    }

    public KeyStroke getKeyStroke(String key) {
        return KeyStroke.getKeyStroke(bundle.getString(key));
    }


}
