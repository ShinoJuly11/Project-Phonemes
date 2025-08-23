package ResamplerEngine.AudioProcess;

import java.util.ArrayList;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.UnsupportedAudioFileException;

import TarsosDSPCustom.TarsosDSPBufferCollector;
import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.io.jvm.AudioDispatcherFactory;
import be.tarsos.dsp.AudioProcessor;

public abstract class TarsosDSPProcessors implements AudioProcess{

    int bufferSize = 1024;
    int overlap = 768;
    ArrayList<AudioProcessor> processorArray = new ArrayList<>();
    AudioDispatcher dispatcher;
    TarsosDSPBufferCollector bufferCollector;
    
    public byte[] processedByteStream;

    public byte[] getProcessedByteStream(){
        return this.processedByteStream;
    }


    public TarsosDSPProcessors(byte[] byteArray, AudioFormat audioFormat) throws UnsupportedAudioFileException{
        dispatcher = AudioDispatcherFactory.fromByteArray(byteArray, audioFormat, bufferSize, overlap);
        bufferCollector = new TarsosDSPBufferCollector(audioFormat.isBigEndian(), overlap);
        
    }

    public void add(){
        //processorArray.add(process);
    }

    @Override
    public byte[] run(byte[] bytestream){

        for (AudioProcessor process : processorArray){
            dispatcher.addAudioProcessor(process);
        }

        dispatcher.addAudioProcessor(bufferCollector);
        dispatcher.run();
        processedByteStream = bufferCollector.getBytes();
        
        return bytestream;

    }
    
    




}
