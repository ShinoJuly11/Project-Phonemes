package NoteEditor.TableEditor;

import javax.swing.JTable;

public interface TableLayer{
    
    public void process();
    public void update(Boolean[][] noteNumber, String[] tickNumber);
    public JTable getNoteTable();
    public Boolean[][] getNoteNumber();
    public String[] getTickNumber();
    public void handleMouseClick(JTable noteTable, int row, int startColumn, boolean flag);
    public void handleMouseDrag(JTable noteTable, int startColumn, int endColumn, int row, int col, boolean flag);

}
