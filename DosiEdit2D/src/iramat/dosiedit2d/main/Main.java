package iramat.dosiedit2d.main;

import iramat.dosiedit2d.controler.GlobalControler;
import iramat.dosiedit2d.model.Dosi2DModel;
import iramat.dosiedit2d.view.View;
import iramat.dosiseed.controler.ForgeControler;
import iramat.dosiseed.model.AbstractModel;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.*;

/**
 * The main class software.
 * 
 * @author alan
 * @since Summer 2016
 */
public class Main
{
	/**
	 * The main method of the software. The argument can be null or empty, if so
	 * the welcoming window will appear with two choices, load a project saved in disk or 
	 * create a new one.
	 * If there is an argument passed in parameter, it must be the path of the saved file with
	 * the .ddvi extension.
	 * @param args only the first case of the tab is used to store the path of the saved file.
	 */
	public static void main(String[] args)
	{
		try {
			UIManager.LookAndFeelInfo[] installedLookAndFeels;
			for (int length = (installedLookAndFeels = UIManager.getInstalledLookAndFeels()).length, i = 0; i < length; ++i) {
				final UIManager.LookAndFeelInfo info = installedLookAndFeels[i];
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		}
		catch (Exception e) {
			try {
				UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
			}
			catch (ClassNotFoundException ex) {}
			catch (InstantiationException ex2) {}
			catch (IllegalAccessException ex3) {}
			catch (UnsupportedLookAndFeelException ex4) {}
		}

		AbstractModel model = null;
		if(args.length != 0)
		{
			System.out.println(args[0]);
			model = load(args[0]);
			model.setCURRENT_PATH(args[0]);
			init(model);
		}
		else
		{
			intro("DosiEdit2D interface", "<html><div style='text-align: center;'><b>DosiEdit2D interface</b><br>" +
					"Editing Pilot Text File (PTF) for DosiVox 2D Software</html>");
			model = new Dosi2DModel();
		}

		model.setChanged();
		model.notifyObservers();
	}

	public static void intro(String titleframe, String labelhtml)
	{
		final JFrame startPanel = new JFrame(titleframe);
		startPanel.setPreferredSize(new Dimension(600,500));
		startPanel.setResizable(false);
		startPanel.setLocationRelativeTo(null);
		GridLayout layout = new GridLayout(0, 1);
		layout.setVgap(30);
		startPanel.setLayout(layout);
		JLabel title = new JLabel(labelhtml);

		title.setFont(new Font("Arial", Font.PLAIN, 40));
		title.setHorizontalAlignment(SwingConstants.CENTER);
		startPanel.add(title);
		JButton newProject = new JButton("Create a new project", View.createImageIcon("/resources/tab_new.png"));
		newProject.setFont(new Font("Arial",Font.PLAIN, 24));
		newProject.setIconTextGap(100);
		newProject.setHorizontalAlignment(SwingConstants.LEFT);
		newProject.addActionListener(e -> {
			Main.init(new Dosi2DModel());
			startPanel.dispose();
		});
		startPanel.add(newProject);

		JButton openProject = new JButton("Load a saved project", View.createImageIcon("/resources/Folder-Open.png"));
		openProject.setFont(new Font("Arial",Font.PLAIN, 24));
		openProject.setIconTextGap(100);
		openProject.setHorizontalAlignment(SwingConstants.LEFT);
		openProject.addActionListener(e -> {
			JFileChooser FileChooser = new JFileChooser();
			final FileNameExtensionFilter filter = new FileNameExtensionFilter("File saved by "+titleframe, new String[] { "ddvi" });
			FileChooser.setFileFilter(filter);

			final int returnVal = FileChooser.showOpenDialog(startPanel);
			if (returnVal == 0)
			{
				init(load(FileChooser.getSelectedFile().getAbsolutePath()));
				startPanel.dispose();
			}
		});
		startPanel.add(openProject);
		startPanel.pack();
		startPanel.validate();
		startPanel.setVisible(true);

	}

	/**
	 * Initialize and link the controller, the model and the view.
	 * @param model the model to work with.
	 */
	public static void init(AbstractModel model)
	{
		GlobalControler controler = new GlobalControler(model);
		ForgeControler forgeControler = new ForgeControler(model);
		View v = new View(forgeControler,controler,model);
		controler.setView(v);
	}

	/**
	 * Deserializes the .ddvi file to the AbstractModel field.
	 * @param path the path to the saved file in disk.
	 * @return the deserialized model.
	 */
	public static AbstractModel load(final String path)
	{
		ObjectInputStream ois = null;
		AbstractModel model = null;
		try {
			ois = new ObjectInputStream(new FileInputStream(new File(path)));
			model = (Dosi2DModel)ois.readObject();
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e2) {
			e2.printStackTrace();
			JOptionPane.showMessageDialog(null, e2.getMessage(), "Error during the reading of the file", 0);
		}
		catch (ClassNotFoundException e3) {
			e3.printStackTrace();
			JOptionPane.showMessageDialog(null, e3.getMessage(), "Class not found", 0);
		}
		finally {
			if (ois != null) {
				try {
					ois.close();
				}
				catch (IOException e4) {
					e4.printStackTrace();
					JOptionPane.showMessageDialog(null, e4.getMessage(), "Error during the closing of the file reader", 0);
				}
			}
		}
		if (ois != null) {
			try {
				ois.close();
			}
			catch (IOException e4) {
				e4.printStackTrace();
				JOptionPane.showMessageDialog(null, e4.getMessage(), "Error during the closing of the file reader", 0);
			}
		}
		return model;
	}
}
