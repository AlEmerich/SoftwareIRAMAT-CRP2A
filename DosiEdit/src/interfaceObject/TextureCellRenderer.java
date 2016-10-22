package interfaceObject;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

import mainPackage.Material;
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
    private JCheckBox check;
    private JLabel label;
    
    public TextureCellRenderer() {
        this.check = new JCheckBox();
        this.label = new JLabel();
    }
    
    @Override
    public Component getListCellRendererComponent(final JList<? extends T> list, final T value, final int index, final boolean isSelected, final boolean cellHasFocus) {
        final Material m = ((CheckListItem)value).getM();
        this.setLayout(new BorderLayout());
        this.add(this.check, "West");
        this.add(this.label, "Center");
        if (m.getIcon() == null && m.getColor() == null) {
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
        else if (m.getColor() == null) {
            this.check.setIcon(m.getIcon());
            this.check.setIconTextGap(5);
            this.check.setHorizontalTextPosition(4);
        }
        else {
            this.setBackground(m.getColor());
        }
        this.check.setEnabled(list.isEnabled());
        this.check.setSelected(((CheckListItem)value).isSelected());
        ((CheckListItem)value).getM().setActive(((CheckListItem)value).isSelected());
        this.label.setFont(list.getFont());
        this.label.setText(value.toString());
        return this;
    }
    
    /**
     * @return the current check box.
     */
    public JComponent getCheckBox() {
        return this.check;
    }
    
    public static class CheckListItem
    {
        private String label;
        private boolean isSelected;
        private Material m;
        
        public CheckListItem(final Material m) {
            this.isSelected = false;
            this.m = m;
            this.label = m.getName();
        }
        
        public boolean isSelected() {
            return this.isSelected;
        }
        
        public void setSelected(final boolean isSelected) {
            this.isSelected = isSelected;
        }
        
        @Override
        public String toString() {
            return this.label;
        }
        
        public void setM(final Material m) {
            this.m = m;
        }
        
        public Material getM() {
            return this.m;
        }
        
        public void setLabel(final String name) {
            this.label = name;
        }
    }
}
