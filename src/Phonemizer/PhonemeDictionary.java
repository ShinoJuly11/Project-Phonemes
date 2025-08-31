package Phonemizer;

import java.util.ArrayList;
import java.util.HashMap;

public class PhonemeDictionary {

    public String grapheme;
    public HashMap<String, String[]> g2pDictionary = new HashMap<>();
    

    String graphemeRule = "aigh|ayer|eigh|ngue|ough|dge|eue|eur|ear|ere|eir|gue|ieu|iew|igh|oeu|our|uoy|ae|ai|ar|au|ay|bb|cc|ce|ch|ci|ck|dd|di|ea|ee|ei|eo|er|et|eu|ew|ey|ff|ft|ge|gg|gh|gn|gu|ie|is|kn|lf|ll|lm|mb|mm|ng|nn|oe|oo|or|ou|ey|ew|ph|pn|pp|ps|qu|rh|rr|sc|se|ss|st|th|tt|ue|ui|ur|uy|ve|wn|wr|ze|zz|[a-z]|[A-Z]";

    //what im about to do is very illegal but idk how to improve on it

    
    public String[] grapheme2phoneme(String grapheme){

        String[] array = {"b", "bb"};
        g2pDictionary.put("b" , array);

        

        return null;

    }
    public void add(){
    }


    
    
}