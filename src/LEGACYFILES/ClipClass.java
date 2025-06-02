package LEGACYFILES;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;


public class ClipClass{

    Clip clip;
    AudioInputStream audioStream;
    int start, end, duration, startFrame, endFrame;
    File musicFile;

    public ClipClass(File file){
        try {
        this.musicFile = file;  
        this.audioStream = AudioSystem.getAudioInputStream(file);
        this.clip = AudioSystem.getClip();
        this.end = clip.getFrameLength();
        this.start = 0;
        this.duration = (int)((clip.getFrameLength() / clip.getFormat().getFrameRate()) * 1000);
        }
        catch (UnsupportedAudioFileException e) {
            System.out.println(e.getMessage());

        }
        catch (IOException e){
            System.out.println(e.getMessage());
        }

        catch (LineUnavailableException e){
            System.out.println(e.getMessage());
        }


    }

    public void setFrame(int startFrame, int endFrame){
        this.startFrame = startFrame;
        this.endFrame = endFrame;
    }

    public void playback() throws InterruptedException, LineUnavailableException, IOException, UnsupportedAudioFileException{
        
        this.clip.start();
        do {
            Thread.sleep(100);
        }
        while (this.clip.isRunning());
    }

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

    public File sampleRate(File file, float targetRate) throws Exception{

        AudioInputStream ais = AudioSystem.getAudioInputStream(file);
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
        AudioInputStream convertedStream = new AudioInputStream(this.audioStream, targetFormat, this.audioStream.getFrameLength());

       File outFile = new File("sound/temp.wav");
       AudioSystem.write(convertedStream, AudioFileFormat.Type.WAVE, outFile);

       return outFile;

    }

    public void close(){    
        this.clip.close();
    }

    private void modulateVolume() {
        // Get the current position in seconds
        // this needs changing
        // double sec = this.clip.getMicrosecondPosition() * 0.000_001;

        int frame = (this.clip.getFramePosition() % (this.endFrame - this.startFrame+1)); // to make it not dependent on the position
        double onewave = (1 / this.clip.getFormat().getFrameRate()); // 1 sec / 48k hz = 0.0000208 sec per hz
        double sec = frame * onewave; // im not getting an internship with this shenanigans

        double wave = Math.sin(Math.PI * sec);  // One full cycle per second
    
        float gain = (float)(wave * 18);  // ~ -18 to +18 dB
    
        // Clamp to allowable range of the gain control
        FloatControl gainControl = (FloatControl) this.clip.getControl(FloatControl.Type.MASTER_GAIN);
        float min = gainControl.getMinimum(); // often ~ -80 dB
        float max = gainControl.getMaximum(); // often ~ +6 dB
    
        gain = Math.max(min, Math.min(max, gain)); // clamp
        gainControl.setValue(gain);
    
        //System.out.println("Gain: " + gain + " dB");
    }

}
