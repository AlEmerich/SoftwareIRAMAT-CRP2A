package iramat.dosiseed.view;

import iramat.dosiseed.controler.*;
import iramat.dosiseed.model.AbstractModel;
import iramat.dosiseed.model.Material;
import iramat.dosiseed.model.Model;
import mainPackage.Component;
import mainPackage.PrimaryParticles;
import util.Couple;
import util.Vector3d;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class GlobalInfoPanel extends JPanel implements Observer
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 689568912280015500L;

	private FormattedLabelField nbPrimaryParticleField;

	private JComboBox<PrimaryParticles> typeParticleComboBox;

	private JCheckBox[] radioElementsCheck;

	private FormattedLabelField maxRangeField;

	private FormattedLabelField productionCut;

	private FormattedLabelField wfInMatrix;

	private JTable grainList;

	private JRadioButton fixedMediumCheck;

	private JRadioButton infiniteMediumCheck;
	
	private TripleFormattedField nbCoarseVoxel;

	public GlobalInfoPanel()
	{
		this.initGui();
	}

	private void initGui()
	{
		JPanel global = new JPanel();
		
		JPanel linkPart = new JPanel();
		JPanel firstPart = new JPanel();
		firstPart.setLayout(new BoxLayout(firstPart, BoxLayout.PAGE_AXIS));
		global.setLayout(new BoxLayout(global, BoxLayout.PAGE_AXIS));
		global.setBorder(BorderFactory.createRaisedBevelBorder());

		JPanel firstLine = new JPanel();
		nbPrimaryParticleField = new FormattedLabelField(this,
				new JLabel("<html><b>Number of particules emitted (x1000): </b></html>"));

		firstLine.add(nbPrimaryParticleField);

		firstLine.add(new JLabel("<html><b>Emitted rays: </b></html>"));
		PrimaryParticles[] tab = {PrimaryParticles.Alpha,PrimaryParticles.Beta,PrimaryParticles.Gamma};
		this.typeParticleComboBox = new JComboBox<PrimaryParticles>(tab);
		firstLine.add(typeParticleComboBox);
		firstPart.add(firstLine);

		JPanel secondLine = new JPanel();
		JPanel radioElementPanel = new JPanel();
		radioElementPanel.add(new JLabel("<html><b>Radio-active elements to simulate: </b></html>"));
		this.radioElementsCheck = new JCheckBox[4];
		JPanel panelcheck = new JPanel();
		radioElementsCheck[0] = new JCheckBox("K");
		panelcheck.add(radioElementsCheck[0]);
		radioElementsCheck[1] = new JCheckBox("U");
		panelcheck.add(radioElementsCheck[1]);
		radioElementsCheck[2] = new JCheckBox("Th");
		panelcheck.add(radioElementsCheck[2]);
		radioElementsCheck[3] = new JCheckBox("Ud");
		panelcheck.add(radioElementsCheck[3]);
		radioElementPanel.add(panelcheck);
		
		nbCoarseVoxel = new TripleFormattedField(
				new JLabel("<html><b>Number of coarse fraction voxels: </b></html>"), false);
		secondLine.add(radioElementPanel);
		firstPart.add(secondLine);

		JPanel secondPart = new JPanel();
		secondPart.setLayout(new BoxLayout(secondPart,BoxLayout.PAGE_AXIS));
		JPanel forthline = new JPanel();
		productionCut = new FormattedLabelField(this, new JLabel("<html><b>Production cut, in millimeter: </b></html>"));
		forthline.add(productionCut);
		JPanel fifthline = new JPanel();
		wfInMatrix = new FormattedLabelField(this,new JLabel("<html><b>Water fraction in the sediment</b></html>"));
		JLabel des = new JLabel("<html><b>GRAIN COMPOSITION</b></html>");
		fifthline.add(wfInMatrix);
		secondPart.add(forthline);
		secondPart.add(fifthline);
		linkPart.add(firstPart);
		linkPart.add(secondPart);
		global.add(linkPart);
		
		JPanel thirdline = new JPanel();
		thirdline.setBorder(BorderFactory.createEtchedBorder());
		thirdline.setLayout(new BoxLayout(thirdline, BoxLayout.PAGE_AXIS));
		ButtonGroup buttongroup = new ButtonGroup();
		fixedMediumCheck = new JRadioButton("<html><b>fixed medium</b></html>");
		fixedMediumCheck.setSelected(true);
		infiniteMediumCheck = new JRadioButton("<html><b>infinite medium</b></html>");
		buttongroup.add(fixedMediumCheck);
		buttongroup.add(infiniteMediumCheck);
		maxRangeField = new FormattedLabelField(this, 
				new JLabel("Particles maximum range in millimeter "));
		JPanel buttonPan = new JPanel();
		buttonPan.add(fixedMediumCheck);
		buttonPan.add(infiniteMediumCheck);
		thirdline.add(buttonPan);
		thirdline.add(maxRangeField);
		linkPart.add(thirdline);
		
		JPanel tmp = new JPanel();
		tmp.setLayout(new BoxLayout(tmp, BoxLayout.PAGE_AXIS));
		tmp.setBorder(BorderFactory.createRaisedBevelBorder());
		JPanel grainPanel = new JPanel(new BorderLayout());
		
		tmp.add(des);
		tmp.add(grainPanel);
		

		DefaultTableModel tableModel = new DefaultTableModel(){
			/**
			 * 
			 */
			private static final long serialVersionUID = -6500592814361772319L;

			@Override
			public boolean isCellEditable(int rowCount,int colCount)
			{
				return false;
			}
		};
		tableModel.addColumn("Description");
		tableModel.addColumn("Name");
		tableModel.addColumn("Density");
		tableModel.addColumn("Nb. of component");
		tableModel.addColumn("List component ID/mass");
		grainList = new JTable(tableModel);
		TableColumn col;
		col = grainList.getColumnModel().getColumn(0);
		col.setPreferredWidth(50);
		col = grainList.getColumnModel().getColumn(1);
		col.setPreferredWidth(10);	
		col = grainList.getColumnModel().getColumn(2);
		col.setPreferredWidth(10);
		col = grainList.getColumnModel().getColumn(3);
		col.setPreferredWidth(100);
		col = grainList.getColumnModel().getColumn(4);
		col.setPreferredWidth(200);

		JScrollPane listScroller = new JScrollPane(grainList);
		listScroller.setPreferredSize(new Dimension(500,150));
		listScroller.setAutoscrolls(true);
		grainPanel.add(listScroller);

		global.add(nbCoarseVoxel);
		global.add(tmp);

		this.add(global);

	}

	public void setNbParticleControler(NbParticleControler nbControler)
	{
		this.nbPrimaryParticleField.getField().addKeyListener(nbControler);
		this.nbPrimaryParticleField.getField().addFocusListener(nbControler);
	}

	public void setMaxRangeControler(MaxRangeControler maxRangeControler)
	{
		this.maxRangeField.getField().addKeyListener(maxRangeControler);
		this.maxRangeField.getField().addFocusListener(maxRangeControler);
	}

	public void setProductionCutControler(ProductionCutControler productCutControler)
	{
		this.productionCut.getField().addKeyListener(productCutControler);
		this.productionCut.getField().addFocusListener(productCutControler);
	}

	public void setWaterFractionControler(WfControler wfControler)
	{
		this.wfInMatrix.getField().addKeyListener(wfControler);
		this.wfInMatrix.getField().addFocusListener(wfControler);
	}
	
	public void setNbVoxelControler(NbVoxelControler nbVoxelControler)
	{
		this.nbCoarseVoxel.addKeyListener(nbVoxelControler);
		nbVoxelControler.setFieldsView(nbCoarseVoxel);
	}

	public void setGlobalInfoControler(GlobalControler control)
	{
		this.typeParticleComboBox.addActionListener(control);
		this.typeParticleComboBox.setActionCommand("type particle");

		this.radioElementsCheck[0].addActionListener(control);
		this.radioElementsCheck[0].setActionCommand("K boolean");
		this.radioElementsCheck[1].addActionListener(control);
		this.radioElementsCheck[1].setActionCommand("U boolean");
		this.radioElementsCheck[2].addActionListener(control);
		this.radioElementsCheck[2].setActionCommand("Th boolean");
		this.radioElementsCheck[3].addActionListener(control);
		this.radioElementsCheck[3].setActionCommand("Ud boolean");

		this.fixedMediumCheck.addActionListener(control);
		this.fixedMediumCheck.setActionCommand("fixed medium");
		this.infiniteMediumCheck.addActionListener(control);
		this.infiniteMediumCheck.setActionCommand("infinite medium");

	}

	/***********************************************************************
	 *                      GETTER / SETTER METHODS
	 ***********************************************************************/

	public void setNbPrimaryParticleField(float nb)
	{
		this.nbPrimaryParticleField.getField().setText(nb+"");
	}

	public void setPrimaryParticleEmitted(PrimaryParticles primaryParticle)
	{
		this.typeParticleComboBox.setSelectedItem(primaryParticle);
	}

	public void setProductionCut(float nb)
	{
		this.productionCut.getField().setText(nb+"");
	}

	public void setMaxRange(float nb)
	{
		this.maxRangeField.getField().setText(nb+"");
	}

	public void setWfInMatrix(float nb)
	{
		this.wfInMatrix.getField().setText(nb+"");
	}

	@Override
	public void update(Observable arg0, Object arg1)
	{
		AbstractModel model = (AbstractModel) arg0;
		
		this.setNbPrimaryParticleField(model.getNbParticlesEmitted());
		this.nbPrimaryParticleField.isUpdated();

		this.setPrimaryParticleEmitted(model.getPrimaryParticle());

		boolean[] radioElem = model.getWhichRadioelementIsSimulated();
		for(int i=0;i<this.radioElementsCheck.length;i++)
			this.radioElementsCheck[i].setSelected(radioElem[i]);

		if(!model.whichMedium())
		{
			this.infiniteMediumCheck.setSelected(true);
		}
		else
		{
			this.fixedMediumCheck.setSelected(true);
		}

		this.setMaxRange(model.getMaxRange());
		this.maxRangeField.isUpdated();

		this.setProductionCut(model.getProductionCut());
		this.productionCut.isUpdated();

		this.setWfInMatrix(model.getWfInMatrix());
		this.wfInMatrix.isUpdated();

		List<Material> listGrain = model.getCompositionOfGrains();
		DefaultTableModel tableModel = (DefaultTableModel) grainList.getModel();
		
		int i;
		for(i=0;i<listGrain.size();i++)
		{
			if(i >= tableModel.getRowCount())
				tableModel.addRow(new String[]{"","","","",""});
			
			Material mat = listGrain.get(i);
			if(i==0)
				tableModel.setValueAt("Matrix", i, 0);
			else
				tableModel.setValueAt("Type "+i, i, 0);
				
			tableModel.setValueAt(mat.getName(), i, 1);
			tableModel.setValueAt(mat.getUsedDensity()+"", i, 2);
			tableModel.setValueAt(mat.getListComponent().size()+"", i, 3);
			
			String str = "";
			for(int j=0;j<mat.getListComponent().size();j++)
			{
				Couple<Component,Float> couple = mat.getListComponent().get(j);
				str+=couple.getValeur1().getName()+" "+couple.getValeur2()+" ";
			}
			tableModel.setValueAt(str, i, 4);
		}
		
		for(int flush = i;flush < tableModel.getRowCount(); flush++)
			tableModel.removeRow(flush);
		
		Vector3d nbVox = ((Model) model).getNbVoxelsInDetector();
		this.nbCoarseVoxel.updateFrameComponent(nbVox.getX()+"", nbVox.getY()+"", nbVox.getZ()+"");
		
		grainList.repaint();
	}

	public int getSelectedType()
	{
		return grainList.getSelectedRow();
	}
}
