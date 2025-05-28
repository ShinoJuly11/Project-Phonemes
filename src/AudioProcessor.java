
import java.io.ByteArrayInputStream;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;

import LEGACYFILES.AudioProcessorInterface;



public class AudioProcessor implements AudioProcessorInterface{

    @Override
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

    @Override
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

    // only works if the voicebank if the CV voicebank
    // this is just chatgpt i cant get my head around this now
    @Override
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
