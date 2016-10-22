package mainPackage;

/**
 * Enum class providing the list of atom present in the periodic table.
 * @author alan
 */
public enum PeriodicTable
{
    H("H", 0, "Hydrogen", 1.00794f), 
    He("He", 1, "Helium", 4.0002f), 
    Li("Li", 2, "Lithium", 6.941f), 
    Be("Be", 3, "Berylium", 9.0122f), 
    B("B", 4, "Boron", 10.611f), 
    C("C", 5, "Carbon", 12.011f), 
    N("N", 6, "Nitrogen", 14.007f), 
    O("O", 7, "Oxygen", 15.999f), 
    F("F", 8, "Fluorine", 18.998f), 
    Ne("Ne", 9, "Neon", 20.18f), 
    Na("Na", 10, "Sodium", 22.99f), 
    Mg("Mg", 11, "Magnesium", 24.305f), 
    Al("Al", 12, "Aluminium", 26.982f), 
    Si("Si", 13, "Silicon", 28.086f), 
    P("P", 14, "Phosphorus", 3.974f), 
    S("S", 15, "Sulfur", 32.065f), 
    Cl("Cl", 16, "Chlorine", 35.453f), 
    Ar("Ar", 17, "Argon", 39.948f), 
    K("K", 18, "Potassium", 39.098f), 
    Ca("Ca", 19, "Calcium", 40.078f), 
    Sc("Sc", 20, "Scandium", 44.956f), 
    Ti("Ti", 21, "Titanium", 47.867f), 
    V("V", 22, "Vanadium", 50.942f), 
    Cr("Cr", 23, "Chromium", 51.996f), 
    Mn("Mn", 24, "Manganese", 54.938f), 
    Fe("Fe", 25, "Iron", 55.845f), 
    Co("Co", 26, "Cobalt", 58.933f), 
    Ni("Ni", 27, "Nickel", 58.693f), 
    Cu("Cu", 28, "Copper", 63.546f), 
    Zn("Zn", 29, "Zinc", 65.39f), 
    Ga("Ga", 30, "Gallium", 69.732f), 
    Ge("Ge", 31, "Germanium", 72.61f), 
    As("As", 32, "Arsenic", 74.922f), 
    Se("Se", 33, "Selenium", 78.972f), 
    Br("Br", 34, "Bromine", 79.904f), 
    Kr("Kr", 35, "Krypton", 84.8f), 
    Rb("Rb", 36, "Rubidium", 84.468f), 
    Sr("Sr", 37, "Strontium", 87.62f), 
    Y("Y", 38, "Yttrium", 88.906f), 
    Zr("Zr", 39, "Zirconium", 91.224f), 
    Nb("Nb", 40, "Niobium", 92.906f), 
    Mo("Mo", 41, "Molybdenum", 95.95f), 
    Tc("Tc", 42, "Technetium", 98.907f), 
    Ru("Ru", 43, "Ruthenium", 101.07f), 
    Rh("Rh", 44, "Rhodium", 102.906f), 
    Pd("Pd", 45, "Palladium", 106.42f), 
    Ag("Ag", 46, "Silver", 107.868f), 
    Cd("Cd", 47, "Cadnium", 112.411f), 
    In("In", 48, "Indium", 114.818f), 
    Sn("Sn", 49, "Tin", 118.71f), 
    Sb("Sb", 50, "Antimony", 121.76f), 
    Te("Te", 51, "Tellurium", 127.6f), 
    I("I", 52, "Iodine", 126.904f), 
    Xe("Xe", 53, "Xenon", 131.29f), 
    Cs("Cs", 54, "Cesium", 132.905f), 
    Ba("Ba", 55, "Barium", 137.327f), 
    La("La", 56, "Lanthanum", 138.906f), 
    Ce("Ce", 57, "Cerium", 140.115f), 
    Pr("Pr", 58, "Praseodymium", 14.908f), 
    Nd("Nd", 59, "Neodymium", 144.24f), 
    Pm("Pm", 60, "Promethium", 144.913f), 
    Sm("Sm", 61, "Samarium", 150.36f), 
    Eu("Eu", 62, "Europium", 151.966f), 
    Gd("Gd", 63, "Gadolinium", 157.25f), 
    Tb("Tb", 64, "Terbium", 158.925f), 
    Dy("Dy", 65, "Dysprosium", 162.5f), 
    Ho("Ho", 66, "Holmium", 164.93f), 
    Er("Er", 67, "Erbium", 167.26f), 
    Tm("Tm", 68, "Thulium", 168.934f), 
    Yb("Yb", 69, "Ytterbium", 173.04f), 
    Lu("Lu", 70, "Lutetium", 174.967f), 
    Hf("Hf", 71, "Hafnium", 178.79f), 
    Ta("Ta", 72, "Tantalum", 180.948f), 
    W("W", 73, "Tungsten", 183.85f), 
    Re("Re", 74, "Rhenium", 186.207f), 
    Os("Os", 75, "Osmium", 190.23f), 
    Ir("Ir", 76, "Iridium", 192.22f), 
    Pt("Pt", 77, "Platinum", 195.08f), 
    Au("Au", 78, "Gold", 196.967f), 
    Hg("Hg", 79, "Mercury", 200.59f), 
    Tl("Tl", 80, "Thallium", 204.383f), 
    Pb("Pb", 81, "Lead", 207.2f), 
    Bi("Bi", 82, "Bismuth", 208.98f), 
    Po("Po", 83, "Polonium", 208.982f), 
    At("At", 84, "Astatine", 209.987f), 
    Rn("Rn", 85, "Radon", 222.018f), 
    Fr("Fr", 86, "Francium", 223.02f), 
    Ra("Ra", 87, "Radium", 226.025f), 
    Ac("Ac", 88, "Actinium", 227.028f), 
    Th("Th", 89, "Thorium", 232.038f), 
    Pa("Pa", 90, "Protactinium", 231.036f), 
    U("U", 91, "Uranium", 238.029f), 
    Np("Np", 92, "Neptunium", 237.048f), 
    Pu("Pu", 93, "Plutonium", 244.064f), 
    Am("Am", 94, "Americium", 243.061f), 
    Cm("Cm", 95, "Curium", 247.07f), 
    Bk("Bk", 96, "Berkelium", 247.07f), 
    Cf("Cf", 97, "Californium", 251.08f), 
    Es("Es", 98, "Einsteinium", 254.0f), 
    Fm("Fm", 99, "Fermium", 257.095f), 
    Md("Md", 100, "Mendelevium", 258.1f), 
    No("No", 101, "Nobelium", 259.101f), 
    Lr("Lr", 102, "Lawrencium", 262.0f), 
    Rf("Rf", 103, "Rutherfordium", 261.0f), 
    Db("Db", 104, "Dubnium", 262.0f), 
    Sg("Sg", 105, "Seaborgium", 266.0f), 
    Bh("Bh", 106, "Bohrium", 264.0f), 
    Hs("Hs", 107, "Hassium", 269.0f), 
    Mt("Mt", 108, "Meitnerium", 268.0f), 
    Ds("Ds", 109, "Darmstadtium", 269.0f), 
    Rg("Rg", 110, "Roentgenium", 272.0f), 
    Cn("Cn", 111, "Copernicium", 277.0f), 
    Uut("Uut", 112, "Ununtrium", -1.0f), 
    Fl("Fl", 113, "Flerovium", 289.0f), 
    Uup("Uup", 114, "Ununpentium", -1.0f), 
    Lv("Lv", 115, "Livermorium", 298.0f), 
    Uus("Uus", 116, "Ununseptium", -1.0f), 
    Uuo("Uuo", 117, "Ununoctium", -1.0f);
    
