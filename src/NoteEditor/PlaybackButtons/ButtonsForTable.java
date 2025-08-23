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
import ResamplerEngine.Resampler;

public class ButtonsForTable implements InterfaceGui{

    Mediator mediator; 
    JPanel panel;
    JMenuBar menuBar;
    GridBagConstraints constraints;
    Resampler resampler = new Resampler();

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

        JMenu menu2 = new JMenu("Test Resampler");
        JMenuItem m4 = new JMenuItem("Process");
        JMenuItem m5 = new JMenuItem("Playback");
        JMenuItem m6 = new JMenuItem("Placeholder");

        menu2.add(m4);
        menu2.add(m5);
        menu2.add(m6);

        m4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                // Resampler resampler = new Resampler();
                try{
                    resampler.process(mediator.getTableEditor().getNoteArray());
                    System.out.println(mediator.getTableEditor().getNoteArray().size());
                    System.out.println("resampler process complete!");
                }
                catch(Exception e2){
                    System.err.println(e2);
                }
            }
        });

        m5.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                // Resampler resampler = new Resampler();
                try{
                    resampler.playback();
                    resampler.save();
                }
                catch(Exception e2){
                    System.err.println(e2);
                }
            }
        });

        menuBar.add(menu2);

        

        

        
    }
    
}
