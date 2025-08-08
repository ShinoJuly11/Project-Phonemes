package NoteEditor.TableEditor;

import javax.swing.JTable;

public interface TableLayer{
    
    public void process();
    public void update(Boolean[][] noteNumber, String[] tickNumber);
    public JTable getNoteTable();
    public Boolean[][] getNoteNumber();
    public String[] getTickNumber();

}
