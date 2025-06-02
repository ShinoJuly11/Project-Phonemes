import javax.sound.sampled.AudioInputStream;
import javax.swing.*; // UI implementation

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class NoteUi{

    class GraphCanvas extends JPanel {

    // in order to draw we have to override JPanel to our own custom graph
    @Override
        protected void paintComponent(Graphics g){
            super.paintComponent(g);
            g.drawLine(100,500,500,500); // X axis;
            g.drawString("X-axis", 300, 530); // X axis

            g.drawLine(100,100,100,500); // y axis;
            g.drawString("Y-Axis", 0, 300); // y axis
            
        }
    }

    class GraphPlotGraph extends JPanel {

        byte[] bytes; //storing all the data from the byteAudioOutputStream;
        int frameLength;
        int frameSize = 2; // mono audio



        public GraphPlotGraph(AudioInputStream ais, long frameLength) throws Exception{
            this.bytes = aisToByte(ais);
            this.frameLength = (int) frameLength;

        }

        private byte[] aisToByte(AudioInputStream ais) throws Exception{
            int bufferSize = 4096;
            byte[] buffer = new byte[bufferSize];
            int byteRead;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            //while bytes are not empty
            while ((byteRead = ais.read(buffer)) != -1){
                baos.write(buffer, 0, byteRead);
            }

            byte[] audioBytes = baos.toByteArray();

            return audioBytes; 
        } 

        //a way to convert a signed 16bit pcm into a number;
        private ArrayList<Short> byteToShort(){

            ArrayList<Short> shortArray = new ArrayList<>();
            
            for (int x = 0; x < (this.frameLength / this.frameSize); x++){
                int byteIndex = x * this.frameSize;
                short sample = (short) ((this.bytes[byteIndex+1] << 8) | (this.bytes[byteIndex] & 0xff));
                shortArray.add(sample);
            }

            return shortArray;

        }


        @Override
        protected void paintComponent(Graphics g){
            super.paintComponent(g);
            ArrayList<Short> bts = byteToShort();
            int arrayLength = bts.size();
            int max = 32000;
            int min = -32000;
            int yaxis = 640;  // panel height in pixels
            int panelWidth = 900;

            double scale = (double) yaxis / (max - min); // e.g., 640 / 64000 = 0.01
            double xScale = (double) panelWidth / arrayLength;
            //int counter = 1;

            for (int x = 0; x < arrayLength; x++) {
                short value = bts.get(x);
                int ydot = (int) ((max - value) * scale); // invert so high values go up
                int xdot = (int) (x * xScale);             // scaled X position
                g.fillOval(xdot, ydot, 2, 2);
                
            }

        }
    }

    Phoneme phoneme;

    public NoteUi(Phoneme phoneme){
        this.phoneme = phoneme;

    }

    public void createBox() throws Exception{

        JFrame frame = new JFrame();
        GraphPlotGraph p = new GraphPlotGraph(this.phoneme.getAis(),this.phoneme.getAis().getFrameLength());
        p.setPreferredSize(new java.awt.Dimension(1000, 800));
        frame.add(p);
        frame.pack();
        frame.setLayout(null);
        frame.setVisible(true);

    }
}