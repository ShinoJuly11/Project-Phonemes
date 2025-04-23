import java.util.ArrayList;
import java.util.List;


public class SentenceTokenizer {

    /**
     * this class is where there is a sentence and we split it into words
     */
    private String text;
    private List<String> arrayString = new ArrayList<String>();
    private List<char[]> arrayChar = new ArrayList<char[]>();


    // basic contstructor here
    // public SentenceTokenizer(String text){
    //     this.text = text;
    // }

    // get and setters

    public List<char[]> getArrayChar(){
        return this.arrayChar;
    }

    public List<String> getArrayString(){
        return this.arrayString;
    }

    public String getText(){
        return this.text;
    }

    // methods

    public void sentenceToWords(String sentence){

        String[] words = sentence.trim().split("\\s+");
        for (String word : words){
            this.arrayString.add(word);
        }

    }

    public void wordToCharArray() {
        //bruh THIS IS in java i wish if they have indexing for strings

        for (String word : this.arrayString) {
            char[] charArray = word.toCharArray(); //tochararray is java only 
            this.arrayChar.add(charArray);

            charArrayToChar();
                
        }

    }

    public void charArrayToChar() {

        for (char[] index : arrayChar){

            for (char x : index){
                System.out.println(x);
            }
        }
    }





}
