package interfaceObject;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.MaskFormatter;

import mainPackage.Component;
import mainPackage.MaterialComponentProviderInterface;
import mainPackage.PeriodicTable;

/**
 * JPanel containing all the material factory, itself contained by {@link ForgeWindow}.
 * @author alan
 * @see ListSelectionListener
 */
public class SouthComponentPanel extends JPanel implements ActionListener, ListSelectionListener
{
    private static final long serialVersionUID = -1757748566916166749L;
    
    /**
     * UI fields.
     */
    private JPanel EastComponentPan;
    private JTextField nameComponentField;
    private JLabel FormulaChangingLabel;
    private JTextField densityCompField;
    private JFormattedTextField setNbAtomLastElement;
    private JList<mainPackage.Component> AllComponentList;
    private DefaultListModel<mainPackage.Component> AllCompModelList;
    private JScrollPane CompScroll;
    
    /**
     * The north material side which listened what happened on components.
     */
    private ComponentAddedListener NorthListener;
    
    /**
     * the top level father.
     */
    private MaterialComponentProviderInterface Father;
    
    /**
     * The current component to work with.
     */
    private mainPackage.Component currentComponent;
    
    /**
     * Counter for how many new component is created.
     */
    private int intCountNewComp;
    
    public SouthComponentPanel(final MaterialComponentProviderInterface father) throws ParseException, IOException {
        this.intCountNewComp = 0;
        this.Father = father;
        this.setLayout(new BorderLayout());
        this.setBorder(BorderFactory.createTitledBorder(BorderFactory.createBevelBorder(1), "COMPONENT FACTORY", 2, 0));
        this.setPreferredSize(new Dimension(800, 300));
        final PeriodicTablePanel perPan = new PeriodicTablePanel(this, new Dimension(500, 250));
        this.add(perPan, "West");
        this.initEastSideOfSouthFactory();
    }
    
    /**
     * Initialization of the east side of the panel.
     * @throws ParseException
     */
    private void initEastSideOfSouthFactory() throws ParseException {
        final JPanel ComponentPan = new JPanel();
        ComponentPan.setLayout(new BoxLayout(ComponentPan, 3));
        ComponentPan.setPreferredSize(new Dimension(200, 350));
        ComponentPan.add(Box.createHorizontalGlue());
        ComponentPan.add(Box.createVerticalGlue());
        final JPanel ButtonComponentPan = new JPanel();
        final JButton CreateNewComponent = new JButton("Create new component");
        CreateNewComponent.setActionCommand("create new component");
        CreateNewComponent.setPreferredSize(new Dimension(200, 30));
        CreateNewComponent.addActionListener(this);
        ButtonComponentPan.add(CreateNewComponent);
        ComponentPan.add(ButtonComponentPan);
        final JPanel namePan = new JPanel();
        namePan.setPreferredSize(new Dimension(200, 70));
        final JLabel nameLabel = new JLabel("Name of the component:");
        nameLabel.setPreferredSize(new Dimension(185, 30));
        (this.nameComponentField = new JTextField()).setPreferredSize(new Dimension(200, 30));
        this.nameComponentField.addActionListener(this);
        namePan.add(nameLabel);
        namePan.add(this.nameComponentField);
        ComponentPan.add(namePan);
        final JPanel densityPan = new JPanel();
        densityPan.setPreferredSize(new Dimension(200, 30));
        final JLabel densityCompLabel = new JLabel("Density (g/cm3):");
        densityCompLabel.setPreferredSize(new Dimension(120, 30));
        densityPan.add(densityCompLabel);
        (this.densityCompField = new JTextField()).setPreferredSize(new Dimension(50, 25));
        this.densityCompField.addActionListener(this);
        densityPan.add(this.densityCompField);
        ComponentPan.add(densityPan);
        final JPanel nbAtomPan = new JPanel();
        final JLabel setAtomNbLastElementLabel = new JLabel("Set the number of atoms for the last selected element:");
        nbAtomPan.add(setAtomNbLastElementLabel);
        nbAtomPan.setPreferredSize(new Dimension(200, 65));
        (this.setNbAtomLastElement = new JFormattedTextField(new MaskFormatter("###"))).setPreferredSize(new Dimension(30, 25));
        this.setNbAtomLastElement.setFocusLostBehavior(3);
        nbAtomPan.add(this.setNbAtomLastElement);
        final JButton OkButtonNbAtom = new JButton("OK");
        OkButtonNbAtom.setActionCommand("Ok button nb atom");
        OkButtonNbAtom.setPreferredSize(new Dimension(50, 30));
        OkButtonNbAtom.setMargin(new Insets(0, 0, 0, 0));
        OkButtonNbAtom.addActionListener(this);
        nbAtomPan.add(OkButtonNbAtom);
        ComponentPan.add(nbAtomPan);
        final JLabel formulaLabel = new JLabel("Formula");
        formulaLabel.setPreferredSize(new Dimension(300, 30));
        ComponentPan.add(formulaLabel);
        (this.FormulaChangingLabel = new JLabel("")).setFont(new Font("Serif", 1, 12));
        this.FormulaChangingLabel.setPreferredSize(new Dimension(150, 30));
        this.FormulaChangingLabel.setBackground(Color.GRAY);
        ComponentPan.add(this.FormulaChangingLabel);
        final JButton ClearFormulaButton = new JButton("Clear formula");
        ClearFormulaButton.setActionCommand("Clear formula");
        ClearFormulaButton.setPreferredSize(new Dimension(60, 30));
        ClearFormulaButton.addActionListener(this);
        ComponentPan.add(ClearFormulaButton);
        this.AllCompModelList = new DefaultListModel<mainPackage.Component>();
        for (int i = 0; i < this.Father.getListOfComponent().size(); ++i) {
            this.AllCompModelList.insertElementAt((Component) this.Father.getListOfComponent().get(i), i);
        }
        (this.AllComponentList = new JList<mainPackage.Component>(this.AllCompModelList)).setSelectedIndex(0);
        this.AllComponentList.addListSelectionListener(this);
        this.currentComponent = this.AllComponentList.getSelectedValue();
        (this.CompScroll = new JScrollPane(this.AllComponentList, 20, 30)).setBorder(BorderFactory.createTitledBorder("Components list"));
        this.CompScroll.setPreferredSize(new Dimension(100, 360));
        (this.EastComponentPan = new JPanel()).setLayout(new BoxLayout(this.EastComponentPan, 2));
        this.EastComponentPan.add(ComponentPan);
        this.EastComponentPan.add(this.CompScroll);
        this.EastComponentPan.setMaximumSize(new Dimension(700, 400));
        this.add(this.EastComponentPan, "Center");
        this.updateFrameComponent();
    }
    
