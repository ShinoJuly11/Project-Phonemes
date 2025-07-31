package NoteEditor;

public interface TableEditorUi {

    public void process();
    public void updateTable(Boolean[][] a, String[] b);
    public void setTickNumber(String[] s);
    public void setNoteNumber(Boolean[][] s);
    public String[] getTickNumber();
    public Boolean[][] getNoteNumber();


}
