package ResamplerEngine.AudioProcess;

import java.io.ByteArrayInputStream;
import java.io.IOException;

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
        var frameLength = audioFormat.getFrameSize() / byteStream.length;
        
        AudioInputStream ais = new AudioInputStream(bais, audioFormat, (long) frameLength);

        DataLine.Info info = new DataLine.Info(SourceDataLine.class, ais.getFormat());
        SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);

        line.open(ais.getFormat());
        line.start();

        byte[] buffer = new byte[1024];
        int bytesRead;

            while ((bytesRead = ais.read(buffer, 0, buffer.length)) != -1) {
                line.write(buffer, 0, bytesRead);
            }

        line.drain();
        line.close();
        ais.close();
    }

    
    
}
