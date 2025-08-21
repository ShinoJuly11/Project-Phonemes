package NoteEditor.TableEditor2;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import javax.swing.JPanel;
import javax.swing.JTable;

class OverlayPanel extends JPanel {

    private ArrayList<Note> notes;
    private JTable table;

        public OverlayPanel(OverlayGetSetters get){

            this.table = get.getJTable();
            this.notes = get.getNoteArray();

        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            for (Note note : notes) {

                Rectangle r = note.getBounds(table);

                //fill inside
                g.setColor(new Color(0, 1, 0, 0.5f));
                g.fillRect(r.x + 1, r.y + 1, r.width - 2, r.height - 2);


                //outline
                g.setColor(Color.BLACK);
                g.drawRect(r.x + 1, r.y + 1, r.width - 2, r.height - 2);

                //text
                g.setColor(Color.WHITE);
                g.setFont(new Font("Arial", Font.BOLD, 10));
                g.drawString(note.getAlias(), r.x + 5, r.y + 20);
            }
        }
    }