public void s() {
    String query = "SELECT * FROM customer WHERE status=?";
    try (java.sql.Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel","root","1234");
         PreparedStatement pst = con.prepareStatement(query)) {
        
        pst.setString(1, "check out");
        try (ResultSet rs = pst.executeQuery()) {
            ResultSetMetaData rsmd = rs.getMetaData();
            DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
            model.setRowCount(0);

            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getString("billid"));
                row.add(rs.getString("roomnumber"));
                row.add(rs.getString("name"));
                row.add(rs.getString("mobile"));
                row.add(rs.getString("nationality"));
                row.add(rs.getString("gender"));
                row.add(rs.getString("email"));
                row.add(rs.getString("id"));
                row.add(rs.getString("address"));
                row.add(rs.getString("date"));
                row.add(rs.getString("outdate"));
                row.add(rs.getString("bed"));
                row.add(rs.getString("roomtype"));
                row.add(rs.getString("price"));
                row.add(rs.getString("days"));
                row.add(rs.getString("amount"));
                model.addRow(row);
            }
        }

    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
    }
}
