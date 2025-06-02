package LEGACYFILES;
import javax.sound.sampled.AudioInputStream;

public interface AudioVolumeInterface{

    public AudioInputStream volumeAudio(AudioInputStream ais, float volumeGradient) throws Exception;
    public AudioInputStream fadeOutAudio(AudioInputStream ais, int fadeFrames) throws Exception;
    public AudioInputStream fadeInAudio(AudioInputStream ais, int fadeFrames) throws Exception;

}
