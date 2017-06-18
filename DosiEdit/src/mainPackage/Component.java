package mainPackage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

import util.Couple;

/**
 * A component object is basically a molecule. 
 * It is made of a formula describing a chemical composition, a name and a density. 
 * If a component is created by the {@link TopLevelInterface}, then the component is read-only.
 * @author alan
 * @see PeriodicTable
 * @see Material
 * @see Serializable
 */
public class Component implements Serializable
{
    private static final long serialVersionUID = 5104679666927455805L;
    private String Name;
    private formula ChemicalFormula;
    private float density;
    
    /**
     * true if the component is built-in, false if not.
     */
    public boolean basic;
    
    /**
     * Constructor. Create a new empty component with the name UnknownX, where X is a positive integer.
     * @param b if true, the component is read-only.
     */
    public Component(final boolean b) {
        this.Name = null;
        this.ChemicalFormula = new formula();
        this.density = 0.0f;
        this.basic = b;
    }
    
    /**
     * Constructor. Create a new empty component with the specified name.
     * @param name the specified name.
     * @param b if true, the component is read-only.
     */
    public Component(final String name, final boolean b) {
        if (b) {
            this.Name = name;
        }
        else {
            this.Name = "_" + name;
        }
        this.ChemicalFormula = new formula();
        this.density = 0.0f;
        this.basic = b;
    }
    
    /**
     * Constructor. Create a new empty component with the specified name and the specified density.
     * @param name the specified name.
     * @param dens the specified density.
     * @param b if true, the component is read-only.
     */
    public Component(final String name, final float dens, final boolean b) {
        if (b) {
            this.Name = name;
        }
        else {
            this.Name = "_" + name;
        }
        this.ChemicalFormula = new formula();
        this.density = dens;
        this.basic = b;
    }
    
    /**
     * @return the name of the component.
     */
    public String getName() {
        return this.Name;
    }
    
    /**
     * Set the name of the component.
     * @param name the new name to set.
     */
    public void setName(final String name) {
        if (this.isBasic()) {
            this.Name = name;
        }
        else if (name.startsWith("_")) {
            this.Name = name;
        }
        else {
            this.Name = "_" + name;
        }
    }
    
    /**
     * @return the density of the component.
     */
    public float getDensity() {
        return this.density;
    }
    
    /**
     * Set the density of the component.
     * @param dens the new density to set.
     */
    public void setDensity(final float dens) {
        this.density = dens;
    }
    
    /**
     * @return true if the component is basic, false if not.
     */
    public boolean isBasic() {
        return this.basic;
    }
    
    /**
     * Add one atom to the end of the formula.
     * @param lettre the atom to add.
     */
    public void addAtomToFormula(final PeriodicTable lettre) {
        if (this.ChemicalFormula.getLast() == null || lettre.toString() != this.ChemicalFormula.getLast().toString()) {
            this.ChemicalFormula.addAtom(lettre, 1);
        }
        else {
            this.ChemicalFormula.addSameThanLast();
        }
    }
    
    /**
     * Add the specified number of atom at the end of the formula.
     * @param lettre the atom to add.
     * @param nb the number of atom to add.
     */
    public void addAtomToFormula(final PeriodicTable lettre, final int nb) {
        this.ChemicalFormula.addAtom(lettre, nb);
    }
    
    /**
     * Set the last number in the formula.
     * @param nb the new number.
     */
    public void setNbOfLastAtom(final int nb) {
        if (this.ChemicalFormula.getLast() != null) {
            this.ChemicalFormula.changeNbLast(nb);
        }
    }
    
    /**
     * Delete the specified atom from the formula.
     * @param lettre the atom to delete.
     */
    public void deleteAtomFromFormula(final PeriodicTable lettre) {
        this.ChemicalFormula.deleteAtom(lettre);
    }
    
    /**
     * @return the formula as a string.
     */
    public String getFormulaAsString() {
        return this.ChemicalFormula.toString();
    }
    