    private String ScientificName;
    private float AtomicMass;
    private static String strMendele\u00efv;
    
    static {
        PeriodicTable.strMendele\u00efv = "H,He,Li,Be,B,C,N,O,F,Ne,Na,Mg,Al,Si,P,S,Cl,Ar,K,Ca,Sc,Ti,V,Cr,Mn,Fe,Co,Ni,Cu,Zn,Ga,Ge,As,Se,Br,Kr,Rb,Sr,Y,Zr,Nb,Mo,Tc,Ru,Rh,Pd,Ag,Cd,In,Sn,Sb,Te,I,Xe,Cs,Ba,La,Ce,Pr,Nd,Pm,Sm,Eu,Gd,Tb,Dy,Ho,Er,Tm,Yb,Lu,Hf,Ta,W,Re,Os,Ir,Pt,Au,Hg,Tl,Pb,Bi,Po,At,Rn,Fr,Ra,Ac,Th,Pa,U,Np,Pu,Am,Cm,Bk,Cf,Es,Fm,Md,No,Lr,Rf,Db,Sg,Bh,Hs,Mt,Ds,Rg,Cn,Uut,Fl,Uup,Lv,Uus,Uuo";
    }
    
    private PeriodicTable(final String name2, final int ordinal, final String name, final float mass) {
        this.ScientificName = name;
        this.AtomicMass = mass;
    }
    
    public String getScientificName() {
        return this.ScientificName;
    }
    
    public float getAtomicMass() {
        return this.AtomicMass;
    }
    
    public static String getPeriodicString() {
        return PeriodicTable.strMendele\u00efv;
    }
}
