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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Polygon;

import javax.swing.JPanel;

import ch.jbead.BeadList;
import ch.jbead.BeadPainter;
import ch.jbead.BeadRun;
import ch.jbead.Model;
import ch.jbead.Point;
import ch.jbead.SimpleCoordinateCalculator;
import ch.jbead.View;
import ch.jbead.ui.SymbolFont;

public class BeadRunViewPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    protected Model model;
    protected View view;

    private BeadList beadList;

    public int currentBeadRunIdx = -1;

    private Font defaultfont;

    /**
     * @param beadList
     *
     */
    public BeadRunViewPanel(BeadList beadList, Model model, View view) {
        this.model = model;
        this.view = view;
        this.beadList = beadList;
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        FontMetrics metrics = g.getFontMetrics();
        int height = metrics.getLeading() + metrics.getAscent();
        int x1 = 40;
        int y = 10;
        int dx = dx(g) * 2;
        int dy = dy(g) * 2;
        // int colwidth = colwidth(g);

        SimpleCoordinateCalculator coord = new SimpleCoordinateCalculator(dx, dy);
        BeadPainter painter = new BeadPainter(coord, model, view, SymbolFont.get(dx));
        defaultfont = SymbolFont.get(dx);

        for (int i = 0; i <= 4; i++) {

            int idx = currentBeadRunIdx - 2 + i;
            if (idx < 0 || idx >= beadList.size()) {
                drawBeadCount(g, x1, y, dx, dy, height, null, 0, painter, coord);
            } else {
                BeadRun bead = beadList.get(idx);
                drawBeadCount(g, x1, y, dx, dy, height, bead.getColor(), bead.getCount(), painter, coord);
            }
            if (i == 2) {
                drawMarker(g, 5, y, dx, dy, height);
            }
            y += dy + 3;

            /*
             * JLabel lbl = (JLabel) beadRunView.getComponent(y); int idx =
             * currentBeadRunIdx - 2 + y; if (idx < 0 || idx >= beadList.size()) {
             * lbl.setText("-----------"); } else { BeadRun br = beadList.get(idx); String
             * prefix = "    "; if (y == 2) prefix = " => "; lbl.setText(idx + "." + prefix
             * + colorNameMap.get(br.getColor()) + " " + br.getCount() + "x"); }
             */

        }

    }

    /*
     * (non-Javadoc)
     *
     * @see javax.swing.JComponent#getPreferredSize()
     */
    @Override
    public Dimension getPreferredSize() {
        Dimension d = new Dimension(200, 200);
        return d;
    }

    /**
     *
     * @param g
     * @param x
     * @param y
     * @param dx
     * @param dy
     * @param height
     * @param color
     * @param count
     * @param painter
     * @param coord
     */
    private void drawBeadCount(Graphics g, int x, int y, int dx, int dy, int height, Byte color, int count, BeadPainter painter,
            SimpleCoordinateCalculator coord) {
        coord.setOffsetX(x);
        coord.setOffsetY(y + dy);
        painter.paint(g, new Point(0, 0), color);
        g.setColor(Color.BLACK);
        g.setFont(defaultfont);
        g.drawString(Integer.toString(count), x + dx + 3, y + 13 + height);
    }

    /**
     * @param g
     * @param i
     * @param y
     */
    private void drawMarker(Graphics g, int x, int y, int dx, int dy, int height) {
        g.setColor(Color.BLACK);

        int[] xpoly_l = { x, x + dx, x };
        int[] ypoly_l = { y, y + (dy / 2), y + dy };

        x = x + 80;
        int[] xpoly_r = { x + dx + 50, x + dx + 50, x + 50 };
        int[] ypoly_r = { y, y + dy, y + (dy / 2) };
        Polygon p_l = new Polygon(xpoly_l, ypoly_l, 3);
        Polygon p_r = new Polygon(xpoly_r, ypoly_r, 3);
        g.fillPolygon(p_l);
        g.fillPolygon(p_r);
    }

    private int dx(Graphics g) {
        return g.getFontMetrics().getHeight();
    }

    private int dy(Graphics g) {
        return dx(g);
    }

    private int colwidth(Graphics g) {
        return dx(g) + 2 + g.getFontMetrics().stringWidth("9999") + 3;
    }

}
