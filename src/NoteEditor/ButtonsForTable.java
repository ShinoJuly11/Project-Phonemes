package NoteEditor;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class ButtonsForTable {

    Mediator mediator; 

    public ButtonsForTable(Mediator mediator){
        this.mediator = mediator;
    }

    public void process(){

        JFrame f = mediator.getJFrame();

        JPanel panel = new JPanel();
        JButton button = new JButton("Save to Class");

        button.addActionListener(e -> { 
            // add something later
            System.out.println("Table to Class:");
            mediator.convertTableToClass();

        });

        JButton button2 = new JButton("Class to Table");
        
        button2.addActionListener(e -> { 
            // add something later
            System.out.println("To be added soon");
        });

        panel.add(button);
        panel.add(button2);
        f.add(panel);
        
    }
    
}
