package NoteEditor;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class NoteTableEditorUi implements TableEditorUi{

    JTable noteTable = new JTable();
    JPanel overlay = new JPanel();
    JScrollPane scrollPane; 
    JLayeredPane layeredPane = new JLayeredPane();
    Boolean[][] noteNumber;
    String[] tickNumber;
    Mediator mediator;

    public NoteTableEditorUi(Mediator mediator){
        this.mediator = mediator;
    }

    // set getters for the mediator

    public void setTickNumber(String[] s){
        this.tickNumber = s;
    }
    public String[] getTickNumber(){
        return this.tickNumber;
    }
    public void setNoteNumber(Boolean[][] s){
        this.noteNumber = s;
    }
    public Boolean[][] getNoteNumber(){
        return this.noteNumber;
    }

    public boolean checkIftheArrayHasData(Boolean[][] matrix) {
    if (matrix.length == 0) return false;
    int colCount = matrix[0].length;
    int startCol = Math.max(0, colCount - 20);
    for (int i = 0; i < matrix.length; i++) {
        for (int j = startCol; j < colCount; j++) {
            if (Boolean.TRUE.equals(matrix[i][j])) {
                return true;
            }
        }
    }
    return false;
}

    public void process() {

        createTable();
        updateTable(noteNumber, tickNumber);

        JFrame f = mediator.getJFrame();
        GridBagConstraints constraints = new GridBagConstraints();


        layeredPane.add(noteTable, Integer.valueOf(0));
        //layeredPane.add(overlay, Integer.valueOf(1));

        scrollPane = new JScrollPane(layeredPane);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        constraints.weightx = 4;
        constraints.weighty = 3;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.fill = GridBagConstraints.BOTH;

        f.add(scrollPane, constraints);
        

    }

    

    private void createTable(){

        noteTable.addMouseListener(new MouseAdapter(){

            int row;
            int startColumn, endColumn;
            Object value;
            boolean flag;

        @Override
        public void mousePressed(MouseEvent e){
            if (e.isPopupTrigger()){
                System.out.println("worked");
            }
            else{

            row = noteTable.rowAtPoint(e.getPoint());
            startColumn = noteTable.columnAtPoint(e.getPoint());

            Rectangle rect = noteTable.getCellRect(row, startColumn, true);
            int x = rect.x;
            int y = rect.y;

            JLabel label = new JLabel("TEST");
            label.setBounds(x, y + noteTable.getRowHeight(), 50, 12);
            label.setOpaque(true);
            layeredPane.add(label, Integer.valueOf(10));

            System.out.println(x + "\t" + y);

            value = noteTable.getValueAt(row, startColumn);
            flag = value.toString().equals("false") ? true : false;
            noteNumber[row][startColumn] = flag;
            noteTable.setValueAt(flag, row, startColumn);

            if(checkIftheArrayHasData(noteNumber)){
                    mediator.TableUpdate();
            }
        }

            
        }

        @Override
            public void mouseReleased(MouseEvent e) {
                endColumn = noteTable.columnAtPoint(e.getPoint());

                int from = Math.min(startColumn, endColumn);
                int to = Math.max(startColumn, endColumn);

                for (int col = from; col <= to; col++) {
                    noteNumber[row][col] = flag;
                    noteTable.setValueAt(flag, row, col);
                }

                if(checkIftheArrayHasData(noteNumber)){
                    mediator.TableUpdate();
                }

            }

        });

    }

    public void updateTable(Boolean[][] noteNumber, String[] tickNumber){

        int width = 20;
        DefaultTableModel newTable = new DefaultTableModel(noteNumber, tickNumber) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
            }
        };
        noteTable.setModel(newTable);

        noteTable.setRowHeight(30); // Set row height
        for (int i = 0; i < noteTable.getColumnCount(); i++) {
            noteTable.getColumnModel().getColumn(i).setPreferredWidth(width); // Set column width
            
        }

        // IDK HOW TO DECOPULE THIS CODE 

        noteTable.setBounds(0, 0, noteTable.getColumnCount() * width, noteTable.getRowHeight() * noteTable.getRowCount());
        overlay.setBounds(0, 0, (noteNumber[0].length * width), noteTable.getRowHeight() * noteTable.getRowCount());
        layeredPane.setPreferredSize(new Dimension((noteNumber[0].length * width), noteTable.getRowHeight() * noteTable.getRowCount()));
        
        noteTable.getTableHeader().setReorderingAllowed(false);
        noteTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        noteTable.setShowGrid(true);
        noteTable.setGridColor(Color.GRAY);
        noteTable.setSelectionBackground(Color.WHITE);
        noteTable.setSelectionForeground(Color.BLACK);

        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                // Example: color based on Boolean value
                if (value instanceof Boolean && (Boolean) value) {
                    c.setBackground(Color.GREEN);
                } else {
                    c.setBackground(Color.WHITE);
                }
                 
                return c;

                
            }
        };

            // Apply renderer to all columns
            for (int i = 0; i < noteTable.getColumnCount(); i++) {
                noteTable.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
            }     
            
        };

        
}

