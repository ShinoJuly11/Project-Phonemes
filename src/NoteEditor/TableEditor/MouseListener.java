package NoteEditor.TableEditor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import NoteEditor.TableEditorUi;

public class MouseListener{

    TableEditorUi mainTable;
    int row;
    int startColumn, endColumn;
    Object value;
    boolean flag;
    ArrayList<HandleMouse> mouseArray = new ArrayList<>();

    public MouseListener(TableEditorUi mainTable){
        this.mainTable = mainTable;

    }

    public void add(HandleMouse mouse){
        mouseArray.add(mouse);

    }

    public void run(){

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

                    for(HandleMouse mouse : mouseArray){
                        mouse.handleMouseClick(noteTable, row, startColumn, flag);
                    }

                
                }
            
            }

        @Override
            public void mouseReleased(MouseEvent e) {
                endColumn = noteTable.columnAtPoint(e.getPoint());
                
                for(HandleMouse mouse : mouseArray){
                    mouse.handleMouseDrag(noteTable, startColumn, endColumn, row, flag);
                }

            }
        });



}}
