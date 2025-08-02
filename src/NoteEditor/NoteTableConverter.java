package NoteEditor;

import java.util.ArrayList;

public interface NoteTableConverter {

    public void process();
    public void setNoteNumber(Boolean[][] noteNumber);
    public ArrayList<MidiNote> getMidiNoteArray();

}
