package iramat.dosiedit2d.view;

import interfaceObject.TripleLabelizedPanel;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;

/**
 * Class extending {@link TripleLabelizedPanel} to add new {@link JLabel}
 * @author alan
 *
 */
public class ExtensibleLabelizedPanel extends TripleLabelizedPanel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8563037086754140231L;

	/**
	 * List of new labels.
	 */
	private List<JLabel> labels;
	
	/**
	 * Constructor.
	 * @param label the description of labels.
	 * @param dim the dimension to set to labels.
	 */
	public ExtensibleLabelizedPanel(JLabel label,
			Dimension dim)
	{
		super(label, dim);
		labels = new ArrayList<>();
	}
	
	/**
	 * Constructor.
	 * @param label the description of labels.
	 * @param x the title border of first field.
	 * @param y the title border of second field.
	 * @param z the title border of third field.
	 * @param dim the dimension of labels.
	 */
	public ExtensibleLabelizedPanel(JLabel label,
			String x, String y, String z, Dimension dim)
	{
		super(label, x, y, z, dim);
		labels = new ArrayList<>();
	}
	
	/**
	 * Add new label.
	 * @param desc the desctiption title of the new label.
	 */
	public void addLabel(String desc)
	{
		JLabel label = new JLabel();
		label.setPreferredSize(new Dimension(60, 40));
		label.setMaximumSize(label.getPreferredSize());
        label.setBorder(BorderFactory.createTitledBorder(desc));
        label.setToolTipText(label.getText());
        this.add(label);
        labels.add(label);
	}
	
	/**
	 * Get the label to the specified index.
	 * @param index the index where to get the label.
	 * @return the label.
	 */
	public JLabel getLabelFromList(int index)
	{
		return labels.get(index);
	}
}
