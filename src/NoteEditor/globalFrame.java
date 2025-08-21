package NoteEditor;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

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
        this.f.setSize(1200,600);
        this.f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.f.setLayout(new GridBagLayout());

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
