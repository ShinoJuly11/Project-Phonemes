package NoteEditor.TableEditor2;

import java.awt.Rectangle;
import javax.swing.JTable;

public class Note {
        int row;
        int start;
        int length;
        String alias;

        public Note(int row, int start, int length) {
            this.row = row;
            this.start = start;
            this.length = length;
            alias = "null";
        }

        public void setAlias(String alias){
            this.alias = alias;
        }

        public String getAlias(){
            return this.alias;
        }

        public int getRow(){
            return this.row;
        }

        public int getStart(){
            return this.start;
        }

        public int getEnd(){
            return this.length + this.start - 1;

        }

        public Rectangle getBounds(JTable table) {
            Rectangle startRect = table.getCellRect(row, start, true);
            Rectangle endRect = table.getCellRect(row, start + length - 1, true);
            return new Rectangle(startRect.x, startRect.y,
                    (endRect.x + endRect.width) - startRect.x, startRect.height);
        }

        public void someRandMethod(){
            
        }
    }