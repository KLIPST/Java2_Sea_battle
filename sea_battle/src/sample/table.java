package sample;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;

/**
 * Created by KLIPST on 06.10.2014.
 */
public class table extends JFrame {
    public void table(boolean privacy) {
        setSize(500, 500);

        ListModel lm = new AbstractListModel() {
            String columns[] = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j"};
            String rows[] = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};

            public int getSize() {
                return columns.length;
            }

            public Object getElementAt(int index) {
                return columns[index];
            }
        };

        DefaultTableModel dm = new DefaultTableModel(lm.getSize(), 10);
        JTable table = new JTable(dm);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        JList rowHeader = new JList(lm);
        rowHeader.setFixedCellWidth(50);

        rowHeader.setFixedCellHeight(table.getRowHeight()
                + table.getRowMargin());
        //                           + table.getIntercellSpacing().height);
        rowHeader.setCellRenderer(new RowHeaderRenderer(table));

        JScrollPane scroll = new JScrollPane(table);
        scroll.setRowHeaderView(rowHeader);
        getContentPane().add(scroll, BorderLayout.CENTER);
    }

    public void hit(ArrayList data, int x, int y){
        switch (data[x][y])
        {
            case 0:
                break;
            default:
                break;
        }
    }
}

