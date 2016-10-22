import ij.*;
import ij.process.*;
import ij.gui.*;
import java.awt.*;
import ij.plugin.*;
import ij.plugin.frame.*;

public class Image_Inverter implements PlugIn {

	public void run(String arg) {
		System.out.println("Macro Java");
		ImagePlus imp = IJ.getImage();
		IJ.run(imp, "Invert", "");
		IJ.wait(1000);
		IJ.run(imp, "Invert", "");
	}

}
