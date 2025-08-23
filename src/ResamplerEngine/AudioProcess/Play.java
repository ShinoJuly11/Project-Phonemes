package ResamplerEngine.AudioProcess;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

import NoteEditor.TableEditor2.Note;

public class Play implements AudioProcess{

    AudioFormat audioFormat;

    public Play(Note note){
        this.audioFormat = note.getPhoneme().getFormat();
    }

    public Play(AudioFormat audioFormat){
        this.audioFormat = audioFormat;
    }

    @Override
    public byte[] run(byte[] byteStream) throws Exception {
        process(byteStream);
        return null;
    }



    public void process(byte[] byteStream) throws LineUnavailableException, IOException{

        ByteArrayInputStream bais = new ByteArrayInputStream(byteStream);

        DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
        SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);

        line.open(audioFormat);
        line.start();

        byte[] buffer = new byte[1024];
        int bytesRead;

            while ((bytesRead = bais.read(buffer)) != -1) {
                line.write(buffer, 0, bytesRead);
            }

        line.drain();
        line.close();
    }

    public void save(byte[] bytes) throws IOException{
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        AudioInputStream ais = new AudioInputStream(bais, audioFormat, bytes.length);
        File outfile = new File("sound/temp.wav");
        AudioSystem.write(ais, AudioFileFormat.Type.WAVE, outfile);


    }

    
    
}
