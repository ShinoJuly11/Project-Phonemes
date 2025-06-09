import javax.sound.sampled.AudioInputStream;
import javax.swing.*; // UI implementation

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class NoteUi{
    
    Phoneme phoneme;

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

        int value, pValue;

            public GraphLines(String property, Phoneme phoneme, GraphDrawLine gdl){

                setLayout(new GridLayout(4,1));
                setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

                JPanel buttonPanel = new JPanel();
                buttonPanel.setLayout(new GridLayout(1,2));
                JButton button1 = new JButton("<");
                JButton button2 = new JButton(">");

                JLabel label1 = new JLabel(property, SwingConstants.CENTER);
                JTextField tf1 = new JTextField(Integer.toString(getValue(property)), SwingConstants.CENTER);
                JButton button3 = new JButton("Enter Value");

                button3.addActionListener(e -> {
                    try {
                        int value = Integer.parseInt(tf1.getText());
                        saveValue(value,property);
                        gdl.setValue(getValue(property));
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
                        saveValue(value,property);
                        gdl.setValue(getValue(property));

                    } catch (NumberFormatException ex){
                        tf1.setText("0");
                    }

                
                });

                button2.addActionListener(e -> {
                    try{
                        int value = Integer.parseInt(tf1.getText());
                        value+= 1;
                        tf1.setText(Integer.toString(value));
                        saveValue(value,property);
                        gdl.setValue(getValue(property));

                    } catch (NumberFormatException ex){ // catches the empty textfield
                        tf1.setText("0");
                    }
                
                });


                // sub panel
                buttonPanel.add(button1);
                buttonPanel.add(button2);

                //main panel
                add(label1);
                add(tf1);
                add(button3);
                add(buttonPanel);
                
                
            }
            // I CANT USE THE DAMN CLASS VARS THIS WILL DO.
            // bad implementation idc anymore
            public void setValue(int value){
                System.out.println("Parsed value: " + this.value);
                this.value = value;
            }

            public int getValue(String property){
                switch (property) {
                    case "offset":
                        return phoneme.getOffset();
                    case "overlap":
                        return phoneme.getOverlap();
                    case "cutoff":
                        return phoneme.getCutoff();
                    case "preuttrance":
                        return phoneme.getPreuttrance();
                    case "audioLoopStart":
                        return phoneme.getAudioLoopStart();
                    case "audioLoopEnd":
                        return phoneme.getAudioLoopEnd();
                    default:
                        throw new IllegalArgumentException("Unknown property: " + property);
                }
            }

            public void saveValue(int value, String property) {
                switch (property) {
                    case "offset":
                        phoneme.setOffset(value);
                        break;
                    case "overlap":
                        phoneme.setOverlap(value);
                        break;
                    case "cutoff":
                        phoneme.setCutoff(value);
                        break;
                    case "preuttrance":
                        phoneme.setPreuttrance(value);
                        break;
                    case "audioLoopStart":
                        phoneme.setAudioLoopStart(value);
                        break;
                    case "audioLoopEnd":
                        phoneme.setAudioLoopEnd(value);
                        break;
                    default:
                        throw new IllegalArgumentException("Unknown property: " + property);
                }
            }

            public void drawLine(int value, JPanel panel){

                GraphDrawLine dl = new GraphDrawLine(value);
                dl.add(panel);

            }
        }

    
    class GraphDrawLine extends JPanel{

        int value;

        public GraphDrawLine(int value){

            setOpaque(false);
            setBounds(0, 0, 800, 400);
            setValue(value);
            //value is the x axis
        }
        public void setValue(int value){
            this.value = value;
            repaint();
        }

        @Override
            public void paintComponent(Graphics g){
                super.paintComponent(g);
                // drawing a red line to visualise where it is from
                g.setColor(Color.RED);
                g.drawLine(this.value, 0, this.value, 900);
                
            }
    }

    class GraphTop extends JPanel {

        public GraphTop() throws Exception {

            JPanel bottomPanel = new JPanel(new BorderLayout()); // <-- Also needs a layout manager
            JLayeredPane layeredPane = new JLayeredPane();
            layeredPane.setPreferredSize(new Dimension(800, 400));

            GraphPlotGraph p = new GraphPlotGraph(phoneme.getAis(), phoneme.getAis().getFrameLength());
            p.setBounds(0, 0, 800, 400);

            GraphDrawLine dl1 = new GraphDrawLine(phoneme.getOffset());
            GraphDrawLine dl2 = new GraphDrawLine(phoneme.getOverlap());
            GraphDrawLine dl3 = new GraphDrawLine(phoneme.getCutoff());
            GraphDrawLine dl4 = new GraphDrawLine(phoneme.getPreuttrance());
            GraphDrawLine dl5 = new GraphDrawLine(phoneme.getAudioLoopStart());
            GraphDrawLine dl6 = new GraphDrawLine(phoneme.getAudioLoopEnd());

            JPanel topPanel = new JPanel(); 
            topPanel.setLayout(new GridLayout(1, 6));
            //topPanel.setBounds(800,400);
            GraphLines gl1 = new GraphLines("offset", phoneme, dl1);
            GraphLines gl2 = new GraphLines("overlap", phoneme,dl2);
            GraphLines gl3 = new GraphLines("cutoff", phoneme,dl3);
            GraphLines gl4 = new GraphLines("preuttrance", phoneme,dl4);
            GraphLines gl5 = new GraphLines("audioLoopStart", phoneme,dl5);
            GraphLines gl6 = new GraphLines("audioLoopEnd", phoneme,dl6);

            topPanel.add(gl1);
            topPanel.add(gl2);
            topPanel.add(gl3);
            topPanel.add(gl4);
            topPanel.add(gl5);
            topPanel.add(gl6);

            layeredPane.add(p, Integer.valueOf(0));
            layeredPane.add(dl1, Integer.valueOf(1));
            layeredPane.add(dl2, Integer.valueOf(2));
            layeredPane.add(dl3, Integer.valueOf(3));
            layeredPane.add(dl4, Integer.valueOf(4)); 
            layeredPane.add(dl5, Integer.valueOf(5)); 
            layeredPane.add(dl6, Integer.valueOf(6)); 
            bottomPanel.add(layeredPane, BorderLayout.CENTER);

            add(topPanel);
            add(bottomPanel);


            
        }
    }   

    public NoteUi(Phoneme phoneme){
        this.phoneme = phoneme;

    }

    public void createBox() throws Exception{

        JFrame frame = new JFrame();
        GraphTop gl = new GraphTop(); // Top control panel
        //GraphBottom gb = new GraphBottom(); // Bottom graph panel
        frame.setPreferredSize(new Dimension(1000,600));
        frame.setLayout(new BorderLayout());
        frame.add(gl, BorderLayout.CENTER);
        //frame.add(gb, BorderLayout.CENTER);

        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

    }
}