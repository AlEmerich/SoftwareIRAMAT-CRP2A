package iramat.dosiseed.controler;

import iramat.dosiseed.model.AbstractModel;
import iramat.dosiseed.model.Incoherence;
import iramat.dosiseed.model.Model;
import iramat.dosiseed.view.LaunchConfigWindow;
import iramat.dosiseed.view.View;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;

import mainPackage.PrimaryParticles;
import mainPackage.SaveBox;

public class GlobalControler implements ActionListener
{
	/**
	 * Model of the project.
	 */
	private AbstractModel model;

	/**
	 * View of the project.
	 */
	private View view;

	private boolean preventToQuit = false;

	public GlobalControler(AbstractModel mod)
	{
		this.model = mod;	
	}

	public void setView(View v)
	{
		this.view = v;
	}

	/*************************************************************************
	 * 						USUAL LISTENER METHOD 
	 *************************************************************************/

	/**
	 * Function which load a serializable object indicates by the filename path. 
	 * This function is not generic, it loads a {@link SaveBox} object.
	 * @param filePath the path to the file to load.
	 */
	public void load(final String filePath)
	{
		final JDialog modalWaiting = new JDialog((Frame)view, "Please wait", true);
		final JLabel saveLab = new JLabel("Loading file...");
		modalWaiting.setSize(200, 80);
		modalWaiting.setLayout(new BorderLayout());
		modalWaiting.setLocationRelativeTo(view);
		modalWaiting.setResizable(false);
		modalWaiting.setDefaultCloseOperation(0);
		modalWaiting.add(saveLab);
		final Thread t = new Thread() {
			@Override
			public void run() {
				iramat.dosiseed.main.Main.main(new String[]{filePath});
				modalWaiting.setVisible(false);
			}
		};
		t.start();
		modalWaiting.setVisible(true);

		view.dispose();
	}

	/**
	 * Save the current project to the path.
	 * @param path the path where to save the file.
	 */
	public void save(final String path) {

		final Thread t = new Thread() {
			@Override
			public void run() {
				ObjectOutputStream oos = null;
				try {

					final File file = new File(path);
					oos = new ObjectOutputStream(new FileOutputStream(file));

					oos.writeObject(GlobalControler.this.model);
					GlobalControler.this.model.setCURRENT_PATH(file.getAbsolutePath());
				}
				catch (FileNotFoundException e) 
				{
					e.printStackTrace();
				}
				catch (IOException e2) 
				{
					e2.printStackTrace();
					JOptionPane.showMessageDialog(null, e2.getMessage(), "Error during the reading of the file", 0);
				}
				finally 
				{
					if (oos != null) {
						try {
							oos.close();
						}
						catch (IOException e4) {
							e4.printStackTrace();
							JOptionPane.showMessageDialog(null, e4.getMessage(), "Error during the closing of the file reader", 0);
						}
					}
				}
				if (oos != null) {
					try {
						oos.close();
					}
					catch (IOException e4) {
						e4.printStackTrace();
						JOptionPane.showMessageDialog(null, e4.getMessage(), "Error during the closing of the file reader", 0);
					}
				}

				JOptionPane.showMessageDialog(view, "Project saved !");
			}
		};
		t.start();
	}

	/**
	 * Ask to the user if he want to save the current project.
	 * @return true if the user has not canceled the action.
	 */
	private boolean requestSaveProject()
	{
		final int result = View.yesnocancel(view,"Do you want to save the current project ?");
		if (result == JOptionPane.OK_OPTION)
			this.actionPerformed(new ActionEvent(this, 0, "Save"));
		else if(result == JOptionPane.CANCEL_OPTION
				|| result == JOptionPane.CLOSED_OPTION)
			return false;
		return true;
	}

	public void Quit()
	{
		if(!preventToQuit)
		{
			view.dispose();
			System.exit(0);
		}
		else
			preventToQuit = false;

	}

	/*************************************************************************
	 * 						ACTION LISTENER METHOD 
	 *************************************************************************/
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		switch(e.getActionCommand())
		{
			/**
			 * MENU COMMAND
			 */
			case "New project":
				if(!requestSaveProject())
					return;
				model.reset();
				break;
			case "Load file":
				if(!requestSaveProject())
					return;
				String path = view.showLoadingWindow();
				if(!path.equals(""))
					this.load(path);
				break;
			case "Save":
				String saving = "";

				if(model.getCURRENT_PATH().isEmpty())
				{
					saving = view.showSavingWindow();
					if(saving.equals("CLOSED"))
					{
						preventToQuit = true;
						return;
					}
					this.save(saving);
				}
				else
					this.save(model.getCURRENT_PATH());
				break;
			case "Save as":
				String savingAs = view.showSavingWindow();
				if(savingAs.equals("CLOSED"))
				{
					preventToQuit = true;
					return;
				}
				this.save(savingAs);
				break;
			case "Generate":
				try
				{
					((Model) model).isValid();
				} catch (Incoherence e1)
				{
					View.showIncoherenceWindow(view, e1.whoIsGuilty(), e1.getMessage());
					return;
				}
				File file = DosiSeedWriterPTF.generate((Model) model, view.showGenerateWindow());
				if(file == null)
					return;
				int result = View.yesnocancel(view,"Open the PTF file to check it ? (Read only)");
				if(result == JOptionPane.OK_OPTION)
					view.openPTFFile(file);
				break;
			case "Run":
				try
				{
					((Model) model).isValid();
				} catch (Incoherence e1)
				{
					View.showIncoherenceWindow(view, e1.whoIsGuilty(), e1.getMessage());
					return;
				}
				launchDosiSeed(view,model);
				break;
			case "Quit":
				if(!requestSaveProject())
					return;
				this.Quit();
				break;
			case "Help":
				view.Help();
				break;

				/**
				 * MAIN INFO COMMAND
				 */
			case "type particle":
				JComboBox<?> source;
				if( e.getSource() instanceof JComboBox<?>)
					source = (JComboBox<?>) e.getSource();
				else
					return;
				model.setPrimaryParticle((PrimaryParticles) source.getSelectedItem());
				break;

			case "K boolean":
				boolean k = ((JCheckBox)e.getSource()).isSelected();
				model.setPotassiumSimulated(k);
				break;

			case "U boolean":
				boolean u = ((JCheckBox)e.getSource()).isSelected();
				model.setUraniumSimulated(u);
				break;

			case "Th boolean":
				boolean th = ((JCheckBox)e.getSource()).isSelected();
				model.setThoriumSimulated(th);
				break;

			case "Ud boolean":
				boolean ud = ((JCheckBox)e.getSource()).isSelected();
				model.setUserDefSimulated(ud);
				break;
			case "fixed medium":
				boolean fm = ((JRadioButton)e.getSource()).isSelected();
				model.setMedium(fm);
				break;
			case "infinite medium":
				boolean im = ((JRadioButton)e.getSource()).isSelected();
				model.setMedium(!im);
				break;
			default:
				break;
		}

		model.notifyObservers();
	}

	public static void launchDosiSeed(Component view,AbstractModel model)
	{
		LaunchConfigWindow window = new LaunchConfigWindow(view,model);
		window.setVisible(true);
	}
}
