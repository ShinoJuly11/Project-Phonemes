package LEGACYFILES;
import javax.sound.sampled.AudioInputStream;

public interface AudioProcessorInterface {
    public AudioInputStream playFrameRange(AudioInputStream originalAis, long startFrame, Long endFrame) throws Exception;
    public AudioInputStream changeSampleRate(AudioInputStream ais, float targetRate) throws Exception;
    public AudioInputStream stretchAudio(AudioInputStream inputAIS, float stretchFactor) throws Exception;


}
