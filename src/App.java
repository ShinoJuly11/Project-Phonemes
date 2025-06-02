
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import javax.sound.sampled.*; // the only dependancy in this whole thing

import LEGACYFILES.ClipClass;
import LEGACYFILES.SolaAlgorithm;
import LEGACYFILES.TimeStretch;
import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.PitchShifter;
import be.tarsos.dsp.io.jvm.AudioDispatcherFactory;
import be.tarsos.dsp.io.jvm.AudioPlayer;


public class App {
    public static void main(String[] args) throws Exception {
        test_NoteUi();
        


    };


    // test for text to graphemes

    private static void test_NoteUi() throws Exception{
        File file1 = new File("sound/hello.wav");
        Phoneme phoneme = new Phoneme(file1,0,0,0,0,0,0);
        NoteUi ni = new NoteUi(phoneme);
        ni.createBox();

    }

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

    private static void test_TimeStretcher() throws Exception{

        File file1 = new File("sound/hello.wav");
        File file2 = new File("sound/fe.wav");

        Clip clip1 = AudioSystem.getClip();

        AudioInputStream ais1  = AudioSystem.getAudioInputStream(file1);
        AudioInputStream ais2  = AudioSystem.getAudioInputStream(file2);
        

        TimeStretch ts = new TimeStretch(ais1, ais2);
        ClipClass cc = new ClipClass(file1);

        //AudioInputStream ais3 = AudioSystem.getAudioInputStream(ts.mergeTwoClips(ais1,ais2));
        AudioInputStream ais3 = AudioSystem.getAudioInputStream(ts.stretchAudio(cc.sampleRate(file1 , 48000),1.25f));
        clip1.open(ais3);
        
        clip1.start();
        Thread.sleep(50);

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

        String filePath = "sound/temp.wav";
        AudioInputStream ais = AudioSystem.getAudioInputStream(new File(filePath));
        int sampleRate = (int) ais.getFormat().getSampleRate();
        int bufferSize = 512;
        int overlap = 500;

        AudioDispatcher dispatcher = AudioDispatcherFactory.fromFile(new File("sound/temp2.wav"),bufferSize,overlap);
        
        dispatcher.addAudioProcessor(new PitchShifter(1.5f,ais.getFormat().getSampleRate(),bufferSize,overlap));
        dispatcher.addAudioProcessor(new AudioPlayer(dispatcher.getFormat()));

        new Thread(dispatcher).start();
    }
        

}
