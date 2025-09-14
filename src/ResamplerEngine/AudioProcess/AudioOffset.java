package ResamplerEngine.AudioProcess;

import javax.sound.sampled.AudioFormat;

import NoteEditor.TableEditor2.Note;

public class AudioOffset implements AudioProcess {
    Note note;
    AudioFormat aFormat;

    @Override
    public byte[] run(byte[] byteStream) throws Exception {
        return process(byteStream);
    }

    private byte[] process(byte[] byteStream){
        // offset is getting rid of the silence at the beginning of the note
        int offsetFrame = this.note.getPhoneme().getOffset(); // in frames
        int offset = offsetFrame * aFormat.getFrameSize();
        byte[] temp = new byte[byteStream.length - offset];
        System.arraycopy(byteStream, offset, temp, 0, temp.length);

        if (offset >= byteStream.length) {
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

}
