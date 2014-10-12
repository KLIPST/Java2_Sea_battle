package sample;

import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;
import javax.swing.AbstractListModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * Created by KLIPST on 06.10.2014.
 */

public class tableSea extends JFrame {
    final int SIZE = 10;    // размер поля
    Object[][] data = new Object[SIZE][SIZE];   // массив данных поля
    public boolean privacy;     // доступность действий
    JTable shareTable;  // глобализуем таблицу

                        // устанавливаем права на таблицу и видимость расставленных кораблей
    public tableSea(final boolean priv, final int type) {
        privacy = priv;
        setSize(430, 321);          // размер игрового поля

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

        final DefaultTableModel dm = new DefaultTableModel(lm.getSize(), 10)
        {
            // запрещаем редактирование ячеек
            public boolean isCellEditable(int rowIndex, int columnIndex)
            {
                return false;
            }
        };

        final JTable table = new JTable(dm){
            @Override
            // отображение ячеек
            public void setValueAt(Object cell, int row, int column){
                switch ((Integer) cell)
                {
                    case -1:
                        // можем видеть только свои корабли. чужие - нельзя
                        if (type == 1)
                            cell = "O";
                        else
                            cell = "";      // изменить на другое - стать читером
                        break;
                    case 0:
                        cell = "";          // пустая клетка
                        break;
                    case 1:
                        cell = "*";         // мимо
                        break;
                    case 2:
                        cell = "X";         // попадание!
                        break;
                    case 3:
                        break;
                    default:
                        break;
                }
                getModel().setValueAt(cell, convertRowIndexToModel(row),  convertColumnIndexToModel(column));
            }
        };

        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setCellSelectionEnabled(true);
        table.setRowHeight(table.getRowHeight() + 10);
        table.setSelectionMode(1);
        table.getTableHeader().setReorderingAllowed( false );

        // центрируем ячейки
        table.setDefaultRenderer(table.getColumnClass(1), new DefaultTableCellRenderer(){
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                super.setHorizontalAlignment(SwingConstants.CENTER);
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                return this;
            }
        });

        // запрещаем изменять вид колонок
        for (int i=0;i<SIZE;i++){
            table.getColumnModel().getColumn(i).setResizable(false);
        }

        // начальная инициализация
        for(int i=0;i<SIZE;i++)
            for(int j=0;j<SIZE;j++)
                data[i][j] = 0;

        // расставление кораблей
        shipPlacement();

        // заполнение полей
        for (int i=0;i<SIZE;i++){
            for (int j=0;j<SIZE;j++) {
                table.setValueAt(data[i][j], i, j);
            }
        }

        shareTable = table;
        // нажатие на ячейку
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = table.rowAtPoint(evt.getPoint());
                int col = table.columnAtPoint(evt.getPoint());

                // стреляем
                int next = hit(row, col);
                if (next >= 0){
                    // если промахнулись - отдаём ход
                    if(next != 1){
                        changePrivacy();
                    }
                }
            }
        });


        JList rowHeader = new JList(lm);
        rowHeader.setFixedCellWidth(40);
        rowHeader.setFixedCellHeight(table.getRowHeight());
        rowHeader.setCellRenderer(new RowHeaderRenderer(table));


        JList colHeader = new JList(lm);
        colHeader.setFixedCellWidth(40);
        colHeader.setFixedCellHeight(table.getRowHeight());
        colHeader.setCellRenderer(new ColumnHeaderRenderer(table));


        JScrollPane scroll = new JScrollPane(table);
        scroll.setRowHeaderView(rowHeader);
        scroll.setColumnHeaderView(colHeader);
        getContentPane().add(scroll, BorderLayout.CENTER);

        this.setEnabled(privacy);
    }

    // меняем доступность окна
    void changePrivacy(){
        privacy = !privacy;
        this.setEnabled(privacy);
    }

    // обновляем данные в таблице
    void refreshTable(){
        for (int i=0;i<SIZE;i++){
            for (int j=0;j<SIZE;j++) {
                shareTable.setValueAt(data[i][j], i, j);
            }
        }
    }

    /*
    Логика расставления кораблей
    - ставятся только в вертикальной позиции
    - нет вычислений на наличие соседних кораблей
     */
    void shipPlacement(){
        Random rnd = new Random();

        for (int i=3;i>=0;i--){
            for (int j=0;j<=(3-i);j++) {
                int x, y;
                boolean ok = false;
                do{
                    y = rnd.nextInt(SIZE-1);
                    x = rnd.nextInt(SIZE-1);
                    if (x > (SIZE-4))
                        x -= i;
                    for (int k=x; k<=x+i; k++){
                        if ((Integer) data[k][y] == -1) {
                            ok = true;
                            break;
                        }
                        else
                            ok = false;
                    }
                }while(ok);
                for (int k=x; k<=x+i; k++){
                    data[k][y] = -1;
                }
            }
        }

    }

    // Вычисление выстрела
    public int hit(int x, int y){
        int cell = (Integer) data[x][y];
        int err = 0;
        switch (cell)
        {
            case -1:
                // если попали по кораблю
                data[x][y] = 2; // to hit
                err = 1; // попали - имеем право на еще один ход
                break;
            case 0:
                // по пустому полю
                data[x][y] = 1;  // to miss
                break;
            // поля, описанные ниже уже были использованы. стрелять по ним нельзя
            case 1:
                // miss
                err = -1;
                break;
            case 2:
                // hit
                err = -1;
                break;
            case 3:
                // destroyed
                break;
            default:
                break;
        }
        refreshTable();
        return err;
    }

    // проверка на наличие "живых" ячеек
    public boolean aliveCount(){
        int count = 0;
        for(int i=0;i<SIZE;i++)
            for(int j=0;j<SIZE;j++)
                if ((Integer) data[i][j] == -1)
                    count++;
        return count > 0;

    }
}



// Логика отрисовки заголовков таблицы
class RowHeaderRenderer extends JLabel implements ListCellRenderer {

    RowHeaderRenderer(JTable table) {
        JTableHeader header = table.getTableHeader();
        setOpaque(true);
        setBorder(UIManager.getBorder("TableHeader.cellBorder"));
        setHorizontalAlignment(CENTER);
        setForeground(header.getForeground());
        setBackground(header.getBackground());
        setFont(header.getFont());
    }

    public Component getListCellRendererComponent(JList list, Object value,
                                                  int index, boolean isSelected, boolean cellHasFocus) {
        setText((value == null) ? "" : value.toString());
        return this;
    }
}

class ColumnHeaderRenderer extends JLabel implements ListCellRenderer {

    ColumnHeaderRenderer(JTable table) {
        JTableHeader header = table.getTableHeader();
        setOpaque(true);
        setBorder(UIManager.getBorder("TableHeader.cellBorder"));
        setVerticalAlignment(CENTER);
        setForeground(header.getForeground());
        setBackground(header.getBackground());
        setFont(header.getFont());
    }

    public Component getListCellRendererComponent(JList list, Object value,
                                                  int index, boolean isSelected, boolean cellHasFocus) {
        setText((value == null) ? "" : value.toString());
        return this;
    }
}
