
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Scanner;

import javax.sound.sampled.*; // the only dependancy in this whole thing

import LEGACYFILES.ClipClass;
import LEGACYFILES.SolaAlgorithm;
import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.FadeIn;
import be.tarsos.dsp.FadeOut;
import be.tarsos.dsp.GainProcessor;
import be.tarsos.dsp.PitchShifter;
import be.tarsos.dsp.SilenceDetector;
import be.tarsos.dsp.WaveformSimilarityBasedOverlapAdd;
import be.tarsos.dsp.io.TarsosDSPAudioFormat;
import be.tarsos.dsp.io.jvm.AudioDispatcherFactory;
import be.tarsos.dsp.io.jvm.AudioPlayer;
import be.tarsos.dsp.util.fft.WindowFunction;
import be.tarsos.dsp.writer.WriterProcessor;

public class App {
    public static void main(String[] args) throws Exception {
        //test_AudioProcessor();
        //test_tarsosdsp();
        //test_solaAlgorithm_stretchAudio();

        test_NoteUi();
    };


    // test for text to graphemes

    private static void test_NoteUi() throws Exception{
        File file1 = new File("sound/hello.wav");
        Phoneme phoneme = new Phoneme(file1,400,25,35,700,10000,30000);
        NoteUi ni = new NoteUi(phoneme);
        ni.createBox();

    }

    // private static void test_AudioProcessor() throws Exception{
    //     File file1 = new File("sound/hello.wav");
    //     Phoneme phoneme = new Phoneme(file1,400,25,35,700,10000,40000);
    //     AudioPitch ap = new AudioPitch(phoneme);
    //     System.out.println(phoneme.getByteStream().length);
    //     ap.AudioPitchFactor(1f);
    //     AudioPlayback apl = new AudioPlayback();
    //     int frameSize = phoneme.getAis().getFormat().getFrameSize();
    //     int numFrames = phoneme.getByteStream().length / frameSize;
    //     apl.playback(phoneme, numFrames);

    //     File outfile = new File("sound/apb2.wav");
    //     ByteArrayInputStream bais = new ByteArrayInputStream(phoneme.getByteStream());
    //     AudioInputStream ais = new AudioInputStream(bais, phoneme.getAis().getFormat(), phoneme.getAis().getFrameLength());
    //     AudioSystem.write(ais, AudioFileFormat.Type.WAVE, outfile);
    //     System.out.println(phoneme.getByteStream().length);

    // }

    private static void test_SentenceTokenizer(){
        Scanner scanner = new Scanner(System.in);
        SentenceTokenizer sentence = new SentenceTokenizer();

        System.out.println("Type a sentence");
        sentence.sentenceToWords(scanner.nextLine());
        
        System.out.println(sentence.graphemeChunker(sentence.getArrayString()));
        scanner.close();
    }

    private static void test_ClipClass() throws UnsupportedAudioFileException, IOException, LineUnavailableException, InterruptedException{

        File sound1 = new File("sound/hello.wav");

        ClipClass clip1 = new ClipClass(sound1);

        clip1.playback();
    }

    private static void test_solaAlgorithm() throws Exception{

        File file1 = new File("sound/Ko.wav");
        File file2 = new File("sound/fe.wav");

        AudioInputStream ais1  = AudioSystem.getAudioInputStream(file1);
        AudioInputStream ais2  = AudioSystem.getAudioInputStream(file2);

        SolaAlgorithm sola = new SolaAlgorithm();
        //sola.playback(sola.wavToChunks(sound1 , 0.1f));
        AudioInputStream ais3 = sola.overlapTwoAudio(ais1, ais2);

        System.out.println(ais3.getFrameLength());
        System.out.println(ais1.getFrameLength());
        System.out.println(ais2.getFrameLength());

        File outfile = new File("sound/temp.wav");
        AudioSystem.write(ais3, AudioFileFormat.Type.WAVE, outfile);
    }

     private static void test_solaAlgorithm_volume() throws Exception{

        File file1 = new File("sound/hello.wav");

        AudioInputStream ais1 = AudioSystem.getAudioInputStream(file1);

        SolaAlgorithm sola = new SolaAlgorithm();
        //sola.playback(sola.wavToChunks(sound1 , 0.1f));
        AudioInputStream ais3 = sola.volumeAudio(ais1, 3f);

        File outfile = new File("sound/volume_3.wav");
        AudioSystem.write(ais3, AudioFileFormat.Type.WAVE, outfile);
    }

