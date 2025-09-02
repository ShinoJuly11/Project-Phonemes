package ResamplerEngine.AudioProcess;
import javax.sound.sampled.UnsupportedAudioFileException;
import be.tarsos.dsp.PitchShifter;


public class PitchShift extends TarsosDSPProcessors{

    public PitchShift() throws UnsupportedAudioFileException{
        super();
    }

    @Override
    public void addProcessors(){
        float pitch = (float) note.getRow() / 10; // magic number
        processorArray.add(new PitchShifter(pitch, audioFormat.getSampleRate(), super.bufferSize, super.overlap));
    }

}
