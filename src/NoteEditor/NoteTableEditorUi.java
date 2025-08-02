package NoteEditor;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class NoteTableEditorUi implements TableEditorUi{

    JTable noteTable = new JTable();
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
        JFrame f = mediator.getJFrame();
        createTable();
        updateTable(noteNumber, tickNumber);
        var scrollPane = new JScrollPane(noteTable); 
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        f.add(scrollPane);
        

    }

    private void createTable(){

        noteTable.addMouseListener(new MouseAdapter(){

            int row;
            int startColumn, endColumn;
            Object value;
            boolean flag;

        @Override
        public void mousePressed(MouseEvent e){

            row = noteTable.rowAtPoint(e.getPoint());
            startColumn = noteTable.columnAtPoint(e.getPoint());
            value = noteTable.getValueAt(row, startColumn);
            flag = value.toString().equals("false") ? true : false;
            noteNumber[row][startColumn] = flag;
            noteTable.setValueAt(flag, row, startColumn);

            if(checkIftheArrayHasData(noteNumber)){
                    mediator.TableUpdate();
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
        DefaultTableModel newTable = new DefaultTableModel(noteNumber, tickNumber) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false; // All cells are read-only
            }
        };
        noteTable.setModel(newTable);

        noteTable.setRowHeight(30); // Set row height
        for (int i = 0; i < noteTable.getColumnCount(); i++) {
            noteTable.getColumnModel().getColumn(i).setPreferredWidth(20); // Set column width
            
        }
        
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

