package NoteEditor.TableEditor;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import NoteEditor.Mediator;
import NoteEditor.TableEditorUi;

public class NoteTableLayer implements TableLayer, HandleMouse{

    TableEditorUi mainTable;
    JTable noteTable = new JTable();
    JPanel overlay = new JPanel();
    Boolean[][] noteNumber;
    String[] tickNumber;
    Mediator mediator;

    public JTable getNoteTable(){
        return this.noteTable;
    }

    public Boolean[][] getNoteNumber(){
        return this.noteNumber;
    }

    public String[] getTickNumber(){
        return this.tickNumber;
    }

    private boolean checkIftheArrayHasData(Boolean[][] matrix) {
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

    public void update(Boolean[][] noteNumber, String[] tickNumber){

        this.noteNumber = mainTable.getNoteNumber();
        this.tickNumber = mainTable.getTickNumber();

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
        mainTable.getLayeredPane().setPreferredSize(new Dimension((noteNumber[0].length * width), noteTable.getRowHeight() * noteTable.getRowCount()));
        
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



    public NoteTableLayer(TableEditorUi mainTable){

        this.mainTable = mainTable;
        this.mediator = mainTable.getMediator();
        this.noteNumber = mainTable.getNoteNumber();
        this.tickNumber = mainTable.getTickNumber();

        process();


    }

    public void process(){
        update(noteNumber, tickNumber);

    }
    
    public void handleMouseClick(JTable noteTable, int row, int startColumn, boolean flag){

                this.noteNumber[row][startColumn] = flag;
                noteTable.setValueAt(flag, row, startColumn);

                    if(checkIftheArrayHasData(this.noteNumber)){
                            mainTable.update(this.noteNumber, this.tickNumber);
                    }
    }

    public void handleMouseDrag(JTable noteTable, int startColumn, int endColumn, int row, boolean flag){

        int from = Math.min(startColumn, endColumn);
        int to = Math.max(startColumn, endColumn);

        for (int col = from; col <= to; col++) {
            this.noteNumber[row][col] = flag;
            noteTable.setValueAt(flag, row, col);
        }

        if(checkIftheArrayHasData(this.noteNumber)){
            mainTable.tableUpdate(this.noteNumber,this.tickNumber);
        }

    }
    

}
