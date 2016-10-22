package iramat.dosiseed.controler;



import iramat.dosiseed.model.AbstractModel;
import iramat.dosiseed.model.ColoredMaterial;
import iramat.dosiseed.model.Material;
import iramat.dosiseed.view.NorthMaterialPanel;
import iramat.dosiseed.view.SouthComponentPanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JTextField;

import mainPackage.Component;
import mainPackage.PeriodicTable;


import util.Couple;

public class ForgeControler implements ActionListener
{
	protected AbstractModel model;

	private int intCountNewComp;

	private SouthComponentPanel compFactory;

	protected int intCountNewMat;

	protected NorthMaterialPanel matFactory;

	public ForgeControler(AbstractModel model)
	{
		this.model = model;
		this.intCountNewMat = model.getListOfMaterial().size();
		this.intCountNewComp = model.getListOfComponent().size() - 18;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		Component currentComponentSouth = compFactory.getCurrentComponent();
		Material currentMaterial = matFactory.getCurrentMaterial();
		Component currentComponentNorth = matFactory.getSelectedComponent();
		Couple<Component,Float> currentComponentInMat = matFactory.getSelectedCompInMat();
		
		switch(e.getActionCommand())
		{
			/**
			 * COMPONENT FACTORY CONTROL
			 */
			case "create new component":
				final mainPackage.Component c = new mainPackage.Component("Unknown" + this.intCountNewComp++, false);
				model.addComponent(c);
				compFactory.addSetCurrentComponent(c);
				break;
			case "little button":
				if (currentComponentSouth != null && !currentComponentSouth.isBasic()) {
					currentComponentSouth.addAtomToFormula(PeriodicTable.valueOf(e.getSource().toString()));
					model.setChanged();
				}
				else 
					SouthComponentPanel.errorWindowAlteringBasic();
				break;
			case "Ok button nb atom":
				if (currentComponentSouth != null && !currentComponentSouth.isBasic()) {
					int nbAtom = 0;
					if((nbAtom = compFactory.getNbAtomLastElement()) != -1)
						currentComponentSouth.setNbOfLastAtom(nbAtom);
					model.setChanged();
				}
				else 
					SouthComponentPanel.errorWindowAlteringBasic();
				break;
			case "Clear formula":
				if (currentComponentSouth != null && !currentComponentSouth.isBasic()) {
					currentComponentSouth.clearChemicalFormula();
					model.setChanged();
				}
				else 
					SouthComponentPanel.errorWindowAlteringBasic();
				break;
			case "name":
				if (currentComponentSouth != null && !currentComponentSouth.isBasic()) {
					JTextField nameField = (JTextField) e.getSource();
					if (!nameField.getText().trim().isEmpty()) {
						currentComponentSouth.setName(nameField.getText().trim());
						model.setChanged();
					}
				}
				else 
					SouthComponentPanel.errorWindowAlteringBasic();
				break;
			case "density":
				if (currentComponentSouth != null && !currentComponentSouth.isBasic()) {
					try {
						currentComponentSouth.setDensity(Float.parseFloat(((JTextField)e.getSource()).getText().trim()));
						model.setChanged();
					}
					catch (NumberFormatException numberE) {
						SouthComponentPanel.numberOnly();
					}
				}
				else 
					SouthComponentPanel.errorWindowAlteringBasic();
				break;
				
			/**
			 * MATERIAL FACTORY CONTROL
			 */
			case "add component":
				
				if(currentComponentNorth != null && currentMaterial != null)
				{
					currentMaterial.addComponent(currentComponentNorth,
							100f - currentMaterial.getTotalMass());
					reloadCalculatedMassAndDensity(currentMaterial);
					model.setChanged();
				}
				break;
			case "name material":
				if(currentMaterial != null)
				{
					JTextField field = (JTextField) e.getSource();
					currentMaterial.setName(field.getText().trim());
					model.setChanged();
				}
				break;
			case "remove component":
				if(currentMaterial != null && currentComponentInMat != null)
				{	
					currentMaterial.removeComponent(currentComponentInMat.getValeur1());
					model.setChanged();
					this.matFactory.flushListCompInMat();
					reloadCalculatedMassAndDensity(currentMaterial);
				}
				break;
			case "use default density":
				JCheckBox useCalculatedDensity = (JCheckBox) e.getSource();
				matFactory.useCalculatedDensity(useCalculatedDensity.isSelected());
				if(currentMaterial == null)
					return;
				currentMaterial.setUseCalculated(useCalculatedDensity.isSelected());
				reloadCalculatedMassAndDensity(currentMaterial);
				model.setChanged();
				break;
			case "add material":
				this.addMaterial();
				break;
			case "delete material":
				this.removeMaterial(currentMaterial);
				break;
				default:
					break;
		}
			
		model.notifyObservers();
	}
	
	public void addMaterial()
	{
		Material m;
		int index = model.getListOfMaterial().size();
		model.addMaterial(m = new ColoredMaterial("Unknown_"+intCountNewMat++, 0f,index));
		this.matFactory.addSetCurrentMaterial(m);
	}
	
	public void removeMaterial(Material currentMaterial)
	{
		model.removeMaterial(currentMaterial);
		this.matFactory.flushListMaterial();
	}
	
	static void reloadCalculatedMassAndDensity(Material m)
	{
		float calculatedMass = 0.0f;
		float calculatedDensity = 0.0f;
        for (int k = 0; k < m.getListComponent().size(); ++k) {
            final float mass = m.getListComponent().get(k).getValeur2();
            calculatedDensity += m.getListComponent().get(k).getValeur1().getDensity() * mass / 100.0f;
            calculatedMass += mass;
        }
        m.setCalculatedDensity(calculatedDensity);
        m.setTotalMass(calculatedMass);
	}

	public void setComponentFactory(SouthComponentPanel factoryComponentPan)
	{
		this.compFactory = factoryComponentPan;
	}

	public void setMaterialFactory(NorthMaterialPanel factoryMaterialPan)
	{
		this.matFactory = factoryMaterialPan;
	}
}
