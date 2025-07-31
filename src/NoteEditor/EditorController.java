package NoteEditor;

public class EditorController implements Controller {

    Boolean[][] noteNumber;
    String[] tickNumber;

    Mediator mediator;

    public EditorController(Mediator mediator){
        this.mediator = mediator;
    }

    public void process(){
        tables();
    }

    public void update(Boolean[][] newNote){
        tableConcatenator(newNote);
        
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


    public void tables(){

        noteNumber = new Boolean[20][20];
        tickNumber = new String[20];

        for (int i = 0; i < noteNumber.length; i++) {
            for (int j = 0; j < noteNumber[i].length; j++) {
                noteNumber[i][j] = Boolean.FALSE;
            }
        }

        for (int i = 0; i < tickNumber.length; i++){
            tickNumber[i] = Integer.toString(i+1);
        }

    }

    public void tableConcatenator(Boolean[][] add){
            
        System.arraycopy(noteNumber, 0, add, noteNumber.length, add.length);

        tickNumber = new String[noteNumber.length + add.length];

        for (int i = 0; i <= (noteNumber.length + add.length); i++){
            tickNumber[i] = Integer.toString(i);
        }

    }
    



}
