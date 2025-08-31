package NoteEditor;
import MidiLogicEngine.MidiConstructor;
import NoteEditor.TableEditor2.tableEditor;
import ResamplerEngine.Resampler;

public interface Mediator {

    public void process();
    public tableEditor getTableEditor();
    public GlobalFrame getFrame();
    public Resampler getResampler();
    public MidiConstructor getMidiConstructor();


    
}
