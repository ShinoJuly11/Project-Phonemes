import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;

import javax.sound.sampled.*;


public class SolaAlgorithm {

    public SolaAlgorithm(){

    }
    /* not meant to be called just to store */
    protected class SolaClass {

    /*
     * this is just to store the bais and chunkstream into an array
     */

    ByteArrayInputStream bais;
    AudioInputStream ais;

    protected SolaClass(ByteArrayInputStream bais, AudioInputStream ais){

        this.bais = bais;
        this.ais = ais;

    }
        

}

    public ArrayList<SolaClass> wavToChunks(File file, float chunkDur) throws Exception{

        AudioInputStream ais = AudioSystem.getAudioInputStream(file);
        AudioFormat format = ais.getFormat();

        int bytesPerFrame = format.getFrameSize();
        float frameRate = format.getFrameRate();

        int chunkFrames = (int)(frameRate * chunkDur); 
        int chunkBytes = chunkFrames * bytesPerFrame;
        byte[] buffer = new byte[chunkBytes];
        int bytesRead;
    
        ArrayList<SolaClass> SolaClassArray = new ArrayList<SolaClass>();

        // so when reading the all of the file buffer

        
        while ((bytesRead = ais.read(buffer)) > 0){


            // added this i really need to learn how to actually manipulate bytes
            //this makes a copy of the buffer so every bais and ais have a 
            //clean buffer
            byte[] chunkCopy = new byte[bytesRead];
            System.arraycopy(buffer, 0, chunkCopy, 0, bytesRead);

            ByteArrayInputStream bais = new ByteArrayInputStream(chunkCopy);
            AudioInputStream chunkStream = new AudioInputStream(bais, format, bytesRead/bytesPerFrame);
            
            // sola class is the frame chunks
            SolaClass solaChunk = new SolaClass(bais, chunkStream);
            SolaClassArray.add(solaChunk);

        }

        // for (SolaClass sc : SolaClassArray){
        //     System.out.println(sc.ais + " " + sc.bais);
        // }

        return SolaClassArray;

    }

    public void mixTwoClips(AudioInputStream ais1, AudioInputStream ais2) throws Exception{

        int bufferSize = 4096;
        byte[] buffer1 = new byte[bufferSize];
        byte[] buffer2 = new byte[bufferSize];
        byte[] mixed = new byte[4096];

        int byteRead1, byteRead2;

        //read the buffers from two files;

        while(((byteRead1 = ais1.read(buffer1)) != -1) && ((byteRead2 = ais2.read(buffer2)) != -1)){

            for (int i = 0; i < byteRead1; i += 2) {

            // Convert bytes to 16-bit samples
            short sample1 = (short)((buffer1[i+1] << 8) | (buffer1[i] & 0xff));
            short sample2 = (short)((buffer2[i+1] << 8) | (buffer2[i] & 0xff));

            // Mix samples
            int mixedSample = sample1 + sample2;

            // Clamp to 16-bit range
            mixedSample = Math.max(Math.min(mixedSample, Short.MAX_VALUE), Short.MIN_VALUE);

            // Convert back to bytes
            mixed[i]   = (byte)(mixedSample & 0xff);
            mixed[i+1] = (byte)((mixedSample >> 8) & 0xff);

            }
            
        }
    }

    public void overlap(AudioInputStream ais1, AudioInputStream ais2) throws Exception{

        int bufferSize = 4096;
        byte[] buffer1 = new byte[bufferSize];
        byte[] buffer2 = new byte[bufferSize];
        int byteRead1, byteRead2;

        ByteArrayOutputStream baos1 = new ByteArrayOutputStream();
        ByteArrayOutputStream baos2 = new ByteArrayOutputStream();

        while(((byteRead1 = ais1.read(buffer1)) != -1) && ((byteRead2 = ais2.read(buffer2)) != -1)) {
            baos1.write(buffer1, 0, byteRead1);
            baos2.write(buffer2, 0, byteRead2);
        }

        byte[] audioBytes1 = baos1.toByteArray();
        byte[] audioBytes2 = baos2.toByteArray();

        byte[] returnByte = overlapAudio(audioBytes1, audioBytes2, 28000, ais1.getFormat());
        long frameLength = returnByte.length / ais1.getFormat().getFrameSize();
        ByteArrayInputStream bais1 = new ByteArrayInputStream(returnByte);
        AudioInputStream ais3 = new AudioInputStream(bais1, ais1.getFormat(), frameLength);

        File outFile = new File("sound/temp.wav");
        AudioSystem.write(ais3, AudioFileFormat.Type.WAVE, outFile);

        
}


    // this is just chatgpt but i really need to learn how to manipulate bytes
    private byte[] overlapAudio(byte[] music, byte[] overlay, int startFrame, AudioFormat format) {
        int frameSize = format.getFrameSize();  // e.g., 2 bytes for 16-bit mono
        int startByte = startFrame * frameSize;

        for (int i = 0; i < overlay.length - 1; i += 2) {
            int musicIndex = startByte + i;
            if (musicIndex + 1 >= music.length) break; // avoid overflow

            // Convert to shorts
            short musicSample = (short) ((music[musicIndex + 1] << 8) | (music[musicIndex] & 0xff));
            short overlaySample = (short) ((overlay[i + 1] << 8) | (overlay[i] & 0xff));

            // Mix and clamp
            int mixed = musicSample + overlaySample;
            mixed = Math.max(Math.min(mixed, Short.MAX_VALUE), Short.MIN_VALUE);

            // Write back
            music[musicIndex] = (byte) (mixed & 0xff);
            music[musicIndex + 1] = (byte) ((mixed >> 8) & 0xff);
        }

    return music;
    }



    //@Override
    public void playback(ArrayList<SolaClass> solaArray) throws Exception{

        byte[] buffer = new byte[4096];

        for (SolaClass sc : solaArray){
            int bytesRead;
            AudioFormat af = sc.ais.getFormat();
            SourceDataLine sdl = (SourceDataLine)AudioSystem.getLine(new DataLine.Info(SourceDataLine.class, af));
            sdl.open(af);
            sdl.start();

            while ((bytesRead = sc.ais.read(buffer)) != -1){
                sdl.write(buffer,0,bytesRead);
            }
            sc.ais.close();

            sdl.drain();
            sdl.stop();
            sdl.close();
        }

    }



}