    private static void test_solaAlgorithm_stretchAudio() throws Exception{

        File file1 = new File("sound/hello.wav");

        AudioInputStream ais1 = AudioSystem.getAudioInputStream(file1);

        AudioPlayback ap = new AudioPlayback();
        //sola.playback(sola.wavToChunks(sound1 , 0.1f));
        AudioInputStream ais3 = ap.stretchAudio(ais1, 3);

        File outfile = new File("sound/volume_3.wav");
        AudioSystem.write(ais3, AudioFileFormat.Type.WAVE, outfile);
    }

    private static void test_solaAlgorithm_fadeOutAudio() throws Exception{

        File file1 = new File("sound/hello.wav");

        AudioInputStream ais1 = AudioSystem.getAudioInputStream(file1);

        SolaAlgorithm sola = new SolaAlgorithm();
        AudioInputStream ais3 = sola.fadeInAudio(ais1, 100000);

        File outfile = new File("sound/volume_FadeIn.wav");
        AudioSystem.write(ais3, AudioFileFormat.Type.WAVE, outfile);
    }

    private static void test_solaAlgorithm_overlapAudio() throws Exception{

        File file1 = new File("sound/Ko.wav");
        File file2 = new File("sound/fe.wav");

        AudioInputStream ais1 = AudioSystem.getAudioInputStream(file1);
        AudioInputStream ais2 = AudioSystem.getAudioInputStream(file2);
        AudioInputStream ais3  = AudioSystem.getAudioInputStream(file2);
        AudioInputStream ais4  = AudioSystem.getAudioInputStream(file2);
        AudioInputStream ais5  = AudioSystem.getAudioInputStream(file2);

        AudioInputStream[] aisArray = {ais1,ais2,ais3,ais4,ais5};

        SolaAlgorithm sa = new SolaAlgorithm();
        AudioInputStream ais = sa.overlapAudio(aisArray);
        
        File file = new File("sound/temp.wav");
        AudioSystem.write(ais, AudioFileFormat.Type.WAVE, file);

        
    }

    private static void test_tarsosdsp() throws Exception{

        AudioInputStream ais = AudioSystem.getAudioInputStream(new File("sound/hello.wav"));
        AudioFormat format = ais.getFormat();
        int bufferSize = 1024;
        int overlap = 768;

        AudioDispatcher dispatcher = AudioDispatcherFactory.fromPipe("sound/hello.wav",48000,bufferSize,overlap);
        TarsosDSPBufferCollector collector = new TarsosDSPBufferCollector(format.isBigEndian(),overlap);
        WaveformSimilarityBasedOverlapAdd wsola = new WaveformSimilarityBasedOverlapAdd(WaveformSimilarityBasedOverlapAdd.Parameters.speechDefaults
        (1.0f, 48000));
        
        int bufferCount = (int) ais.getFrameLength() % bufferSize;

        //dispatcher.addAudioProcessor(new PitchShifter(1.2f,format.getSampleRate(),bufferSize,overlap));
        
        //dispatcher.addAudioProcessor(new TarsosDSPPitchBend(3, 3, bufferCount, 48000, bufferSize, overlap));
        dispatcher.addAudioProcessor(new FadeIn(1));
        dispatcher.addAudioProcessor(new FadeOut(1));
        dispatcher.addAudioProcessor(new GainProcessor(1));
        dispatcher.addAudioProcessor(new AudioPlayer(dispatcher.getFormat()));
        dispatcher.addAudioProcessor(wsola);
        dispatcher.addAudioProcessor(collector);
        dispatcher.run();

        byte[] processedBytes = collector.getBytes();
        ByteArrayInputStream bais = new ByteArrayInputStream(processedBytes);
        int frameSize = format.getFrameSize();
        long numFrames = processedBytes.length / frameSize;
        AudioInputStream processedAIS = new AudioInputStream(bais, format, numFrames);

        File outfile = new File("sound/processed.wav");
        AudioSystem.write(processedAIS, AudioFileFormat.Type.WAVE, outfile);


    }

    public int frameBufferRatio(long frameLength, int bufferSize){
        return (int) frameLength % bufferSize;
        }
        

}
