import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.util.List;

public class IMDBAddDeleteRequest extends JFrame {
    private JComboBox<RequestType> requestTypeComboBox;
    private JTextField titleOrNameField;
    private JTextArea descriptionArea;
    private JButton submitButton;
    private JComboBox<String> userRequestsComboBox;
    private JButton deleteRequestButton;

    private User<?> currentUser;

    public IMDBAddDeleteRequest(User<?> user) {
        this.currentUser = user;

        setTitle("Make a Request / Delete Request");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 800);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new GridLayout(15, 1));

        JLabel makeRequestLabel = new JLabel("Make a Request", SwingConstants.CENTER);
        makeRequestLabel.setFont(new Font("Arial", Font.BOLD, 24));
        mainPanel.add(makeRequestLabel);

        // Request Type ComboBox
        JLabel requestTypeLabel = new JLabel("Request Type:");
        requestTypeComboBox = new JComboBox<>(RequestType.values());
        mainPanel.add(requestTypeLabel);
        mainPanel.add(requestTypeComboBox);

        // Title/Name Field
        JLabel titleOrNameLabel = new JLabel("Title/Name:");
        titleOrNameField = new JTextField();
        mainPanel.add(titleOrNameLabel);
        mainPanel.add(titleOrNameField);

        // Description Area
        JLabel descriptionLabel = new JLabel("Description:");
        descriptionArea = new JTextArea();
        JScrollPane descriptionScrollPane = new JScrollPane(descriptionArea);
        mainPanel.add(descriptionLabel);
        mainPanel.add(descriptionScrollPane);

        // Submit Button for making a request
        submitButton = new JButton("Submit Request");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get user input and create a new request
                RequestType selectedType = (RequestType) requestTypeComboBox.getSelectedItem();
                String titleOrName = titleOrNameField.getText();
                String description = descriptionArea.getText();
                LocalDateTime now = LocalDateTime.now();

                if (description.isEmpty() ||
                        (titleOrName.isEmpty() &&
                                (selectedType == RequestType.ACTOR_ISSUE
                                        || selectedType == RequestType.MOVIE_ISSUE))) {
                    JOptionPane.showMessageDialog(null, "Please fill in all the fields.");
                    return;
                }

                Request newRequest;
                if (selectedType == RequestType.ACTOR_ISSUE || selectedType == RequestType.MOVIE_ISSUE) {
                    newRequest = new Request(selectedType, titleOrName, now, description, currentUser.username);
                } else {
                    newRequest = new Request(selectedType, null, now, description, currentUser.username);
                }

                RequestsHolder.addRequest(newRequest);
                JOptionPane.showMessageDialog(null, "Request submitted successfully.");

                // Clear input fields after submitting the request
                titleOrNameField.setText("");
                descriptionArea.setText("");

                // Update the user's requests
                updateUserRequests();
            }
        });
        mainPanel.add(submitButton);

        // Leave some space
        mainPanel.add(new JLabel());

        JLabel deleteRequestLabel = new JLabel("Delete a Request", SwingConstants.CENTER);
        deleteRequestLabel.setFont(new Font("Arial", Font.BOLD, 24));
        mainPanel.add(deleteRequestLabel);


        // User Requests ComboBox for deletion
        JLabel userRequestsLabel = new JLabel("Select a Request to Delete:");
        userRequestsComboBox = new JComboBox<>();
        updateUserRequests();
        mainPanel.add(userRequestsLabel);
        mainPanel.add(userRequestsComboBox);

        // Delete Request Button
        deleteRequestButton = new JButton("Delete Request");
        deleteRequestButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedRequest = (String) userRequestsComboBox.getSelectedItem();

                // Find the request to delete
                Request requestToDelete = null;
                for (Request request : IMDB.requestsList) {
                    if (request.getDescription().equals(selectedRequest)) {
                        requestToDelete = request;
                        break;
                    }
                }

                // Delete the request
                RequestsHolder.removeRequest(requestToDelete);
                JOptionPane.showMessageDialog(null, "Request deleted successfully.");

                // Update the user's requests
                updateUserRequests();
            }
        });
        mainPanel.add(deleteRequestButton);

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

    private void updateUserRequests() {
        // Clear previous items and add the current user's requests
        userRequestsComboBox.removeAllItems();

        for (Request request : IMDB.requestsList) {
            if (request.getRequester().equals(currentUser.username)) {
                userRequestsComboBox.addItem(request.getDescription());
            }
        }
    }
}
