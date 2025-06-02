import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SentenceTokenizer {

    /**
     * this class is where there is a sentence and we split it into words
     */
    
    private String text;
    private List<String> arrayString = new ArrayList<String>();
    private List<char[]> arrayChar = new ArrayList<char[]>();

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

        String graphemeRule = "aigh|ayer|eigh|ngue|ough|dge|eue|eur|ear|ere|eir|gue|ieu|iew|igh|oeu|our|uoy|ae|ai|ar|au|ay|bb|cc|ce|ch|ci|ck|dd|di|ea|ee|ei|eo|er|et|eu|ew|ey|ff|ft|ge|gg|gh|gn|gu|ie|is|kn|lf|ll|lm|mb|mm|ng|nn|oe|oo|or|ou|ey|ew|ph|pn|pp|ps|qu|rh|rr|sc|se|ss|st|th|tt|ue|ui|ur|uy|ve|wn|wr|ze|zz|[a-z]|[A-Z]";
                           
    //    "aigh|ayer|eigh|ngue|ough|dge|eue|eur|ear|ere|eir|gue|ieu|
    //    iew|igh|oeu|our|uoy|ae|ai|ar|au|ay|bb|cc|ce|ch|ci|ck|dd|
    //    di|ea|ee|ei|eo|er|et|eu|ew|ey|ff|ft|ge|gg|gh|gn|gu|ie|is|
    //    kn|lf|ll|lm|mb|mm|ng|nn|oe|oo|or|ou|ey|ew|ph|pn|pp|ps|qu|
<<<<<<< HEAD
    //    rh|rr|sc|se|ss|st|th|tt|ue|ui|ur|uy|ve|wn|wr|ze|zz|[a-z]|[A-Z]"
=======
    //    rh|rr|sc|se|ss|st|th|tt|ue|ui|ur|uy|ve|wn|wr|ze|zz|[a-z]"
>>>>>>> main

    public List<String> graphemeChunker(List<String> listOfWords){
        //List<String> listOfwords = this.sToken.getArrayString();
        List<String> graphemes = new ArrayList<String>(); // to store each graphemes
        Pattern p = Pattern.compile(graphemeRule);

        for (String c : listOfWords){ // for words inside that damn word in list

            Matcher m = p.matcher(c);

            while (m.find()){
                graphemes.add(m.group());
            }

        }
        return graphemes;

    }





}
