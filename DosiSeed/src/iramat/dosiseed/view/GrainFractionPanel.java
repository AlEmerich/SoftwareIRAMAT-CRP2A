package iramat.dosiseed.view;

import iramat.dosiseed.controler.DisablingTableListener;
import iramat.dosiseed.controler.GrainFractionControler;
import iramat.dosiseed.controler.VolumicFractionFieldControler;
import iramat.dosiseed.controler.VoxelSizeFieldControler;
import iramat.dosiseed.model.GrainFraction;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Enumeration;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;


public class GrainFractionPanel extends JPanel implements Observer
{
	private class GrainTableModel extends DefaultTableModel
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = -1229544639502871724L;

		private boolean isForSize;
		
		public GrainTableModel(boolean size)
		{
			this.isForSize = size;
		}
		
		@Override
		public boolean isCellEditable(int row,int column)
		{
			return (!isForSize && column == 0 ? false : true);
		}
		
		public Vector<?> getColumnIdentifiers()
		{
			return columnIdentifiers;
		}
	}

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 6256652432199517497L;

	private FormattedLabelField volumicFractionField;

	private FormattedLabelField voxelSizeField;

	private JRadioButton randomButton;

	private JRadioButton recurentButton;

	private boolean isCoarse;

	private GrainTableModel descOfSizeModel;

	private JTable descOfEachSizeTab;

	private GrainTableModel descOfTypeModel;

	private JTable descOfEachTypeTab;

	private JButton addSizeOfGrainButton;

	private JButton removeSizeOfGrainButton;

	public GrainFractionPanel(boolean coarse)
	{
		this.isCoarse = coarse;
		this.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
	

		JLabel volumicLabel = new JLabel("<html><b>Volumic fraction :</b></html>");
		volumicLabel.setToolTipText((this.isCoarse ? 
				"On the total volume of sample" : " Without the matrix and on the total volume of sample"));
		this.volumicFractionField = new FormattedLabelField(null, volumicLabel);
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		this.add(volumicFractionField,gbc);

		JLabel sizeLabel = new JLabel("<html><b>voxel size, in millimeter</b></html>");
		if(!this.isCoarse)
			sizeLabel.setToolTipText(" this size will be adapted to fit integraly in the coarse fraction voxel");
		this.voxelSizeField = new FormattedLabelField(null, sizeLabel);
		
		gbc.gridx = 1;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		this.add(voxelSizeField,gbc);

		ButtonGroup bg = new ButtonGroup();
		this.randomButton = new JRadioButton("Random");
		this.randomButton.setSelected(true);
		this.recurentButton = new JRadioButton("Recurent");
		bg.add(this.randomButton);
		bg.add(recurentButton);
		JLabel RRLabel = new JLabel("<html><b>Distribution of material in grain:</html></b>");
		RRLabel.setToolTipText("<html>check = Random grain composition in each voxel <br> uncheck = recurent grain composition in every voxels");
		RRLabel.setAlignmentX(CENTER_ALIGNMENT);
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		this.add(RRLabel,gbc);

		JPanel RRPanel = new JPanel();
		RRPanel.add(this.randomButton);
		RRPanel.add(this.recurentButton);
		RRPanel.setAlignmentX(CENTER_ALIGNMENT);
		gbc.gridx = 1;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		this.add(RRPanel,gbc);

		descOfSizeModel = new GrainTableModel(true);
		descOfEachSizeTab = new JTable(descOfSizeModel);
		descOfEachSizeTab.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		JPanel tableAndButton = new JPanel(new BorderLayout());
		(this.addSizeOfGrainButton = new JButton("+")).setMinimumSize(new Dimension(40,20));
		(this.removeSizeOfGrainButton = new JButton("-")).setMinimumSize(new Dimension(40,20));
		this.addSizeOfGrainButton.setFocusable(false);
		this.removeSizeOfGrainButton.setFocusable(false);

		JPanel buttonPan = new JPanel(new GridBagLayout());
		GridBagConstraints gbc2 = new GridBagConstraints();
		gbc2.gridx = 0; gbc2.gridy = 0; gbc2.gridheight = 1; gbc2.gridwidth = GridBagConstraints.REMAINDER;
		gbc2.fill = GridBagConstraints.HORIZONTAL;
		buttonPan.add(this.addSizeOfGrainButton,gbc2);
		gbc2.gridy = 1;
		buttonPan.add(this.removeSizeOfGrainButton,gbc2);
		gbc2.gridy = 2; gbc2.gridheight = 3;
		buttonPan.add(Box.createRigidArea(new Dimension(20,200)),gbc2);

		tableAndButton.add(buttonPan,BorderLayout.WEST);
		tableAndButton.add(new JScrollPane(descOfEachSizeTab));

		JTabbedPane grainFractionTab = new JTabbedPane();
		grainFractionTab.addTab("Grain size distribution",tableAndButton);

		descOfTypeModel = new GrainTableModel(false);
		descOfEachTypeTab = new JTable(descOfTypeModel);
		descOfEachTypeTab.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		grainFractionTab.addTab("Radioactive element contents",new JScrollPane(descOfEachTypeTab));

		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		this.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(),
				(isCoarse ? "COARSE " : "FINE ")+"FRACTION", TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION));

		grainFractionTab.setPreferredSize(new Dimension(580,300));
		this.add(grainFractionTab,gbc);
	}

	public void setControler(VolumicFractionFieldControler volumicFractionFieldControler,
			VoxelSizeFieldControler voxelSizeControler,
			GrainFractionControler grainFractionControler)
	{
		volumicFractionField.getField().addKeyListener(volumicFractionFieldControler);
		volumicFractionField.getField().addFocusListener(volumicFractionFieldControler);

		voxelSizeField.getField().addKeyListener(voxelSizeControler);
		voxelSizeField.getField().addFocusListener(voxelSizeControler);

		randomButton.addActionListener(grainFractionControler);
		randomButton.setActionCommand("random");
		recurentButton.addActionListener(grainFractionControler);
		recurentButton.setActionCommand("recurent");

		descOfSizeModel.addTableModelListener(grainFractionControler.getTableSizeControl());
		descOfTypeModel.addTableModelListener(grainFractionControler.getTableTypeControl());
		
		this.addSizeOfGrainButton.addActionListener(grainFractionControler);
		this.addSizeOfGrainButton.setActionCommand("add size of grain");
		this.removeSizeOfGrainButton.addActionListener(grainFractionControler);
		this.removeSizeOfGrainButton.setActionCommand("remove size of grain");
	}

	public int getSelectedSizeOfGrain()
	{
		return this.descOfEachSizeTab.getSelectedRow();
	}

	@Override
	public void update(Observable obs, Object arg1)
	{
		GrainFraction gf = (GrainFraction) obs;

		this.volumicFractionField.getField().setText(gf.getVolumicFraction()+"");
		this.volumicFractionField.isUpdated();
		this.voxelSizeField.getField().setText(gf.getVoxelSize()+"");
		this.voxelSizeField.isUpdated();
		
		DisablingTableListener.setActive(false);
		/**
		 * SIZE TAB UPDATE
		 */
		int nbType = gf.getNbTypeGrain();

		//COLUMN NAME VERIFICATION
		String[] colId;
		if(!isCoarse)
		{
			if(nbType != 0)
				colId = new String[nbType + 2 - 1]; //type N + Diameter + fraction column - matrix
			else
				colId = new String[2];
		}
		else
			colId = new String[nbType + 2]; // no matrix in coarse

		colId[0] = "Diameter";
		colId[1] = "Fraction";
		int typeId = 1;
		for(int numCol = 2; numCol < colId.length;numCol++)
		{
			colId[numCol] = "Type "+(typeId++);
		}
		this.descOfSizeModel.setColumnIdentifiers(colId);


		int nbRowInModel = gf.getDescOfEachSize().size();
		for(int i=0;i<nbRowInModel;i++)
		{
			//There is not a row
			if(i >= this.descOfSizeModel.getRowCount())
			{
				List<Float> value = gf.getDescOfEachSize().get(i);
				this.descOfSizeModel.addRow(new Vector<Float>(value));
			}
			else
			{
				for(int j=0; j<this.descOfSizeModel.getColumnCount(); j++)
				{
					float value = gf.getDescOfEachSize().get(i)
							.get(j);
					this.descOfSizeModel.setValueAt(value+"", i, j);
				}
			}
		}

		//REMOVE EXTRA ROWS
		int nbExtraRow = this.descOfSizeModel.getRowCount() - nbRowInModel;
		if(nbExtraRow < 0)
			System.err.println("ERROR update grain fraction panel");
		else if(nbExtraRow > 0)
		{
			for(int i=0;i<nbExtraRow;i++)
			{
				this.descOfSizeModel.removeRow(this.descOfSizeModel.getRowCount()-1 - i);
			}
		}

		/**
		 * TYPE TAB UPDATE
		 */
		int i;
		if(this.descOfEachTypeTab.getColumnCount() == 0)
		{
			Vector<String> vec = new Vector<String>();
			vec.add("% of K");
			vec.add("ppm of U series");
			vec.add("ppm of Th series"); vec.add("ppm of user-defined\radio element");
			descOfTypeModel.addColumn("", vec);
		}
		
		for(i=0;i<nbType;i++)
		{
			//If there is no column
			
			if(i >= this.descOfEachTypeTab.getColumnCount()-1)
			{
				String nameCol="";

				if(!isCoarse)
				{
					if(i==0)
						nameCol = "Matrix";
					else
						nameCol = "Type "+i;

					this.descOfTypeModel.addColumn(nameCol,
							new Vector<Float>(gf.getDescOfEachType().get(i)));
				}
				else
				{
					nameCol = "Type "+(i+1);
					this.descOfTypeModel.addColumn(nameCol,
							new Vector<Float>(gf.getDescOfEachType().get(i)));
				}
			}
			else
			{
				for(int row=0;row<4;row++)
				{
					float value = gf.getDescOfEachType().get(i).get(row);
					this.descOfTypeModel.setValueAt(value+"", row, i+1);
				}
			}
		}

		//remove extra columns
		i++;
		TableColumn col;
		for( ; i<this.descOfEachTypeTab.getColumnCount();i++)
		{
			col = this.descOfEachTypeTab.getColumnModel().getColumn(i);
			int columnModelIndex = col.getModelIndex();
			Vector<?> data = this.descOfTypeModel.getDataVector();
			Vector<?> colIds = this.descOfTypeModel.getColumnIdentifiers();

			this.descOfEachTypeTab.removeColumn(col);
			colIds.removeElementAt(columnModelIndex);

			for(int j=0;j<data.size();j++)
			{
				Vector<?> row = (Vector<?>) data.get(j);
				row.removeElementAt(columnModelIndex);
			}
			this.descOfTypeModel.setDataVector(data, colIds);

			Enumeration<TableColumn> enume = this.descOfEachTypeTab.getColumnModel().getColumns();
			while(enume.hasMoreElements())
			{
				TableColumn c = (TableColumn) enume.nextElement();
				if(c.getModelIndex() >= columnModelIndex)
					c.setModelIndex(c.getModelIndex()-1);
			}
		}
		this.descOfTypeModel.fireTableStructureChanged();
		
		DisablingTableListener.setActive(true);
	}
}
