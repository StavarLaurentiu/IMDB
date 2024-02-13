import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class IMDBLoginScreen {
    private JFrame frame;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JLabel errorLabel;
    private static boolean jsonLoaded = false;

    public IMDBLoginScreen() {
        // Load data from JSON files only once
        if (!jsonLoaded) {
            try {
                IMDB.getInstance().loadDataFromJSON();
                jsonLoaded = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Initialize GUI
        initialize();
    }
    private void initialize() {
        frame = new JFrame("IMDb Login");
        frame.setSize(700, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel emailLabel = new JLabel("Email:");
        emailField = new JTextField();

        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField();

        errorLabel = new JLabel();
        errorLabel.setForeground(Color.RED); // Set error text color to red

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = emailField.getText();
                String password = new String(passwordField.getPassword());

                if (email.isEmpty() || password.isEmpty()) {
                    errorLabel.setText("Please enter username and password.");
                } else {
                    // Check if user exists in database
                    for (User<?> user : IMDB.usersList) {
                        if (user.info.getCredentials().getEmail().equals(email) &&
                                user.info.getCredentials().getPassword().equals(password)) {
                            frame.dispose();
                            new IMDBHomeScreen(user);
                        }
                    }

                    errorLabel.setText("Incorrect username or password.");
                }
            }
        });

        panel.add(emailLabel);
        panel.add(emailField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(new JLabel());
        panel.add(loginButton);
        panel.add(errorLabel);

        frame.add(panel);
        frame.setVisible(true);
    }
}
