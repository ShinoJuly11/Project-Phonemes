package ResamplerEngine;
import java.io.ByteArrayOutputStream;
import java.io.File;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

// LETS RECREATE THE OTO FILE!!!!

public class Phoneme{

    private AudioInputStream ais;
    private byte[] byteStream;
    private byte[] processedByteStream;
    private int id;
    private String fileName;
    private String alias;
    private String comment;

    /**
     * offset
     * 
     * Plays the phoneme note early.
     * 
     */
    private int offset;

    /**
     * Overlap 
     * 
     * affects the phoneme note fade in and fade out (crossfade)
     * 
     */
    
    private int overlap;

    /**
     * 
     * @deprecated
     * AudioLoopStart, AudioLoopEnd
     * 
     * Used to loop vowels 
     * 
     * Instead Constentant and preuttrance is used
     */

    private int audioLoopStart, audioLoopEnd;

    /**
     * Cutoff
     * 
     * End of the phoneme note
     * 
     */
    private int cutoff;

    /**
     * Consonant
     * 
     * Start of the consonant sound
     * 
     * between consonant and preutturance its unlooped
     */
    private int consonant; // add later here

    /**
     * 
     * preuttrance
     * 
     * end of the consonant sound, beginning of the vowel sound (if any)
     * if none
     */
    private int preuttrance;

    /**
     * 
     * Pitch 
     * 
     * changes the pitch of the phoneme note
     */
    private float pitch;

    public Phoneme(){
        //i am so dumb
    }

    public Phoneme(Phoneme phoneme){

        this.id = phoneme.id;
        this.fileName = phoneme.fileName;
        this.alias = phoneme.alias;
        this.comment = phoneme.comment;
        this.offset = phoneme.offset;
        this.overlap = phoneme.overlap;
        this.audioLoopStart = phoneme.audioLoopStart;
        this.audioLoopEnd = phoneme.audioLoopEnd;
        this.cutoff = phoneme.cutoff;
        this.consonant = phoneme.consonant;
        this.preuttrance = phoneme.preuttrance;
        this.pitch = phoneme.pitch;
        
    }

    public Phoneme(String file, int offset, int overlap,int consonant,int preuttrance, int cutoff) throws Exception{
        this.ais = AudioSystem.getAudioInputStream(StringToFile(file));
        this.fileName = file;
        //this.alias = alias;
        this.offset = offset;
        this.overlap = overlap;
        this.cutoff = cutoff;
        this.consonant = consonant;
        this.preuttrance = preuttrance; // end of constantant to beginning of vowel
        this.byteStream = AISToByte(ais);
        //this.comment = comment;
    }

    /** 
     * @deprecated
     * DONT USE THIS OVERLOADED CLASS ANYMORE
     * 
     *  */

    public Phoneme(File file, int offset, int overlap, int cutoff, int preuttrance, int audioLoopStart, int audioLoopEnd) throws Exception{
        this.ais = AudioSystem.getAudioInputStream(file);
        this.offset = offset;
        this.overlap = overlap;
        this.audioLoopStart = audioLoopStart;
        this.audioLoopEnd = audioLoopEnd;
        this.cutoff = cutoff;
        this.preuttrance = preuttrance; // end of constantant to beginning of vowel
        this.byteStream = AISToByte(ais);
    }

    // methods

    private byte[] AISToByte(AudioInputStream ais) throws Exception{
            byte[] buffer = new byte[4096];
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

    private File StringToFile(String text){
        File file = new File(text);
        return file;
    }

    public int getId(){
        return this.id;
    }

    public void setId(int id){
        this.id = id;
    }

    public float getPitch(){
        return this.pitch;
    }

    public void setPitch(float pitch){
        this.pitch = pitch;
    }

    public void setFileName(String fs){
        this.fileName = fs;
    }

    public String getFileName(){
        return this.fileName;
    }

    public void setAlias(String alias){
        this.alias = alias;
    }

    public String getAlias(){
        return this.alias;
    }

    public void setComment(String comment){
        this.comment = comment;
    }

    public String getComment(){
        return this.comment;
    }

    public int getConsonant() {
        return consonant;
    }

    public void setConsonant(int consonant) {
        this.consonant = consonant;
    }

    public byte[] getProcessedByteStream(){
        return this.processedByteStream;
    }

    public void setProcessedByteStream (byte[] pbs){
        this.processedByteStream = pbs;
    }

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
        System.out.println(id);
        System.out.println(fileName);
        System.out.println(alias);
        System.out.println(offset);
        System.out.println(overlap);
        System.out.println(cutoff);
        System.out.println(preuttrance);
        System.out.println(audioLoopStart);
        System.out.println(audioLoopEnd);
        System.out.println(comment);
        
    }

    public AudioFormat getFormat(){
        return this.getAis().getFormat();
    }

}


