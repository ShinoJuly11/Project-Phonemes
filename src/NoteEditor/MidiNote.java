package NoteEditor;

public class MidiNote {

    private int noteLength, row, start, end;

    public MidiNote(int row ,int noteLength, int start, int end){
        this.noteLength = noteLength;
        this.start = start;
        this.end = end;
        this.row = row;
    }

    public void printAll(){
        System.out.println(Integer.toString(row)+ "\t" + Integer.toString(noteLength)+ "\t" +Integer.toString(start)+ "\t" +Integer.toString(end));
    }

    public int getNoteLength() {
        return noteLength;
    }

    public void setNoteLength(int noteLength) {
        this.noteLength = noteLength;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

        



    }
