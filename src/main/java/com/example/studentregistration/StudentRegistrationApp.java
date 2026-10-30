package com.example.studentregistration;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.*;

public final class StudentRegistrationApp extends JFrame {
    private final JTextField name = new JTextField(24);
    private final JTextField email = new JTextField(24);
    private final JTextField phone = new JTextField(24);
    private final JTextField course = new JTextField(24);
    private final JLabel status = new JLabel(" ");

    private StudentRegistrationApp() {
        super("Student Registration");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(560, 390));
        JPanel root = new JPanel(new BorderLayout(16, 16));
        root.setBorder(new EmptyBorder(24, 28, 20, 28));
        root.setBackground(new Color(246, 248, 252));
        JLabel heading = new JLabel("Student registration");
        heading.setFont(new Font("SansSerif", Font.BOLD, 24));
        root.add(heading, BorderLayout.NORTH);

        JPanel fields = new JPanel(new GridBagLayout());
        fields.setOpaque(false);
        addField(fields, "Full name", name, 0);
        addField(fields, "Email address", email, 1);
        addField(fields, "Phone number", phone, 2);
        addField(fields, "Course / program", course, 3);
        root.add(fields, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new BorderLayout(8, 8));
        bottom.setOpaque(false);
        status.setForeground(new Color(33, 112, 75));
        bottom.add(status, BorderLayout.CENTER);
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        actions.setOpaque(false);
        JButton clear = new JButton("Clear");
        JButton submit = new JButton("Register student");
        submit.setBackground(new Color(42, 91, 215));
        submit.setForeground(Color.WHITE);
        clear.addActionListener(e -> clearForm());
        submit.addActionListener(e -> registerStudent());
        actions.add(clear);
        actions.add(submit);
        bottom.add(actions, BorderLayout.SOUTH);
        root.add(bottom, BorderLayout.SOUTH);
        setContentPane(root);
        pack();
        setLocationRelativeTo(null);
    }

    private static void addField(JPanel panel, String label, JTextField field, int row) {
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(9, 4, 9, 12);
        c.gridy = row;
        c.gridx = 0;
        c.anchor = GridBagConstraints.LINE_START;
        panel.add(new JLabel(label), c);
        c.gridx = 1;
        c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        panel.add(field, c);
    }

    private void registerStudent() {
        String studentName = name.getText().trim();
        String studentEmail = email.getText().trim();
        String studentPhone = phone.getText().trim();
        String studentCourse = course.getText().trim();
        if (studentName.isEmpty() || studentEmail.isEmpty() || studentPhone.isEmpty() || studentCourse.isEmpty()) {
            showError("Please complete every field.");
            return;
        }
        if (!studentEmail.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
            showError("Enter a valid email address.");
            return;
        }

        String url = System.getenv().getOrDefault("DB_URL", "jdbc:mysql://localhost:3306/student_registration?serverTimezone=UTC");
        String user = System.getenv().getOrDefault("DB_USER", "root");
        String password = System.getenv().getOrDefault("DB_PASSWORD", "");
        String sql = "INSERT INTO students (full_name, email, phone, course) VALUES (?, ?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, studentName);
            statement.setString(2, studentEmail);
            statement.setString(3, studentPhone);
            statement.setString(4, studentCourse);
            statement.executeUpdate();
            status.setForeground(new Color(33, 112, 75));
            status.setText("Registration saved successfully.");
            clearFields();
        } catch (SQLIntegrityConstraintViolationException ex) {
            showError("That email address is already registered.");
        } catch (SQLException ex) {
            showError("Could not save registration. Check MySQL settings and that the database script was run.");
            ex.printStackTrace();
        }
    }

    private void showError(String message) {
        status.setForeground(new Color(180, 45, 45));
        status.setText(message);
    }

    private void clearForm() {
        clearFields();
        status.setText(" ");
    }

    private void clearFields() {
        name.setText("");
        email.setText("");
        phone.setText("");
        course.setText("");
        name.requestFocusInWindow();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new StudentRegistrationApp().setVisible(true));
    }
}
