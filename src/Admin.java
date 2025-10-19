import java.sql.*;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class Admin extends javax.swing.JFrame {

    public Admin() {
        initComponents();
        loadUsers();
        txtemail.requestFocus();
    }

    // Helper method to get DB connection
    private Connection getConnection() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel","root","1234");
    }

    // Load all users into JTable
    public void loadUsers() {
        DefaultTableModel RecordTable = (DefaultTableModel) jTable1.getModel();
        RecordTable.setRowCount(0);

        try (Connection con = getConnection();
             PreparedStatement pst = con.prepareStatement("SELECT * FROM login");
             ResultSet rs = pst.executeQuery()) {

            while(rs.next()) {
                Vector<String> columnData = new Vector<>();
                columnData.add(rs.getString("name"));
                columnData.add(rs.getString("email"));
                columnData.add(rs.getString("password"));
                columnData.add(rs.getString("sq"));
                columnData.add(rs.getString("ans"));
                columnData.add(rs.getString("status"));
                RecordTable.addRow(columnData);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading users: " + ex.getMessage());
        }
    }

    // Search users by email
    public void searchByEmail(String email) {
        DefaultTableModel RecordTable = (DefaultTableModel) jTable1.getModel();
        RecordTable.setRowCount(0);

        try (Connection con = getConnection();
             PreparedStatement pst = con.prepareStatement("SELECT * FROM login WHERE email = ?")) {

            pst.setString(1, email);
            try (ResultSet rs = pst.executeQuery()) {

                if (!rs.isBeforeFirst()) {
                    JOptionPane.showMessageDialog(this, "No record found for this email.");
                    return;
                }

                while(rs.next()) {
                    Vector<String> columnData = new Vector<>();
                    columnData.add(rs.getString("name"));
                    columnData.add(rs.getString("email"));
                    columnData.add(rs.getString("password"));
                    columnData.add(rs.getString("sq"));
                    columnData.add(rs.getString("ans"));
                    columnData.add(rs.getString("status"));
                    RecordTable.addRow(columnData);
                }
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error searching: " + ex.getMessage());
        }
    }

    // Update status on double-click
    private void updateStatus(String
