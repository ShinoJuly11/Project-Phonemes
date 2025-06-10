import javax.sound.sampled.AudioInputStream;
import javax.swing.*; // UI implementation

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class NoteUi{

    int yAxis = 800;
    int xAxis = 800;
    Phoneme phoneme;

    class GraphPlotGraph extends JPanel {

        byte[] bytes; //storing all the data from the byteAudioOutputStream;
        int frameLength;
        int frameSize = 2; // mono audio
        int ypad = 200;
        int arrayLength;

        public GraphPlotGraph(AudioInputStream ais, long frameLength) throws Exception{
            this.bytes = aisToByte(ais);
            this.frameLength = (int) frameLength;
            this.arrayLength = byteToShort().size();

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

        public void setArrayLength(int arrayLength){
            this.arrayLength = arrayLength;
        }

        public int getArrayLength(){
            return this.arrayLength;
        }


        @Override
        protected void paintComponent(Graphics g){
            super.paintComponent(g);

            ArrayList<Short> bts = byteToShort();
            int max = 32000;
            int min = -32000;
            double yScale = (double) yAxis / (max - min); // e.g., 640 / 64000 = 0.01
            double xScale = (double) xAxis / arrayLength;

            for (int x = 0; x < arrayLength; x++) {
                short value = bts.get(x);
                int ydot = (int) ((max - value) * yScale); // invert so high values go up
                int xdot = (int) (x * xScale);             // scaled X position
                g.fillOval(xdot, ydot - ypad, 2, 2);
                
            }

        }
    }

    class GraphLines extends JPanel {

        int value, pValue;
        GraphDraw gd;


            public GraphLines(String property, Phoneme phoneme, GraphDrawBox gdb){

                this.gd = gdb;

               initUi(property);
            }

            public void initUi(String property){
                setLayout(new GridLayout(4,1));
                setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

                JPanel buttonPanel = new JPanel();
                buttonPanel.setLayout(new GridLayout(1,2));
                JButton button1 = new JButton("<");
                JButton button2 = new JButton(">");

                JLabel label1 = new JLabel(property, SwingConstants.CENTER);
                JTextField tf1 = new JTextField(Integer.toString(getValue(property)), SwingConstants.CENTER);
                JButton button3 = new JButton("Enter Value");


                if (property.equals("audioLoopEnd")){
                    buttonPlusEnd(button3, tf1, property); // fix later
                    buttonPlusEnd(button2, tf1, property);
                    buttonMinusEnd(button1, tf1, property);
                }

                else if(property.equals("audioLoopStart")){
                    buttonPlusStart(button3, tf1, property); // fix later
                    buttonPlusStart(button2, tf1, property);
                    buttonMinusStart(button1, tf1, property);
                }


                // sub panel
                buttonPanel.add(button1);
                buttonPanel.add(button2);

                //main panel
                add(label1);
                add(tf1);
                add(button3);
                add(buttonPanel);
            }



            public GraphLines(String property, Phoneme phoneme, GraphDrawLine gd){

                this.gd = gd;

                setLayout(new GridLayout(4,1));
                setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

                JPanel buttonPanel = new JPanel();
                buttonPanel.setLayout(new GridLayout(1,2));
                JButton button1 = new JButton("<");
                JButton button2 = new JButton(">");

                JLabel label1 = new JLabel(property, SwingConstants.CENTER);
                JTextField tf1 = new JTextField(Integer.toString(getValue(property)), SwingConstants.CENTER);
                JButton button3 = new JButton("Enter Value");


                buttonFunc(button3,tf1,property);
                buttonMinus(button1,tf1,property);
                buttonPlus(button2,tf1,property);

                // sub panel
                buttonPanel.add(button1);
                buttonPanel.add(button2);

                //main panel
                add(label1);
                add(tf1);
                add(button3);
                add(buttonPanel);
            
            }



            public void buttonFunc(JButton button, JTextField tf1, String property){

                button.addActionListener(e -> {
                    try {
                        int value = Integer.parseInt(tf1.getText());
                        saveValue(value,property);
                        if (gd != null){
                            gd.setValue(value);
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "Please enter a valid number.");
                        tf1.setText("0"); 
                    }
                
                });
            }

            public void buttonMinus(JButton button, JTextField tf1, String property){
                button.addActionListener(e -> {
                    try{

                        int value = Integer.parseInt(tf1.getText());
                        value -= 1;
                        tf1.setText(Integer.toString(value));
                        saveValue(value,property);
                        if (gd != null){
                            gd.setValue(value);
                        }

                    } catch (NumberFormatException ex){
                        tf1.setText("0");
                    }

                
                });
            }


            public void buttonPlus(JButton button, JTextField tf1, String property){
                button.addActionListener(e -> {
                    try{
                        int value = Integer.parseInt(tf1.getText());
                        value += 1;
                        tf1.setText(Integer.toString(value));
                        saveValue(value,property);
                        if (gd != null){
                            gd.setValue(value);
                        }
                        

                    } catch (NumberFormatException ex){ // catches the empty textfield
                        tf1.setText("0");
                    }
                });

            }

            public void buttonPlusStart(JButton button, JTextField tf1, String property){
                button.addActionListener(e -> {
                    try{
                        int value1 = Integer.parseInt(tf1.getText());
                        value1 += 1;
                        tf1.setText(Integer.toString(value1));
                        saveValue(value1,property);
                        gd.setValue(phoneme.getAudioLoopStart(),phoneme.getAudioLoopEnd());

                    } catch (NumberFormatException ex){ // catches the empty textfield
                        tf1.setText("0");
                    }
                });
                
            }

            public void buttonMinusStart(JButton button, JTextField tf1, String property){
                button.addActionListener(e -> {
                    try{
                        int value1 = Integer.parseInt(tf1.getText());
                        value1 -= 1;
                        tf1.setText(Integer.toString(value1));
                        saveValue(value1,property);
                        gd.setValue(phoneme.getAudioLoopStart(),phoneme.getAudioLoopEnd());


                    } catch (NumberFormatException ex){ // catches the empty textfield
                        tf1.setText("0");
                    }
                });
                
            }

            public void buttonPlusEnd(JButton button, JTextField tf1, String property){
                button.addActionListener(e -> {
                    try{

                        int value2 = Integer.parseInt(tf1.getText());
                        value2 += 1;
                        tf1.setText(Integer.toString(value2));
                        saveValue(value2,property);
                        gd.setValue(phoneme.getAudioLoopStart(),phoneme.getAudioLoopEnd());

                    } catch (NumberFormatException ex){ // catches the empty textfield
                        tf1.setText("0");
                    }
                });
                
            }

            public void buttonMinusEnd(JButton button, JTextField tf1, String property){
                button.addActionListener(e -> {
                    try{
                        int value2 = Integer.parseInt(tf1.getText());
                        value2 -= 1;
                        tf1.setText(Integer.toString(value2));
                        saveValue(value2,property);
                        gd.setValue(phoneme.getAudioLoopStart(),phoneme.getAudioLoopEnd());

                    } catch (NumberFormatException ex){ // catches the empty textfield
                        tf1.setText("0");
                    }
                });
                
            }

            public GraphDraw getGraphDraw(GraphDraw gd){
                return this.gd;

            }
            public void setGraphDraw(GraphDraw gd){
                this.gd = gd;

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

            // public void drawLine(int value, JPanel panel){

            //     GraphDrawLine dl = new GraphDrawLine(value);
            //     dl.add(panel);

            // }
        }
    
    // Java Moment
    // Because shit cant Barbara Liskov
    abstract class GraphDraw extends JPanel{
        int x1, x2;
        double xScale;

        public void setValue(int x){     
        }
        public void setValue(int x, int x2){
        }
    }

    
    class GraphDrawLine extends GraphDraw{


        public GraphDrawLine(int x1, double xScale){

            setOpaque(false);
            setBounds(0, 0, xAxis, (yAxis/2));
            setValue(x1);
            this.xScale = xScale;
            //value is the x axis
        }
        @Override
        public void setValue(int x1){
            this.x1 = x1;
            repaint();
        }

        @Override
            public void paintComponent(Graphics g){
                super.paintComponent(g);
                // drawing a red line to visualise where it is from
                g.setColor(Color.RED);
                g.drawLine((int)(this.x1*this.xScale), 1,(int)(this.x1*this.xScale), 900);
                
            }
    }

    class GraphDrawBox extends GraphDraw{

        public GraphDrawBox(int x1, int x2, double xScale){

            setOpaque(false);
            setBounds(0, 0, xAxis, (yAxis/2));
            setValue(x1, x2);
            this.xScale = xScale;
            //value is the x axis
        }
        @Override
        public void setValue(int x1,int x2){

            this.x1 = x1;
            this.x2 = x2;
            repaint();
        }

        @Override
        public void paintComponent(Graphics g){
            super.paintComponent(g);

         // drawing a red line to visualise where it is from
                Color color = new Color(0f, 1f, 0f, 0.5f);
                g.setColor(color);

                int boxStart = Math.min(this.x1, this.x2);
                int boxEnd = Math.max(this.x1, this.x2);
                int boxWidth = boxEnd - boxStart;

                

                //g.drawRect((int)(boxStart*this.xScale), 0, (int)(boxWidth*this.xScale), 900);
                g.fillRect((int)(boxStart*this.xScale), 0, (int)(boxWidth*this.xScale), 900);

        }


    }


    class GraphTop extends JPanel {

        public GraphTop() throws Exception {

            JPanel bottomPanel = new JPanel(new BorderLayout()); // <-- Also needs a layout manager
            JLayeredPane layeredPane = new JLayeredPane();
            layeredPane.setPreferredSize(new Dimension(xAxis, (yAxis/2)));

            GraphPlotGraph p = new GraphPlotGraph(phoneme.getAis(), phoneme.getAis().getFrameLength());

            int arrayLength = p.getArrayLength();
            double xScale = (double) xAxis / arrayLength;

            System.out.println(arrayLength);

            p.setBounds(0, 0, xAxis, (yAxis/2));

            GraphDrawLine dl1 = new GraphDrawLine(phoneme.getOffset(),xScale);
            GraphDrawLine dl2 = new GraphDrawLine(phoneme.getOverlap(),xScale);
            GraphDrawLine dl3 = new GraphDrawLine(phoneme.getCutoff(),xScale);
            GraphDrawLine dl4 = new GraphDrawLine(phoneme.getPreuttrance(),xScale);
            GraphDrawBox dl5 = new GraphDrawBox(phoneme.getAudioLoopStart(),phoneme.getAudioLoopEnd(),xScale);

            JPanel topPanel = new JPanel(); 
            topPanel.setLayout(new GridLayout(1, 6));
            //topPanel.setBounds(800,400);
            GraphLines gl1 = new GraphLines("offset", phoneme, dl1);
            GraphLines gl2 = new GraphLines("overlap", phoneme,dl2);
            GraphLines gl3 = new GraphLines("cutoff", phoneme,dl3);
            GraphLines gl4 = new GraphLines("preuttrance", phoneme,dl4);
            GraphLines gl5 = new GraphLines("audioLoopStart", phoneme,dl5);
            GraphLines gl6 = new GraphLines("audioLoopEnd", phoneme,dl5);

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
        frame.setPreferredSize(new Dimension(xAxis+200,yAxis-200));
        frame.add(gl);
        //frame.add(gb, BorderLayout.CENTER);

        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

    }
}