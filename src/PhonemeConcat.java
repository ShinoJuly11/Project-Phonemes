import javax.sound.sampled.AudioInputStream;

public class PhonemeConcat {

    public AudioInputStream concatTwoPhonemes(Phoneme p1, Phoneme p2, int overlap) throws Exception{
        AudioInputStream ais1 = p1.getAis();
        AudioInputStream ais2 = p2.getAis();
        AudioOverlap ao = new AudioOverlap();

        AudioInputStream returnAIS = ao.overlapTwoAudio(ais1,ais2, overlap);
        return returnAIS;
    }
    
}
