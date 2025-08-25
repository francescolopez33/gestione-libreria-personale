package com.gestionelibreria.gui;

import javax.swing.*;
import java.awt.*;
import java.util.EventObject;

class BottoneEditor extends DefaultCellEditor {
    protected JPanel panel;
    protected JButton modificaBottone;
    protected JButton rimuoviBottone;
    private int row;
    private final LibreriaGUI gui;

    public BottoneEditor(JCheckBox checkBox, LibreriaGUI gui) {
        super(checkBox);
        this.gui = gui;
        panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        modificaBottone = new JButton("Modifica");
        rimuoviBottone = new JButton("Rimuovi");

        modificaBottone.addActionListener(e -> {
            fireEditingStopped();
            gui.modificaLibro(row);
        });

        rimuoviBottone.addActionListener(e -> {
            fireEditingStopped();
            gui.rimuoviLibro(row);
        });

        panel.add(modificaBottone);
        panel.add(rimuoviBottone);
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        this.row = row;
        return panel;
    }//getTableCellEditor

    @Override
    public Object getCellEditorValue() {
        return new Object();
    }//getCeelEditor

    @Override
    public boolean isCellEditable(EventObject anEvent) {
        return true;
    }//isCelleditable

}//BottoneEditor
