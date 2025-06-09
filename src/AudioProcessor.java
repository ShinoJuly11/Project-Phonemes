
import java.io.ByteArrayInputStream;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;




public class AudioProcessor{

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
     * @version 1
     * @author ShinoJuly11
     */

    public AudioInputStream playFrameRange(AudioInputStream originalAis, long startFrame, Long endFrame) throws Exception{
        
        AudioFormat format = originalAis.getFormat();
        int frameSize = format.getFrameSize();
        long numFrames = endFrame - startFrame;

        // Skip to the starting frame (in bytes)
        long bytesToSkip = startFrame * frameSize;
        originalAis.skip(bytesToSkip);

        // Create a new AudioInputStream limited to the selected range
        AudioInputStream selectedAis = new AudioInputStream(originalAis, format, numFrames);

        return selectedAis;
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
