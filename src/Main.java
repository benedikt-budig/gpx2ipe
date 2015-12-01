import java.io.IOException;

import model.Trajectory;

import org.jdom2.JDOMException;

import io.GPXReader;
import io.IpeWriter;


public class Main {
    public static void main(String[] args) throws JDOMException, IOException {
        // parse arguments
        String gpxPath = "";
        String ipePath = "";
        boolean reverse = false;
        
        if (args.length == 2) {
            gpxPath = args[0];
            ipePath = args[1];
        } else if (args.length == 3 && args[0].equals("-r")) {
            gpxPath = args[1];
            ipePath = args[2];
            reverse = true;
        } else {
            System.out.println("Usage: gpx2ipe [-r] <input.gpx> <output.ipe>");
            System.exit(0);
        }
        
        // read trajectory from gpx file
        GPXReader reader = new GPXReader();
        Trajectory trajectory = reader.readGPXFile(gpxPath);
        
        // transform trajectory if desired
        // TODO: add flag
        trajectory.sparsify(0.01);
        if (reverse)
            trajectory.reverse();
        
        // write trajectory to ipe file
        IpeWriter writer = new IpeWriter();
        writer.writeTrajectory(trajectory, ipePath);
        
        // print summary
        System.out.print("Successfully processed " + trajectory.size() + " points");
        if (reverse)
            System.out.println(", order reversed.");
        else
            System.out.println(", order retained.");
    }
}
