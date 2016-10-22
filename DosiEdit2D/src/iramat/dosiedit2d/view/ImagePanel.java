package iramat.dosiedit2d.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

/**
 * Class extending {@link JPanel} to contain BufferedImage into chil panel with the file name in title.
 * @author alan
 *
 */
public class ImagePanel extends JPanel implements MouseListener
{
	/**
	 * The {@link JPanel} displaying the {@link BufferedImage} in its background.
	 * @author alan
	 *
	 */
	public class ImageBackground extends JPanel
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 3672965739830998955L;

		/**
		 * The {@link BufferedImage} to draw.
		 */
		private BufferedImage image;
		
		/**
		 * That filter is set when that object is given to association window. When the select a
		 * grey level, the filter highlight the specified pixel with that grey level.
		 */
		private BufferedImage filter;
		
		/**
		 * Constructor.
		 * @param img the image to render.
		 */
		public ImageBackground(BufferedImage img)
		{
			super();
			this.image = img;
		}
		
		/**
		 * Set the grey value to highlight.
		 * @param value the grey value to select.
		 */
		public void setHightlight(int value)
		{
			filter = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
			
			for(int y=0;y<image.getHeight();y++)
				for(int x=0;x<image.getWidth();x++)
				{
					WritableRaster rasterFrom = image.getRaster();
					
					int[] iArray = new int[1];
					rasterFrom.getPixel(x, y,iArray);
					if(iArray[0] == value)
					{
						
						filter.setRGB(x, y, (new Color(1f,0,0,0.5f).getRGB()));
					}
				}
			this.repaint();
		}
		
		/**
		 * Get the rendered image.
		 * @return the rendered image.
		 */
		public BufferedImage getImage()
		{
			return this.image;
		}

		@Override
		public void paintComponent(Graphics g)
		{
			super.paintComponents(g);
			g.drawImage(image, 0, 0, this.getWidth(), this.getHeight(),null);
			if(filter != null)
				g.drawImage(filter, 0, 0, this.getWidth(), this.getHeight(),null);
		}
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8146970780795149301L;

	/**
	 * The window handling all {@link ImagePanel}.
	 */
	private ScanningWindow parent;
	
	/**
	 * The name of the file containing the grey scale image text file.
	 */
	private String nameFile;
	
	/**
	 * The image background panel.
	 */
	private ImageBackground image;
	
	/**
	 * The border to set when the user select an image.
	 */
	private static Border highlightBorder = BorderFactory.createCompoundBorder(BorderFactory.createRaisedBevelBorder()
			, BorderFactory.createLineBorder(Color.RED, 3));
	
	/**
	 * The default border, when the image is not selected.
	 */
	private static Border defaultBorder = BorderFactory.createRaisedBevelBorder();
	
	/**
	 * Constructor.
	 * @param parent the scanning window handling all {@link ImagePanel}
	 * @param img the image to render.
	 * @param name the name of the text file.
	 */
	public ImagePanel(ScanningWindow parent,BufferedImage img,String name)
	{
		this.parent = parent;
		this.nameFile = name;
		this.image = new ImageBackground(img);
		this.image.setBorder(defaultBorder);
		this.image.setPreferredSize(new Dimension(img.getWidth(),img.getHeight()));
		this.setLayout(new BorderLayout());
		this.add(new JLabel(name),BorderLayout.NORTH);
		this.add(this.image);
		
		this.addMouseListener(this);
	}
	
	/**
	 * Get the name of the file.
	 * @return the name of the file.
	 */
	public String getNameFile()
	{
		return nameFile;
	}

	/**
	 * Get the rendered {@link BufferedImage}.
	 * @return the rendred {@link BufferedImage}.
	 */
	public BufferedImage getImage()
	{
		return image.getImage();
	}
	
	/**
	 * Get the panel with image in background.
	 * @return the panel with image in background.
	 */
	public ImageBackground getImagePanelBackground()
	{
		return this.image;
	}
	
	/**
	 * Set the image panel to selected of not, changing the border.
	 * @param b true if set to selected, false if not.
	 */
	public void setSelected(boolean b)
	{
		if(b)
			this.image.setBorder(highlightBorder);
		else
			this.image.setBorder(defaultBorder);
	}
	
	@Override
	public void mouseClicked(MouseEvent e)
	{
		parent.setCurrentImage(this);
	}

	@Override
	public void mouseEntered(MouseEvent e)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		parent.setCurrentImage(this);
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		// TODO Auto-generated method stub
		
	}
}
