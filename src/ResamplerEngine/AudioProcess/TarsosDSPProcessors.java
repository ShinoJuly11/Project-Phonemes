package ResamplerEngine.AudioProcess;

import java.util.ArrayList;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.UnsupportedAudioFileException;

import NoteEditor.TableEditor2.Note;
import TarsosDSPCustom.TarsosDSPBufferCollector;
import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.io.jvm.AudioDispatcherFactory;
import be.tarsos.dsp.AudioProcessor;


/** uncessesary abstract class for the lolz */
public abstract class TarsosDSPProcessors implements AudioProcess{

    int bufferSize = 1024;
    int overlap = 768;

    Note note;
    AudioFormat audioFormat;
    ArrayList<AudioProcessor> processorArray = new ArrayList<>();
    public byte[] processedByteStream;

    public byte[] getProcessedByteStream(){
        return this.processedByteStream;
    }

    public abstract void addProcessors();

    @Override
    public byte[] run(byte[] bytestream) throws UnsupportedAudioFileException{

        AudioDispatcher dispatcher = AudioDispatcherFactory.fromByteArray(bytestream, audioFormat, bufferSize, overlap);
        TarsosDSPBufferCollector bufferCollector = new TarsosDSPBufferCollector(audioFormat.isBigEndian(), overlap);
        
        addProcessors();
        for (AudioProcessor process : processorArray){
            dispatcher.addAudioProcessor(process);
        }

        dispatcher.addAudioProcessor(bufferCollector);
        dispatcher.run();
        processedByteStream = bufferCollector.getBytes();
        
        return processedByteStream;

    }

    @Override
    public void addNote(Note note) {
        this.note = note;
    }

    @Override
    public void addAudioFormat(AudioFormat audioFormat) {
        this.audioFormat = audioFormat;
    }
    
    




}
