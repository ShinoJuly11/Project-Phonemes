package ResamplerEngine;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.sound.midi.Sequence;
import javax.sound.sampled.AudioFormat;

import NoteEditor.Mediator;
import NoteEditor.TableEditor2.Note;
import ResamplerEngine.AudioProcess.AudioLoop;
import ResamplerEngine.AudioProcess.PitchShift;
import ResamplerEngine.AudioProcess.Play;

public class Resampler {

    byte[] processedByteStream;
    AudioFormat aFormat = new AudioFormat(44100, 16, 1, true, false);
    Mediator mediator;

    public Resampler(Mediator mediator){
        this.mediator = mediator;
    }

    public Sequence getSequence(){
        return mediator.getMidiConstructor().getSequence();
    }

    public void process(ArrayList<Note> notes) throws Exception{

        // imma just write the AudioPlayback again

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        for (Note note: notes){

            AudioResampler resampler = new AudioResampler(note,aFormat);
            resampler.add(new AudioLoop(this.getSequence().getResolution()));
            resampler.add(new PitchShift());
            resampler.process();
            byte[] byteStream = resampler.getByteStream();
            baos.write(byteStream);
            //System.out.println("processed length = " + baos.size());

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
