package NoteEditor;
import javax.swing.JFrame;

public class EditorMediator implements Mediator {

    //istg if this class becomes GOD ill kms

    TableEditorUi a;
    Controller b;
    InterfaceGui c;
    ButtonsForTable d;
    NoteTableConverter e, f;

    public void process(){
        this.a = new NoteTableEditorUi(this);
        this.b = new EditorController(this);
        this.c = new globalFrame(this);
        this.d = new ButtonsForTable(this);
        this.e = new TableToClass(this);
        this.f = new ClassToTable(this);

        c.process();
        b.process();
        getAll();
        a.process();
        d.process();
        c.setVisible();

    }

    public void getAll(){
        a.setNoteNumber(b.getNoteNumber());
        a.setTickNumber(b.getTickNumber());
    }

    public void convertTableToClass(){
        this.e.setNoteNumber(b.getNoteNumber());
        this.e.process();
        
    }

    public void TableUpdate(){

        b.update(a.getNoteNumber());
        getAll();
        a.updateTable(b.getNoteNumber(), b.getTickNumber());

    }

    public JFrame getJFrame(){
        return c.getJFrame(); 

    }

    public NoteTableConverter getTableToClass(){
        return this.e;

    }

    public TableEditorUi getTableEditor(){
        return this.a;
    }
    public Controller getController(){
        return this.b;
    }


}
