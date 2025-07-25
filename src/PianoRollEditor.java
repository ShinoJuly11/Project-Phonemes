import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;
import java.io.File;

public class PianoRollEditor {

    public PianoRollEditor() throws Exception{

        Sequence sequence = new Sequence(Sequence.PPQ, 24);
        Track track = sequence.createTrack();

        // Add a NOTE ON event (Middle C)
        ShortMessage noteOn = new ShortMessage();
        noteOn.setMessage(ShortMessage.NOTE_ON, 0, 70, 100); // channel, note, velocity
        track.add(new MidiEvent(noteOn, 1)); // tick = 1

        // Add a NOTE OFF event (stop note)
        ShortMessage noteOff = new ShortMessage();
        noteOff.setMessage(ShortMessage.NOTE_OFF, 0, 70, 0);
        track.add(new MidiEvent(noteOff, 64)); // tick = 16

        // Add a NOTE ON event (Middle C)
        ShortMessage noteOn1 = new ShortMessage();
        noteOn.setMessage(ShortMessage.NOTE_ON, 1, 60, 100); // channel, note, velocity
        track.add(new MidiEvent(noteOn1, 64)); // tick = 1

        // Add a NOTE OFF event (stop note)
        ShortMessage noteOff1 = new ShortMessage();
        noteOff.setMessage(ShortMessage.NOTE_OFF, 1, 60, 0);
        track.add(new MidiEvent(noteOff1, 128)); // tick = 16

        // Save the file
        MidiSystem.write(sequence, 1, new File("output.mid"));
        
        Sequencer sequencer = MidiSystem.getSequencer();
        sequencer.open();
        sequencer.setSequence(sequence);

        sequencer.start();

        

        
    }


}
