import java.awt.event.KeyEvent;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;

public class CustomerCheckIn extends javax.swing.JFrame {

    public CustomerCheckIn() {
        initComponents();
        txtdate.setText(new SimpleDateFormat("yyyy/MM/dd").format(new Date()));
        txtname.requestFocus();
        loadAvailableRooms();
    }

    private void loadAvailableRooms() {
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel", "root", "1234");
             PreparedStatement pst = con.prepareStatement(
                     "SELECT roomnumber, price FROM room WHERE status=? AND roomtype=? AND bed=?")) {

            pst.setString(1, "Not Booked");
            pst.setString(2, comboroomtype.getSelectedItem().toString());
            pst.setString(3, combobed.getSelectedItem().toString());

            try (ResultSet rs = pst.executeQuery()) {
                comboroomnumber.removeAllItems();
                while (rs.next()) {
                    comboroomnumber.addItem(rs.getString("roomnumber"));
                }
            }

            updatePriceForSelectedRoom();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading rooms: " + e.getMessage());
        }
    }

    private void updatePriceForSelectedRoom() {
        if (comboroomnumber.getItemCount() == 0) {
            txtprice.setText("");
            return;
        }

        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel", "root", "1234");
             PreparedStatement pst = con.prepareStatement("SELECT price FROM room WHERE roomnumber=?")) {

            pst.setString(1, comboroomnumber.getSelectedItem().toString());
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    txtprice.setText(rs.getString("price"));
                }
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error fetching price: " + e.getMessage());
        }
    }

    private boolean validateFields() {
        if (txtname.getText().isEmpty() || txtmob.getText().isEmpty() || txtemail.getText().isEmpty()
                || txtnat.getText().isEmpty() || txtadhar.getText().isEmpty() || txtaddres.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required");
            return false;
        }

        if (!txtmob.getText().matches("\\d{11}")) {
            JOptionPane.showMessageDialog(this, "Mobile number must be 11 digits.");
            txtmob.requestFocus();
            return false;
        }

        if (!txtadhar.getText().matches("\\d{13}")) {
            JOptionPane.showMessageDialog(this, "Aadhaar number must be 13 digits.");
            txtadhar.requestFocus();
            return false;
        }

        if (!isValidEmail(txtemail.getText())) {
            JOptionPane.showMessageDialog(this, "Invalid Email id");
            txtemail.requestFocus();
            return false;
        }

        if (txtprice.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "This type of room not available. Select another room.");
            return false;
        }

        return true;
    }

    private boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@gmail\\.com$");
    }

    private void clearFields() {
        txtname.setText("");
        txtmob.setText("");
        txtnat.setText("");
        txtemail.setText("");
        txtadhar.setText("");
        txtaddres.setText("");
        combogender.setSelectedIndex(0);
        combobed.setSelectedIndex(0);
        comboroomtype.setSelectedIndex(0);
        loadAvailableRooms();
    }

    private void allotRoom() {
        if (!validateFields()) return;

        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel", "root", "1234");
             PreparedStatement pst = con.prepareStatement(
                     "INSERT INTO customer(name, mobile, email, gender, address, id, nationality, date, roomnumber, bed, roomtype, price, status) " +
                             "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")) {

            pst.setString(1, txtname.getText());
            pst.setString(2, txtmob.getText());
            pst.setString(3, txtemail.getText().toLowerCase());
            pst.setString(4, combogender.getSelectedItem().toString());
            pst.setString(5, txtaddres.getText());
            pst.setString(6, txtadhar.getText());
            pst.setString(7, txtnat.getText());
            pst.setString(8, txtdate.getText());
            pst.setString(9, comboroomnumber.getSelectedItem().toString());
            pst.setString(10, combobed.getSelectedItem().toString());
            pst.setString(11, comboroomtype.getSelectedItem().toString());
            pst.setString(12, txtprice.getText());
            pst.setNull(13, Types.VARCHAR);

            pst.executeUpdate();
            JOptionPane.showMessageDialog(this, "Room Allotted");

            // Update room status
            try (PreparedStatement updateRoom = con.prepareStatement("UPDATE room SET status=? WHERE roomnumber=?")) {
                updateRoom.setString(1, "Booked");
                updateRoom.setString(2, comboroomnumber.getSelectedItem().toString());
                updateRoom.executeUpdate();
            }

            clearFields();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage());
        }
    }

    // --- GUI events simplified for brevity ---
    private void comboroomtypeItemStateChanged(java.awt.event.ItemEvent evt) { loadAvailableRooms(); }
    private void combobedItemStateChanged(java.awt.event.ItemEvent evt) { loadAvailableRooms(); }
    private void comboroomnumberItemStateChanged(java.awt.event.ItemEvent evt) { updatePriceForSelectedRoom(); }
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) { allotRoom(); }
    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) { clearFields(); }

    // --- Main method ---
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> new CustomerCheckIn().setVisible(true));
    }

    // --- Variables declaration (keep as-is) ---
    private javax.swing.JComboBox<String> combobed;
    private javax.swing.JComboBox<String> combogender;
    private javax.swing.JComboBox<String> comboroomnumber;
    private javax.swing.JComboBox<String> comboroomtype;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JTextField txtaddres;
    private javax.swing.JTextField txtadhar;
    private javax.swing.JTextField txtdate;
    private javax.swing.JTextField txtemail;
    private javax.swing.JTextField txtmob;
    private javax.swing.JTextField txtname;
    private javax.swing.JTextField txtnat;
    private javax.swing.JTextField txtprice;
    // --- End of variables declaration ---
}
