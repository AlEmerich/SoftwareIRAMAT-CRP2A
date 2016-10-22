package iramat.dosiseed.model;

public class Incoherence extends Exception
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 477131341768087612L;

	public enum Inc
	{
		WF_UP("Error in water fraction"),
		VOLUMIC_FRACTION_UP("Error in volumic fraction: "),
		COARSE_FRACTION_SIZE_TAB("Error in coarse fraction size table"),
		COARSE_FRACTION_TYPE_TAB("Error in coarse fraction type table"),
		FINE_FRACTION_SIZE_TAB("Error in fine fraction size table"),
		FINE_FRACTION_TYPE_TAB("Error in fine fraction type table"),
		FINE_COARSE_SIZE("Error between sizes of each type"),
		MATERIAL("Error in material"),
		WORLD("Error in the grid");
		
		private final String description;
		
		private Inc(String s)
		{
			description = s;
		}

		/**
		 * @return the description
		 */
		public String getDescription()
		{
			return description;
		}
	}
	
	private final Inc guilty;
	
	public Incoherence(Inc type,String message)
	{
		super(type.getDescription() +": "+ message);
		this.guilty = type;
	}
	
	public Inc whoIsGuilty()
	{
		return guilty;
	}
}
