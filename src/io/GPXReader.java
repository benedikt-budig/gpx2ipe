package io;

import java.io.IOException;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.filter.ElementFilter;
import org.jdom2.input.SAXBuilder;
import org.jdom2.util.IteratorIterable;

import model.Point;
import model.Trajectory;

public class GPXReader {
    public Trajectory readGPXFile(String filepath) throws JDOMException, IOException {
        // initialize XML reader
        SAXBuilder builder = new SAXBuilder();
        //builder.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
        Document doc = builder.build(filepath);
        
        // get xml root element
        Element root = doc.getRootElement();
        
        // read track points
        Trajectory trajectory = new Trajectory();
        IteratorIterable<Element> trackpoints = root.getDescendants(new ElementFilter("trkpt"));
        //IteratorIterable<Element> trackpoints = root.getDescendants(new ElementFilter("wpt"));
        for (Element trackpoint : trackpoints) {
            Point p = new Point(trackpoint.getAttribute("lon").getDoubleValue(), 
                                trackpoint.getAttribute("lat").getDoubleValue());
            trajectory.addPoint(p);
        }
        
        return trajectory;
    }
}
