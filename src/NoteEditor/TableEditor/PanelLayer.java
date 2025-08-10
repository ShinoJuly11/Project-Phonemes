package NoteEditor.TableEditor;

import javax.swing.JTable;

public interface PanelLayer{

    public void process();
    public void update();
    public void handleMouseClick(JTable noteTable, int row, int startColumn);

}
