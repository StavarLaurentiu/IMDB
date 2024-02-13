import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class IMDBProductionScreen extends JFrame implements ActionListener {
    private User<?> currentUser;
    private Production currentProduction;
    private JTextField valueTextField;
    private JTextArea textArea;

    public IMDBProductionScreen(User<?> user, Production production) {
        setTitle("Production Screen for \"" + production.title + "\"");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 800);
        setLocationRelativeTo(null);

        currentUser = user;
        currentProduction = production;

        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Panel for production details
        JPanel detailsPanel = new JPanel(new BorderLayout());

        // Production image
        ImageIcon productionImage = new ImageIcon(currentProduction.imagePath);
        JLabel imageLabel = new JLabel();
        imageLabel.setIcon(productionImage);

        // Name of the production
        JLabel nameLabel = new JLabel(currentProduction.title);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 24));
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Panel for image and title in a horizontal layout
        JPanel imageTitlePanel = new JPanel(new BorderLayout());
        imageTitlePanel.add(imageLabel, BorderLayout.WEST);
        imageTitlePanel.add(nameLabel, BorderLayout.CENTER);

        // Add image and name to details panel
        detailsPanel.add(imageTitlePanel, BorderLayout.NORTH);

        // Information about the production
        JTextArea infoTextArea = null;
        if (currentProduction instanceof Movie) {
            infoTextArea = new JTextArea("Here you can see information about the production.\n" +
                    "Title: " + currentProduction.title + "\n" +
                    "Type: Movie" + "\n" +
                    "Directors: " + currentProduction.directors + "\n" +
                    "Actors: " + currentProduction.actors + "\n" +
                    "Genres: " + currentProduction.genres.toString() + "\n" +
                    "Description: " + currentProduction.description + "\n" +
                    "Average Rating: " + currentProduction.averageRating + "\n" +
                    "Duration: " + ((Movie) currentProduction).duration + "\n" +
                    "Release Year: " + ((Movie) currentProduction).releaseYear + "\n" +
                    "Ratings: " + currentProduction.ratings.toString() + "\n" +
                    "Contributor: " + currentProduction.contributor

            );
        } else {
            infoTextArea = new JTextArea("Here you can see information about the production.\n" +
                    "Title: " + currentProduction.title + "\n" +
                    "Type: Series" + "\n" +
                    "Directors: " + currentProduction.directors + "\n" +
                    "Actors: " + currentProduction.actors + "\n" +
                    "Genres: " + currentProduction.genres.toString() + "\n" +
                    "Description: " + currentProduction.description + "\n" +
                    "Average Rating: " + currentProduction.averageRating + "\n" +
                    "Release Year: " + ((Series) currentProduction).releaseYear + "\n" +
                    "Number of Seasons: " + ((Series) currentProduction).numSeasons + "\n" +
                    "Episodes by Season: " + ((Series) currentProduction).getEpisodesBySeason().toString() + "\n" +
                    "Ratings: " + currentProduction.ratings.toString() + "\n" +
                    "Contributor: " + currentProduction.contributor
            );
        }
        infoTextArea.setLineWrap(true);
        infoTextArea.setWrapStyleWord(true);
        infoTextArea.setEditable(false);
        JScrollPane infoScrollPane = new JScrollPane(infoTextArea);

        // Button to add/remove production to/from favorites
        JButton addToFavoritesButton;
        if (currentUser.hasPreference(currentProduction)) {
            addToFavoritesButton = new JButton("Remove from Favorites");
        } else {
            addToFavoritesButton = new JButton("Add to Favorites");
        }
        addToFavoritesButton.addActionListener(this);
        detailsPanel.add(addToFavoritesButton, BorderLayout.SOUTH);

        // Panel for rating section (bottom)
        JPanel ratingPanel = new JPanel(new GridBagLayout());

        // Rating components
        valueTextField = new JTextField(5); // Input for rating value
        textArea = new JTextArea(3, 20); // Input for rating text
        JButton submitRatingButton = new JButton("Submit Rating");
        submitRatingButton.addActionListener(this);

        // Add rating components to panel using GridBagConstraints
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 5, 5, 5);
        ratingPanel.add(new JLabel("Value:"), gbc);

        gbc.gridx = 1;
        ratingPanel.add(valueTextField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        ratingPanel.add(new JLabel("Text:"), gbc);

        gbc.gridx = 1;
        ratingPanel.add(new JScrollPane(textArea), gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        ratingPanel.add(submitRatingButton, gbc);

        // Button to return to home menu
        JButton homeButton = new JButton("Return to Home Menu");
        homeButton.addActionListener(this);

        // Add all components to the main panel using GridBagConstraints for positioning
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(detailsPanel, gbc);

        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(infoScrollPane, gbc);

        gbc.gridy = 2;
        gbc.weighty = 0.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 1;
        mainPanel.add(addToFavoritesButton, gbc);

        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;
        mainPanel.add(ratingPanel, gbc);

        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(homeButton, gbc);

        // Add main panel to the frame
        add(mainPanel);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Add to Favorites") || e.getActionCommand().equals("Remove from Favorites")) {
            if (currentUser.hasPreference(currentProduction)) {
                currentUser.removePreference(currentProduction);
                JOptionPane.showMessageDialog(this, "Production removed from favorites.");
            } else {
                currentUser.addPreference(currentProduction);
                JOptionPane.showMessageDialog(this, "Production added to favorites.");
            }

            dispose();
            new IMDBProductionScreen(currentUser, currentProduction);
        } else if (e.getActionCommand().equals("Submit Rating")) {
            if (currentUser instanceof Regular) {
                boolean alreadyRated = false;
                for (Rating r : currentProduction.ratings) {
                    if (r.getUsername().equals(currentUser.username)) {
                        alreadyRated = true;
                        break;
                    }
                }

                if (alreadyRated) {
                    JOptionPane.showMessageDialog(this, "You have already rated this production. " +
                            "\nIf you want to change your rating, please delete the current one and submit a new one.");
                    return;
                }

                try {
                    int value = Integer.parseInt(valueTextField.getText());
                    String comments = textArea.getText();

                    if (value < 1 || value > 10) {
                        JOptionPane.showMessageDialog(this, "Invalid rating value. The value should be between 1 and 10.");
                    } else {
                        Rating rating = new Rating(currentUser.username, value, comments);
                        ((Regular<?>) currentUser).addRating(currentProduction, rating);
                        JOptionPane.showMessageDialog(this, "Rating submitted successfully.");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid rating value. The value should be between 1 and 10.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Only regular users can rate productions.");
            }

            dispose();
            new IMDBProductionScreen(currentUser, currentProduction);
        } else if (e.getActionCommand().equals("Return to Home Menu")) {
            dispose();
            new IMDBHomeScreen(currentUser);
        }
    }
}
