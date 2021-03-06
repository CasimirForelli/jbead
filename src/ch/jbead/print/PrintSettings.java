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

package ch.jbead.print;

import java.awt.print.Paper;
import java.awt.print.PrinterJob;
import java.util.HashMap;
import java.util.Map;

import javax.print.PrintService;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.OrientationRequested;

import ch.jbead.Settings;

public class PrintSettings {

    private static Map<String, MediaSizeName> mediamap = new HashMap<String, MediaSizeName>();

    static {
        mediamap.put("a0", MediaSizeName.ISO_A0);
        mediamap.put("a1", MediaSizeName.ISO_A1);
        mediamap.put("a2", MediaSizeName.ISO_A2);
        mediamap.put("a3", MediaSizeName.ISO_A3);
        mediamap.put("a4", MediaSizeName.ISO_A4);
        mediamap.put("a5", MediaSizeName.ISO_A5);
        mediamap.put("a6", MediaSizeName.ISO_A6);
        mediamap.put("a7", MediaSizeName.ISO_A7);
        mediamap.put("a8", MediaSizeName.ISO_A8);
        mediamap.put("a9", MediaSizeName.ISO_A9);
        mediamap.put("a10", MediaSizeName.ISO_A10);
        mediamap.put("letter", MediaSizeName.NA_LETTER);
        mediamap.put("legal", MediaSizeName.NA_LEGAL);
        mediamap.put("executive", MediaSizeName.EXECUTIVE);
        mediamap.put("ledger", MediaSizeName.LEDGER);
        mediamap.put("tabloid", MediaSizeName.TABLOID);
        mediamap.put("invoice", MediaSizeName.INVOICE);
        mediamap.put("folio", MediaSizeName.FOLIO);
        mediamap.put("quarto", MediaSizeName.QUARTO);
    }

    private PrintService service;
    private PrintRequestAttributeSet attributes;

    public PrintSettings(Settings settings) {
        service = null;
        attributes = new HashPrintRequestAttributeSet();
        initDefaultFormat(settings);
    }

    public PrintService getService() {
        return service;
    }

    public PrintRequestAttributeSet getAttributes() {
        return attributes;
    }

    public void setService(PrintService service) {
        this.service = service;
    }

    public void setAttributes(PrintRequestAttributeSet attributes) {
        this.attributes = attributes;
    }

    private void initDefaultFormat(Settings settings) {
        addOrientation(settings);
        addCopies();
        addMedia(settings);
    }

    private void addOrientation(Settings settings) {
        settings.setCategory("print");
        String paperorient = settings.loadString("orientation");
        if (paperorient.equalsIgnoreCase("portrait")) {
            attributes.add(OrientationRequested.PORTRAIT);
        } else if (paperorient.equalsIgnoreCase("reverse_portrait")) {
            attributes.add(OrientationRequested.REVERSE_PORTRAIT);
        } else if (paperorient.equalsIgnoreCase("reverse_landscape")) {
            attributes.add(OrientationRequested.REVERSE_LANDSCAPE);
        } else {
            attributes.add(OrientationRequested.LANDSCAPE);
        }
    }

    private void addCopies() {
        attributes.add(new Copies(1));
    }

    private void addMedia(Settings settings) {
        settings.setCategory("print");
        String papername = settings.loadString("paper");
        if (papername.length() > 0) {
            attributes.add(getMedia(papername));
        } else {
            Paper paper = PrinterJob.getPrinterJob().defaultPage().getPaper();
            if (isLetter(paper)) {
                attributes.add(MediaSizeName.NA_LETTER);
            } else if (isLegal(paper)) {
                attributes.add(MediaSizeName.NA_LEGAL);
            } else {
                attributes.add(getMedia("a4"));
            }
        }
    }

    private MediaSizeName getMedia(String paper) {
        paper = paper.toLowerCase();
        if (!mediamap.containsKey(paper)) return MediaSizeName.ISO_A4;
        return mediamap.get(paper);
    }

    private boolean isLetter(Paper paper) {
        return paper.getWidth() == 612.0 && paper.getHeight() == 792.0;
    }

    private boolean isLegal(Paper paper) {
        return paper.getWidth() == 612.0 && paper.getHeight() == 1008.0;
    }

}
