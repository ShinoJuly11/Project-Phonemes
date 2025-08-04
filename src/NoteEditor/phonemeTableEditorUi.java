package NoteEditor;

public class phonemeTableEditorUi {

    Mediator mediator;
    Boolean[][] noteNumber;
    String[] tickNumber;

    public phonemeTableEditorUi(Mediator m){
        this.mediator = m;

    }

    public void setNoteNumber(Boolean[][] n){
        this.noteNumber = n;
    }

    public void setTickNumber(String[] n){
        this.tickNumber = n;
    }

    public String[] getTickNumber(){
        return mediator.getController().getTickNumber();

    }
    public Boolean[][] getNoteNumber(){
        return mediator.getController().getNoteNumber();

    }

    public void process(){

    }
    
}
