package interfaceObject;

import java.awt.AlphaComposite;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import mainPackage.PeriodicTable;
import mainPackage.TopLevelInterface;

/**
 * A JPanel with a Periodic Panel image on the background, 
 * with a grid of invisible button to make the chemical element clickable.
 * @author alan
 * @see PeriodicTable
 */
public class PeriodicTablePanel extends JPanel
{
    private static Dimension sizeButton;
    private static final long serialVersionUID = 3355506038359746005L;
    private Image periodicImg;
    private ActionListener Father;
    
    static {
        PeriodicTablePanel.sizeButton = new Dimension(15, 15);
    }
    
    /**
     * Constructor. Loads the image from the resources folder and call the initButtons method.
     * @param father the panel father.
     * @param dim the dimension of the periodic panel.
     * @throws IOException
     */
    public PeriodicTablePanel(final ActionListener father, final Dimension dim) throws IOException {
        this.setPreferredSize(dim);
        this.Father = father;
        final ImageIcon img = TopLevelInterface.createImageIcon("/resources/Periodic_table_(polyatomic).png");
        this.periodicImg = img.getImage();
        final GridLayout gridL = new GridLayout(0, 19);
        gridL.setHgap(2);
        gridL.setVgap(1);
        this.setLayout(gridL);
        this.initButtons();
    }
    
