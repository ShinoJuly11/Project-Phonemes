
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import javax.sound.sampled.AudioInputStream;

import LEGACYFILES.AudioVolumeInterface;

public class AudioVolume implements AudioVolumeInterface{
    @Override
    public AudioInputStream volumeAudio(AudioInputStream ais, float volumeGradient) throws Exception {

        byte[] audioBytes = AISToByte(ais);
        int frameSize = ais.getFormat().getFrameSize();

        for (int x = 0; x < (ais.getFrameLength() / frameSize); x++){
            int byteIndex = x * frameSize;
            short sample = (short) ((audioBytes[byteIndex+1] << 8) | (audioBytes[byteIndex] & 0xff));

            sample = (short) (sample * volumeGradient);

            audioBytes[byteIndex] = (byte) (sample & 0xff);
            audioBytes[byteIndex + 1] = (byte) ((sample >> 8) & 0xff);

        }

        ByteArrayInputStream bais = new ByteArrayInputStream(audioBytes);
        AudioInputStream returnAIS = new AudioInputStream(bais, ais.getFormat(), ais.getFrameLength());

        return returnAIS;

    }
    @Override
    public AudioInputStream fadeOutAudio(AudioInputStream ais, int fadeFrames) throws Exception{

        byte[] audioBytes = AISToByte(ais);
        int frameSize = ais.getFormat().getFrameSize();
        int totalFrames = (int) ais.getFrameLength();
        int startFrame = totalFrames - fadeFrames;
        if (startFrame < 0) startFrame = 0;

        for (int x = startFrame; x < totalFrames; x++){

            float fadeFactor = 1.0f - ((float)(x - startFrame) / fadeFrames); // 1 -> 0

            int byteIndex = x * frameSize;
            short sample = (short) ((audioBytes[byteIndex+1] << 8) | (audioBytes[byteIndex] & 0xff));
            sample = (short) (sample * fadeFactor);

            // Write back
            audioBytes[byteIndex] = (byte) (sample & 0xff);
            audioBytes[byteIndex + 1] = (byte) ((sample >> 8) & 0xff);

        }

        ByteArrayInputStream bais = new ByteArrayInputStream(audioBytes);
        AudioInputStream returnAIS = new AudioInputStream(bais, ais.getFormat(), ais.getFrameLength());

        return returnAIS;

    }
    @Override
    public AudioInputStream fadeInAudio(AudioInputStream ais, int fadeFrames) throws Exception{

        byte[] audioBytes = AISToByte(ais);
        int frameSize = ais.getFormat().getFrameSize();
        int totalFrames = (int) ais.getFrameLength();

        for (int x = 0; x < totalFrames; x++){

            float fadeFactor = (float)(x) / fadeFrames; // 0 -> 1

            int byteIndex = x * frameSize;
            short sample = (short) ((audioBytes[byteIndex+1] << 8) | (audioBytes[byteIndex] & 0xff));

            sample = (short) (sample * fadeFactor);

            // Write back
            audioBytes[byteIndex] = (byte) (sample & 0xff);
            audioBytes[byteIndex + 1] = (byte) ((sample >> 8) & 0xff);

        }

        ByteArrayInputStream bais = new ByteArrayInputStream(audioBytes);
        AudioInputStream returnAIS = new AudioInputStream(bais, ais.getFormat(), ais.getFrameLength());

        return returnAIS;

    }

    private byte[] AISToByte(AudioInputStream ais) throws Exception{
            int bufferSize = 4096;
            byte[] buffer = new byte[bufferSize];
            int byteRead;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            //while bytes are not empty
            while ((byteRead = ais.read(buffer)) != -1){
                baos.write(buffer, 0, byteRead);
            }

            byte[] audioBytes = baos.toByteArray();

            return audioBytes; 
    }        

}
