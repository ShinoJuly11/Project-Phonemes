package NoteEditor.TableEditor;

import java.awt.GridBagConstraints;
import java.awt.Rectangle;

import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JScrollPane;
import javax.swing.JLabel;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import NoteEditor.Mediator;
import NoteEditor.TableEditorUi;

public class NoteTableEditorUi implements TableEditorUi{

    Boolean[][] noteNumber;
    String[] tickNumber;
    Mediator mediator;
    JScrollPane scrollPane; 
    JLayeredPane layeredPane;
    TableLayer baseTable;
    PanelLayer AliasLayer;

    public NoteTableEditorUi(Mediator mediator){
        this.mediator = mediator;
        
    }

    // set getters for the mediator

    public Mediator getMediator(){
        return this.mediator;
    }

    public TableLayer getBaseTable(){
        return this.baseTable;
    }
    
    public JLayeredPane getLayeredPane(){
        return this.layeredPane;
    }

    public void setTickNumber(String[] s){
        this.tickNumber = s;
    }
    public String[] getTickNumber(){
        return this.tickNumber;
    }
    public void setNoteNumber(Boolean[][] s){
        this.noteNumber = s;
    }
    public Boolean[][] getNoteNumber(){
        return this.noteNumber;
    }

    public void process() {

        JFrame f = mediator.getJFrame();
        GridBagConstraints constraints = new GridBagConstraints();
        layeredPane = new JLayeredPane();

        this.baseTable = new NoteTableLayer(this); // creates the base table
        this.AliasLayer = new AliasLabelLayer(this);

        layeredPane.add(baseTable.getNoteTable(), Integer.valueOf(0));
        //TableEditor.getLayeredPane().add(label, Integer.valueOf(10)); inside the AliasLayer Class

        scrollPane = new JScrollPane(layeredPane);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        constraints.weightx = 4;
        constraints.weighty = 3;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.fill = GridBagConstraints.BOTH;

        f.add(scrollPane, constraints);
        

    }

    public void update(Boolean[][] noteNumbers, String[] tickNumber){
        baseTable.update(noteNumbers, tickNumber);
        
    }

        
}

