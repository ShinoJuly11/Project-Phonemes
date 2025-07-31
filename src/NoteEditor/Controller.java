package NoteEditor;

public interface Controller {
    public void process();
    public void update(Boolean[][] newNote);
    public void setTickNumber(String[] s);
    public void setNoteNumber(Boolean[][] s);
    public String[] getTickNumber();
    public Boolean[][] getNoteNumber();

}
