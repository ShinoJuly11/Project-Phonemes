package ResamplerEngine.AudioProcess;
import javax.sound.sampled.AudioFormat;
import NoteEditor.TableEditor2.Note;

public class AudioFade implements AudioProcess {

    FadeType fadeType;
    int duration;
    Note note;
    AudioFormat aFormat;
    

    public enum FadeType{
        FADE_IN,
        FADE_OUT
    }

    public AudioFade(FadeType fadeType){
        this.fadeType = fadeType;
        
    }

    private byte[] fadeInAudio(byte[] byteStream) throws Exception{

            
            int fadeFrames = note.getPhoneme().getOverlap();
            byte[] audioBytes = byteStream;
            int frameSize = aFormat.getFrameSize();
            int totalFrames = byteStream.length / aFormat.getFrameSize();

            for (int x = 0; x < totalFrames; x++){

                float fadeFactor = (float) x / fadeFrames; // 0 -> 1

                int byteIndex = x * frameSize;
                short sample = (short) ((audioBytes[byteIndex+1] << 8) | (audioBytes[byteIndex] & 0xff));

                sample = (short) (sample * fadeFactor);

                // clamping?
                sample = (short) Math.max(Math.min(sample, Short.MAX_VALUE), Short.MIN_VALUE);

                // Write back
                audioBytes[byteIndex] = (byte) (sample & 0xff);
                audioBytes[byteIndex + 1] = (byte) ((sample >> 8) & 0xff);


            }

            return audioBytes;

    }

    private byte[] fadeOutAudio(byte[] byteStream) throws Exception{

        
        int fadeFrames = note.getPhoneme().getOverlap();
        byte[] audioBytes = byteStream;
        int frameSize = aFormat.getFrameSize();
        int totalFrames = byteStream.length / aFormat.getFrameSize();
        int startFrame = totalFrames - fadeFrames;

        if (startFrame < 0) startFrame = 0;

        for (int x = startFrame; (float) x < totalFrames; x++){

            float fadeFactor = 1.0f - ((float)(x - startFrame) / fadeFrames); // 1 -> 0

            int byteIndex = x * frameSize;
            short sample = (short) ((audioBytes[byteIndex+1] << 8) | (audioBytes[byteIndex] & 0xff));

            sample = (short) (sample * fadeFactor);
            sample = (short) Math.max(Math.min(sample, Short.MAX_VALUE), Short.MIN_VALUE);

            // Write back
            audioBytes[byteIndex] = (byte) (sample & 0xff);
            audioBytes[byteIndex + 1] = (byte) ((sample >> 8) & 0xff);

        }

        return audioBytes;
    }

    @Override
    public byte[] run(byte[] byteStream) throws Exception {

        switch (fadeType) {
            case FADE_IN:
                return fadeInAudio(byteStream);
            case FADE_OUT:
                return fadeOutAudio(byteStream);
            default:
                System.out.println("AudioFade has no enum check arguements");
                return byteStream;

        }

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
