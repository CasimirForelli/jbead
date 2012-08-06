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

package ch.jbead.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import ch.jbead.BeadSymbols;
import ch.jbead.JBeadFrame;
import ch.jbead.Model;
import ch.jbead.Point;
import ch.jbead.Selection;
import ch.jbead.SelectionListener;
import ch.jbead.ViewListener;

public class DraftPanel extends BasePanel implements SelectionListener, ViewListener {

    private static final long serialVersionUID = 1L;

    private static final int GAP = 6;
    private static final int MARKER_WIDTH = 30;

    private int offsetx;
    private int maxj;
    private Font defaultfont;
    private Font symbolfont;
    private boolean drawColors = true;
    private boolean drawSymbols = false;

    public DraftPanel(Model model, Selection selection, final JBeadFrame frame) {
        super(model, selection);
        model.addListener(this);
        frame.addListener(this);
        setBackground(Color.LIGHT_GRAY);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                frame.draftMouseDown(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                frame.draftMouseUp(e);
            }
        });
        addMouseMotionListener(new MouseMotionListener() {
            public void mouseDragged(MouseEvent e) {
                frame.draftMouseMove(e);
            }
            public void mouseMoved(MouseEvent e) {
                // empty
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        defaultfont = g.getFont();
        symbolfont = new Font("SansSerif", Font.PLAIN, gridy - 1);
        offsetx = getOffsetX();
        maxj = getMaxJ();
        paintBeads(g);
        paintMarkers(g);
        if (selection.isNormal()) {
            paintSelection(g, Color.RED, selection);
        }
    }

    @Override
    public Dimension getMinimumSize() {
        return new Dimension(model.getWidth() * gridx + MARKER_WIDTH + GAP, 3 * gridy);
    }

    @Override
    public Dimension getPreferredSize() {
        return getMinimumSize();
    }

    @Override
    public Dimension getMaximumSize() {
        return getMinimumSize();
    }

    private int getOffsetX() {
        return Math.max(3 + MARKER_WIDTH + GAP, (getWidth() - model.getWidth() * gridx - 1) / 2);
    }

    private int getMaxJ() {
        return Math.min(model.getHeight() - scroll, getHeight() / gridy + 1);
    }

    private int x(int i) {
        return offsetx + i * gridx;
    }

    private int y(int j) {
        return getHeight() - 1 - (j + 1) * gridy;
    }

    private void paintBeads(Graphics g) {
        g.setFont(symbolfont);
        for (int j = 0; j < maxj; j++) {
            for (int i = 0; i < model.getWidth(); i++) {
                byte c = model.get(new Point(i, j).scrolled(scroll));
                paintBead(g, i, j, c);
            }
        }
    }

    private void paintBead(Graphics g, int i, int j, byte c) {
        Color color = model.getColor(c);
        if (drawColors) {
            g.setColor(model.getColor(c));
            g.fillRect(x(i) + 1, y(j) + 1, gridx - 1, gridy - 1);
            g.setColor(Color.DARK_GRAY);
            g.drawRect(x(i), y(j), gridx, gridy);
        }
        if (drawSymbols) {
            setSymbolColor(g, color);
            g.drawString(BeadSymbols.get(c), x(i) + (gridx - g.getFontMetrics().stringWidth(BeadSymbols.get(c))) / 2, y(j) + symbolfont.getSize());
        }
    }

    private void setSymbolColor(Graphics g, Color color) {
        if (drawColors) {
            g.setColor(getContrastingColor(color));
        } else {
            g.setColor(Color.BLACK);
        }
    }

    private Color getContrastingColor(Color color) {
        if (getContrast(color, Color.WHITE) > getContrast(color, Color.BLACK)) {
            return Color.WHITE;
        } else {
            return Color.BLACK;
        }
    }

    private int getContrast(Color a, Color b) {
        int red_diff = a.getRed() - b.getRed();
        int green_diff = a.getGreen() - b.getGreen();
        int blue_diff = a.getBlue() - b.getBlue();
        return (int) Math.sqrt(red_diff * red_diff + green_diff * green_diff + blue_diff * blue_diff);
    }

    private void paintMarkers(Graphics g) {
        g.setFont(defaultfont);
        g.setColor(Color.DARK_GRAY);
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int fontHeight = g.getFontMetrics().getAscent();
        for (int j = 0; j < maxj; j++) {
            if (((j + scroll) % 10) == 0) {
                g.drawLine(offsetx - GAP - MARKER_WIDTH, y(j) + gridy, offsetx - GAP, y(j) + gridy);
                String label = Integer.toString(j + scroll);
                int labelWidth = g.getFontMetrics().stringWidth(label);
                g.drawString(label, offsetx - GAP - MARKER_WIDTH + (MARKER_WIDTH - labelWidth) / 2, y(j) + gridy + fontHeight + 1);
            }
        }
    }

    public void drawSelection(Selection sel) {
        if (!sel.isNormal()) return;
        Graphics g = getGraphics();
        paintSelection(g, Color.RED, sel);
        g.dispose();
    }

    public void clearSelection(Selection sel) {
        if (!sel.isNormal()) return;
        Graphics g = getGraphics();
        paintSelection(g, Color.DARK_GRAY, sel);
        g.dispose();
    }

    private void paintSelection(Graphics g, Color color, Selection sel) {
        g.setColor(color);
        g.drawRect(x(sel.left()), y(sel.top()), sel.width() * gridx, sel.height() * gridy);
    }

    public void redraw(int i, int j) {
        if (!isVisible()) return;
        byte c = model.get(new Point(i, j).scrolled(model.getScroll()));
        Graphics g = getGraphics();
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setFont(symbolfont);
        paintBead(g, i, j, c);
        g.dispose();
    }

    @Override
    public void redraw(Point pt) {
        redraw(pt.getX(), pt.getY() - scroll);
    }

    public void selectPreview(boolean draw, Point pt1, Point pt2) {
        Graphics g = getGraphics();
        g.setColor(draw ? Color.RED : Color.DARK_GRAY);
        g.drawRect(x(pt1.getX()), y(pt2.getY()), (pt2.getX() - pt1.getX() + 1) * gridx, (pt2.getY() - pt1.getY() + 1) * gridy);
        g.dispose();
    }

    public void linePreview(Point pt1, Point pt2) {
        Graphics g = getGraphics();
        g.setColor(Color.WHITE);
        g.setXORMode(Color.BLACK);
        g.drawLine(x(pt1.getX()) + gridx / 2, y(pt1.getY()) + gridy / 2, x(pt2.getX()) + gridx / 2, y(pt2.getY()) + gridy / 2);
        g.dispose();
    }

    public void drawPrepress(Point pt) {
        Graphics g = getGraphics();
        int x0 = x(pt.getX());
        int y0 = y(pt.getY());
        g.setColor(Color.BLACK);
        g.drawLine(x0 + 1, y0 + gridy - 1, x0 + 1, y0 + 1);
        g.drawLine(x0 + 1, y0 + 1, x0 + gridx - 1, y0 + 1);
        g.setColor(Color.WHITE);
        g.drawLine(x0 + 1 + 1, y0 + gridy - 1, x0 + gridx - 1, y0 + gridy - 1);
        g.drawLine(x0 + gridx - 1, y0 + gridy - 1, x0 + gridx - 1, y0 + 1);
        g.dispose();
    }

    public Point mouseToField(Point pt) {
        int _i = pt.getX();
        int _j = pt.getY();
        int i, jj;
        if (_i < offsetx || _i > offsetx + model.getWidth() * gridx) return null;
        i = (_i - offsetx) / gridy;
        if (i >= model.getWidth()) return null;
        jj = (getHeight() - _j) / gridy;
        return new Point(i, jj);
    }

    public void selectionUpdated(Selection before, Selection current) {
        clearSelection(before);
        drawSelection(current);
    }

    public void selectionDeleted(Selection sel) {
        clearSelection(sel);
    }

    public void drawColorsChanged(boolean drawColors) {
        this.drawColors = drawColors;
        repaint();
    }

    public void drawSymbolsChanged(boolean drawSymbols) {
        this.drawSymbols = drawSymbols;
        repaint();
    }

}
