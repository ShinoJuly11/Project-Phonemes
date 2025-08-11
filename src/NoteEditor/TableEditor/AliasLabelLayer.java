package NoteEditor.TableEditor;

import javax.swing.JLabel;
import javax.swing.JTable;

import java.awt.Rectangle;

import NoteEditor.TableEditorUi;

public class AliasLabelLayer implements PanelLayer, HandleMouse{

    TableEditorUi TableEditor;

    public AliasLabelLayer(TableEditorUi m){
        this.TableEditor = m;
        process();

    }

    public void process(){

    }
    
    public void update(){
        
    }

    public void handleMouseClick(JTable noteTable, int row, int startColumn, boolean flag){
        Rectangle rect = noteTable.getCellRect(row, startColumn, true);
        int x = rect.x;
        int y = rect.y;

        JLabel label = new JLabel("TEST");
        label.setBounds(x, y + noteTable.getRowHeight(), 50, 12);
        label.setOpaque(true);
        TableEditor.getLayeredPane().add(label, Integer.valueOf(10));
    }

    public void handleMouseDrag(JTable noteTable, int startColumn, int endColumn, int row, boolean flag){
        //no function needed
    }
}
