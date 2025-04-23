import java.util.Scanner;

public class App {
    public static void main(String[] args) throws Exception {

        Scanner scanner = new Scanner(System.in);
        SentenceTokenizer sentence = new SentenceTokenizer();
        TextToGraphemes ttg = new TextToGraphemes();

        System.out.println("Type a sentence");
        sentence.sentenceToWords(scanner.nextLine());
        
        System.out.println(ttg.graphemeChunker(sentence.getArrayString()));
        scanner.close();
        


    }
}
