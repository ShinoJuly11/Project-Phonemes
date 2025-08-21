package NoteEditor.PlaybackButtons;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import MidiLogicEngine.MidiConstructor;
import NoteEditor.InterfaceGui;
import NoteEditor.Mediator;

public class ButtonsForTable implements InterfaceGui{

    Mediator mediator; 
    JPanel panel;
    JMenuBar menuBar;
    GridBagConstraints constraints;

    public ButtonsForTable(Mediator mediator){
        this.mediator = mediator;
        process();
    }

    public GridBagConstraints getConstraints(){
        return this.constraints;
    }

    public JComponent getMainComponent(){
        return this.menuBar;
    }

    public void process(){
        MidiConstructor midi = new MidiConstructor(12);
        midi.getMidiNoteArray(mediator.getTableEditor().getNoteArray());

        menuBar = new JMenuBar();
        JMenu menu = new JMenu("Midi");
        JMenuItem m1 = new JMenuItem("Playback");
        JMenuItem m2 = new JMenuItem("Save");
        JMenuItem m3 = new JMenuItem("Placeholder");


        menu.add(m1);
        menu.add(m2);
        menu.add(m3);

        m1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                midi.playback();
            }
        });

        m2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                midi.saveMidi();
            }
        });

        menuBar.add(menu);
        

        

        
    }
    
}
