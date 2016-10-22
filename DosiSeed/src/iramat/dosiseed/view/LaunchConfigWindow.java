package iramat.dosiseed.view;

import iramat.dosiseed.controler.DosiSeedWriterPTF;
import iramat.dosiseed.main.Main;
import iramat.dosiseed.model.AbstractModel;
import iramat.dosiseed.model.ConfigRunModel;
import iramat.dosiseed.model.Model;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.border.TitledBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;


public class LaunchConfigWindow extends JFrame implements ActionListener, TreeSelectionListener
{
	private class FileNode
	{
		private File file;

		public FileNode(File f)
		{
			this.file = f;
		}

		public File getFile()
		{
			return file;
		}

		@Override
		public String toString()
		{
			return file.getName();
		}
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = -6133550981356947395L;

	private static final String EXEC_FILE_COMMAND = "exec";

	private static final String PILOT_FILE_COMMAND = "pilot";

	private static final String USE_CURRENT_COMMAND = "use current";

	private static final String ARGS_FILE_COMMAND = "args";

	private static final String USE_ARGS_COMMAND = "use args";

	private static final String LAUNCH_COMMAND = "launch";

	private static final String DEFAULT_USE_CURRENT = "default use current";

	private static final String DEFAULT_USE_ARGS = "default use arg";

	private JTabbedPane tab;

	private JFileChooser chooser;

	private Component parent;

	private AbstractModel model;

	private JTextField execField;
	private JButton execButton;

	private JTextField piloteField;
	private JButton piloteButton;
	private JCheckBox useCurrent;

	private JTextField argsField;
	private JButton argsButton;
	private JCheckBox useArgs;

	private JButton launchButton;

	/**
	 * DEFAULT 
	 */
	private File Dexec = null;

	private File Dpilot = null;
	private JTree dataFolderTree;
	private JCheckBox DuseCurrent;

	private File Darg = null;
	private JTree argFolderTree;
	private JCheckBox DuseArgs;

	/**
	 * 
	 * @param parent
	 * @param model
	 */
	public LaunchConfigWindow(Component parent,AbstractModel model)
	{
		super("Run configurations");

		this.tab = new JTabbedPane();
		this.chooser = new JFileChooser();
		this.model = model;

		this.setLocationRelativeTo(parent);
		this.setSize(400,400);
		this.parent = parent;

		String path = Main.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		String decodedPath="";
		try
		{
			decodedPath = URLDecoder.decode(path, "UTF-8");
		} catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		String root = (new File(decodedPath)).getParent();
		File dataLoc = new File(root+"/data/");
		File execLoc = new File(root+"/build/DosiSeed");

		this.initEnvironmentPanel(new File(root),dataLoc,execLoc,dataLoc.exists(), dataLoc.isDirectory(),
				execLoc.exists());
		this.initCustomPanel();

		this.add(this.tab);
	}

	private void initEnvironmentPanel(File root,File data,File exec,boolean dataExist,boolean dataIsDir,boolean execExist)
	{
		if(dataExist && dataIsDir && execExist)
		{
			JPanel env = new JPanel();
			JLabel pathExec = new JLabel("<html><b>Path executable: </b>"+exec.getAbsolutePath()+"</html>");
			this.Dexec = exec;
			pathExec.setPreferredSize(new Dimension(390,30));
			env.add(pathExec);

			this.DuseCurrent = new JCheckBox("Use current project as pilot");
			this.DuseCurrent.addActionListener(this);
			this.DuseCurrent.setActionCommand(DEFAULT_USE_CURRENT);
			env.add(DuseCurrent);

			DefaultMutableTreeNode dataTree = new DefaultMutableTreeNode(new FileNode(data), true);
			this.createChildren(data, dataTree,false);
			JScrollPane scrollData = new JScrollPane(this.dataFolderTree = new JTree(dataTree));

			scrollData.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), "Choose pilot text file"));
			scrollData.setPreferredSize(new Dimension(400,120));
			env.add(scrollData);

			this.dataFolderTree.addTreeSelectionListener(this);

			this.DuseArgs = new JCheckBox("Use argument file");
			this.DuseArgs.addActionListener(this);
			this.DuseArgs.setActionCommand(DEFAULT_USE_ARGS);
			env.add(DuseArgs);

