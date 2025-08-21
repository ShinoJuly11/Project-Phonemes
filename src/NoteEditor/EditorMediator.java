package NoteEditor;
import java.awt.GridBagConstraints;

import javax.swing.JMenuBar;

import NoteEditor.PlaybackButtons.ButtonsForTable;
import NoteEditor.TableEditor2.PianoRollEditorMoveResize;
import NoteEditor.TableEditor2.tableEditor;

public class EditorMediator implements Mediator {

    //istg if this class becomes GOD ill kms

    PianoRollEditorMoveResize table;
    ButtonsForTable bottomButtons;
    GlobalFrame frame;


    public void process(){
        this.frame = new GlobalFrame(this);
        this.table = new PianoRollEditorMoveResize(this);
        this.bottomButtons = new ButtonsForTable(this);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;

        frame.addJComponent(table.getMainComponent(), gbc);
        frame.setMenuBar((JMenuBar) bottomButtons.getMainComponent()); //This is wack

        frame.setVisible();

    }


    @Override
    public tableEditor getTableEditor() {
        return this.table;
    }

    @Override
    public GlobalFrame getFrame(){
        return this.frame;
    }


}
