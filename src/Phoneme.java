import java.io.ByteArrayOutputStream;
import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

// LETS RECREATE THE OTO FILE!!!!

public class Phoneme{

    private AudioInputStream ais;
    private byte[] byteStream;
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
        this.byteStream = AISToByte(ais);
    }

    // methods

    private byte[] AISToByte(AudioInputStream ais) throws Exception{
            int bufferSize = 4096;
            byte[] buffer = new byte[bufferSize];
            int byteRead;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            //while bytes are not empty
            while ((byteRead = ais.read(buffer)) != -1){
                baos.write(buffer, 0, byteRead);
            }

            byte[] audioBytes = baos.toByteArray();

            return audioBytes; 
    }  

    // get / set

    public byte[] getByteStream(){
        return this.byteStream;
    }

    public void setByteStream(byte[] bs){
        this.byteStream = bs;
    }
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


