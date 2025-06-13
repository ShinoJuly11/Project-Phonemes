public interface AudioResampler {

    /**
     * this is created so I can rewrite some of the DSPs in the future note
     * but right now im depending on TarsosDSP cuz skill issue thats why
     */

     public void fadein();
     public void fadeout();
     public void pitchup();
     public void pitchdown();
     

}
