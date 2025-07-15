import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.PitchShifter;

public class TarsosDSPPitchBend extends PitchShifter {

    double pitchFactor;
    double endFactor;
    int bufferCount;

    public TarsosDSPPitchBend(double startFactor, double endFactor, int bufferCount, double sampleRate, int size, int overlap){
        super(startFactor, sampleRate, size, overlap);
        this.pitchFactor = startFactor;
        this.endFactor = endFactor;
        this.bufferCount = bufferCount;
    }

    @Override
    public boolean process(AudioEvent audioEvent){

        // add some convoulted A-level graph maths in here so i can have good pitch factor
        double increment = (endFactor - pitchFactor) / bufferCount;
        this.pitchFactor += increment;
        if ((increment > 0 && this.pitchFactor > endFactor) || (increment < 0 && this.pitchFactor < endFactor)) {
            this.pitchFactor = endFactor;
        }

        super.setPitchShiftFactor((float) pitchFactor);
        return super.process(audioEvent);

    }

    @Override
    public void processingFinished(){
        super.processingFinished();
    }





}
