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
import java.util.Locale;
import java.util.Map;

public class DefaultColorNameMap {

    /**
     * @param german
     * @return
     */
    public static Map<Byte, String> create(Locale locale) {

        Map<Byte, String> map = new HashMap<>();
        if(locale.getLanguage().equals(Locale.GERMAN.getLanguage())) {
            map.put((byte) 0, "weiß");
            map.put((byte) 1, "rotbraun");
            map.put((byte) 2, "hellrot");
            map.put((byte) 3, "dunkelrot");
            map.put((byte) 4, "mittelrot");
            map.put((byte) 5, "orange");
            map.put((byte) 6, "gelb");
            map.put((byte) 7, "zitrone");
            map.put((byte) 8, "hellgelb");
            map.put((byte) 9, "dunkelbraun");
            map.put((byte) 10, "hellbraun");
            map.put((byte) 11, "dunkelblau");
            map.put((byte) 12, "mittelblau");
            map.put((byte) 13, "hellblau");
            map.put((byte) 14, "blau");
            map.put((byte) 15, "blau");
            map.put((byte) 16, "blau");
            map.put((byte) 17, "stahl");
            map.put((byte) 18, "oliv");
            map.put((byte) 19, "dunkelgrün");
            map.put((byte) 20, "grün");
            map.put((byte) 21, "grasgrün");
            map.put((byte) 22, "hellgrün");
            map.put((byte) 23, "rosa");
            map.put((byte) 24, "pink");
            map.put((byte) 25, "violett");
            map.put((byte) 26, "violett");
            map.put((byte) 27, "hellgrau");
            map.put((byte) 28, "mittelgrau");
            map.put((byte) 29, "dunkelgrau");
            map.put((byte) 30, "anthrazit");
            map.put((byte) 31, "schwarz");
        } else {
            map.put((byte) 0, "white");
            map.put((byte) 1, "red-brown");
            map.put((byte) 2, "red");
            map.put((byte) 3, "red");
            map.put((byte) 4, "red");
            map.put((byte) 5, "orange");
            map.put((byte) 6, "yellow");
            map.put((byte) 7, "citron");
            map.put((byte) 8, "light yellow");
            map.put((byte) 9, "dark brown");
            map.put((byte) 10, "light brown");
            map.put((byte) 11, "dark blue");
            map.put((byte) 12, "medium blue");
            map.put((byte) 13, "light blue");
            map.put((byte) 14, "blue");
            map.put((byte) 15, "blue");
            map.put((byte) 16, "blue");
            map.put((byte) 17, "steel");
            map.put((byte) 18, "oliv");
            map.put((byte) 19, "dark green");
            map.put((byte) 20, "green");
            map.put((byte) 21, "gras green");
            map.put((byte) 22, "light green");
            map.put((byte) 23, "pink");
            map.put((byte) 24, "pink");
            map.put((byte) 25, "violet");
            map.put((byte) 26, "violet");
            map.put((byte) 27, "light grey");
            map.put((byte) 28, "medium grey");
            map.put((byte) 29, "dark grey");
            map.put((byte) 30, "anthrazit");
            map.put((byte) 31, "black");


        }
        return map;
    }

}
