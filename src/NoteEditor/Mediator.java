package NoteEditor;
import NoteEditor.TableEditor2.tableEditor;

public interface Mediator {

    public void process();
    public tableEditor getTableEditor();
    public GlobalFrame getFrame();


    
}