			DefaultMutableTreeNode argTree = new DefaultMutableTreeNode(new FileNode(root), true);
			this.createChildren(root, argTree,true);
			JScrollPane scrollArg = new JScrollPane(this.argFolderTree = new JTree(argTree));
			scrollArg.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), "(Optional) Choose argument text file"));
			scrollArg.setPreferredSize(new Dimension(400,120));
			env.add(scrollArg);
			this.argFolderTree.addTreeSelectionListener(this);

			//button
			JButton lButton = new JButton("Launch DosiSeed");
			lButton.setPreferredSize(new Dimension(250,30));
			lButton.setActionCommand(LAUNCH_COMMAND);
			lButton.addActionListener(this);

			env.add(lButton);
			this.tab.addTab("Run in", env);

			this.DuseCurrent.setSelected(true);
			this.dataFolderTree.setEnabled(false);
			this.DuseArgs.setSelected(false);
			this.argFolderTree.setEnabled(false);
		}
		else
		{
			JPanel env = new JPanel();
			String message = "<html><b>Please put the DosiSeed interface <br>in the root of the DosiSeed software." +
					"<br>The workspace must have access to <br>build/, data/, results/ and spectres/<br>"+
					(dataExist ? "": "Error: Data directory doesn't exist.<br>")+
					((dataExist && !dataIsDir) ? "Error: Data is not a directory.<br>" : "")+
					(execExist ? "" : "Error: Executable not found in build/<br>")+"</b></html>";

			env.add(new JLabel(message));
			this.tab.addTab("Run in", env);
		}
	}

	private void createChildren(File root,DefaultMutableTreeNode rootTree,boolean args)
	{
		if(root == null || !root.isDirectory())
			return;

		for(File file : root.listFiles())
		{
			if(file != null && !file.isHidden())
			{
				DefaultMutableTreeNode node = new DefaultMutableTreeNode(new FileNode(file));
				if(args)
				{
					if(file.getName().equals("visAF.mac") 
							|| file.getName().equals("visCF.mac") 
							|| file.getName().equals("visFF.mac"))
						rootTree.add(node);
				}
				else
					rootTree.add(node);

				if(file.isDirectory())
					createChildren(file, node,args);
			}
		}
	}

	private void initCustomPanel()
	{
		JPanel cus = new JPanel(new GridBagLayout());
		//exec
		JPanel execPan = new JPanel();
		execPan.setLayout(new BoxLayout(execPan, BoxLayout.PAGE_AXIS));
		execPan.setPreferredSize(new Dimension(350,100));
		execPan.add(new JLabel("Executable path:"));
		execField = new JTextField();
		execField.setPreferredSize(new Dimension(250, 30));
		JPanel fieldButtonE = new JPanel();
		fieldButtonE.add(execField);
		execButton = new JButton("...");
		execButton.setPreferredSize(new Dimension(30,30));
		execButton.setActionCommand(EXEC_FILE_COMMAND);
		execButton.addActionListener(this);
		fieldButtonE.add(execButton);
		execPan.add(fieldButtonE);
		execPan.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createRaisedBevelBorder(), "Choose executable",
				TitledBorder.LEFT, TitledBorder.LEFT));

		//pilot
		JPanel pilotePan = new JPanel();
		pilotePan.setLayout(new BoxLayout(pilotePan, BoxLayout.PAGE_AXIS));
		pilotePan.setPreferredSize(new Dimension(350,100));
		useCurrent = new JCheckBox("Use current project as pilot");
		useCurrent.setPreferredSize(new Dimension(250,30));
		useCurrent.setActionCommand(USE_CURRENT_COMMAND);
		useCurrent.addActionListener(this);
		pilotePan.add(useCurrent);
		pilotePan.add(new JLabel("Pilot file path"));
		piloteField = new JTextField();
		piloteField.setPreferredSize(new Dimension(250, 30));
		JPanel fieldButtonP = new JPanel();
		fieldButtonP.add(piloteField);
		piloteButton = new JButton("...");
		piloteButton.setPreferredSize(new Dimension(30,30));
		piloteButton.setActionCommand(PILOT_FILE_COMMAND);
		piloteButton.addActionListener(this);
		fieldButtonP.add(piloteButton);
		pilotePan.add(fieldButtonP);
		pilotePan.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createRaisedBevelBorder(), "Choose pilot file",
				TitledBorder.LEFT, TitledBorder.LEFT));

		//args
		JPanel argsPan = new JPanel();
		argsPan.setLayout(new BoxLayout(argsPan,BoxLayout.PAGE_AXIS));
		argsPan.setPreferredSize(new Dimension(350,100));
		useArgs = new JCheckBox("Use argument file");
		useArgs.setPreferredSize(new Dimension(250,30));
		useArgs.setActionCommand(USE_ARGS_COMMAND);
		useArgs.addActionListener(this);
		argsPan.add(useArgs);
		argsPan.add(new JLabel("(Optional) Choose argument file path:"));
		argsField = new JTextField();
		argsField.setPreferredSize(new Dimension(250, 30));
		JPanel fieldButtonA = new JPanel();
		fieldButtonA.add(argsField);
		argsButton = new JButton("...");
		argsButton.setPreferredSize(new Dimension(30,30));
		argsButton.setActionCommand(ARGS_FILE_COMMAND);
		argsButton.addActionListener(this);
		fieldButtonA.add(argsButton);
		argsPan.add(fieldButtonA);
		argsPan.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createRaisedBevelBorder(), "Choose argument file",
				TitledBorder.LEFT, TitledBorder.LEFT));

		//button
		launchButton = new JButton("Launch DosiSeed");
		launchButton.setPreferredSize(new Dimension(250,30));
		launchButton.setActionCommand(LAUNCH_COMMAND);
		launchButton.addActionListener(this);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.gridheight = 1;
		cus.add(execPan,gbc);
		gbc.gridy = 1;
		cus.add(pilotePan,gbc);
		gbc.gridy = 2;
		cus.add(argsPan,gbc);
		gbc.gridy = 3;
		cus.add(launchButton,gbc);
		this.setResizable(false);

		this.tab.addTab("Run out", cus);
		initCustomValues(model.getConfig());
	}

	private void initCustomValues(ConfigRunModel config)
	{
		this.execField.setText(config.getExecutable_path());
		this.piloteField.setText(config.getPilot_file_path());
		this.argsField.setText(config.getArgument_file_path());

		this.useCurrent.setSelected(config.isUsingCurrent());
		this.useArgs.setSelected(config.isUsingArgs());

		if(useCurrent.isSelected())
		{
			this.piloteButton.setEnabled(false);
			this.piloteField.setEnabled(false);
		}
		else
		{
			this.piloteButton.setEnabled(true);
			this.piloteField.setEnabled(true);
		}

		if(!useArgs.isSelected())
		{
			this.argsButton.setEnabled(false);
			this.argsField.setEnabled(false);
		}
		else
		{
			this.argsButton.setEnabled(true);
			this.argsField.setEnabled(true);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		ConfigRunModel config = model.getConfig();
		switch(e.getActionCommand())
		{
			case DEFAULT_USE_CURRENT:

				if(DuseCurrent.isSelected())
				{
					this.dataFolderTree.setEnabled(false);
				}
				else
				{
					this.dataFolderTree.setEnabled(true);
				}
				break;
			case DEFAULT_USE_ARGS:

				if(DuseArgs.isSelected())
				{
					this.argFolderTree.setEnabled(true);
				}
				else
				{
					this.argFolderTree.setEnabled(false);
				}
				break;
			case USE_CURRENT_COMMAND:
				config.setUseCurrent(useCurrent.isSelected());
				if(useCurrent.isSelected())
				{
					this.piloteButton.setEnabled(false);
					this.piloteField.setEnabled(false);
				}
				else
				{
					this.piloteButton.setEnabled(true);
					this.piloteField.setEnabled(true);
				}
				break;
			case USE_ARGS_COMMAND:
				config.setUseArgs(useArgs.isSelected());
				if(!useArgs.isSelected())
				{
					this.argsButton.setEnabled(false);
					this.argsField.setEnabled(false);
				}
				else
				{
					this.argsButton.setEnabled(true);
					this.argsField.setEnabled(true);
				}
				break;
			case EXEC_FILE_COMMAND:
				String pathexec="";
				final int returnVal = this.chooser.showDialog(this,"Choose executable");
				if (returnVal == 0)
				{
					pathexec = this.chooser.getSelectedFile().getAbsolutePath();
					execField.setText(pathexec);
					config.setExecutable_path(pathexec);
				}
				break;
			case PILOT_FILE_COMMAND:
				String pathpilot="";
				final int returnVal2 = this.chooser.showDialog(this,"Choose pilot");
				if (returnVal2 == 0)
				{
					pathpilot = this.chooser.getSelectedFile().getAbsolutePath();
					piloteField.setText(pathpilot);
					config.setPilot_file_path(pathpilot);
				}
				break;
			case ARGS_FILE_COMMAND:
				String pathargs="";
				final int returnVal3 = this.chooser.showDialog(this,"Choose pilot");
				if (returnVal3 == 0)
				{
					pathargs = this.chooser.getSelectedFile().getAbsolutePath();
					argsField.setText(pathargs);
					config.setArgument_file_path(pathargs);
				}
				break;
			case LAUNCH_COMMAND:
				if(this.tab.getSelectedIndex() == 1 && !checkExecutable())
				{
					JOptionPane.showMessageDialog(parent, 
							"Executable path is missing or is not valid.");
					return;
				}
				if(!checkPilot())
				{
					JOptionPane.showMessageDialog(parent, 
							"pilot file path is missing or is not valid.");
					return;
				}
				if(!checkArgs())
				{
					JOptionPane.showMessageDialog(parent, 
							"Arguments is not a valid file. Must be visAF.mac, visCF.mac or visFF.mac.");
					return;
				}

				File executable,pilot,argument;
				boolean useC,useA;

				if(this.tab.getSelectedIndex() == 0)
				{
					executable = Dexec;
					pilot = Dpilot;
					argument = Darg;
					useC = DuseCurrent.isSelected();
					useA = DuseArgs.isSelected();
				}
				else
				{
					executable = new File(config.getExecutable_path());
					pilot = new File(config.getPilot_file_path());
					argument = new File(config.getArgument_file_path());
					useC = config.isUsingCurrent();
					useA = config.isUsingArgs();
				}

				launch(executable,pilot,argument,useC,useA);

				break;
			default:
				break;
		}
	}

	private void launch(File executable, File pilot, File argument, boolean useC, boolean useA)
	{

		String command = executable.getParent()+"/./"+executable.getName();

		String pilotS = "";
		File file = null;
		if(!useC)
			pilotS = pilot.getAbsolutePath();
		else
		{
			file = DosiSeedWriterPTF.generate((Model) model, "tmp.txt");
			pilotS = file.getAbsolutePath();
		}
		String args = "";
		if(useA)
			args = argument.getAbsolutePath();

		String script = "#!/bin/bash \n " +
				command+" " +
				pilotS+" " +
				args+"\n"+
				"$SHELL";
		try
		{
			String[] comm = new String[]{"xterm","-e",script + " ; le_exec" };

			ProcessBuilder proc = new ProcessBuilder(comm);
			Process process =proc.start();
			process.waitFor();
			if(useC && file != null)
			{
				file.delete();
			}
		} catch (Exception e1)
		{
			try
			{
				String[] comm = new String[]{"gnome_terminal","-e",script + " ; le_exec" };

				ProcessBuilder proc = new ProcessBuilder(comm);
				Process process =proc.start();
				process.waitFor();
				if(useC && file != null)
				{
					file.delete();
				}
			}catch(InterruptedException e2)
			{
				JOptionPane.showMessageDialog(parent, "Error when trying to run DosiSeed. Please try to run manually. "
						+e2.getMessage());
				e1.printStackTrace();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}

	private boolean checkExecutable()
	{
		File file = new File(execField.getText().trim());
		if(!file.isFile() || execField.getText().trim().equals(""))
			return false;
		if(!file.canExecute())
			file.setExecutable(true);

		model.getConfig().setExecutable_path(file.getAbsolutePath());
		return true;
	}

	private boolean checkPilot()
	{
		if(this.tab.getSelectedIndex() == 1 && !useCurrent.isSelected())
		{
			File file = new File(piloteField.getText().trim());
			if(!file.isFile() || piloteField.getText().trim().equals(""))
				return false;

			model.getConfig().setPilot_file_path(file.getAbsolutePath());
		}
		else if(this.tab.getSelectedIndex() == 0)
		{
			if(!DuseCurrent.isSelected() && !Dpilot.isFile())
				return false;
		}
		return true;
	}

	private boolean checkArgs()
	{
		if(this.tab.getSelectedIndex() == 1)
		{
			if(useArgs.isSelected())
			{
				File file = new File(argsField.getText().trim());
				if(!file.isFile())
					return false;
				if(!file.getName().equals("visAF.mac") 
						&& !file.getName().equals("visCF.mac") 
						&& !file.getName().equals("visFF.mac") )
					return false;

				model.getConfig().setArgument_file_path(file.getAbsolutePath());
			}
		}
		else
		{
			if(DuseArgs.isSelected())
			{
				if(Darg == null || !Darg.isFile())
					return false;
			}
		}

		return true;
	}

	@Override
	public void valueChanged(TreeSelectionEvent e)
	{
		if(e.getSource() == this.dataFolderTree)
		{
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) this.dataFolderTree.getLastSelectedPathComponent();
			this.Dpilot = ((FileNode) node.getUserObject()).getFile();
		}
		else if(e.getSource() == this.argFolderTree)
		{
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) this.argFolderTree.getLastSelectedPathComponent();
			this.Darg = ((FileNode) node.getUserObject()).getFile();
		}
	}
}
