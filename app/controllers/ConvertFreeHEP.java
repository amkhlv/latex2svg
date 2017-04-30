package controllers;

/**
 * Created by andrei on 4/29/17.
 */
 import org.apache.batik.dom.GenericDOMImplementation;
 import org.freehep.graphicsbase.util.UserProperties;
 import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.freehep.graphicsio.AbstractVectorGraphicsIO;
import org.freehep.graphicsio.svg.SVGGraphics2D;

import org.scilab.forge.jlatexmath.*;

import org.scilab.forge.jlatexmath.cyrillic.CyrillicRegistration;
import org.scilab.forge.jlatexmath.greek.GreekRegistration;


 import javax.swing.JLabel;
import java.awt.*;
 import java.io.*;
 import java.util.stream.Collectors;

public class ConvertFreeHEP {
    static String convertStreamToString(java.io.InputStream is) {
    java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
    return s.hasNext() ? s.next() : "";
}
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

            SVGGraphics2D g2 = new SVGGraphics2D(baos, new Dimension(width, height));
            
            UserProperties props = (UserProperties) SVGGraphics2D.getDefaultProperties();
            props.setProperty(SVGGraphics2D.EMBED_FONTS, true);
            props.setProperty(AbstractVectorGraphicsIO.TEXT_AS_SHAPES, false);
            SVGGraphics2D.setDefaultProperties(props);

            //g2.setColor(bgc);
            //g2.fillRect(0,0,icon.getIconWidth(),icon.getIconHeight());
            JLabel jl = new JLabel();
            jl.setForeground(fgc);
            g2.writeHeader();
            icon.paintIcon(jl, g2, 0, 0);
            //g2.drawRect(1,1,width - 2,height - 2);
            g2.endExport();
            String svgstring  = new String( baos.toByteArray(), java.nio.charset.StandardCharsets.UTF_8);
            g2.closeStream();
            return new FormulaSVG(svgstring, depth, height, false, false, "");
        } catch (ParseException e) {
            return new FormulaSVG("", 0, 0, true, false, e.getMessage());
        }
    }
}
