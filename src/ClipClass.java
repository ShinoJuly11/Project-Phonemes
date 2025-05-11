import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
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

public class ClipClass implements Runnable{

    Clip clip;
    AudioInputStream audioStream;
    int start, end, duration, startFrame, endFrame;

    public ClipClass(File file) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        this.audioStream = AudioSystem.getAudioInputStream(file);
        this.clip = AudioSystem.getClip();

        // convert frames
        this.end = clip.getFrameLength();
        this.start = 0;
        this.duration = (int)((clip.getFrameLength() / clip.getFormat().getFrameRate()) * 1000);

        this.clip.open(this.audioStream);

    }

    public void setFrame(int startFrame, int endFrame){
        this.startFrame = startFrame;
        this.endFrame = endFrame;
    }

    public void playback(int startFrame, int endFrame) throws InterruptedException{
        this.clip.setFramePosition(startFrame);
        this.clip.start();
        do {
            modulateVolume();
        }
        while (this.clip.isRunning());
    }

    public void run(){
        try {
            playback(this.startFrame, this.endFrame);
        } catch (InterruptedException e) {
            //idk if this works but OH WELL :)
            e.printStackTrace();
        }
    }

    public void close(){
        this.clip.close();
    }

    private void modulateVolume() {
        // Get the current position in seconds
        // this needs changing
        // double sec = this.clip.getMicrosecondPosition() * 0.000_001;

        int frame = (this.clip.getFramePosition() % (this.endFrame - this.startFrame)); // to make it not dependent on the position
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
