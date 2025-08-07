package NoteEditor.TableEditor;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import NoteEditor.TableEditorUi;

public class BaseTableEditor {

    TableEditorUi mainTable;

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

    private void updateTable(Boolean[][] noteNumber, String[] tickNumber){

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



    public BaseTableEditor(TableEditorUi mainTable){
        this.mainTable = mainTable;

    }

    public void process(){


    }

    public void update(){

    }

    

}
