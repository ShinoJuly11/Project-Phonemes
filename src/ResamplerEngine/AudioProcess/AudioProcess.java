package ResamplerEngine.AudioProcess;

import javax.sound.sampled.AudioFormat;

import NoteEditor.TableEditor2.Note;

public interface AudioProcess{

    public byte[] run(byte[] byteStream) throws Exception;
    public void addNote(Note note);
    public void addAudioFormat(AudioFormat audioFormat);

}