    /**
     * Initialization of all little buttons.
     */
    private void initButtons() {
        for (int i = 0; i < 19; ++i) {
            this.add(new JLabel());
        }
        this.add(new JLabel());
        this.add(new littleButton(PeriodicTable.H, PeriodicTablePanel.sizeButton));
        for (int i = 0; i < 16; ++i) {
            this.add(new JLabel());
        }
        this.add(new littleButton(PeriodicTable.He, PeriodicTablePanel.sizeButton));
        this.add(new JLabel());
        this.add(new littleButton(PeriodicTable.Li, PeriodicTablePanel.sizeButton));
        this.add(new littleButton(PeriodicTable.Be, PeriodicTablePanel.sizeButton));
        for (int i = 0; i < 10; ++i) {
            this.add(new JLabel());
        }
        this.add(new littleButton(PeriodicTable.B, PeriodicTablePanel.sizeButton));
        this.add(new littleButton(PeriodicTable.C, PeriodicTablePanel.sizeButton));
        this.add(new littleButton(PeriodicTable.N, PeriodicTablePanel.sizeButton));
        this.add(new littleButton(PeriodicTable.O, PeriodicTablePanel.sizeButton));
        this.add(new littleButton(PeriodicTable.F, PeriodicTablePanel.sizeButton));
        this.add(new littleButton(PeriodicTable.Ne, PeriodicTablePanel.sizeButton));
        this.add(new JLabel());
        this.add(new littleButton(PeriodicTable.Na, PeriodicTablePanel.sizeButton));
        this.add(new littleButton(PeriodicTable.Mg, PeriodicTablePanel.sizeButton));
        for (int i = 0; i < 10; ++i) {
            this.add(new JLabel());
        }
        this.add(new littleButton(PeriodicTable.Al, PeriodicTablePanel.sizeButton));
        this.add(new littleButton(PeriodicTable.Si, PeriodicTablePanel.sizeButton));
        this.add(new littleButton(PeriodicTable.P, PeriodicTablePanel.sizeButton));
        this.add(new littleButton(PeriodicTable.S, PeriodicTablePanel.sizeButton));
        this.add(new littleButton(PeriodicTable.Cl, PeriodicTablePanel.sizeButton));
        this.add(new littleButton(PeriodicTable.Ar, PeriodicTablePanel.sizeButton));
        this.add(new JLabel());
        this.add(new littleButton(PeriodicTable.K, PeriodicTablePanel.sizeButton));
        this.add(new littleButton(PeriodicTable.Ca, PeriodicTablePanel.sizeButton));
        this.add(new littleButton(PeriodicTable.Sc, PeriodicTablePanel.sizeButton));
        this.add(new littleButton(PeriodicTable.Ti, PeriodicTablePanel.sizeButton));
        this.add(new littleButton(PeriodicTable.V, PeriodicTablePanel.sizeButton));
        this.add(new littleButton(PeriodicTable.Cr, PeriodicTablePanel.sizeButton));
        this.add(new littleButton(PeriodicTable.Mn, PeriodicTablePanel.sizeButton));
        this.add(new littleButton(PeriodicTable.Fe, PeriodicTablePanel.sizeButton));
        this.add(new littleButton(PeriodicTable.Co, PeriodicTablePanel.sizeButton));
        this.add(new littleButton(PeriodicTable.Ni, PeriodicTablePanel.sizeButton));
        this.add(new littleButton(PeriodicTable.Cu, PeriodicTablePanel.sizeButton));
        this.add(new littleButton(PeriodicTable.Zn, PeriodicTablePanel.sizeButton));
        this.add(new littleButton(PeriodicTable.Ga, PeriodicTablePanel.sizeButton));
        this.add(new littleButton(PeriodicTable.Ge, PeriodicTablePanel.sizeButton));
        this.add(new littleButton(PeriodicTable.As, PeriodicTablePanel.sizeButton));
        this.add(new littleButton(PeriodicTable.Se, PeriodicTablePanel.sizeButton));
        this.add(new littleButton(PeriodicTable.Br, PeriodicTablePanel.sizeButton));
        this.add(new littleButton(PeriodicTable.Kr, PeriodicTablePanel.sizeButton));
        this.add(new JLabel());
        this.add(new littleButton(PeriodicTable.Rb, PeriodicTablePanel.sizeButton));
        this.add(new littleButton(PeriodicTable.Sr, PeriodicTablePanel.sizeButton));
        this.add(new littleButton(PeriodicTable.Y, PeriodicTablePanel.sizeButton));
        this.add(new littleButton(PeriodicTable.Zr, PeriodicTablePanel.sizeButton));
        this.add(new littleButton(PeriodicTable.Nb, PeriodicTablePanel.sizeButton));
        this.add(new littleButton(PeriodicTable.Mo, PeriodicTablePanel.sizeButton));
        this.add(new littleButton(PeriodicTable.Tc, PeriodicTablePanel.sizeButton));
        this.add(new littleButton(PeriodicTable.Ru, PeriodicTablePanel.sizeButton));
        this.add(new littleButton(PeriodicTable.Rh, PeriodicTablePanel.sizeButton));
        this.add(new littleButton(PeriodicTable.Pd, PeriodicTablePanel.sizeButton));
        this.add(new littleButton(PeriodicTable.Ag, PeriodicTablePanel.sizeButton));
        this.add(new littleButton(PeriodicTable.Cd, PeriodicTablePanel.sizeButton));
        this.add(new littleButton(PeriodicTable.In, PeriodicTablePanel.sizeButton));
        this.add(new littleButton(PeriodicTable.Sn, PeriodicTablePanel.sizeButton));
        this.add(new littleButton(PeriodicTable.Sb, PeriodicTablePanel.sizeButton));
        this.add(new littleButton(PeriodicTable.Te, PeriodicTablePanel.sizeButton));
        this.add(new littleButton(PeriodicTable.I, PeriodicTablePanel.sizeButton));
        this.add(new littleButton(PeriodicTable.Xe, PeriodicTablePanel.sizeButton));
        this.add(new JLabel());
        this.add(new littleButton(PeriodicTable.Cs, PeriodicTablePanel.sizeButton));
        this.add(new littleButton(PeriodicTable.Ba, PeriodicTablePanel.sizeButton));
        this.add(new JLabel());
        this.add(new littleButton(PeriodicTable.Hf, PeriodicTablePanel.sizeButton));
        this.add(new littleButton(PeriodicTable.Ta, PeriodicTablePanel.sizeButton));
        this.add(new littleButton(PeriodicTable.W, PeriodicTablePanel.sizeButton));
        this.add(new littleButton(PeriodicTable.Re, PeriodicTablePanel.sizeButton));
        this.add(new littleButton(PeriodicTable.Os, PeriodicTablePanel.sizeButton));
        this.add(new littleButton(PeriodicTable.Ir, PeriodicTablePanel.sizeButton));
        this.add(new littleButton(PeriodicTable.Pt, PeriodicTablePanel.sizeButton));
        this.add(new littleButton(PeriodicTable.Au, PeriodicTablePanel.sizeButton));
        this.add(new littleButton(PeriodicTable.Hg, PeriodicTablePanel.sizeButton));
        this.add(new littleButton(PeriodicTable.Tl, PeriodicTablePanel.sizeButton));
        this.add(new littleButton(PeriodicTable.Pb, PeriodicTablePanel.sizeButton));
        this.add(new littleButton(PeriodicTable.Bi, PeriodicTablePanel.sizeButton));
        this.add(new littleButton(PeriodicTable.Po, PeriodicTablePanel.sizeButton));
        this.add(new littleButton(PeriodicTable.At, PeriodicTablePanel.sizeButton));
        this.add(new littleButton(PeriodicTable.Rn, PeriodicTablePanel.sizeButton));
        this.add(new JLabel());
        this.add(new littleButton(PeriodicTable.Fr, PeriodicTablePanel.sizeButton));
        this.add(new littleButton(PeriodicTable.Ra, PeriodicTablePanel.sizeButton));
        this.add(new JLabel());
        this.add(new littleButton(PeriodicTable.Rf, PeriodicTablePanel.sizeButton));
        this.add(new littleButton(PeriodicTable.Db, PeriodicTablePanel.sizeButton));
        this.add(new littleButton(PeriodicTable.Sg, PeriodicTablePanel.sizeButton));
        this.add(new littleButton(PeriodicTable.Bh, PeriodicTablePanel.sizeButton));
        this.add(new littleButton(PeriodicTable.Hs, PeriodicTablePanel.sizeButton));
        this.add(new littleButton(PeriodicTable.Mt, PeriodicTablePanel.sizeButton));
        this.add(new littleButton(PeriodicTable.Ds, PeriodicTablePanel.sizeButton));
        this.add(new littleButton(PeriodicTable.Rg, PeriodicTablePanel.sizeButton));
        this.add(new littleButton(PeriodicTable.Cn, PeriodicTablePanel.sizeButton));
        this.add(new littleButton(PeriodicTable.Uut, PeriodicTablePanel.sizeButton));
        this.add(new littleButton(PeriodicTable.Fl, PeriodicTablePanel.sizeButton));
        this.add(new littleButton(PeriodicTable.Uup, PeriodicTablePanel.sizeButton));
        this.add(new littleButton(PeriodicTable.Lv, PeriodicTablePanel.sizeButton));
        this.add(new littleButton(PeriodicTable.Uus, PeriodicTablePanel.sizeButton));
        this.add(new littleButton(PeriodicTable.Uuo, PeriodicTablePanel.sizeButton));
        for (int i = 0; i < 3; ++i) {
            this.add(new JLabel());
        }
        this.add(new littleButton(PeriodicTable.La, PeriodicTablePanel.sizeButton));
        this.add(new littleButton(PeriodicTable.Ce, PeriodicTablePanel.sizeButton));
        this.add(new littleButton(PeriodicTable.Pr, PeriodicTablePanel.sizeButton));
        this.add(new littleButton(PeriodicTable.Nd, PeriodicTablePanel.sizeButton));
        this.add(new littleButton(PeriodicTable.Pm, PeriodicTablePanel.sizeButton));
        this.add(new littleButton(PeriodicTable.Sm, PeriodicTablePanel.sizeButton));
        this.add(new littleButton(PeriodicTable.Eu, PeriodicTablePanel.sizeButton));
        this.add(new littleButton(PeriodicTable.Gd, PeriodicTablePanel.sizeButton));
        this.add(new littleButton(PeriodicTable.Tb, PeriodicTablePanel.sizeButton));
        this.add(new littleButton(PeriodicTable.Dy, PeriodicTablePanel.sizeButton));
        this.add(new littleButton(PeriodicTable.Ho, PeriodicTablePanel.sizeButton));
        this.add(new littleButton(PeriodicTable.Er, PeriodicTablePanel.sizeButton));
        this.add(new littleButton(PeriodicTable.Tm, PeriodicTablePanel.sizeButton));
        this.add(new littleButton(PeriodicTable.Yb, PeriodicTablePanel.sizeButton));
        this.add(new littleButton(PeriodicTable.Lu, PeriodicTablePanel.sizeButton));
        this.add(new JLabel());
        for (int i = 0; i < 3; ++i) {
            this.add(new JLabel());
        }
        this.add(new littleButton(PeriodicTable.Ac, PeriodicTablePanel.sizeButton));
        this.add(new littleButton(PeriodicTable.Th, PeriodicTablePanel.sizeButton));
        this.add(new littleButton(PeriodicTable.Pa, PeriodicTablePanel.sizeButton));
        this.add(new littleButton(PeriodicTable.U, PeriodicTablePanel.sizeButton));
        this.add(new littleButton(PeriodicTable.Np, PeriodicTablePanel.sizeButton));
        this.add(new littleButton(PeriodicTable.Pu, PeriodicTablePanel.sizeButton));
        this.add(new littleButton(PeriodicTable.Am, PeriodicTablePanel.sizeButton));
        this.add(new littleButton(PeriodicTable.Cm, PeriodicTablePanel.sizeButton));
        this.add(new littleButton(PeriodicTable.Bk, PeriodicTablePanel.sizeButton));
        this.add(new littleButton(PeriodicTable.Cf, PeriodicTablePanel.sizeButton));
        this.add(new littleButton(PeriodicTable.Es, PeriodicTablePanel.sizeButton));
        this.add(new littleButton(PeriodicTable.Fm, PeriodicTablePanel.sizeButton));
        this.add(new littleButton(PeriodicTable.Md, PeriodicTablePanel.sizeButton));
        this.add(new littleButton(PeriodicTable.No, PeriodicTablePanel.sizeButton));
        this.add(new littleButton(PeriodicTable.Lr, PeriodicTablePanel.sizeButton));
    }
    
