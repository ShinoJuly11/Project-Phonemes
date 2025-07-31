package NoteEditor;

public interface Mediator {

    public void process();
    public TableEditorUi getTableEditor();
    public Controller getController();
    public void NoteTableEditorUiUpdate();

    
}
