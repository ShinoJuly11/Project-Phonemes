package NoteEditor;

import java.util.ArrayList;

public class ClassToTable implements NoteTableConverter {

    Mediator mediator;
    Boolean[][] noteNumber;
    ArrayList<MidiNote> MidiNoteArray;

    public ClassToTable(Boolean[][] noteNumber){
        setNoteNumber(noteNumber);
    }

    public ClassToTable(Mediator mediator){
        this.mediator = mediator;
    }

    public void setNoteNumber(Boolean[][] noteNumber){
        this.noteNumber = noteNumber;
    }
    
    public ArrayList<MidiNote> getMidiNoteArray(){
        return this.MidiNoteArray;
    }

    public void process(){
    
    int numRows = 6;
    int numCols = 6;

    Boolean[][] noteNumber = new Boolean[numRows][numCols];
    // Initialize all to false
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                noteNumber[i][j] = Boolean.FALSE;
            }
        }
        // Set true for each MidiNote's range
        for (MidiNote note : this.MidiNoteArray) {
            int row = note.getRow();
            int start = note.getStart();
            int end = note.getEnd();
            for (int j = start; j <= end && j < numCols; j++) {
                noteNumber[row][j] = Boolean.TRUE;
            }
        }

    }
}