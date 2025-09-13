package ResamplerEngine;

import java.util.ArrayList;

import javax.sound.sampled.AudioFormat;

import NoteEditor.TableEditor2.Note;
import ResamplerEngine.AudioProcess.AudioProcess;

public class AudioResampler {

    ArrayList<AudioProcess> audioProcesses = new ArrayList<>();
    byte[] byteStream;
    AudioFormat audioFormat; 
    Note note;
    

    public AudioResampler(Note note, AudioFormat aFormat){
        this.byteStream = note.getPhoneme().getByteStream();
        this.audioFormat = aFormat;
        this.note = note;

    }

    public void add(AudioProcess ap){
        ap.addNote(this.note);
        ap.addAudioFormat(this.audioFormat);
        audioProcesses.add(ap);

    }

    public byte[] getByteStream(){
        return this.byteStream;
    }

    public void process() throws Exception{
        byte[] tempStream = this.byteStream;

        for (AudioProcess process : audioProcesses) {
            tempStream = process.run(tempStream);
            System.out.println("bytestream length = " + tempStream.length);
        }

        this.byteStream = tempStream;
    }






    


}
