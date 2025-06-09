import javax.sound.sampled.AudioInputStream;

public class PhonemeUtils {

    /**
     * Concatenates two phonemes with an overlap of frames between them.
     *
     * @param p1 the first Phoneme
     * @param p2 the second Phoneme
     * @param overlap the number of frames to overlap between the two phonemes
     * @return a new AudioInputStream representing the concatenated phonemes
     * @throws Exception if an error occurs during processing
     *
     * @since v0.5.4 - 09/06/25
     * @version 1
     * @author ShinoJuly11
     */

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
