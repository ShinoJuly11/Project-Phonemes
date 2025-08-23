package ResamplerEngine;

import java.util.ArrayList;

import NoteEditor.TableEditor2.Note;
import ResamplerEngine.AudioProcess.AudioProcess;

public class AudioResampler {

    ArrayList<AudioProcess> audioProcesses = new ArrayList<>();
    byte[] byteStream;

    public AudioResampler(Note note){
        this.byteStream = note.getPhoneme().getByteStream();
    }

    public void add(AudioProcess ap){
        audioProcesses.add(ap);
    }

    public byte[] getByteStream(){
        return this.byteStream;
    }

    public void process() throws Exception{
        for (AudioProcess process: audioProcesses){
            this.byteStream = process.run(this.byteStream);
        }
    }






    


}
