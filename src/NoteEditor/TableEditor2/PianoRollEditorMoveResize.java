package NoteEditor.TableEditor2;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import NoteEditor.InterfaceGui;
import NoteEditor.Mediator;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class PianoRollEditorMoveResize implements OverlayGetSetters, tableEditor, InterfaceGui{

    private JTable table = new JTable();
    private OverlayPanel overlay;
    private JLayeredPane layeredPane;
    private JScrollPane scrollPane;
    private Mediator mediator;
    private ArrayList<Note> notes = new ArrayList<>();

    // state for dragging
    private Note activeNote = null;
    private boolean resizing = false;
    private boolean moving = false;

    final int rowSize = 30;
    final int columnSize = 20; // this needs fixing for later so i can do this damn pitch

    public ArrayList<Note> getNoteArray(){
        return this.notes;
    }

    public JTable getJTable(){
        return this.table;
    }

    DefaultTableModel model;


    public void growTableModelColumn(int length){

        int currentCols = model.getColumnCount();
        for (int i = currentCols; i < currentCols + length; i++) {
            model.addColumn("" + i);
            
        }

        table.setBounds(0,0,table.getColumnCount()*columnSize,table.getRowCount()*rowSize);
        overlay.setBounds(0, 0, table.getColumnCount()*columnSize,table.getRowCount()*rowSize);
        layeredPane.setPreferredSize(new Dimension(table.getColumnCount()*columnSize,table.getRowCount()*rowSize));
        table.repaint();

    }

    public JComponent getScrollPane(){
        return this.scrollPane;
    }

    public JComponent getMainComponent(){
        return getScrollPane();
    }

    public PianoRollEditorMoveResize(Mediator mediator){
        this.mediator = mediator;
        process();
    }


    public PianoRollEditorMoveResize() {
        process();

    }

    public void process(){
        model = new DefaultTableModel(60,60);
        table.setGridColor(Color.black);
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(false);
        table.setModel(model);
        table.setRowHeight(rowSize);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(columnSize); // Set column width
        }


        overlay = new OverlayPanel(this);   
        overlay.setOpaque(false);

        layeredPane = new JLayeredPane();
        scrollPane = new JScrollPane(layeredPane);

        table.setBounds(0,0,table.getColumnCount()*columnSize,table.getRowCount()*rowSize);
        overlay.setBounds(0, 0, table.getColumnCount()*columnSize,table.getRowCount()*rowSize);

        layeredPane.add(table, Integer.valueOf(0));
        layeredPane.add(overlay, Integer.valueOf(1));

        // --- Mouse handling ---
        overlay.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Point p = e.getPoint();

                // Right click -> delete note
                if (SwingUtilities.isRightMouseButton(e)) {

                    var pm = new JPopupMenu();

                    // create menuItems
                    JMenuItem m1 = new JMenuItem("Delete");
                    JMenuItem m2 = new JMenuItem("Edit Alias");
                    JMenuItem m3 = new JMenuItem("Item3");

                    pm.add(m1);
                    pm.add(m2);
                    pm.add(m3);


                    m1.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e)
                        {

                            for (Note note : notes) {
                                if (note.getBounds(table).contains(p)) {
                                    notes.remove(note);
                                    overlay.repaint();
                                    break;
                                }
                            }
                        }
                    });

                    // edit frame with new textbox
                    m2.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e){

                            for (Note note : notes) {
                                if (note.getBounds(table).contains(p)) {

                                    JFrame insideFrame = new JFrame("Change Name");
                                    insideFrame.setLayout(new GridBagLayout());
                                    GridBagConstraints gbc = new GridBagConstraints();

                                    gbc.gridx = 0;
                                    gbc.gridy = 0;
                                    gbc.weightx = 2;
                                    gbc.weighty = 2;
                                    gbc.anchor = GridBagConstraints.CENTER;

                                    JTextArea textArea = new JTextArea("change Alias", 30, 30);
                                    textArea.setOpaque(true);
                                    insideFrame.add(textArea, gbc);
                                    
                                    gbc.gridx = 2;
                                    gbc.gridy = 2;
                                    gbc.anchor = GridBagConstraints.EAST;
                                    gbc.weightx = 1;

                                    JButton button = new JButton("Enter");
                                    insideFrame.add(button,gbc);

                                    insideFrame.setSize(400,200);
                                    insideFrame.setVisible(true);

                                    button.addActionListener(new ActionListener() {
                                        public void actionPerformed(ActionEvent e) {
                                            if (textArea.getText() != null){
                                                note.setAlias(textArea.getText());
                                                overlay.repaint();
                                                
                                            }
                                        };
                                    });
                                    
                                    break;
                                }
                            }

                            
                            

                            
                        }
                    });

                    pm.show(layeredPane, e.getX(), e.getY());
                }

                // check if clicking on a note
                for (Note note : notes) {
                    Rectangle r = note.getBounds(table);

                    // near right edge -> resize
                    if (r.contains(p) && Math.abs(p.x - (r.x + r.width)) < 6) {
                        activeNote = note;
                        resizing = true;
                        return;
                    }

                    // inside note -> move
                    if (r.contains(p)) {
                        activeNote = note;
                        moving = true;
                        return;
                    }
                }

                int row = table.rowAtPoint(p);
                int col = table.columnAtPoint(p);
                int colEnd = table.getColumnCount();

                if (col > colEnd - 60){
                    growTableModelColumn(60);
                }

                // otherwise, create a new note
                if (row >= 0 && col >= 0) {
                    Note newNote = new Note(row, col, 4);
                    notes.add(newNote);
                    overlay.repaint();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                activeNote = null;
                resizing = false;
                moving = false;
            }
        });

        overlay.addMouseMotionListener(new MouseMotionAdapter() {

            // for note to move when dragged with the mouse
            @Override
            public void mouseDragged(MouseEvent e) {
                if (resizing && activeNote != null) {
                    int col = table.columnAtPoint(e.getPoint());
                    if (col > activeNote.start) {
                        activeNote.length = col - activeNote.start + 1;
                        overlay.repaint();
                    }
                } else if (moving && activeNote != null) {
                    int row = table.rowAtPoint(e.getPoint());
                    int col = table.columnAtPoint(e.getPoint());

                    if (row >= 0 && col >= 0) {
                        activeNote.row = row;
                        activeNote.start = col;
                        overlay.repaint();
                    }
                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                // change cursor
                for (Note note : notes) {
                    Rectangle r = note.getBounds(table);
                    if (r.contains(e.getPoint()) &&
                        Math.abs(e.getX() - (r.x + r.width)) < 6) {
                        overlay.setCursor(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));
                        return;
                    } else if (r.contains(e.getPoint())) {
                        overlay.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                        return;
                    }
                }
                overlay.setCursor(Cursor.getDefaultCursor());
            }
        });

        int width = this.mediator.getFrame().getJFrame().getWidth()-100;
        int height = this.mediator.getFrame().getJFrame().getHeight()-100;

        scrollPane.setPreferredSize(new Dimension(width,height));
    }
}
