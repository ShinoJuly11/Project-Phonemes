import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class NoteDictionaryUi {

    String[][] data;
    String[] columnNames;
    int selectedRow = -1;
    String selected = "";
    JTable j = new JTable();
    Database sql = new Database();

    private class MouseListener extends MouseAdapter{

        JTable table;

        public MouseListener(JTable table){
            this.table = table;
        }

        public void mouseClicked(MouseEvent e){
            int row = this.table.rowAtPoint(e.getPoint());
            int column = this.table.columnAtPoint(e.getPoint());

            if (row != 0 && column != 0){
                Object value = this.table.getValueAt(row, column);
                System.out.println(value);
            }
        }

    }

    private class CRUDPanel extends JPanel{

        private JLabel textVar;


        public CRUDPanel(){

        setLayout(new GridLayout(6,1));


        JLabel text = new JLabel("Selected Note:",SwingConstants.CENTER);
        textVar = new JLabel(selected,SwingConstants.CENTER);


        JButton create = new JButton("Create");
        JButton update = new JButton("Update Table");
        JButton edit = new JButton("Edit");
        JButton delete = new JButton("Delete");

        add(text);
        add(textVar);
        add(update);
        add(create);
        add(edit);
        add(delete);

        create.addActionListener(e -> { 
            // add something later
            System.out.println("To be added soon");
        });

        edit.addActionListener(e -> {

            try {

                Phoneme phoneme;
                String[] tempData = data[selectedRow];

                phoneme = new Phoneme(tempData[0],
                                              Integer.parseInt(tempData[2]),
                                              Integer.parseInt(tempData[3]),
                                              Integer.parseInt(tempData[4]),
                                              Integer.parseInt(tempData[5]),
                                              Integer.parseInt(tempData[6])
                                              );

                phoneme.setAlias(tempData[1]);
                phoneme.setComment(tempData[7]);

                NoteUi noteUi = new NoteUi(phoneme);
                noteUi.createBox();

            } catch (ArrayIndexOutOfBoundsException e2){
                System.out.println("selected nothing");
            }
            catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            
        });

        update.addActionListener(e -> {
            updateTable();
        });

        delete.addActionListener(e -> {
            sql.deleteData(phonemeStrings.FILENAME, selected);

            updateTable();
        });


        }

        public void updateSelected(String newSelected) {
        textVar.setText(newSelected);
    }

    }

    public void updateTable(){
        List<Phoneme> pArray = sql.selectAllPhoneme();

        ArrayList<ArrayList<String>> pList = new ArrayList<>();
        for (Phoneme phoneme: pArray){
            pList.add(pStrings(phoneme));
        }

        data = new String[pList.size()][];
        for (int i = 0; i < pList.size(); i++) {
            data[i] = pList.get(i).toArray(new String[0]);
        }

        DefaultTableModel newModel = new DefaultTableModel(data, columnNames);
        j.setModel(newModel);


    }

    public NoteDictionaryUi(){

        updateTable();
        columnNames = new String[] {"File Name","Alias","Offset","Overlap","Consonant","Preuttrance","Cutoff","Comment"};

    }

    public ArrayList<String> pStrings(Phoneme phoneme){

        ArrayList<String> ps = new ArrayList<>();
                       ps.add(phoneme.getFileName());
                       ps.add(phoneme.getAlias());
                       ps.add(Integer.toString(phoneme.getOffset()));
                       ps.add(Integer.toString(phoneme.getOverlap()));
                       ps.add(Integer.toString(phoneme.getConsonant()));
                       ps.add(Integer.toString(phoneme.getPreuttrance()));
                       ps.add(Integer.toString(phoneme.getCutoff()));
                       ps.add(phoneme.getComment());

        return ps;

    }

    public void run(){

        var f = new JFrame();
        f.setTitle("Note Dictionary Ui");
        f.setLayout(new GridLayout(1,2));

        var crud = new CRUDPanel();
        crud.setSize(400,300);

        var newModel = new DefaultTableModel(data,columnNames);
        j.setModel(newModel);
        j.setBounds(30,40,200,300);

        j.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                selectedRow = j.getSelectedRow();
                if (selectedRow >= 0 && selectedRow < data.length) {
                    selected = data[selectedRow][0];
                    crud.updateSelected(selected); // Update the label
                    System.out.println("Row selected: " + selectedRow);
                } else {
                    selected = "";
                    crud.updateSelected("");
                }
            }
        });

        JScrollPane sp = new JScrollPane(j);
        f.add(sp);
        f.add(crud);
        f.setSize(500,500);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);


    }

}