    public String getFormulaAsPTFString()
    {
    	return this.ChemicalFormula.toPTFString();
    }
    
    /**
     * @return the formula as an ArrayList.
     */
    public ArrayList<Couple<PeriodicTable, Integer>> getFormulaAsList() {
        return this.ChemicalFormula.description;
    }
    
    /**
     * @return the number of different atom in the atom.
     */
    public int getNbAtomInTheFormula() {
        return this.ChemicalFormula.getNbAtomInIt();
    }
    
    /**
     * Clear the formula of the component.
     */
    public void clearChemicalFormula() {
        this.ChemicalFormula.clearFormula();
    }
    
    /**
     * Parse the String tab to a formula.
     * @param form The tab of string to parse.
     */
    public void StringToFomula(final String[] form) {
        final String Mendele\u00efv = PeriodicTable.getPeriodicString();
        for (int i = 0; i < form.length; ++i) {
            final String atomToDefine = form[i];
            if (Mendele\u00efv.contains(atomToDefine)) {
                final PeriodicTable atom = PeriodicTable.valueOf(atomToDefine);
                this.addAtomToFormula(atom, Integer.parseInt(form[++i]));
            }
        }
    }
    
    public String toPTFString()
    {
    	String result = "";
        result = String.valueOf(this.Name) + " " + this.density + " " + this.ChemicalFormula.getNbAtomInIt() + " "
        		+this.getFormulaAsPTFString().replaceAll("\\s+", " ");
        return result;
    }
    
    @Override
    public String toString() {
        String result = "";
        result = this.density + " " + String.valueOf(this.Name) + " " + this.ChemicalFormula.getNbAtomInIt() + " "
        		+this.getFormulaAsString().replaceAll("\\s+", " ");
        return result;
    }
    
    /**
     * Represents a chemical formula.
     * @author alan
     */
    private class formula implements Serializable
    {
        private static final long serialVersionUID = -7945180220537288622L;
        private ArrayList<Couple<PeriodicTable, Integer>> description;
        
        public formula() {
            this.description = new ArrayList<Couple<PeriodicTable, Integer>>();
        }
        
        public void addAtom(final PeriodicTable atom, final int nb) {
            final Couple<PeriodicTable, Integer> newAtom = new Couple<PeriodicTable, Integer>();
            newAtom.setValeur(atom, nb);
            this.description.add(newAtom);
        }
        
        public void deleteAtom(final PeriodicTable lettre) {
            final Iterator<Couple<PeriodicTable, Integer>> it = this.description.iterator();
            while (it.hasNext()) {
                final Couple<PeriodicTable, Integer> couple = it.next();
                if (couple.getValeur1() == lettre) {
                    it.remove();
                }
            }
        }
        
        public PeriodicTable getLast() {
            if (this.toString() == "") {
                return null;
            }
            return this.description.get(this.description.size() - 1).getValeur1();
        }
        
        public void addSameThanLast() {
            final int nb = this.description.get(this.description.size() - 1).getValeur2();
            this.description.get(this.description.size() - 1).setValeur2(nb + 1);
        }
        
        public void changeNbLast(final int nb) {
            this.description.get(this.description.size() - 1).setValeur2(nb);
        }
        
        public void clearFormula() {
            this.description.clear();
        }
        
        public int getNbAtomInIt() {
            return this.description.size();
        }
        
        public String toPTFString()
        {
        	String result = "";
            for (final Couple<PeriodicTable, Integer> couple : this.description) {
                result += " "+ couple.getValeur1()+" "+ couple.getValeur2()+" ";
            }
            return result;
        }
        
        @Override
        public String toString() {
            String result = "";
            for (final Couple<PeriodicTable, Integer> couple : this.description) {
                result += ""+couple.getValeur1()+couple.getValeur2();
            }
            return result;
        }
    }
}
