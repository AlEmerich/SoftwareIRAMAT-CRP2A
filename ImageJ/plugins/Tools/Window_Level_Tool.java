import ij.IJ;
import ij.ImagePlus;
import ij.Menus;
import ij.gui.Toolbar;
import ij.measure.Calibration;
import ij.plugin.tool.PlugInTool;
import ij.process.ImageStatistics;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

/**
 * This tool will change the window/level by dragging the mouse.  The window is changed
 * by dragging the mouse over the image on the x axis.  The level is changed
 * by dragging the mouse on the y axis.
 */
public final class Window_Level_Tool extends PlugInTool implements ActionListener {
	static final int AUTO_THRESHOLD = 5000;
	int autoThreshold;
	private double currentMin = 0;
	private double currentMax = 0;
	private int lastX = -1;
	private int lastY = -1;
	private ImagePlus impLast = null;
	private PopupMenu popup1;
	private MenuItem autoItem, resetItem;
	private final int OFFSET = 0;
	private boolean RGB;
	
	@Override
	public void mousePressed(ImagePlus imp, MouseEvent e) {
		RGB = imp.getType() == ImagePlus.COLOR_RGB;
		if (impLast != imp) {
			if(RGB) imp.getProcessor().snapshot();
			autoThreshold = 0;
		}
		impLast = imp;
		lastX = e.getX();
		lastY = e.getY();
		currentMin = imp.getDisplayRangeMin();
		currentMax = imp.getDisplayRangeMax();
	}

	@Override
	public void mouseDragged(ImagePlus imp, MouseEvent e ) {
		double minMaxDifference = currentMax - currentMin;
		int x = e.getX();
		int y = e.getY();
		int xDiff = x - lastX;
		int yDiff = y - lastY;
		int totalWidth  = (int) (imp.getWidth() * imp.getCanvas().getMagnification() );
		int totalHeight = (int) (imp.getHeight() * imp.getCanvas().getMagnification() );
		double xRatio = ((double)xDiff)/((double)totalWidth);
		double yRatio = ((double)yDiff)/((double)totalHeight);
		
		//scale to our image range
		double xScaledValue = minMaxDifference*xRatio;
		double yScaledValue = minMaxDifference*yRatio;

		//invert x
		xScaledValue = xScaledValue * -1;

		adjustWindowLevel(imp, xScaledValue, yScaledValue );
	}

	void adjustWindowLevel(ImagePlus imp, double xDifference, double yDifference ) {

		//current settings
		double currentWindow = currentMax - currentMin;
		double currentLevel = currentMin + (.5*currentWindow);

		//change
		double newWindow = currentWindow + xDifference;
		double newLevel = currentLevel + yDifference;

		if( newWindow < 0 )
			newWindow = 0;
		if( newLevel < 0 )
			newLevel = 0;

		Calibration cal = imp.getCalibration();
		IJ.showStatus( "Window: " + IJ.d2s(newWindow) + ", Level: " + IJ.d2s(cal.getCValue(newLevel)) );

		//convert to min/max
		double newMin = newLevel - (.5*newWindow);
		double newMax = newLevel + (.5*newWindow);

		imp.setDisplayRange(newMin, newMax);
		if(RGB) imp.draw();
		else imp.updateAndDraw();
	}


	@Override
	public void showPopupMenu(MouseEvent e, Toolbar par) {
		addPopupMenu(par);
		popup1.show(e.getComponent(), e.getX()+OFFSET, e.getY()+OFFSET);
	}

	void addPopupMenu(Toolbar par) {
		if ( popup1 != null) return; // do only once
		popup1 = new PopupMenu();
		if (Menus.getFontSize()!=0)
			popup1.setFont(Menus.getFont());
		autoItem = new MenuItem("Auto");
		autoItem.addActionListener(this);
		popup1.add(autoItem);
		resetItem = new MenuItem("Reset");
		resetItem.addActionListener(this);
		popup1.add(resetItem);
		par.add(popup1);
	}
	
	public void actionPerformed(ActionEvent e) {
		MenuItem item = (MenuItem)e.getSource();
		String cmd = e.getActionCommand();
		if ("Auto".equals(cmd))
			autoItemActionPerformed(e);
		else if ("Reset".equals(cmd))
			resetItemActionPerformed(e);
	}

	private void autoItemActionPerformed(ActionEvent evt) {
		if( impLast == null || !impLast.isVisible()) return;
		int depth = impLast.getBitDepth();
		if( depth != 16 && depth != 32) {
			resetItemActionPerformed(evt);
			return;
		}
		int hmin, hmax;
		Calibration cal = impLast.getCalibration();
		impLast.setCalibration(null);
		ImageStatistics stat1 = impLast.getStatistics();		// uncalibrated
		impLast.setCalibration(cal);
		int limit = stat1.pixelCount/10;
		int[] histogram = stat1.histogram;
		if (autoThreshold<10) autoThreshold = AUTO_THRESHOLD;
		else autoThreshold /= 2;
		int threshold = stat1.pixelCount/autoThreshold;
		int i = -1;
		boolean found;
		int count;
		do {
			i++;
			count = histogram[i];
			if (count>limit) count = 0;
			found = count> threshold;
		} while (!found && i<255);
		hmin = i;
		i = 256;
		do {
			i--;
			count = histogram[i];
			if (count>limit) count = 0;
			found = count > threshold;
		} while (!found && i>0);
		hmax = i;
		if ( hmax >= hmin) {
			currentMin = stat1.histMin + hmin*stat1.binSize;
			currentMax = stat1.histMin + hmax*stat1.binSize;
			if( currentMin == currentMax) {
				currentMin = stat1.min;
				currentMax = stat1.max;
			}
			adjustWindowLevel(impLast, 0 ,0);
		}
	}
	
	private void resetItemActionPerformed(ActionEvent evt) {
		if ( impLast == null || !impLast.isVisible()) return;
		impLast.resetDisplayRange();
		currentMin = impLast.getDisplayRangeMin();
		currentMax = impLast.getDisplayRangeMax();
		autoThreshold = 0;
		if (RGB) {
			impLast.getProcessor().reset();
			currentMin = 0;
			currentMax = 255;
			impLast.setDisplayRange(currentMin, currentMax);
			impLast.draw();
		} else
			adjustWindowLevel(impLast, 0 ,0);
	}

	@Override
	public String getToolIcon() {
		return "T0b12W Tbb12L";
	}

	@Override
	public String getToolName() {
		return "Window Level Tool (right click for Reset, Auto)";
	}
	
}
