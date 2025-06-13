import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.PitchShifter;

public class PitchShifterBuffer extends PitchShifter{

    private byte[] byteBuffer;
    private float[] floatBuffer;

    public PitchShifterBuffer(double pitchShiftFactor, double sampleRate, int bufferSize, int overlap) {
        super(pitchShiftFactor,sampleRate, bufferSize, overlap);
    }

    @Override
    public boolean process(AudioEvent audioEvent) {
        boolean result = super.process(audioEvent);
        byteBuffer = audioEvent.getByteBuffer();
        floatBuffer = audioEvent.getFloatBuffer();
        return result;
    }

    public byte[] getByteBuffer(){
        return this.byteBuffer;
    }

    public float[] getFloatBuffer(){
        return this.floatBuffer;
    }

    @Override
    public void processingFinished(){
        //ignore
    }
    
    
}
