package mainPackage;

public class ScanException extends Exception
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3854946741595612837L;

	public ScanException(String message)
	{
		System.err.println(message);
	}
}
