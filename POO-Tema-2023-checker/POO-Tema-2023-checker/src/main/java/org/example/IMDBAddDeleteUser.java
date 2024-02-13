import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class IMDBAddDeleteUser extends JFrame {
    private JButton addUserButton;
    private JButton deleteUserButton;
    private JComboBox<String> accountTypeComboBox;
    private JTextField emailField;
    private JTextField nameField;
    private JTextField ageField;
    private JTextField genderField;
    private JTextField countryField;
    private JTextField dobField;
    private JTextField experienceField;
    private JComboBox<String> userComboBox;

    private User<?> currentUser;

    public IMDBAddDeleteUser(User<?> user) {
        this.currentUser = user;

        setTitle("Add/Delete User");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 800);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new GridLayout(25, 1));

        JLabel addUserLabel = new JLabel("Add User", SwingConstants.CENTER);
        addUserLabel.setFont(new Font("Arial", Font.BOLD, 24));
        mainPanel.add(addUserLabel);

        JLabel accountTypeLabel = new JLabel("Account Type:");
        accountTypeComboBox = new JComboBox<>(new String[]{"Regular", "Contributor", "Admin"});
        mainPanel.add(accountTypeLabel);
        mainPanel.add(accountTypeComboBox);

        JLabel emailLabel = new JLabel("Email:");
        emailField = new JTextField();
        mainPanel.add(emailLabel);
        mainPanel.add(emailField);

        JLabel nameLabel = new JLabel("Name:");
        nameField = new JTextField();
        mainPanel.add(nameLabel);
        mainPanel.add(nameField);

        JLabel ageLabel = new JLabel("Age:");
        ageField = new JTextField();
        mainPanel.add(ageLabel);
        mainPanel.add(ageField);

        JLabel genderLabel = new JLabel("Gender (Male/Female):");
        genderField = new JTextField();
        mainPanel.add(genderLabel);
        mainPanel.add(genderField);

        JLabel countryLabel = new JLabel("Country:");
        countryField = new JTextField();
        mainPanel.add(countryLabel);
        mainPanel.add(countryField);

        JLabel dobLabel = new JLabel("Date of Birth (yyyy-MM-dd):");
        dobField = new JTextField();
        mainPanel.add(dobLabel);
        mainPanel.add(dobField);

        JLabel experienceLabel = new JLabel("Experience:");
        experienceField = new JTextField();
        mainPanel.add(experienceLabel);
        mainPanel.add(experienceField);

        addUserButton = new JButton("Add User");
        addUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedType = (String) accountTypeComboBox.getSelectedItem();

                if (emailField.getText().isEmpty() || nameField.getText().isEmpty() || ageField.getText().isEmpty() ||
                        genderField.getText().isEmpty() || countryField.getText().isEmpty() || dobField.getText().isEmpty() ||
                        experienceField.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "All fields must be filled!");
                    return;
                }

                // Retrieve user input
                String email = emailField.getText();
                String name = nameField.getText();
                int age = 0;
                try {
                    age = Integer.parseInt(ageField.getText());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Age must be a number!");
                    return;
                }
                char gender = genderField.getText().charAt(0);
                String country = countryField.getText();

                LocalDate dob;
                try {
                    dob = LocalDate.parse(dobField.getText());
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Date of Birth must be in the format yyyy-MM-dd!");
                    return;
                }
                int experience = 0;
                try {
                    experience = Integer.parseInt(experienceField.getText());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Experience must be a number!");
                    return;
                }

                UserFactory userFactory = new UserFactory();
                User<?> newUser = null;

                if (selectedType.equals("Regular")) {
                    newUser = userFactory.getInstance(AccountType.REGULAR);
                } else if (selectedType.equals("Contributor")) {
                    newUser = userFactory.getInstance(AccountType.CONTRIBUTOR);
                } else if (selectedType.equals("Admin")) {
                    newUser = userFactory.getInstance(AccountType.ADMIN);
                }

                User <?>.InformationBuilder informationBuilder = newUser.new InformationBuilder();
                informationBuilder.setCredentials(new Credentials(email, User.generatePassword()));
                informationBuilder.setName(name);
                informationBuilder.setCountry(country);
                informationBuilder.setAge(age);
                informationBuilder.setGender(gender);
                informationBuilder.setDateOfBirth(dob);
                User.Information information = informationBuilder.getInformation();

                newUser.setInfo(information);
                newUser.setUsername(newUser.generateUsername());
                newUser.setExperience(experience);

                ((Admin <?>) currentUser).addUser(newUser);
                clearFields();
                JOptionPane.showMessageDialog(null, "User added successfully!");
                updateUser();
            }
        });
        mainPanel.add(addUserButton);

        // Leave some space
        mainPanel.add(new JLabel());

        JLabel deleteUserLabel = new JLabel("Delete User", SwingConstants.CENTER);
        deleteUserLabel.setFont(new Font("Arial", Font.BOLD, 24));
        mainPanel.add(deleteUserLabel);

        // User ComboBox for deletion
        JLabel userLabel = new JLabel("Select a User to Delete:");
        userComboBox = new JComboBox<>();
        updateUser();
        mainPanel.add(userLabel);
        mainPanel.add(userComboBox);

        // Delete Request Button
        deleteUserButton = new JButton("Delete User");
        deleteUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedUser = (String) userComboBox.getSelectedItem();

                // Find the user in the list
                User<?> userToDelete = null;
                for (User<?> user : IMDB.usersList) {
                    if (user.username.equals(selectedUser)) {
                        userToDelete = user;
                        break;
                    }
                }

                if (userToDelete == null) {
                    JOptionPane.showMessageDialog(null, "User not found!");
                    return;
                }

                // Remove the user from the list
                ((Admin <?>) currentUser).removeUser(userToDelete);

                updateUser();
                JOptionPane.showMessageDialog(null, "User deleted successfully!");
            }
        });
        mainPanel.add(deleteUserButton);

        // Leave some space
        mainPanel.add(new JLabel());

        // Return to Home Screen Button
        JButton homeButton = new JButton("Return to Home Screen");
        homeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new IMDBHomeScreen(currentUser);
            }
        });
        mainPanel.add(homeButton);

        getContentPane().add(mainPanel);
        setVisible(true);
    }

    private void updateUser() {
        // Clear previous items and add the current user's requests
        userComboBox.removeAllItems();

        // Add all users to the ComboBox
        for (User<?> user : IMDB.usersList) {
            if (!user.username.equals(currentUser.username))
                userComboBox.addItem(user.username);
        }
    }

    private void clearFields() {
        emailField.setText("");
        nameField.setText("");
        ageField.setText("");
        genderField.setText("");
        countryField.setText("");
        dobField.setText("");
        experienceField.setText("");
    }
}
