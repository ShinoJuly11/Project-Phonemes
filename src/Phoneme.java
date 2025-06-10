import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

// LETS RECREATE THE OTO FILE!!!!

public class Phoneme{

    private AudioInputStream ais;
    private int offset;
    private int overlap;
    private int audioLoopStart, audioLoopEnd;
    private int cutoff;
    private int preuttrance;

    public Phoneme(File file, int offset, int overlap, int cutoff, int preuttrance, int audioLoopStart, int audioLoopEnd) throws Exception{
        this.ais = AudioSystem.getAudioInputStream(file);
        this.offset = offset;
        this.overlap = overlap;
        this.audioLoopStart = audioLoopStart;
        this.audioLoopEnd = audioLoopEnd;
        this.cutoff = cutoff;
        this.preuttrance = preuttrance;
        
    }

    // get / set
    public AudioInputStream getAis(){
        return this.ais;
    }

    public void setAis(AudioInputStream ais){
        this.ais = ais;
    }

    public int getOffset() {
    return offset;
    }   

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getOverlap() {
        return overlap;
    }

    public void setOverlap(int overlap) {
        this.overlap = overlap;
    }

    public int getAudioLoopStart() {
        return audioLoopStart;
    }

    public void setAudioLoopStart(int audioLoopStart) {
        this.audioLoopStart = audioLoopStart;
    }

     public int getAudioLoopEnd() {
        return audioLoopEnd;
    }

    public void setAudioLoopEnd(int audioLoopEnd) {
        this.audioLoopEnd = audioLoopEnd;
    }

    public int getCutoff() {
        return cutoff;
    }

    public void setCutoff(int cutoff) {
        this.cutoff = cutoff;
    }

    public int getPreuttrance() {
        return preuttrance;
    }

    public void setPreuttrance(int preuttrance) {
        this.preuttrance = preuttrance;
    }

    public void printAll(){
        System.out.println(offset);
        System.out.println(overlap);
        System.out.println(cutoff);
        System.out.println(preuttrance);
        System.out.println(audioLoopStart);
        System.out.println(audioLoopEnd);
        
    }

}


