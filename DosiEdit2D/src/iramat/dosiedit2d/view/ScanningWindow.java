package iramat.dosiedit2d.view;

import iramat.dosiedit2d.view.ImagePanel.ImageBackground;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * Class listing the image text file present in the directory chosen by the user, and give him a way
 * to choose whch one to load into the grid in the model.
 * @author alan
 * @see ImagePanel
 * @see ImageBackground
 */
public class ScanningWindow extends JDialog implements ActionListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1801511946312998774L;

	/**
	 * Size of the scroll bar. Useful to adjust dimension of panel.
	 */
	private static final int SIZE_SCROLLBAR = 25;
	
	/**
	 * The global panel.
	 */
	private JPanel globalScroll;

	/**
	 * The class can show the image in a color way, not gray scale.
	 */
	private boolean gray;
	
	/**
	 * List of text file which describe matrices integer value.
	 */
	private List<String> nonImageFiles;
	
	/**
	 * The button of the current image selected. Clicking on it launch the association window with that image.
	 */
	private JButton currentButton;
	
	/**
	 * The image panel selected.
	 */
	private ImagePanel currentImage;
	
	/**
	 * The set of shades of grey. No doublons.
	 */
	private SortedSet<Integer> shades;

	/**
	 * The boolean to know if the image panel is valid.
	 */
	private boolean valid = false;
	
	private static String path;
	
	/**
	 * Constructor.
	 * @param parent where to show the frame on screen.
	 * @param gray true if drawing the image in grey scale, false if not.
	 */
	public ScanningWindow(Component parent,boolean gray)
	{
		super((Frame) parent, "Choose a grey scale image", true);
		
		this.gray = gray;
		nonImageFiles = new ArrayList<>();
		this.setLocationRelativeTo(parent);

		this.setDefaultCloseOperation(0);
		globalScroll = new JPanel(new GridLayout(0,1));
		
		this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(final WindowEvent e) {
                ScanningWindow.this.valid = false;
                ScanningWindow.this.dispose();
            }
        });
		
		shades = new TreeSet<>();
	}

	/**
	 * Initializes GUI with image text file translate into {@link BufferedImage}.
	 * @param files the directory in where the image are.
	 */
	public void showAvailableScan(File[] files)
	{
		JPanel nonImagePanel = new JPanel();
		nonImagePanel.setLayout(new BoxLayout(nonImagePanel,BoxLayout.PAGE_AXIS));
		nonImagePanel.add(new JLabel("<html><b>List of ignored text files<br> because of non number value:</b></html>"));
		int width=0;
		for(File file : files)
		{
			if(file.getName().endsWith(".txt"))
			{
				BufferedImage image;
				try
				{
					image = ScanningWindow.createGrayImageFromFile(file,this.gray);
					ImagePanel pan = new ImagePanel(this,image,file.getName());
					
					globalScroll.add(pan);
					
					if(image.getWidth() > width)
						width = image.getWidth();
				}
				catch (IOException e)
				{
					nonImageFiles.add(file.getName());
					nonImagePanel.add(new JLabel(file.getName()));
				}
				catch(NumberFormatException e)
				{
					nonImageFiles.add(file.getName());
					nonImagePanel.add(new JLabel(file.getName()));
				}
			}
		}

		this.setLayout(new BorderLayout());
		
		JScrollPane westScroll = new JScrollPane(globalScroll);
		this.add(westScroll,BorderLayout.CENTER);
		this.add(nonImagePanel,BorderLayout.EAST);
		this.setPreferredSize(new Dimension(width+SIZE_SCROLLBAR+nonImagePanel.getPreferredSize().width,600));
		
		currentButton = new JButton("No image selected");
		currentButton.addActionListener(this);
		
		this.add(this.currentButton,BorderLayout.NORTH);
		
		this.validate();
		this.pack();
	}

	/**
	 * Create a gray scale {@link BufferedImage} from the file describing the matrix. If an error occured,
	 * the file is add to the list of non image file.
	 * @param file the file to translate.
	 * @param gray true to create gray scale image, false to create a colored one..
	 * @return the newly created {@link BufferedImage}
	 * @throws IOException In out exception during the reading of the file.
	 * @throws NumberFormatException if a value in the file is not a integer.
	 */
	private static BufferedImage createGrayImageFromFile(File file,boolean gray) throws IOException, NumberFormatException
	{
		BufferedReader reader = new BufferedReader(new FileReader(file));
		List<List<Integer>> lines = new ArrayList<List<Integer>>();
		String line = reader.readLine();

		while(line != null)
		{
			List<Integer> list = new ArrayList<Integer>();
			String[] array = line.trim().replaceAll("\\s+", " ").split(" ");
			for(String s : array)
				list.add(Integer.parseInt(s));

			lines.add(list);
			line = reader.readLine();
		}

		int width = lines.get(0).size();
		int height = lines.size();
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
		WritableRaster raster = image.getRaster();
		for(int y=0;y<lines.size();y++)
			for(int x=0;x<lines.get(y).size();x++)
			{
				int[] iArray = new int[1];
				int rgb = lines.get(y).get(x);
				iArray[0] = rgb;
				raster.setPixel(x, y, iArray);
			}
		reader.close();
		path = file.getAbsolutePath();
		return image;
	}
	
	/**
	 * Set the current image, updating the GUI with selected border.
	 * @param pan
	 */
	public void setCurrentImage(ImagePanel pan)
	{
		if(currentImage != null)
			this.currentImage.setSelected(false);
		this.currentImage = pan;
		this.currentImage.setSelected(true);
		currentButton.setText("<html><b>Validate the selected image:</b> "+currentImage.getNameFile());
	}
	
	/**
	 * Get the selected image panel.
	 * @return the selected image panel.
	 */
	public ImagePanel getSelectedPanel()
	{
		return currentImage;
	}
	
	/**
	 * Get the list of shades of gray.
	 * @return the list of shades of gray.
	 */
	public SortedSet<Integer> getShadesOfGray()
	{
		return shades;
	}
	
	/**
	 * Check if the scan is valid.
	 * @return true if the scan is valid, false if not.
	 */
	public boolean isScanValid()
	{
		return valid;
	}

	public String getPath()
	{
		return path;
	}
	
	@Override
	public void actionPerformed(ActionEvent e)
	{
		if(this.currentImage == null)
			return;
		BufferedImage img = this.currentImage.getImage();
		for(int y=0;y<img.getHeight();y++)
			for(int x=0;x<img.getWidth();x++)
			{
				WritableRaster raster = img.getRaster();
				int[] iArray = new int[1];
				raster.getPixel(x, y,iArray);
				final int shade = iArray[0];
                if (shade < 0 || shade > 255) 
                {
                	JOptionPane.showMessageDialog(null, "A value is not between 0 and 255.", "Error in file",
                			JOptionPane.ERROR_MESSAGE);
                	return;
                }
                this.shades.add(shade);
			}
		valid=true;
		
		this.dispose();
	}
}
