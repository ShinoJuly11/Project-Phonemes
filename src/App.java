import java.util.Scanner;

public class App {
    public static void main(String[] args) throws Exception {
        


    };




    // test for text to graphemes

    private void test_SentenceTokenizer(){
        Scanner scanner = new Scanner(System.in);
        SentenceTokenizer sentence = new SentenceTokenizer();

        System.out.println("Type a sentence");
        sentence.sentenceToWords(scanner.nextLine());
        
        System.out.println(sentence.graphemeChunker(sentence.getArrayString()));
        scanner.close();
    }
}
