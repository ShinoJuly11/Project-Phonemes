package NoteEditor;

public class EditorController implements Controller {

    Boolean[][] noteNumber;
    String[] tickNumber;

    Mediator mediator;

    int newCols = 60;

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

        noteNumber = new Boolean[newCols][newCols];
        tickNumber = new String[newCols];

        for (int i = 0; i < noteNumber.length; i++) {
            for (int j = 0; j < noteNumber[i].length; j++) {
                noteNumber[i][j] = Boolean.FALSE;
            }
        }

        for (int i = 0; i < tickNumber.length; i++){
            tickNumber[i] = Integer.toString(i+1);
        }

    }

    public void tableConcatenator(Boolean[][] add) {
    int rows = noteNumber.length;
    int oldCols = noteNumber[0].length;

    // Create a new array with combined columns
    Boolean[][] combined = new Boolean[rows][oldCols + newCols];

    for (int i = 0; i < combined.length; i++) {
            for (int j = 0; j < combined[i].length; j++) {
                combined[i][j] = Boolean.FALSE;
            }
        }

    // Copy old data
    for (int i = 0; i < rows; i++) {
        System.arraycopy(noteNumber[i], 0, combined[i], 0, oldCols);
    }

    noteNumber = combined;

    // Update tickNumber for new column count
    tickNumber = new String[noteNumber[0].length];
    for (int i = 0; i < tickNumber.length; i++) {
        tickNumber[i] = Integer.toString(i + 1);
    }

    }
}