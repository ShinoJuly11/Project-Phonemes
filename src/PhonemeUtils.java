import javax.sound.sampled.AudioInputStream;

public class PhonemeUtils {

    public AudioInputStream concatTwoPhonemes(Phoneme p1, Phoneme p2, int overlap) throws Exception{
        AudioInputStream ais1 = p1.getAis();
        AudioInputStream ais2 = p2.getAis();
        AudioOverlap ao = new AudioOverlap();
        AudioVolume av = new AudioVolume();

        AudioInputStream ais3 = av.fadeOutAudio(ais1, p1.getOverlap());
        AudioInputStream ais4 = av.fadeInAudio(ais2, p2.getPreuttrance());

        AudioInputStream returnAIS = ao.overlapTwoAudio(ais3,ais4,p1.getOverlap());
        return returnAIS;
    }

}
