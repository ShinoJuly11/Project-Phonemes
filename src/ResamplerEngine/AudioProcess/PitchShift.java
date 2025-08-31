package ResamplerEngine.AudioProcess;
import javax.sound.sampled.UnsupportedAudioFileException;
import be.tarsos.dsp.PitchShifter;


public class PitchShift extends TarsosDSPProcessors{

    float pitch;

    public PitchShift() throws UnsupportedAudioFileException{
        super();
    }

    @Override
    public void addProcessors(){
        this.pitch = (float) note.getRow() / 10;
        processorArray.add(new PitchShifter(pitch, audioFormat.getSampleRate(), super.bufferSize, super.overlap));
    }

}
