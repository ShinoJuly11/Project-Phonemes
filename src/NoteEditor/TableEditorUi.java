package NoteEditor;

public interface TableEditorUi {

    public void process();
    public void process(Boolean[][] a, String[] b);
    public void updateTable();
    public void setTickNumber(String[] s);
    public void setNoteNumber(Boolean[][] s);
    public String[] getTickNumber();
    public Boolean[][] getNoteNumber();
    

}
