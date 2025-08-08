package NoteEditor.TableEditor;

import javax.swing.JLabel;

import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import NoteEditor.TableEditorUi;

public class AliasLabelLayer implements PanelLayer{

    TableEditorUi TableEditor;

    public AliasLabelLayer(TableEditorUi m){
        this.TableEditor = m;
        process();

    }

    public void process(){
        mouseFunction();

    }
    
    public void update(){
        
    }

    private void mouseFunction(){

            var noteTable = TableEditor.getBaseTable().getNoteTable();

            noteTable.addMouseListener(new MouseAdapter(){

                int row;
                int startColumn;

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
                    TableEditor.getLayeredPane().add(label, Integer.valueOf(10));

                    System.out.println(x + "\t" + y);
                    }

            }
        });
}
}
