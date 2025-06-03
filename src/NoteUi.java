import javax.sound.sampled.AudioInputStream;
import javax.swing.*; // UI implementation

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class NoteUi{

    class GraphPlotGraph extends JPanel {

        byte[] bytes; //storing all the data from the byteAudioOutputStream;
        int frameLength;
        int frameSize = 2; // mono audio
        int yaxis = 800;  // panel height in pixels
        int panelWidth = 800;
        int ypad = 200;

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

            double yscale = (double) yaxis / (max - min); // e.g., 640 / 64000 = 0.01
            double xScale = (double) panelWidth / arrayLength;

            for (int x = 0; x < arrayLength; x++) {
                short value = bts.get(x);
                int ydot = (int) ((max - value) * yscale); // invert so high values go up
                int xdot = (int) (x * xScale);             // scaled X position
                g.fillOval(xdot, ydot - ypad, 2, 2);
                
            }

        }
    }

    class GraphError extends JFrame {

        public GraphError(String text){

            JLabel label = new JLabel(text);
            add(label);
            pack();
            setVisible(true);

        }
    }

    class GraphLines extends JPanel {

        int value;

            public GraphLines(String text, int pValue){

                setLayout(new GridLayout(4,1));
                setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

                JPanel buttonPanel = new JPanel();
                buttonPanel.setLayout(new GridLayout(1,2));
                JButton button1 = new JButton("<");
                JButton button2 = new JButton(">");
                buttonPanel.add(button1);
                buttonPanel.add(button2);

                JLabel label1 = new JLabel(text, SwingConstants.CENTER);
                JTextField tf1 = new JTextField(Integer.toString(pValue), SwingConstants.CENTER);
                JButton button3 = new JButton("Enter");

                button3.addActionListener(e -> {
                    try {
                        int value = Integer.parseInt(tf1.getText());
                        setValue(value);
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "Please enter a valid number.");
                        tf1.setText("0"); 
                    }
                
                });

                button1.addActionListener(e -> {
                    try{

                        int value = Integer.parseInt(tf1.getText());
                        value -= 1;
                        tf1.setText(Integer.toString(value));
                        setValue(value);

                    } catch (NumberFormatException ex){
                        tf1.setText("0");
                    }

                
                });

                button2.addActionListener(e -> {
                    try{
                        int value = Integer.parseInt(tf1.getText());
                        value+= 1;
                        tf1.setText(Integer.toString(value));
                        setValue(value);

                    } catch (NumberFormatException ex){ // catches the empty textfield
                        tf1.setText("0");
                    }
                
                });

                add(label1);
                add(tf1);
                add(button3);
                add(buttonPanel);
                
                
            }
            // I CANT USE THE DAMN CLASS VARS THIS WILL DO.
            public void setValue(int value){
                System.out.println("Parsed value: " + this.value);
                this.value = value;
            }
        }

    class GraphLayout extends JPanel {

        public GraphLayout() {
            setLayout(new GridLayout(1, 6));
            add(new GraphLines("Offset", phoneme.getOffset()));
            add(new GraphLines("Overlap", phoneme.getOverlap()));
            add(new GraphLines("Cutoff", phoneme.getCutoff()));
            add(new GraphLines("Preuttrance", phoneme.getPreuttrance()));
            add(new GraphLines("AudioLoopStart", phoneme.getAudioLoopStart()));
            add(new GraphLines("AudioLoopEnd", phoneme.getAudioLoopEnd()));
        }
    }   

    Phoneme phoneme;

    public NoteUi(Phoneme phoneme){
        this.phoneme = phoneme;

    }

    public void createBox() throws Exception{

        JFrame frame = new JFrame();
        GraphPlotGraph p = new GraphPlotGraph(this.phoneme.getAis(),this.phoneme.getAis().getFrameLength());
        GraphLayout gl = new GraphLayout();

        frame.setLayout(new GridLayout(2,1));
        p.setPreferredSize(new Dimension(800, 400));
        gl.setPreferredSize(new Dimension(100,100));

        frame.add(gl);
        frame.add(p);


        frame.pack();
        frame.setVisible(true);

    }
}