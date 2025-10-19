// Correct JTable column name typo
jTable1.setModel(new javax.swing.table.DefaultTableModel(
    new Object [][] {
        {null, null, null, null, null},
        {null, null, null, null, null},
        {null, null, null, null, null},
        {null, null, null, null, null}
    },
    new String [] {
        "Room Number", "Room Type", "Bed", "Price", "Status" // Fixed "Pricce"
    }
) {
    boolean[] canEdit = new boolean [] {
        false, false, false, false, false
    };

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return canEdit [columnIndex];
    }
});

// Improve price validation in Add Room
try {
    double pric = Double.parseDouble(txtprice.getText());
    if(pric < 0){
        JOptionPane.showMessageDialog(this, "Price cannot be negative");
        return;
    }
    ...
} catch(NumberFormatException e){
    JOptionPane.showMessageDialog(this, "Price is not valid");
    return;
}
