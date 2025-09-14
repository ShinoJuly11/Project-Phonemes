package ResamplerEngine;
import java.io.IOException;
import java.util.ArrayList;

import javax.sound.midi.Sequence;
import javax.sound.sampled.AudioFormat;

import NoteEditor.Mediator;
import NoteEditor.TableEditor2.Note;
import ResamplerEngine.AudioProcess.*;
import ResamplerEngine.AudioProcess.AudioFade.FadeType;

public class Resampler {

    //Utau ahh resampler here
    //imma rewrite this into a vocoder + Spectral Peak Synthesis during the write up of my project
    //propah vocaloid resampler ^^

    AudioFormat aFormat = new AudioFormat(44100, 16, 1, true, false);
    Mediator mediator;
    byte[] processedByteStream;

    public Resampler(Mediator mediator){
        this.mediator = mediator;
    }

    public Sequence getSequence(){
        return mediator.getMidiConstructor().getSequence();
    }

    Play play = new Play(aFormat);
    public void playback() throws Exception{
        play.run(processedByteStream);

    }

    public void save() throws IOException{
        play.save(processedByteStream);
    }

    public void process(ArrayList<Note> notes) throws Exception {
        byte[] accumulated = new byte[0];

        for (Note note : notes) {

            AudioResampler resampler = new AudioResampler(note, aFormat);
            resampler.add(new AudioOffset());
            resampler.add(new AudioCutoff());
            resampler.add(new AudioLoop(this.getSequence().getResolution()));   //stretch
            resampler.add(new PitchShift()); //pitch
            resampler.add(new AudioFade(FadeType.FADE_IN)); //overlap 
            resampler.add(new AudioFade(FadeType.FADE_OUT));
            resampler.process();

            //preutturance when for making graphemes from phonemes 

            byte[] currentNote = resampler.getByteStream();
            int overlapFrames = note.getPhoneme().getOverlap();

            int startFrame = Math.max(0, (accumulated.length / aFormat.getFrameSize()) - overlapFrames);

            if(overlapFrames > (accumulated.length / aFormat.getFrameSize())){
                System.out.println("overlap too big");
            }
            accumulated = overlapAudio(accumulated, currentNote, startFrame);
        }

        processedByteStream = accumulated;
    }

    private byte[] overlapAudio(byte[] music, byte[] overlay, int startFrame) {
        int frameSize = aFormat.getFrameSize();
        int startByte = startFrame * frameSize;

        int newLength = Math.max(music.length, startByte + overlay.length);
        byte[] result = new byte[newLength];

        System.arraycopy(music, 0, result, 0, music.length);

        for (int i = 0; i < overlay.length - 1; i += 2) {
            int musicIndex = startByte + i;
            if (musicIndex + 1 >= result.length) break;

            short musicSample = 0;
            if (musicIndex + 1 < music.length) {
                musicSample = (short) ((result[musicIndex + 1] << 8) | (result[musicIndex] & 0xff));
            }

            short overlaySample = (short) ((overlay[i + 1] << 8) | (overlay[i] & 0xff));

            int mixed = musicSample + overlaySample;
            mixed = Math.max(Math.min(mixed, Short.MAX_VALUE), Short.MIN_VALUE);

            result[musicIndex] = (byte) (mixed & 0xff);
            result[musicIndex + 1] = (byte) ((mixed >> 8) & 0xff);
        }

        return result;
    }
}
