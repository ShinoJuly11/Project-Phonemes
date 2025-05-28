
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.SequenceInputStream;
import java.util.ArrayList;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;

import LEGACYFILES.AudioOverlapInterface;


public class AudioOverlap{

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
    
    public AudioInputStream overlapTwoAudio(AudioInputStream ais1, AudioInputStream ais2, int startFrame) throws Exception{

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

        byte[] returnByte = overlapAudio(audioBytes1, audioBytes2, startFrame, ais1.getFormat());

        long framelength1 = audioBytes1.length / ais1.getFormat().getFrameSize();
        long framelength2 = audioBytes2.length / ais2.getFormat().getFrameSize();
        long frameLength = (framelength1 + framelength2 - 28000);

        //long frameLength = returnByte.length / ais1.getFormat().getFrameSize();
        ByteArrayInputStream bais1 = new ByteArrayInputStream(returnByte);
        AudioInputStream ais3 = new AudioInputStream(bais1, ais1.getFormat(), frameLength);

        return ais3;
        
    }

    public AudioInputStream mergeTwoClips(AudioInputStream clip1, AudioInputStream clip2) throws Exception {
        AudioFormat format = clip1.getFormat();

        AudioInputStream appended = new AudioInputStream(
            new SequenceInputStream(clip1, clip2),
            format,
            clip1.getFrameLength() + clip2.getFrameLength()
        );

        return appended;

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

}
