package NoteEditor;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Taskbar;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenuBar;

public class GlobalFrame{

    Mediator mediator;
    JFrame f;

    public GlobalFrame(Mediator mediator){
        this.mediator = mediator;
        process();
    }

    public void process(){
        
        this.f = new JFrame();
        this.f.setTitle("Note Editor Ui");

        try{

            File iconImage = new File("Photos/DJMiku.png");

            //for windows
            this.f.setIconImage(ImageIO.read(iconImage));

            //for MacOs
            Taskbar taskbar = Taskbar.getTaskbar();
            taskbar.setIconImage(ImageIO.read(iconImage));

        }
        catch(IOException e){
            e.printStackTrace();
            System.out.println("Picture error in " + this.getClass().getName());
        }
        finally{ // if theres no icon detected it should just show the java mascot
            
            this.f.setSize(1200,600);
            this.f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            this.f.setLayout(new GridBagLayout());
        }

        // well i hate my life being this convoluted in ui

    }


    public void addJComponent(JComponent component, GridBagConstraints gbc){
        f.add(component,gbc);
        
    }

    public void setMenuBar(JMenuBar menu){
        f.setJMenuBar(menu);
    }

    public JFrame getJFrame(){
        return this.f;
    }

    public void setVisible(){
        this.f.setVisible(true);
    }
    
}
