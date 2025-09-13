package ResamplerEngine;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.sound.midi.Sequence;
import javax.sound.sampled.AudioFormat;

import NoteEditor.Mediator;
import NoteEditor.TableEditor2.Note;
import ResamplerEngine.AudioProcess.AudioFade;
import ResamplerEngine.AudioProcess.AudioLoop;
import ResamplerEngine.AudioProcess.PitchShift;
import ResamplerEngine.AudioProcess.Play;
import ResamplerEngine.AudioProcess.AudioFade.FadeType;

public class Resampler {

    AudioFormat aFormat = new AudioFormat(44100, 16, 1, true, false);
    Mediator mediator;
    byte[] processedByteStream;

    public Resampler(Mediator mediator){
        this.mediator = mediator;
    }

    public Sequence getSequence(){
        return mediator.getMidiConstructor().getSequence();
    }

    // public void process(ArrayList<Note> notes) throws Exception{

    //     // imma just write the AudioPlayback again

    //     // EVERY NOTE PROCESSOR

    //     byte[] temp = new byte[0];

    //     for (Note note: notes){

    //         AudioResampler resampler = new AudioResampler(note,aFormat);
    //         resampler.add(new AudioFade(FadeType.FADE_IN, note.getPhoneme().getOffset()));
    //         resampler.add(new AudioLoop(this.getSequence().getResolution()));
    //         resampler.add(new PitchShift());
    //         resampler.add(new AudioFade(FadeType.FADE_OUT, note.getPhoneme().getCutoff()));
    //         resampler.process();


    //         byte[] byteStream = resampler.getByteStream();
    //         temp = overlapAudio(temp, byteStream, note.getPhoneme().getOverlap());
    //         //System.out.println("processed length = " + baos.size());

    //     }

    //     processedByteStream = temp;

    // }


    Play play = new Play(aFormat);
    public void playback() throws Exception{
        play.run(processedByteStream);

    }

    public void save() throws IOException{
        play.save(processedByteStream);
    }

    public void process(ArrayList<Note> notes) throws Exception {
        byte[] accumulated = new byte[0]; // running mix of all notes

        for (Note note : notes) {
            // Resample and apply audio effects
            AudioResampler resampler = new AudioResampler(note, aFormat);
            resampler.add(new AudioLoop(this.getSequence().getResolution()));
            resampler.add(new PitchShift());
            resampler.add(new AudioFade(FadeType.FADE_IN));
            resampler.add(new AudioFade(FadeType.FADE_OUT));
            resampler.process();

            byte[] currentNote = resampler.getByteStream();
            int overlapFrames = note.getPhoneme().getOverlap();

            // Calculate start frame relative to the end of accumulated audio
            
            int startFrame = Math.max(0, (accumulated.length / aFormat.getFrameSize()) - overlapFrames);

            if(overlapFrames > (accumulated.length / aFormat.getFrameSize())){
                System.out.println("overlap too big");
            }

            // Mix the current note into the accumulated audio
            accumulated = overlapAudio(accumulated, currentNote, startFrame);
        }

        processedByteStream = accumulated;
    }

    private byte[] overlapAudio(byte[] music, byte[] overlay, int startFrame) {
        int frameSize = aFormat.getFrameSize();
        int startByte = startFrame * frameSize;

        int newLength = Math.max(music.length, startByte + overlay.length);
        byte[] result = new byte[newLength];

        // Copy original music into the result buffer
        System.arraycopy(music, 0, result, 0, music.length);

        // Mix overlay samples into result
        for (int i = 0; i < overlay.length - 1; i += 2) {
            int musicIndex = startByte + i;
            if (musicIndex + 1 >= result.length) break;

            // Get existing sample from music
            short musicSample = 0;
            if (musicIndex + 1 < music.length) {
                musicSample = (short) ((result[musicIndex + 1] << 8) | (result[musicIndex] & 0xff));
            }

            // Get sample from overlay
            short overlaySample = (short) ((overlay[i + 1] << 8) | (overlay[i] & 0xff));

            // Mix the samples and clamp to prevent overflow
            int mixed = musicSample + overlaySample;
            mixed = Math.max(Math.min(mixed, Short.MAX_VALUE), Short.MIN_VALUE);

            result[musicIndex] = (byte) (mixed & 0xff);
            result[musicIndex + 1] = (byte) ((mixed >> 8) & 0xff);
        }

        return result;
    }
}
