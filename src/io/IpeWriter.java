package io;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;

import model.Point;
import model.Trajectory;

public class IpeWriter {
    public void writeTrajectory(Trajectory trajectory, String filepath) throws JDOMException, IOException {
        // load empty ipe template file
        SAXBuilder builder = new SAXBuilder();
        builder.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
        Document doc = builder.build(IpeWriter.class.getResourceAsStream("empty.ipe"));
        
        // add trajectory to ipe xml structure
        translateAndScale(trajectory);
        
        Element page = doc.getRootElement().getChild("page");
        Element xmlPath = new Element("path");
        xmlPath.setAttribute("stroke", "black");
        xmlPath.setText("\n");
        boolean firstPoint = true;
        for (Point p : trajectory.getPoints()) {
            xmlPath.addContent(p.getX() + " " + p.getY());
            if (firstPoint) {
                xmlPath.addContent(" m\n");
                firstPoint = false;
            } else {
                xmlPath.addContent(" l\n");
            }
        }
        page.addContent(xmlPath);
        
        // write out ipe file
        XMLOutputter out = new XMLOutputter();
        FileWriter fw = new FileWriter(new File(filepath));
        out.output(doc, fw);
    }
    
    protected void translateAndScale(Trajectory trajectory) {
        // target size and position
        double target = 500.0;
        double xZero = 50.0;
        double yZero = 50.0;
        
        // determine min and max x and y coordinates of all points
        double xMin = Double.POSITIVE_INFINITY;
        double yMin = Double.POSITIVE_INFINITY;
        double xMax = Double.NEGATIVE_INFINITY;
        double yMax = Double.NEGATIVE_INFINITY;
        for (Point p : trajectory.getPoints()) {
            if (p.getX() > xMax) xMax = p.getX();
            if (p.getY() > yMax) yMax = p.getY();
            if (p.getX() < xMin) xMin = p.getX();
            if (p.getY() < yMin) yMin = p.getY();
        }
        
        // calculate stretch factor and offset
        double largerDistance = Math.max(xMax - xMin, yMax - yMin);
        double stretchFactor = target / largerDistance;
        
        // stretch and shift all points
        for (Point p : trajectory.getPoints()) {
            p.setX(((p.getX() - xMin) * stretchFactor) + xZero);
            p.setY(((p.getY() - yMin) * stretchFactor) + yZero);
        }
    }
}
