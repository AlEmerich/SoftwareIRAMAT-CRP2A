package iramat.dosiseed.model;

import java.io.Serializable;

public class ConfigRunModel implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 406106003230097895L;

	private String executable_path = "";
	
	private String pilot_file_path = "";
	
	private String argument_file_path = "";
	
	private boolean useCurrent = true;
	
	private boolean useArgs = false;

	/**
	 * @return the executable_path
	 */
	public String getExecutable_path()
	{
		return executable_path;
	}

	/**
	 * @param executable_path the executable_path to set
	 */
	public void setExecutable_path(String executable_path)
	{
		this.executable_path = executable_path;
	}

	/**
	 * @return the pilot_file_path
	 */
	public String getPilot_file_path()
	{
		return pilot_file_path;
	}

	/**
	 * @param pilot_file_path the pilot_file_path to set
	 */
	public void setPilot_file_path(String pilot_file_path)
	{
		this.pilot_file_path = pilot_file_path;
	}

	/**
	 * @return the argument_file_path
	 */
	public String getArgument_file_path()
	{
		return argument_file_path;
	}

	/**
	 * @param argument_file_path the argument_file_path to set
	 */
	public void setArgument_file_path(String argument_file_path)
	{
		this.argument_file_path = argument_file_path;
	}

	/**
	 * @return the useCurrent
	 */
	public boolean isUsingCurrent()
	{
		return useCurrent;
	}

	/**
	 * @param useCurrent the useCurrent to set
	 */
	public void setUseCurrent(boolean useCurrent)
	{
		this.useCurrent = useCurrent;
	}

	/**
	 * @return the useArgs
	 */
	public boolean isUsingArgs()
	{
		return useArgs;
	}

	/**
	 * @param useArgs the useArgs to set
	 */
	public void setUseArgs(boolean useArgs)
	{
		this.useArgs = useArgs;
	}
}
