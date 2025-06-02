package LEGACYFILES;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.SequenceInputStream;

import javax.sound.sampled.*;

public class TimeStretch {

    AudioInputStream clip1;
    AudioInputStream clip2;


    public TimeStretch(AudioInputStream clip1, AudioInputStream clip2){
        this.clip1 = clip1;
        this.clip2 = clip2;

    }

    public File mergeTwoClips(AudioInputStream clip1, AudioInputStream clip2) throws Exception {
        AudioFormat format = clip1.getFormat();

        AudioInputStream appended = new AudioInputStream(
            new SequenceInputStream(clip1, clip2),
            format,
            clip1.getFrameLength() + clip2.getFrameLength()
        );

        File outFile = new File("sound/temp.wav");
        AudioSystem.write(appended, AudioFileFormat.Type.WAVE, outFile);

        return outFile;

    }
    
    // only works if the voicebank if the CV voicebank
    // this is just chatgpt i cant get my head around this now
    public File stretchAudio(File inputFile, float stretchFactor) throws Exception {
        AudioInputStream inputAIS = AudioSystem.getAudioInputStream(inputFile);
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

        File outFile = new File("sound/stretched.wav");
        AudioSystem.write(stretchedAIS, AudioFileFormat.Type.WAVE, outFile);
        return outFile;
    }
    


}
