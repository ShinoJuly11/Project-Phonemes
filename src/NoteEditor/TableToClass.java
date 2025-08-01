package NoteEditor;

import java.util.ArrayList;

public class TableToClass {

    Mediator mediator;
    Boolean[][] noteNumber;

    public class MidiNote {

        int noteLength, row;
        int start, end;

        public MidiNote(int row ,int noteLength, int start, int end){
            this.noteLength = noteLength;
            this.start = start;
            this.end = end;
            this.row = row;
        }

        public void printAll(){
            System.out.println(Integer.toString(row)+ "\t" + Integer.toString(noteLength)+ "\t" +Integer.toString(start)+ "\t" +Integer.toString(end));
        }

    }

    public TableToClass(Boolean[][] noteNumber){
        setNoteNumber(noteNumber);
    }


    public TableToClass(Mediator mediator){
        this.mediator = mediator;
    }

    public void setNoteNumber(Boolean[][] noteNumber){
        this.noteNumber = noteNumber;
    }

    public void process(){

        ArrayList<MidiNote> MidiNoteArray = new ArrayList<>();

        for (int i = 0; i < noteNumber.length; i++) {
            int count = 0;
            int start = 0;
            for (int j = 0; j < noteNumber[i].length; j++) {
                if (Boolean.TRUE.equals(noteNumber[i][j])) {
                    if (count == 0) {
                        start = j;
                    }
                    count++;
                } else {
                    if (count > 0) {
                        int end = j - 1;
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

        for (MidiNote note : MidiNoteArray){
            note.printAll();
        }

        }

    }

