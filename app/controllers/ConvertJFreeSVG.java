package controllers;

/**
 * Created by andrei on 4/29/17.
 */
import org.apache.batik.dom.GenericDOMImplementation;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.jfree.graphics2d.svg.*;
 import javax.swing.JLabel;
import java.awt.*;
 import java.io.*;
 import java.util.stream.Collectors;
import org.scilab.forge.jlatexmath.*;


public class ConvertJFreeSVG {
  public static FormulaSVG toSVG(String latex, int size, String bg, String fg, boolean fontAsShapes) throws IOException {
        DOMImplementation domImpl = GenericDOMImplementation.getDOMImplementation();
        String[] bgcols = bg.split(":");
        String[] fgcols = fg.split(":");
        java.awt.Color bgc = new Color(
                Integer.parseInt(bgcols[0]),
                Integer.parseInt(bgcols[1]),
                Integer.parseInt(bgcols[2]));
        java.awt.Color fgc = new Color(
                Integer.parseInt(fgcols[0]),
                Integer.parseInt(fgcols[1]),
                Integer.parseInt(fgcols[2]));
        PipedInputStream inputStream = new PipedInputStream();
        OutputStream os = new PipedOutputStream(inputStream);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            TeXFormula formula = new TeXFormula(latex);
            TeXIcon icon = formula.createTeXIcon(TeXConstants.STYLE_DISPLAY, size);
            int width = icon.getIconWidth();
            int height = icon.getIconHeight();
            int depth = icon.getIconDepth();
            SVGGraphics2D g2 = new SVGGraphics2D(width, height);
            g2.setRenderingHint(SVGHints.KEY_TEXT_RENDERING, SVGHints.VALUE_TEXT_RENDERING_PRECISION);
            g2.setColor(bgc);
            g2.fillRect(0,0, width , height);
            JLabel jl = new JLabel();
            jl.setForeground(fgc);
            icon.paintIcon(jl, g2, 0, 0);
            //g2.drawRect(1,1,width - 2,height - 2);
            return new FormulaSVG(g2.getSVGDocument(), depth, height, false, false, "");
        } catch (ParseException e) {
            return new FormulaSVG("", 0, 0, true, false, e.getMessage());
        }
    }
}


