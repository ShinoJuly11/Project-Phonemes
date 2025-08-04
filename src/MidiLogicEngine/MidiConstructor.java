package MidiLogicEngine;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;
import NoteEditor.MidiNote;
import NoteEditor.NoteTableConverter;

public class MidiConstructor {

    ArrayList<MidiNote> midiNoteArray;
    Sequence sequence;
    Track track;
    NoteTableConverter midiArray;

    public void getMidiNoteArray(ArrayList<MidiNote> m){
        this.midiNoteArray = m;
    }

    public MidiConstructor(NoteTableConverter midiArray, int resolution){
        try{
            midiNoteArray = midiArray.getMidiNoteArray();
            sequence = new Sequence(Sequence.PPQ, resolution);
            track = sequence.createTrack();
        }
        catch(InvalidMidiDataException e){
            System.err.println("Invalid Midi Data inserted!");
        }
    }

    public void process(){
        try{
            for (MidiNote note: midiNoteArray){
            addNote(note.getRow(),note.getStart(),note.getEnd());
        }

        }
        catch(ArrayIndexOutOfBoundsException e){
            System.out.println("add the midinotearray");
        }

    }

    private void addNote(int row, int start, int end){

            try{

                ShortMessage noteOn = new ShortMessage();
                noteOn.setMessage(ShortMessage.NOTE_ON, 0, row*10, 100); // channel, note, velocity
                track.add(new MidiEvent(noteOn,start)); // tick = 1

                // Add a NOTE OFF event (stop note)
                ShortMessage noteOff = new ShortMessage();
                noteOff.setMessage(ShortMessage.NOTE_OFF, 0, row*10, 100);
                track.add(new MidiEvent(noteOff, end)); // tick = 16

            }
            
            catch(InvalidMidiDataException e){
                System.err.println(e);
            }
        }

    public void saveMidi(){
        try{
            MidiSystem.write(sequence, 1, new File("sound/output.mid"));
        }
        catch(IOException e){
            System.err.println(e);
        }

    }

    public void playback(){
        
        try{
            Sequencer sequencer = MidiSystem.getSequencer();
            sequencer.open();
            sequencer.setSequence(sequence);
            sequencer.start();
        }

        catch(MidiUnavailableException e){
            System.err.println(e);
        }

        catch(InvalidMidiDataException e2){
            System.err.println(e2);
        }

    }


}
