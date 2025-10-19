public GenerateBill() {
    initComponents();
    fetchCustomerDetails(); // Populate fields from DB first
    generateBillText();     // Then set txtbill text
}

private void fetchCustomerDetails() {
    String query = "SELECT * FROM customer WHERE billid=?";
    try (java.sql.Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel","root","1234");
         PreparedStatement pst = con.prepareStatement(query)) {
        Class.forName("com.mysql.cj.jdbc.Driver");
        pst.setString(1, id);
        try (ResultSet rs = pst.executeQuery()) {
            if(rs.next()){
                nm = rs.getString("name");
                mobile = rs.getString("mobile");
                email = rs.getString("email");
                roomnumber = rs.getString("roomnumber");
                bed = rs.getString("bed");
                type = rs.getString("roomtype");
                indate = rs.getString("date");
                outdate = rs.getString("outdate");
                price = rs.getString("price");
                days = rs.getString("days");
                amount = rs.getString("amount");
            }
        }
    } catch (ClassNotFoundException | SQLException ex) {
        JOptionPane.showMessageDialog(this, "Error fetching customer details: " + ex.getMessage());
    }
}

private void generateBillText() {
    StringBuilder bill = new StringBuilder();
    bill.append("\t\t-: Zahid Ali Mahaar :-\n");
    bill.append("**********************************************************************************\n");
    bill.append("Bill ID:- ").append(id).append("\n");
    bill.append("Customer Details:- \n");
    bill.append("Name:- ").append(nm).append("\n");
    bill.append("Mobile Number:- ").append(mobile).append("\n");
    bill.append("Email:- ").append(email).append("\n");
    bill.append("**********************************************************************************\n");
    bill.append("Room Details:- \n");
    bill.append("Room Number:- ").append(roomnumber).append("\n");
    bill.append("Type:- ").append(type).append("\n");
    bill.append("Bed:- ").append(bed).append("\n");
    bill.append("Price:- ").append(price).append("\n");
    bill.append("Check IN Date=").append(indate).append("\t\tNumber of Days=").append(days).append("\n");
    bill.append("Check OUT Date=").append(outdate).append("\t\tTotal Amount=").append(amount).append("\n");
    bill.append("**********************************************************************************\n");
    bill.append("\tThank You from Karachi Hotel Management, Please Visit Again.");

    txtbill.setText(bill.toString());
}
