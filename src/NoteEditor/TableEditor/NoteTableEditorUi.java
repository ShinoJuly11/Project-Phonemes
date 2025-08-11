package NoteEditor.TableEditor;

import java.awt.GridBagConstraints;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JScrollPane;
import NoteEditor.Mediator;
import NoteEditor.TableEditorUi;

public class NoteTableEditorUi implements TableEditorUi{

    Boolean[][] noteNumber;
    String[] tickNumber;
    Mediator mediator;
    JScrollPane scrollPane; 
    JLayeredPane layeredPane;

    //Layers inside the NoteTableEditor For LayeredPane
    TableLayer baseTable;
    PanelLayer AliasLayer;

    //MouseActions
    HandleMouse tableMouse;
    HandleMouse aliasMouse;

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

    public PanelLayer getAliasLayer(){
        return this.AliasLayer;
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
        this.AliasLayer = new AliasLabelLayer(this);   //idk if this is the best approach to do SRP oh well
        this.tableMouse = new NoteTableLayer(this);     //atleast OCP isnt violated inside the mouseListener now
        this.aliasMouse = new AliasLabelLayer(this);
        
        MouseListener mouseListener = new MouseListener(this);

        mouseListener.add(aliasMouse);
        mouseListener.add(tableMouse);
        mouseListener.run();

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

    public void tableUpdate(Boolean[][] noteNumber, String[] tickNumber){
        
        this.noteNumber = noteNumber;
        this.tickNumber = tickNumber;
        mediator.TableUpdate();
    }

        
}

