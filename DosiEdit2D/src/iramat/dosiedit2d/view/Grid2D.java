package iramat.dosiedit2d.view;

import interfaceObject.Grid2D.RadMode;
import interfaceObject.ZoomAndPanListener;
import iramat.dosiedit2d.controler.GlobalControler;
import iramat.dosiedit2d.model.Dosi2DModel;
import iramat.dosiedit2d.model.VoxelObject;
import iramat.dosiseed.model.ColoredMaterial;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * Class handling the view for displaying the 2 dimensional grid in the model. It contains camera handlers
 * and edition tools like pen selection, cursor rectangle selection and fast way to fill all voxel in one click.
 * @author alan
 *
 */
public class Grid2D extends JPanel implements MouseListener, MouseMotionListener,
						ActionListener,Observer
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6543888376489219835L;

	/**
	 * Default number of voxel in the X-axis.
	 */
	private static final int DEFAULT_NB_VOXEL_Y = 20;

	/**
	 * Default number of voxel in the Y-axis.
	 */
	private static final int DEFAULT_NB_VOXEL_X = 20;

	/**
	 * The global controller of the project.
	 */
	private GlobalControler controler;
	
	/**
	 * Rectangle for rectangle selection.
	 */
	private Rectangle2D.Float cursorRectSelection;

	/**
	 * The 2D camera.
	 */
	private ZoomAndPanListener zoomManager;

	/**
	 * The copy of the grid to render.
	 */
	private HashMap<Integer,HashMap< Integer,VoxelObject>> grid;

	/**
	 * The display mode, Material, Uranium value, Thorium value, Potassium value or user-defined value.
	 */
	private RadMode showMode;
	
	/**
	 * The font of texts.
	 */
	private Font font;

	/**
	 * The current material to fill the voxel with.
	 */
	private ColoredMaterial currentMaterial;

	/**
	 * The editor mode, 0 for pen selection, 1 for cursor rectangle selection mode.
	 */
	private int editorMode;

	/**
	 * If true, the left button has been pushed. Use in methods coming from mouse listener.
	 */
	private boolean leftButtonPushed;
	
	/**
	 * If true, the user want the edge excluded during the simulation in DosiVox2D software. If so,
	 * the edge is set to low light to let the user which edge will be excluded.
	 */
	private boolean exclusion;
	
	/**
	 * Constructor.
	 * @param global the global controller.
	 */
	public Grid2D(GlobalControler global)
	{
		this.controler = global;
		this.zoomManager = new ZoomAndPanListener(this, 1, DEFAULT_NB_VOXEL_X * DEFAULT_NB_VOXEL_Y * 10, 1.2);
		this.font = new Font("Serif", 0, 20);
		this.showMode = RadMode.Mat;
		this.editorMode = 0;
		this.leftButtonPushed = false;
		this.exclusion = false;
		this.addMouseListener(this.zoomManager);
		this.addMouseMotionListener(this.zoomManager);
		this.addMouseWheelListener(this.zoomManager);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
	}

	/**
	 * @return the currentMaterial
	 */
	public ColoredMaterial getCurrentMaterial()
	{
		return currentMaterial;
	}

	/**
	 * @param currentMaterial the currentMaterial to set
	 */
	public void setCurrentMaterial(ColoredMaterial currentMaterial)
	{
		this.currentMaterial = currentMaterial;
	}

	/**
	 * Draw the black strips of the grid, and all voxels.
	 * @param g the current context graphics.
	 */
	private void drawGrid(final Graphics g) {
		final Graphics2D g2d = (Graphics2D)g;
		final RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		rh.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g2d.setRenderingHints(rh);
		g2d.setTransform(this.zoomManager.getCoordTransform());

		g2d.setColor(Color.black);
		for (int y = 0; y < this.grid.size(); ++y) {
			for (int x = 0; x < this.grid.get(y).size(); ++x) {
				final VoxelObject vox = this.grid.get(y).get(x);
				if (y == 0) {
					g.drawString(new StringBuilder(String.valueOf(x)).toString(), (int)(x * vox.width + vox.width / 3.0f), (int)(y * vox.height - vox.height / 3.0f));
				}
				if (vox != null && vox.isVisible(this.getVisibleRect(), this.zoomManager.getCoordTransform())) {
					vox.draw(g2d, this.showMode,this.exclusion);
				}			
				g2d.setColor(Color.black);
				g2d.setStroke(new BasicStroke(2.0f));
				g2d.draw(vox);
				if (x == 0) {
					g.drawString(new StringBuilder(String.valueOf(y)).toString(), (int)(x * vox.width - vox.width), (int)(y * vox.height + vox.height / 2.0f));
				}
			}
		}
	}

	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		if(grid != null)
		{
			this.setBackground(Color.white);
			g.setFont(this.font);
			this.drawGrid(g);
		}
		if (this.cursorRectSelection != null && this.currentMaterial != null) {
			if(!this.currentMaterial.isUseTexture())
			{
				final Color c = new Color(this.currentMaterial.getColor().getRed(), 
						this.currentMaterial.getColor().getGreen(), 
						this.currentMaterial.getColor().getBlue(), 120);
				g2d.setColor(c);
				g2d.fill(this.cursorRectSelection);
			}
			else
			{
				Composite old = g2d.getComposite();
				Composite newC = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f);
				g2d.setComposite(newC);
				g2d.drawImage(this.currentMaterial.getIcon().getImage(),
						(int)cursorRectSelection.x, (int)cursorRectSelection.y, 
						(int)cursorRectSelection.width, (int)cursorRectSelection.height, null);
				g2d.setComposite(old);
			}
			
		}

	}

	

	@Override
	public void mouseClicked(MouseEvent arg0)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent arg0)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		Point2D pTransform = null;
        try {
            pTransform = this.zoomManager.transformPoint(e.getPoint());
        }
        catch (NoninvertibleTransformException e2) {
            e2.printStackTrace();
        }
        if (e.getButton() == 1) {
            if (this.editorMode == 0) {
                this.controler.checkAndFill(pTransform.getX(), pTransform.getY(), this.currentMaterial);   
            }
            else {
                this.cursorRectSelection = new Rectangle2D.Float();
                this.cursorRectSelection.x = (float)pTransform.getX();
                this.cursorRectSelection.y = (float)pTransform.getY();
            }
            this.leftButtonPushed = true;
        }
        this.repaint();
	}

	@Override
	public void mouseReleased(MouseEvent arg0)
	{
		if (this.cursorRectSelection != null) {
            this.controler.checkAndFill(this.cursorRectSelection, this.currentMaterial);
            this.cursorRectSelection = null;
            
            this.repaint();
        }
        this.leftButtonPushed = false;
	}

	@Override
	public void mouseDragged(MouseEvent e)
	{
		Point2D pTransform = null;
        try {
            pTransform = this.zoomManager.transformPoint(e.getPoint());
        }
        catch (NoninvertibleTransformException e2) {
            e2.printStackTrace();
        }
        if (this.leftButtonPushed) {
            if (this.editorMode == 0) {
                this.controler.checkAndFill(pTransform.getX(), pTransform.getY(),this.currentMaterial);
            }
            else {
                this.cursorRectSelection.width = (float)pTransform.getX() - this.cursorRectSelection.x;
                this.cursorRectSelection.height = (float)pTransform.getY() - this.cursorRectSelection.y;
            }
        }
        this.repaint();
	}

	@Override
	public void mouseMoved(MouseEvent e)
	{
		Point2D pTransform = null;
        try {
            pTransform = this.zoomManager.transformPoint(e.getPoint());
        }
        catch (NoninvertibleTransformException e2) {
            e2.printStackTrace();
        }
        if (this.leftButtonPushed) {
            if (this.editorMode == 0) {
                this.controler.checkAndFill(pTransform.getX(), pTransform.getY(), this.currentMaterial);
                
            }
            else {
                this.cursorRectSelection.width = (float)pTransform.getX() - this.cursorRectSelection.x;
                this.cursorRectSelection.height = (float)pTransform.getY() - this.cursorRectSelection.y;
            }
        }
        this.repaint();
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if(e.getActionCommand().equals("pen mode selection"))
		{
			this.editorMode = 0;
		}
		else if(e.getActionCommand().equals("rectangle mode selection"))
		{
			this.editorMode = 1;
		}
		else if(e.getActionCommand().equals("fill the selected layer"))
		{
			if (this.currentMaterial == null) {
				return;
			}
			final int option = JOptionPane.showConfirmDialog(null, "Do you really want to fill all the selected layer with " + this.currentMaterial.getName() + " ? Even filled voxels will be erase and fill with " + this.currentMaterial.getName(), "Filling the selected layer", 1, 3);
			if (option != 1 && option != 2 && option != -1) {
				this.controler.fillLayer(this.currentMaterial,false);
			}
		}
		else if(e.getActionCommand().equals("fill empty voxel in the selected layer"))
		{
			if (this.currentMaterial == null) {
				return;
			}
			this.controler.fillLayer(this.currentMaterial,true);
		}
	}

	@Override
	public void update(Observable obs, Object arg1)
	{
		Dosi2DModel model = (Dosi2DModel) obs;
		this.exclusion = model.isExcludeEdge();
		this.grid = model.getGrid();
		this.revalidate();
		this.repaint();
	}

	/**
	 * Change the displaying mode.
	 * @param mode the displaying mode.
	 */
	public void changeMode(RadMode mode)
	{
		this.showMode = mode;
		this.repaint();
	}

	/**
	 * Get the camera controller.
	 * @return the camera controller.
	 */
	public ZoomAndPanListener getZoomManager()
	{
		return zoomManager;
	}
}
