
import java.io.ByteArrayInputStream;
import java.io.IOException;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;





public class AudioPlayback{

    /**
     * Extracts and returns a subrange of frames from the provided AudioInputStream,
     * starting at {@code startFrame} and ending at {@code endFrame} (if not null).
     *
     * @param originalAis the original AudioInputStream
     * @param startFrame the starting frame index (inclusive)
     * @param endFrame the ending frame index (exclusive); if null, plays until the end
     * @return a new AudioInputStream containing only the specified frame range
     * @throws Exception if an error occurs during processing the AudioInputStream
     * 
     * @since v0.5.4 - 09/06/25
     * @version 2 13/06/25
     * @author ShinoJuly11
     */

    public void playFrameRange(Phoneme phoneme, int startFrame, int endFrame) throws Exception{
        
        int frameSize = phoneme.getAis().getFormat().getFrameSize(); // e.g., 2 bytes per frame (for 16-bit mono)
        int numFrames = endFrame - startFrame;
        int numBytes = numFrames * frameSize;

        byte[] byteStream = phoneme.getByteStream();
        byte[] newByteStream = new byte[numBytes];

        // Copy the correct slice of the byte array
        System.arraycopy(
            byteStream,
            startFrame * frameSize, // source start in bytes
            newByteStream,
            0,             // destination start at 0
            numBytes               // total bytes to copy
        );


    }

    private AudioInputStream baisToAis(Phoneme phoneme, byte[] byteStream, int numFrames) throws IOException{
        ByteArrayInputStream bais = new ByteArrayInputStream(byteStream);
        AudioInputStream ais = new AudioInputStream(bais, phoneme.getAis().getFormat(), numFrames);
        return ais;
    }



    public void playback(Phoneme phoneme, int numFrames) throws Exception{
        AudioInputStream ais = baisToAis(phoneme, phoneme.getByteStream(), numFrames);
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, ais.getFormat());
        SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);

        line.open(ais.getFormat());
        line.start();

        byte[] buffer = new byte[4096];
        int bytesRead;

            while ((bytesRead = ais.read(buffer, 0, buffer.length)) != -1) {
                line.write(buffer, 0, bytesRead);
            }

        line.drain();
        line.close();
    }
        

    /**
     * Changes the sample rate of the provided AudioInputStream to the specified target rate.
     *
     * @param ais the original AudioInputStream
     * @param targetRate the desired sample rate in Hz
     * @return a new AudioInputStream with the updated sample rate
     * @throws Exception if an error occurs during the conversion
     *
     * @since v0.5.4 - 09/06/25
     * @version 1
     * @author ShinoJuly11
     */

    public AudioInputStream changeSampleRate(AudioInputStream ais, float targetRate) throws Exception{

        AudioFormat sourceFormat = ais.getFormat();

        AudioFormat targetFormat = new AudioFormat(
                                                sourceFormat.getEncoding(),
                                                targetRate,
                                                sourceFormat.getSampleSizeInBits(),
                                                sourceFormat.getChannels(),
                                                sourceFormat.getFrameSize(),
                                                targetRate,
                                                sourceFormat.isBigEndian()
                                                );

        //now i need to recreate the clip class
        AudioInputStream convertedStream = new AudioInputStream(ais, targetFormat, ais.getFrameLength());

       return convertedStream;

    }

    /**
     * Stretches the audio by the given stretch factor.
     * A factor > 1 lengthens the audio, while a factor < 1 shortens it.
     * @implNote might rewrite this as its chatGPT genereated so krill issue but i think i can do this Closest neigbour maths
     *
     * @param inputAIS the original AudioInputStream
     * @param stretchFactor the factor by which to stretch the audio
     * @return a new AudioInputStream with adjusted length
     * @throws Exception if an error occurs during processing
     *
     * @since v0.5.4 - 09/06/25
     * @version 1
     * @author ShinoJuly11
     */
    public AudioInputStream stretchAudio(AudioInputStream inputAIS, float stretchFactor) throws Exception {
        AudioFormat format = inputAIS.getFormat();

        // Read all audio data
        byte[] inputBytes = inputAIS.readAllBytes();
        int frameSize = format.getFrameSize();
        int totalFrames = inputBytes.length / frameSize;

        // Calculate stretched frame count
        int newFrames = (int) (totalFrames * stretchFactor);
        byte[] outputBytes = new byte[newFrames * frameSize];

        for (int i = 0; i < newFrames; i++) {
            // Simple nearest-neighbor interpolation (repeats samples)
            int originalIndex = (int) (i / stretchFactor) * frameSize;
            int outputIndex = i * frameSize;

            if (originalIndex + frameSize <= inputBytes.length) {
                System.arraycopy(inputBytes, originalIndex, outputBytes, outputIndex, frameSize);
            } else {
                // Pad with silence if out of bounds
                for (int j = 0; j < frameSize; j++) {
                    outputBytes[outputIndex + j] = 0;
                }
            }
        }

        // Create AudioInputStream from stretched data
        ByteArrayInputStream bais = new ByteArrayInputStream(outputBytes);
        AudioInputStream stretchedAIS = new AudioInputStream(bais, format, newFrames);

        return stretchedAIS;
    }
}
