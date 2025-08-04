package NoteEditor;

import java.awt.GridBagLayout;
import javax.swing.JFrame;

public class globalFrame implements InterfaceGui {

    Mediator mediator;
    JFrame f;

    globalFrame(Mediator mediator){
        this.mediator = mediator;
    }

    public void process(){

        this.f = new JFrame();
        this.f.setTitle("Note Editor Ui");
        this.f.setSize(800,600);
        this.f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.f.setLayout(new GridBagLayout());

    }

    public JFrame getJFrame(){
        return this.f;
    }

    public void setVisible(){
        this.f.setVisible(true);
    }
    
}
