import java.io.File;
import java.io.IOException;
import java.io.SequenceInputStream;
import java.util.Scanner;

import javax.sound.sampled.*; // the only dependancy in this whole thing


public class App {
    public static void main(String[] args) throws Exception {
        test_TimeStretcher();

        


    };


    // test for text to graphemes

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

        File file1 = new File("sound/Ko.wav");
        File file2 = new File("sound/fe.wav");

        Clip clip1 = AudioSystem.getClip();

        AudioInputStream ais1  = AudioSystem.getAudioInputStream(file1);
        AudioInputStream ais2  = AudioSystem.getAudioInputStream(file2);
        

        TimeStretch ts = new TimeStretch(ais1, ais2);

        AudioInputStream ais3 = AudioSystem.getAudioInputStream(ts.theActualTimeStretcher(ais1,ais2));
        clip1.open(ais3);
        
        clip1.start();
        Thread.sleep(50);

    }
        

}
