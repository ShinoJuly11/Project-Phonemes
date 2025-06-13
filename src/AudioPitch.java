import java.io.ByteArrayInputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.UnsupportedAudioFileException;
import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.io.jvm.AudioDispatcherFactory;

public class AudioPitch {

    Phoneme phoneme;
    AudioDispatcher dispatcher;
    int bufferSize = 2048;
    int bufferOverlap = 1024;

    public AudioPitch(Phoneme phoneme) throws UnsupportedAudioFileException{
        this.phoneme = phoneme;
        this.dispatcher = AudioDispatcherFactory.fromByteArray(phoneme.getByteStream(),phoneme.getAis().getFormat(), bufferSize, bufferOverlap);          
        }

    public void AudioPitchFactor(double pitchFactor){
        double sampleRate = (double) phoneme.getAis().getFormat().getSampleRate();
        PitchShifterBuffer pShifter = new PitchShifterBuffer(pitchFactor,sampleRate,bufferSize,bufferOverlap);
        dispatcher.addAudioProcessor(pShifter);
        dispatcher.run();
        float[] buffer = pShifter.getFloatBuffer();
        byte[] byteBuffer = floatToByteArray(buffer);

        phoneme.setByteStream(byteBuffer);
        int frameSize = phoneme.getAis().getFormat().getFrameSize();
        int numFrames = byteBuffer.length / frameSize;
        ByteArrayInputStream bais = new ByteArrayInputStream(byteBuffer);
        AudioInputStream newAis = new AudioInputStream(bais, phoneme.getAis().getFormat(), numFrames);
        phoneme.setAis(newAis); // You need to add setAis() to your Phoneme class
    }

    private byte[] floatToByteArray(float[] fArray){
        
        ByteBuffer bb = (phoneme.getAis().getFormat().isBigEndian())
        ? ByteBuffer.allocate(2 * fArray.length).order(ByteOrder.BIG_ENDIAN)
        : ByteBuffer.allocate(2 * fArray.length).order(ByteOrder.LITTLE_ENDIAN);

        for (float f: fArray){
            int intSample = Math.max(-32768, Math.min(32767, (int)(f * 32767.0f)));
            bb.putFloat(intSample);

        }

        return bb.array();
        
    }
        
}
