package NoteEditor;

public class EditorMediator implements Mediator {

    TableEditorUi a;
    Controller b;

    public void process(){
        this.a = new NoteTableEditorUi(this);
        this.b = new EditorController(this);

        b.process();
        a.process(b.getNoteNumber(),b.getTickNumber());


    }

    public void NoteTableEditorUiUpdate(){

        b.update(a.getNoteNumber());
        a.process(b.getNoteNumber(), b.getTickNumber());

        
    }

    public TableEditorUi getTableEditor(){
        return this.a;
    }
    public Controller getController(){
        return this.b;
    }


}
