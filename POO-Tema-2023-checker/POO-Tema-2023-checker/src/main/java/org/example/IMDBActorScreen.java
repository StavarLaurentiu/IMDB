import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class IMDBActorScreen extends JFrame implements ActionListener {
    private User<?> currentUser;
    private Actor currentActor;

    public IMDBActorScreen(User<?> user, Actor actor) {
        setTitle("Actor Screen for \"" + actor.getName() + "\"");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);

        currentUser = user;
        currentActor = actor;

        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Panel for production details
        JPanel detailsPanel = new JPanel(new BorderLayout());

        // Production image
        ImageIcon productionImage = new ImageIcon(actor.getPhotoPath());
        JLabel imageLabel = new JLabel();
        imageLabel.setIcon(productionImage);

        // Name of the production
        JLabel nameLabel = new JLabel(actor.getName());
        nameLabel.setFont(new Font("Arial", Font.BOLD, 24));
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Panel for image and title in a horizontal layout
        JPanel imageTitlePanel = new JPanel(new BorderLayout());
        imageTitlePanel.add(imageLabel, BorderLayout.WEST);
        imageTitlePanel.add(nameLabel, BorderLayout.CENTER);

        // Add image and name to details panel
        detailsPanel.add(imageTitlePanel, BorderLayout.NORTH);

        // Information about the actor
        JTextArea infoTextArea = new JTextArea("Here you can see information about the actor.\n" +
                "Name: " + actor.getName() + "\n" +
                "Biography: " + actor.getBiography() + "\n" +
                "Performances: " + actor.getPerformances() + "\n" +
                "Contributor: " + actor.getContributor() + "\n"
        );
        infoTextArea.setLineWrap(true);
        infoTextArea.setWrapStyleWord(true);
        infoTextArea.setEditable(false);
        JScrollPane infoScrollPane = new JScrollPane(infoTextArea);

        // Button to add/remove actor to/from favorites
        JButton addToFavoritesButton;
        if (currentUser.hasPreference(currentActor)) {
            addToFavoritesButton = new JButton("Remove from Favorites");
        } else {
            addToFavoritesButton = new JButton("Add to Favorites");
        }
        addToFavoritesButton.addActionListener(this);
        detailsPanel.add(addToFavoritesButton, BorderLayout.SOUTH);

        // Panel for rating section (bottom)
        JPanel ratingPanel = new JPanel(new GridBagLayout());

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
            if (currentUser.hasPreference(currentActor)) {
                currentUser.removePreference(currentActor);
                JOptionPane.showMessageDialog(this, "Actor removed from favorites.");
            } else {
                currentUser.addPreference(currentActor);
                JOptionPane.showMessageDialog(this, "Actor added to favorites.");
            }

            dispose();
            new IMDBActorScreen(currentUser, currentActor);
        } else if (e.getActionCommand().equals("Return to Home Menu")) {
            dispose();
            new IMDBHomeScreen(currentUser);
        }
    }
}
