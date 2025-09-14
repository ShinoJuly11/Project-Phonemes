package ResamplerEngine.AudioProcess;

import javax.sound.sampled.AudioFormat;

import NoteEditor.TableEditor2.Note;

public class AudioCutoff implements AudioProcess {

        Note note;
        AudioFormat aFormat;

        private byte[] process(byte[] byteStream){
        // cutoff is getting rid of the silence at the end of the note

        int cutoffFrame = this.note.getPhoneme().getCutoff(); // in frames
        int cutoff = cutoffFrame * aFormat.getFrameSize();
        byte[] temp = new byte[byteStream.length - cutoff];
        
        //byteStream will be bigger than temp so ends get cutoff
        System.arraycopy(byteStream, 0, temp, 0, temp.length);
        
        if (cutoff >= byteStream.length) {
                return byteStream;
            }
        
        return temp;

    }

    @Override
    public void addNote(Note note) {
        this.note = note;
    }

    @Override
    public void addAudioFormat(AudioFormat audioFormat) {
        this.aFormat = audioFormat;
    }

    @Override
    public byte[] run(byte[] byteStream) throws Exception {
        return process(byteStream);
    }

}
