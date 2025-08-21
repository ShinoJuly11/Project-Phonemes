package ResamplerEngine;
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
     * @version 2 - 13/06/25
     * @author ShinoJuly11
     * 
     */

    public void volumeAudio(Phoneme phoneme, float volumeGradient) throws Exception {

        byte[] audioBytes = phoneme.getByteStream();
        AudioInputStream ais = phoneme.getAis();
        int frameSize = ais.getFormat().getFrameSize();

        for (int x = 0; x < (ais.getFrameLength() / frameSize); x++){
            int byteIndex = x * frameSize;
            short sample = (short) ((audioBytes[byteIndex+1] << 8) | (audioBytes[byteIndex] & 0xff));

            sample = (short) (sample * volumeGradient);

            audioBytes[byteIndex] = (byte) (sample & 0xff);
            audioBytes[byteIndex + 1] = (byte) ((sample >> 8) & 0xff);

        }

        phoneme.setByteStream(audioBytes);

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
     * @version 2 13/06/25
     * @author ShinoJuly11
     * 
     */

    public void fadeOutAudio(Phoneme phoneme, int fadeFrames) throws Exception{

        byte[] audioBytes = phoneme.getByteStream();
        AudioInputStream ais = phoneme.getAis();
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

        phoneme.setByteStream(audioBytes);

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
     * @version 2 13/06/25
     * @author ShinoJuly11
     * 
     */

    
    public void fadeInAudio(Phoneme phoneme, int fadeFrames) throws Exception{

        byte[] audioBytes = phoneme.getByteStream();
        AudioInputStream ais = phoneme.getAis();
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

        phoneme.setByteStream(audioBytes);


    }

}
