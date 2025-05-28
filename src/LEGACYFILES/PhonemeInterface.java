package LEGACYFILES;
import javax.sound.sampled.AudioInputStream;

public interface PhonemeInterface extends AudioOverlapInterface, AudioProcessorInterface, AudioVolumeInterface{

    public void Phoneme(AudioInputStream ais);
    public AudioInputStream getAis();
    public void setAis(AudioInputStream ais);

}
