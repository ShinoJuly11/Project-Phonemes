package NoteEditor.TableEditor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import NoteEditor.TableEditorUi;

public class MouseListener{

    TableEditorUi mainTable;
    int row;
    int startColumn, endColumn;
    Object value;
    boolean flag;

    public MouseListener(TableEditorUi mainTable){
        this.mainTable = mainTable;

    }

    public void mouseFunction(){

        var noteTable = mainTable.getBaseTable().getNoteTable();

        noteTable.addMouseListener(new MouseAdapter(){

        @Override
        public void mousePressed(MouseEvent e){
                if (e.isPopupTrigger()){
                    System.out.println("worked");
                }

                else{

                    row = noteTable.rowAtPoint(e.getPoint());
                    startColumn = noteTable.columnAtPoint(e.getPoint());
                    value = noteTable.getValueAt(row, startColumn);
                    flag = value.toString().equals("false") ? true : false;

                    mainTable.getAliasLayer().handleMouseClick(noteTable, row, startColumn);
                    mainTable.getBaseTable().handleMouseClick(noteTable, row, startColumn, flag);

                
                }
            
            }

        @Override
            public void mouseReleased(MouseEvent e) {
                endColumn = noteTable.columnAtPoint(e.getPoint());
                mainTable.getBaseTable().handleMouseDrag(noteTable, startColumn, endColumn, row, flag);

            }
        });



}}
