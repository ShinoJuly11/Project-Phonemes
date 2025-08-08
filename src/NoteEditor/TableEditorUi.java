package NoteEditor;
import javax.swing.JLayeredPane;

import NoteEditor.TableEditor.TableLayer;

public interface TableEditorUi {

    public void process();
    public void update(Boolean[][] a, String[] b);
    public void setTickNumber(String[] s);
    public void setNoteNumber(Boolean[][] s);
    public String[] getTickNumber();
    public Boolean[][] getNoteNumber();
    public Mediator getMediator();
    public JLayeredPane getLayeredPane();
    public TableLayer getBaseTable();
    public void tableUpdate(Boolean[][] noteNumber, String[] tickNumber);
    


}
