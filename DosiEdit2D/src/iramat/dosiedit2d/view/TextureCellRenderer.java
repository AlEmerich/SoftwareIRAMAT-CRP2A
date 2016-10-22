package iramat.dosiedit2d.view;

import iramat.dosiseed.model.ColoredMaterial;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

import util.ColorMaterial;

/**
 * ListCellRenderer used to add the specified color of the material in the cell.
 * @author alan
 *
 * @param <T> Generic type.
 * @see ListCellRenderer
 */
public class TextureCellRenderer<T> extends JPanel implements ListCellRenderer<T>
{
    private static final long serialVersionUID = -731417224399692806L;
    
    /**
     * The label of the cell.
     */
    private JLabel label;
    
    /**
     * is the material using texture of color.
     */
    private boolean useTexture;
    
    /**
     * Constructor.
     */
    public TextureCellRenderer() {
        this.label = new JLabel();
    }
    
    @Override
    public Component getListCellRendererComponent(final JList<? extends T> list, final T value, final int index, final boolean isSelected, final boolean cellHasFocus) {
        final ColoredMaterial m = ((CheckListItem)value).getM();
        this.label.setPreferredSize(new Dimension(list.getWidth(),20));
        this.setLayout(new BorderLayout());
        this.add(this.label, "Center");
        this.useTexture = m.isUseTexture();
        if(!useTexture)
        {
        	this.label.setIcon(null);
        	if (m.getColor() == null) {
        		if(m.getIndexMaterial() >= 9)
        		{
        			m.setColor(new Color((float)Math.random(),
        					(float)Math.random(),
        					(float)Math.random()));
        		}
        		else
        			m.setColor(ColorMaterial.values()[m.getIndexMaterial()].getColor());
        		this.setBackground(m.getColor());
        		((CheckListItem)value).setM(m);
        	}
        	else {
        		this.setBackground(m.getColor());
        	}
        }
        else
        {
        	this.setBackground(Color.WHITE);
        	this.label.setIcon(m.getIcon());
        	this.label.setIconTextGap(10);
        }

        this.label.setFont(list.getFont());
        this.label.setText(value.toString());
        this.label.repaint();
        return this;
    }
    
    /**
     * Inner class which describes cell rendered by {@link TextureCellRenderer}
     * @author alan
     *
     */
    public static class CheckListItem
    {
    	/**
    	 * The label of the cell.
    	 */
        private String label;
        
        /**
         * 
         */
        private boolean isSelected;
        
        /**
         * The material to render.
         */
        private ColoredMaterial m;
        
        /**
         * Constructor.
         * @param m the material to render.
         */
        public CheckListItem(final ColoredMaterial m) {
            this.isSelected = false;
            this.m = m;
            this.label = m.getName();
        }
        
        /**
         * 
         * @return true if cell is selected, false if not.
         */
        public boolean isSelected() {
            return this.isSelected;
        }
        
        /**
         * 
         * @param isSelected
         */
        public void setSelected(final boolean isSelected) {
            this.isSelected = isSelected;
        }
        
        @Override
        public String toString() {
            return this.label;
        }
        
        /**
         * Set the new material to render.
         * @param m the new material to render.
         */
        public void setM(final ColoredMaterial m) {
            this.m = m;
        }
        
        /**
         * Get the new material to render
         * @return the new material to render
         */
        public ColoredMaterial getM() {
            return this.m;
        }
        
        /**
         * Set the name of the cell.
         * @param name the name of the cell.
         */
        public void setLabel(final String name) {
            this.label = name;
        }
    }
}
