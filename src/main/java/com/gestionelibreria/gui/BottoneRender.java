package com.gestionelibreria.gui;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

class BottoneRender extends JPanel implements TableCellRenderer {
    private final JButton modificaBottone;
    private final JButton rimuoviButtone;

    public BottoneRender() {
        setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
        modificaBottone = new JButton("Modifica");
        modificaBottone.setForeground(Color.BLUE);
        rimuoviButtone = new JButton("Rimuovi");
        rimuoviButtone.setForeground(Color.RED);
        add(modificaBottone);
        add(rimuoviButtone);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        return this;
    }


}//bottoneRender