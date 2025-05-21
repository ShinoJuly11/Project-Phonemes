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

    public File mergeTwoClips(AudioInputStream clip1, AudioInputStream clip2) throws Exception{

    AudioFormat clip1Format = this.clip1.getFormat();


    //edit the framelength if you want to play a specific frame of the audio file
    AudioInputStream appendedFiles = new AudioInputStream(new SequenceInputStream(clip1, clip2), clip1Format,
                                                         clip1.getFrameLength() + clip2.getFrameLength());

        File outFile = new File("sound/combined.wav");
        AudioSystem.write(appendedFiles, AudioFileFormat.Type.WAVE, outFile);

        return outFile;

    }
    
    // only works if the voicebank if the CV voicebank
    public File theActualTimeStretcher(AudioInputStream clip1, AudioInputStream clip2) throws Exception{

        AudioInputStream ais = ;

        File outFile = new File("sound/combined2.wav");

        for (int x = 0; x < 5; x++){

            ais = AudioSystem.getAudioInputStream(mergeTwoClips(clip1,clip2));
            clip1 = ais;

        }

        AudioSystem.write(ais, AudioFileFormat.Type.WAVE, outFile);

        return outFile;


    }


}
