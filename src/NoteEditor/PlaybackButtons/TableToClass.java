package NoteEditor.PlaybackButtons;

import java.util.ArrayList;

import NoteEditor.Mediator;
import NoteEditor.MidiNote;
import NoteEditor.NoteTableConverter;

public class TableToClass implements NoteTableConverter {

    Mediator mediator;
    Boolean[][] noteNumber;
    ArrayList<MidiNote> MidiNoteArray;


    public TableToClass(Mediator mediator){
        this.mediator = mediator;
    }

    public void setNoteNumber(Boolean[][] noteNumber){
        this.noteNumber = noteNumber;
    }

    public ArrayList<MidiNote> getMidiNoteArray(){
        return this.MidiNoteArray;
    }

    public void process(){

        
        this.MidiNoteArray = new ArrayList<>();

        for (int i = 0; i < noteNumber.length; i++) {
            int count = 0;
            int start = 0;
            for (int j = 0; j < noteNumber[i].length; j++) {
                if (Boolean.TRUE.equals(noteNumber[i][j])) {
                    if (count == 0) {
                        start = j+1;
                    }
                    count++;
                } else {
                    if (count > 0) {
                        int end = j;
                        MidiNote note = new MidiNote(i, count, start, end);
                        MidiNoteArray.add(note);
                        count = 0; // Reset for next possible note
                    }
                }
            }
            // Handle note that goes to the end of the row
            if (count > 0) {
                int end = noteNumber[i].length - 1;
                MidiNote note = new MidiNote(i, count, start, end);
                MidiNoteArray.add(note);
            }
        }

        for (MidiNote note : this.MidiNoteArray){
            note.printAll();
        }

        }

    }

