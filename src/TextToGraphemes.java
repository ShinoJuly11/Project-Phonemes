import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher; // probably using it for the matcher and pattern
import java.util.regex.Pattern;

public class TextToGraphemes {

    //String graphemeRule = "ough|eigh|igh|tch|sh|ch|th|ph|wh|ck|ng|qu|[a-z]";

    String graphemeRule = "aigh|ayer|eigh|ngue|ough|dge|eue|eur|ear|ere|eir|gue|ieu|iew|igh|oeu|our|uoy|ae|ai|ar|au|ay|bb|cc|ce|ch|ci|ck|dd|di|ea|ee|ei|eo|er|et|eu|ew|ey|ff|ft|ge|gg|gh|gn|gu|ie|is|kn|lf|ll|lm|mb|mm|ng|nn|oe|oo|or|ou|ey|ew|ph|pn|pp|ps|qu|rh|rr|sc|se|ss|st|th|tt|ue|ui|ur|uy|ve|wn|wr|ze|zz|[a-z]";
                           
    //    "aigh|ayer|eigh|ngue|ough|dge|eue|eur|ear|ere|eir|gue|ieu|
    //    iew|igh|oeu|our|uoy|ae|ai|ar|au|ay|bb|cc|ce|ch|ci|ck|dd|
    //    di|ea|ee|ei|eo|er|et|eu|ew|ey|ff|ft|ge|gg|gh|gn|gu|ie|is|
    //    kn|lf|ll|lm|mb|mm|ng|nn|oe|oo|or|ou|ey|ew|ph|pn|pp|ps|qu|
    //    rh|rr|sc|se|ss|st|th|tt|ue|ui|ur|uy|ve|wn|wr|ze|zz|[a-z]"

    // Constants are easy to map as theyre just constants
    // vowels we will run into ambiguity i need Maschin learnin to solve ambiguity

    // the implementation here is so dogshit
    // i know theres a better way of doing it

    //this is like solving the texttographemes without HMM/DNN solutions so
    //it will struggle against words like 'read' past tense or present tense
    // or cake

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