    @Override
    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);
        g.drawImage(this.periodicImg, 0, 0, this.getWidth(), this.getHeight(), null);
    }
    
    /**
     * Class handling the invisible button the PeriodicTablePanel put on the image representing
     * the periodic table of the elements.
     * @author alan
     *
     */
    private class littleButton extends JButton
    {
        private static final long serialVersionUID = -3864372564021235432L;
        
        public littleButton(final PeriodicTable atom, final Dimension dim) {
            super(atom.toString());
            this.setMargin(new Insets(0, 0, 0, 0));
            String tip = "";
            tip = String.valueOf(tip) + "<html><b>Scientific name:</b> " + atom.getScientificName() + "<br>";
            tip = String.valueOf(tip) + "<b>Atomic mass:</b> ";
            if (atom.getAtomicMass() == -1.0f) {
                tip = String.valueOf(tip) + "Unknown</html>";
            }
            else {
                tip = String.valueOf(tip) + atom.getAtomicMass() + "</html>";
            }
            this.setToolTipText(tip);
            this.setSize(dim);
            this.setOpaque(false);
            this.setActionCommand("little button");
            this.addActionListener((ActionListener)PeriodicTablePanel.this.Father);
        }
        
        @Override
        public void paint(final Graphics g) {
            final Graphics2D g2 = (Graphics2D)g.create();
            g2.setComposite(AlphaComposite.getInstance(3, 0.0f));
            super.paint(g2);
            g2.dispose();
        }
        
        @Override
        public String toString() {
            return this.getText();
        }
    }
}
