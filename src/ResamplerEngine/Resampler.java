package ResamplerEngine;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import javax.sound.sampled.AudioFormat;

import NoteEditor.TableEditor2.Note;
import ResamplerEngine.AudioProcess.PitchShift;
import ResamplerEngine.AudioProcess.Play;

public class Resampler {

    byte[] processedByteStream;
    AudioFormat aFormat = new AudioFormat(44100, 16, 1, true, false);

    public void process(ArrayList<Note> notes) throws Exception{

        // imma just write the AudioPlayback again

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        for (Note note: notes){

            AudioResampler resampler = new AudioResampler(note);
            resampler.add(new PitchShift(note, aFormat));
            resampler.process();
            byte[] byteStream = resampler.getByteStream();
            baos.write(byteStream);
            System.out.println("processed length = " + baos.size());

        }

        processedByteStream = baos.toByteArray();

    }

    Play play = new Play(aFormat);
    public void playback() throws Exception{
        play.run(processedByteStream);

    }

    public void save() throws IOException{
        play.save(processedByteStream);
    }


}
