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
    
    /** for the pitch */
    public PitchShift(Note note){
        this.note = note;
        this.pitch = (float) note.getRow() / 10;
    }

    @Override
    public byte[] run(byte[] byteStream) throws Exception{

        try{
            return pitchShift(byteStream, pitch);

        }catch(UnsupportedAudioFileException e){

            System.err.println(e);

        }
        return null;
    }

    public byte[] pitchShift(byte[] byteArray, float pitchFactor) throws UnsupportedAudioFileException{

        int bufferSize = 1024;
        int overlap = 768;
        AudioFormat format = note.getPhoneme().getAis().getFormat();

        AudioDispatcher dispatcher = AudioDispatcherFactory.fromByteArray(byteArray, format, bufferSize, overlap);
        TarsosDSPBufferCollector collector = new TarsosDSPBufferCollector(format.isBigEndian(),overlap);
        PitchShifter pShifter = new PitchShifter(pitchFactor, format.getSampleRate(), bufferSize, overlap);

        //dispatcher.addAudioProcessor(new PitchShifter(1.2f,format.getSampleRate(),bufferSize,overlap));
        //dispatcher.addAudioProcessor(new TarsosDSPPitchBend(3, 3, bufferCount, 48000, bufferSize, overlap));

        dispatcher.addAudioProcessor(pShifter);
        dispatcher.addAudioProcessor(collector);
        dispatcher.run();

        byte[] processedBytes = collector.getBytes();
        return processedBytes;

    }
    
}
