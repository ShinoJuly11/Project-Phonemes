package LEGACYFILES;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import javax.sound.sampled.*;


public class SolaAlgorithm{
    // just an empty constructor lol wut
    public SolaAlgorithm(){

    }
    /* not meant to be called just to store */
    protected class SolaClass {

    /*
     * this is just to store the bais and chunkstream into an array
     */

    ByteArrayInputStream bais;
    AudioInputStream ais;

    public SolaClass(ByteArrayInputStream bais, AudioInputStream ais){

        this.bais = bais;
        this.ais = ais;

    }
}

    public ArrayList<SolaClass> wavToChunks(File file, float chunkDur) throws Exception{

        //honestly reading this idk what this does anymore

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
        return SolaClassArray;

    }

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

public AudioInputStream volumeAudio(AudioInputStream ais, float volumeGradient) throws Exception{

        byte[] audioBytes = AISToByte(ais);
        int frameSize = ais.getFormat().getFrameSize();

        for (int x = 0; x < (ais.getFrameLength() / frameSize); x++){
            int byteIndex = x * frameSize;
            short sample = (short) ((audioBytes[byteIndex+1] << 8) | (audioBytes[byteIndex] & 0xff));

            sample = (short) (sample * volumeGradient);

            audioBytes[byteIndex] = (byte) (sample & 0xff);
            audioBytes[byteIndex + 1] = (byte) ((sample >> 8) & 0xff);

        }

        ByteArrayInputStream bais = new ByteArrayInputStream(audioBytes);
        AudioInputStream returnAIS = new AudioInputStream(bais, ais.getFormat(), ais.getFrameLength());

        return returnAIS;

    }
    
    public AudioInputStream fadeOutAudio(AudioInputStream ais, int fadeFrames) throws Exception{

        byte[] audioBytes = AISToByte(ais);
        int frameSize = ais.getFormat().getFrameSize();
        int totalFrames = (int) ais.getFrameLength();
        int startFrame = totalFrames - fadeFrames;
        if (startFrame < 0) startFrame = 0;

        for (int x = startFrame; x < totalFrames; x++){

            float fadeFactor = 1.0f - ((float)(x - startFrame) / fadeFrames); // 1 -> 0

            int byteIndex = x * frameSize;
            short sample = (short) ((audioBytes[byteIndex+1] << 8) | (audioBytes[byteIndex] & 0xff));
            sample = (short) (sample * fadeFactor);

            // Write back
            audioBytes[byteIndex] = (byte) (sample & 0xff);
            audioBytes[byteIndex + 1] = (byte) ((sample >> 8) & 0xff);

        }

        ByteArrayInputStream bais = new ByteArrayInputStream(audioBytes);
        AudioInputStream returnAIS = new AudioInputStream(bais, ais.getFormat(), ais.getFrameLength());

        return returnAIS;

    }
    public AudioInputStream fadeInAudio(AudioInputStream ais, int fadeFrames) throws Exception{

        byte[] audioBytes = AISToByte(ais);
        int frameSize = ais.getFormat().getFrameSize();
        int totalFrames = (int) ais.getFrameLength();

        for (int x = 0; x < totalFrames; x++){

            float fadeFactor = (float)(x) / fadeFrames; // 0 -> 1

            int byteIndex = x * frameSize;
            short sample = (short) ((audioBytes[byteIndex+1] << 8) | (audioBytes[byteIndex] & 0xff));

            sample = (short) (sample * fadeFactor);

            // Write back
            audioBytes[byteIndex] = (byte) (sample & 0xff);
            audioBytes[byteIndex + 1] = (byte) ((sample >> 8) & 0xff);

        }

        ByteArrayInputStream bais = new ByteArrayInputStream(audioBytes);
        AudioInputStream returnAIS = new AudioInputStream(bais, ais.getFormat(), ais.getFrameLength());

        return returnAIS;

    }


    public AudioInputStream overlapAudio(AudioInputStream[] aisArray) throws Exception{

         ArrayList<byte[]> audioBytesList = new ArrayList<>();

        for (AudioInputStream ais : aisArray){
            byte[] audioBytes = AISToByte(ais);
            audioBytesList.add(audioBytes);
        }

        byte[] returnByte = audioBytesList.get(0);
        int startFrame = 24000; // needs fixing later
                   
        for (int x = 1; x < audioBytesList.size(); x++){
                returnByte = overlapAudio(returnByte,
                                            audioBytesList.get(x),
                                            startFrame,
                                            aisArray[x].getFormat());

                startFrame += 24000;

        }

        long length = returnByte.length / aisArray[0].getFormat().getFrameSize();

        ByteArrayInputStream bais = new ByteArrayInputStream(returnByte);
        AudioInputStream returnAIS = new AudioInputStream(bais, aisArray[0].getFormat(), length);
        System.out.println(length);

        return returnAIS;
        
    }
    

    public AudioInputStream overlapTwoAudio(AudioInputStream ais1, AudioInputStream ais2) throws Exception{

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
        int startFrame = 28000;

        byte[] returnByte = overlapAudio(audioBytes1, audioBytes2, startFrame, ais1.getFormat());

        long framelength1 = audioBytes1.length / ais1.getFormat().getFrameSize();
        long framelength2 = audioBytes2.length / ais2.getFormat().getFrameSize();
        long frameLength = (framelength1 + framelength2 - 28000);

        //long frameLength = returnByte.length / ais1.getFormat().getFrameSize();
        ByteArrayInputStream bais1 = new ByteArrayInputStream(returnByte);
        AudioInputStream ais3 = new AudioInputStream(bais1, ais1.getFormat(), frameLength);

        return ais3;
        
}

    // this is just chatgpt but i really need to learn how to manipulate bytes
    //for mixing
    private byte[] overlapAudio(byte[] music, byte[] overlay, int startFrame, AudioFormat format) {
        int frameSize = format.getFrameSize();
        int startByte = startFrame * frameSize;

        int newLength = Math.max(music.length, startByte + overlay.length);
        byte[] result = new byte[newLength];

        // Copy original music to result
        System.arraycopy(music, 0, result, 0, music.length);

        for (int i = 0; i < overlay.length - 1; i += 2) {
            int musicIndex = startByte + i;
            if (musicIndex + 1 >= result.length) break;

            short musicSample = 0;
            if (musicIndex + 1 < music.length) {
                musicSample = (short) ((result[musicIndex + 1] << 8) | (result[musicIndex] & 0xff));
            }

            short overlaySample = (short) ((overlay[i + 1] << 8) | (overlay[i] & 0xff));

            int mixed = musicSample + overlaySample;
            mixed = Math.max(Math.min(mixed, Short.MAX_VALUE), Short.MIN_VALUE);

            result[musicIndex] = (byte) (mixed & 0xff);
            result[musicIndex + 1] = (byte) ((mixed >> 8) & 0xff);
        }

        return result;
    }

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
