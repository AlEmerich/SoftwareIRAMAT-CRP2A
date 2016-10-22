import ij.*;
import ij.process.*;
import ij.gui.*;
import java.awt.*;
import ij.plugin.*;

public class My_Plugin implements PlugIn {

	public void run(String arg) {


		IJ.run("Add Noise");
		IJ.run("Smooth");
	}

}
