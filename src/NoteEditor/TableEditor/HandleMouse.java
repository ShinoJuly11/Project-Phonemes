package NoteEditor.TableEditor;

import javax.swing.JTable;

public interface HandleMouse {

    public void handleMouseClick(JTable noteTable, int row, int startColumn, boolean flag);
    public void handleMouseDrag(JTable noteTable, int startColumn, int endColumn, int row, boolean flag);


}


