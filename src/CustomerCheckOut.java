import java.sql.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class CustomerCheckOut extends javax.swing.JFrame {

    /**
     * Creates new form CustomerCheckOut
     */
    public CustomerCheckOut() {
        initComponents();
        // Set today's date for check-out
        LocalDate today = LocalDate.now(ZoneId.of("Asia/Colombo"));
        txtoutdate.setText(today.toString());

        loadCustomers(); // Populate table
    }

    // Load customers who are currently checked in
    private void loadCustomers() {
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(
