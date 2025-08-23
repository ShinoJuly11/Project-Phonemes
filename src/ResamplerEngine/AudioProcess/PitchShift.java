package ResamplerEngine.AudioProcess;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.UnsupportedAudioFileException;

import NoteEditor.TableEditor2.Note;
import TarsosDSPCustom.TarsosDSPBufferCollector;
import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.PitchShifter;
import be.tarsos.dsp.io.jvm.AudioDispatcherFactory;

public class PitchShift implements AudioProcess {

    Note note;
    float pitch;
    AudioFormat audioFormat;
    
    /** for the pitch */
    public PitchShift(Note note, AudioFormat audioFormat){
        this.note = note;
        this.audioFormat = audioFormat;
        this.pitch = (float) note.getRow() / 10;
    }

    @Override
    public byte[] run(byte[] byteStream) throws Exception{

        try{
            int bufferSize = 1024;
            int overlap = 768;
            float pitchFactor = pitch;

            AudioDispatcher dispatcher = AudioDispatcherFactory.fromByteArray(byteStream, audioFormat, bufferSize, overlap);
            TarsosDSPBufferCollector collector = new TarsosDSPBufferCollector(audioFormat.isBigEndian(),overlap);
            PitchShifter pShifter = new PitchShifter(pitchFactor, audioFormat.getSampleRate(), bufferSize, overlap);

            dispatcher.addAudioProcessor(pShifter);
            dispatcher.addAudioProcessor(collector);
            dispatcher.run();

            byte[] processedBytes = collector.getBytes();
            return processedBytes;

        }catch(UnsupportedAudioFileException e){

            e.printStackTrace();

        }
        return null;

    }
    
}
