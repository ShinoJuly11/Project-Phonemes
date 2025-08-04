package NoteEditor;
import javax.swing.JFrame;

public interface Mediator {

    public void process();
    public TableEditorUi getTableEditor();
    public Controller getController();
    public JFrame getJFrame();
    public void TableUpdate();
    public void convertTableToClass();
    public NoteTableConverter getTableToClass();

    
}
