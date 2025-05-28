import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

public class Phoneme{

    private AudioInputStream ais;

    public Phoneme(File file) throws Exception{
        this.ais = AudioSystem.getAudioInputStream(file);
    }

    // get / set
    public AudioInputStream getAis(){
        return this.ais;
    }

    public void setAis(AudioInputStream ais){
        this.ais = ais;
    }
    
}


