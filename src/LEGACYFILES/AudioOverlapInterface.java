package LEGACYFILES;
import javax.sound.sampled.AudioInputStream;

public interface AudioOverlapInterface {
    public AudioInputStream overlapAudio(AudioInputStream[] aisArray) throws Exception;
    public AudioInputStream overlapTwoAudio(AudioInputStream ais1, AudioInputStream ais2) throws Exception;
    public AudioInputStream mergeTwoClips(AudioInputStream clip1, AudioInputStream clip2) throws Exception;
}