import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import javax.sound.sampled.AudioInputStream;

    /**
     * 
     * AudioVolume class stores all the Byte manipulation algorithms.
     * 
     * @since v0.5.4 - 09/06/25
     * @version 1
     * @author ShinoJuly11
     * 
     * @see volumeAudio
     * @see fadeOutAudio
     * @see fadeInAudio
     * 
     */

public class AudioVolume{

    /**
     * 
     * increasing or decreasing the gain of the audio.
     * 
     * @param ais - AudioInputStream
     * @param volumeGradient - float
     * @throws Exception usually AudioInputStream related errors 
     * @return AudioInputStream
     * 
     * @since v0.5.4 - 09/06/25
     * @version 1
     * @author ShinoJuly11
     * 
     */

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

    /**
     * 
     * Decreases the gain from the starting frame (fadeFrames) to the end of the array frames.
     * 
     * @param ais - AudioInputStream
     * @param fadeFrames - integer
     * @throws Exception usually AudioInputStream related errors 
     * @return AudioInputStream
     * 
     * @since v0.5.4 - 09/06/25
     * @version 1
     * @author ShinoJuly11
     * 
     */

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

    /**
     * 
     * increasing the gain from from the start of the frame array to the the selected frame (fadeFrames).
     * 
     * @param ais - AudioInputStream
     * @param fadeFrames - integer
     * @throws Exception usually AudioInputStream related errors 
     * @return AudioInputStream
     * 
     * @since v0.5.4 - 09/06/25
     * @version 1
     * @author ShinoJuly11
     * 
     */

    
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

        /**
     * 
     * Decreases the gain from the starting frame (fadeFrames) to the end of the array frames.
     * 
     * @param ais - AudioInputStream
     * @throws Exception usually AudioInputStream related errors 
     * @return byte[] - An array of the audio file but in bytes
     * 
     * @since v0.5.4 - 09/06/25
     * @version 1
     * @author ShinoJuly11
     * 
     */

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
