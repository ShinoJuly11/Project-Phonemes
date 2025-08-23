package ResamplerEngine.AudioProcess;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.UnsupportedAudioFileException;

import NoteEditor.TableEditor2.Note;
import be.tarsos.dsp.PitchShifter;


public class PitchShift extends TarsosDSPProcessors{

    float pitch;

    public PitchShift(Note note, AudioFormat format) throws UnsupportedAudioFileException{
        super(note, format);
        this.pitch = (float) note.getRow() / 10;
    }

    @Override
    public void addProcessors(){
        processorArray.add(new PitchShifter(pitch, audioFormat.getSampleRate(), super.bufferSize, super.overlap));
    }

}
