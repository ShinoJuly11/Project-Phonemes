import java.io.File;
import java.io.IOException;
import java.io.SequenceInputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * 
 * Clip class handles the creation of the Clip audio line and playback
 * 
 * Basic DSP with overlap-add using java sound
 * need to implement SONA soon
 * 
 */

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
        
        changeSampleRate(18_000);
        this.clip.start();
        do {
            Thread.sleep(100);
        }
        while (this.clip.isRunning());
    }

    private void changeSampleRate(float targetRate) throws LineUnavailableException, IOException, UnsupportedAudioFileException{

        AudioFormat sourceFormat = this.clip.getFormat();

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

        // Prepare new clip with converted format
        DataLine.Info info = new DataLine.Info(Clip.class, targetFormat);
        this.clip = (Clip) AudioSystem.getLine(info);
        this.clip.open(convertedStream);

        System.out.println(this.clip.getFormat().getSampleRate());
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
