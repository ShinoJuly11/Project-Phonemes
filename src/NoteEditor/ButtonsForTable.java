package NoteEditor;

import java.awt.GridBagConstraints;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import MidiLogicEngine.MidiConstructor;

public class ButtonsForTable {

    Mediator mediator; 

    public ButtonsForTable(Mediator mediator){
        this.mediator = mediator;
    }

    public void process(){

        JFrame f = mediator.getJFrame();
        GridBagConstraints constraints = new GridBagConstraints();

        JPanel panel = new JPanel();
        JButton button = new JButton("Save to Class");

        button.addActionListener(e -> { 
            // add something later
            System.out.println("Table to Class:");
            mediator.convertTableToClass();

        });

        JButton button2 = new JButton("Playback");

        button2.addActionListener(e -> { 
            // add something later
            mediator.convertTableToClass();
            MidiConstructor engine = new MidiConstructor(mediator.getTableToClass(), 12);
            engine.process();
            engine.playback();

        });

        JButton button3 = new JButton("Class to Table");
        
        button3.addActionListener(e -> { 
            // add something later
            System.out.println("To be added soon");
        });

        constraints.weightx = 4;
        constraints.weighty = 1;
        constraints.gridx = 0;
        constraints.gridy = 1;

        panel.add(button);
        panel.add(button2);
        panel.add(button3);
        f.add(panel,constraints);
        
    }
    
}