    /**
     * Update Swing components.
     */
    public void updateFrameComponent() {
        this.FormulaChangingLabel.setText(this.currentComponent.getFormulaAsString().toString());
        this.nameComponentField.setText(this.currentComponent.getName());
        String dens = "";
        dens = String.valueOf(dens) + this.currentComponent.getDensity();
        this.densityCompField.setText(dens);
    }
    
    /**
     * Triggered when an action is performed, like a click on a button.
     */
    @SuppressWarnings("unchecked")
	@Override
    public void actionPerformed(final ActionEvent e) {
        Label_0309: {
            if (e.getActionCommand().equals("create new component")) {
                final mainPackage.Component c = new mainPackage.Component("Unknown" + this.intCountNewComp++, false);
                this.currentComponent = c;
                ((List<Component>)this.Father.getListOfComponent()).add(c);
                this.NorthListener.componentAdded(c);
                this.AllCompModelList.addElement(c);
                this.AllComponentList.setSelectedValue(c, true);
            }
            else if (!this.currentComponent.isBasic()) {
                if (e.getActionCommand().equals("little button")) {
                    this.currentComponent.addAtomToFormula(PeriodicTable.valueOf(e.getSource().toString()));
                }
                else if (e.getActionCommand().equals("Ok button nb atom")) {
                    if (this.setNbAtomLastElement.getText() != "") {
                        this.currentComponent.setNbOfLastAtom(Integer.parseInt(this.setNbAtomLastElement.getText().replaceAll("\\s", "")));
                    }
                }
                else {
                    if (e.getSource() != this.densityCompField) {
                        if (e.getSource() != this.nameComponentField) {
                            if (e.getActionCommand().equals("Clear formula")) {
                                this.currentComponent.clearChemicalFormula();
                            }
                            break Label_0309;
                        }
                    }
                    try {
                        this.currentComponent.setDensity(Float.parseFloat(this.densityCompField.getText().replaceAll("\\s", "")));
                    }
                    catch (NumberFormatException numberE) {
                        JOptionPane.showMessageDialog(null, "Number format exception in density field. Please input numbers only.");
                    }
                    if (!this.nameComponentField.getText().trim().isEmpty()) {
                        this.currentComponent.setName(this.nameComponentField.getText().trim());
                    }
                }
            }
            else {
                JOptionPane.showMessageDialog(null, "Not allowed to alterate basic component. You may want to create a new one.", "Information", 1);
            }
        }
        this.updateFrameComponent();
    }
    
    @Override
    public void valueChanged(final ListSelectionEvent e) {
        this.currentComponent = this.AllComponentList.getSelectedValue();
        this.updateFrameComponent();
    }
    
    /**
     * The listener will be notified if a component is added.
     * @param f the listener to notify.
     */
    public void addComponentAddedListener(final ComponentAddedListener f) {
        this.NorthListener = f;
    }
}
