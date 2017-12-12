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

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

public class ColorCanvas extends Canvas {

    private static final int SIZE = 16;

    private static final long serialVersionUID = 1L;
    private Color color;
    private Byte colorIdx;

    /**
     * @param color
     * @param color2
     * @param colorNamesMap
     */
    public ColorCanvas(Byte colorIdx, Color color) {
        this.colorIdx = colorIdx;
        this.color = color;
    }

    /* (non-Javadoc)
     * @see java.awt.Canvas#paint(java.awt.Graphics)
     */
    @Override
    public void paint(Graphics g) {
        g.setColor(color);
        g.fillRect(0,0,SIZE,SIZE);
        g.setColor(Color.BLACK);
        g.drawRect(0,0,SIZE,SIZE);
    }

    /* (non-Javadoc)
     * @see java.awt.Component#getPreferredSize()
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(SIZE+1, SIZE+1);
    }

    /**
     * @return the color
     */
    public Color getColor() {
        return color;
    }

    /**
     * @return the colorIdx
     */
    public Byte getColorIdx() {
        return colorIdx;
    }


}
