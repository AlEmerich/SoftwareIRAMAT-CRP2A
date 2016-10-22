package iramat.dosiseed.main;

import iramat.dosiseed.controler.ForgeControler;
import iramat.dosiseed.controler.GlobalControler;
import iramat.dosiseed.model.AbstractModel;
import iramat.dosiseed.model.ColoredMaterial;
import iramat.dosiseed.model.Material;
import iramat.dosiseed.model.Model;
import iramat.dosiseed.view.View;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileNameExtensionFilter;


public class Main {

	/**
     * The very first function of the software.
     * @param args the argument of the software.
     */
    public static void main(final String[] args) {
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
        	final JFrame startPanel = new JFrame("DosiSeed interface");
        	startPanel.setPreferredSize(new Dimension(600,500));
        	startPanel.setResizable(false);
        	startPanel.setLocationRelativeTo(null);
        	GridLayout layout = new GridLayout(0, 1);
        	layout.setVgap(30);
        	startPanel.setLayout(layout);
        	JLabel title = new JLabel("<html><div style='text-align: center;'><b>DosiSeed interface</b><br>" +
        			"Dosimetry Sedimentary elements distribution</html>");
        	
        	title.setFont(new Font("Arial", Font.PLAIN, 40));
        	title.setHorizontalAlignment(SwingConstants.CENTER);
        	startPanel.add(title);
        	JButton newProject = new JButton("Create a new project", View.createImageIcon("/resources/tab_new.png"));
        	newProject.setFont(new Font("Arial",Font.PLAIN, 24));
        	newProject.setIconTextGap(100);
        	newProject.setHorizontalAlignment(SwingConstants.LEFT);
        	newProject.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					Main.init(new Model());
					startPanel.dispose();
				}
			});
        	startPanel.add(newProject);
        	
        	JButton openProject = new JButton("Load a saved project", View.createImageIcon("/resources/Folder-Open.png"));
        	openProject.setFont(new Font("Arial",Font.PLAIN, 24));
        	openProject.setIconTextGap(100);
        	openProject.setHorizontalAlignment(SwingConstants.LEFT);
        	openProject.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					JFileChooser FileChooser = new JFileChooser();
					final FileNameExtensionFilter filter = new FileNameExtensionFilter(null, new String[] { "cf", "CF" });
			        FileChooser.setFileFilter(filter);
			        
			        final int returnVal = FileChooser.showOpenDialog(startPanel);
			        if (returnVal == 0)
			        {
			        	init(load(FileChooser.getSelectedFile().getAbsolutePath()));
			        	startPanel.dispose();
			        }
			        
			        
				}
			});
        	startPanel.add(openProject);
        	startPanel.pack();
        	startPanel.validate();
        	startPanel.setVisible(true);
        	model = new Model();
        }
    }
    
    public static void init(AbstractModel model)
    {
    	GlobalControler controler = new GlobalControler(model);
    	ForgeControler forgeControler = new ForgeControler(model){
    		@Override
    		public void addMaterial()
    		{
    			Material m;
    			int index = model.getListOfMaterial().size();
    			m = new ColoredMaterial("Type"+this.intCountNewMat++, 0f,index);
    			((Model) model).addTypeOfGrain(m);
    			this.matFactory.addSetCurrentMaterial(m);
    		}
    		
    		@Override
    		public void removeMaterial(Material currentMaterial)
    		{
    			((Model) model).removeTypeOfGrain(model.getCompositionOfGrains().indexOf(currentMaterial));
    			this.matFactory.flushListMaterial();
    		}
    	};
    	View v = new View(forgeControler,controler,model);
    	controler.setView(v);

    	model.setChanged();
    	model.notifyObservers();
    }
    
    public static AbstractModel load(final String path)
    {
    	ObjectInputStream ois = null;
    	AbstractModel model = null;
        try {
            ois = new ObjectInputStream(new FileInputStream(new File(path)));
            model = (Model)ois.readObject();
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
