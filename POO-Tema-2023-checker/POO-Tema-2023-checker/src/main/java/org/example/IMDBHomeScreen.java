import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class IMDBHomeScreen extends JFrame {

    public IMDBHomeScreen(User<?> user) {
        setTitle("IMDb Home Screen");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 800);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // First layer - Menu button and user information in the top panel
        JPanel topPanel = new JPanel(new BorderLayout());

        JButton menuButton = new JButton("Menu");
        menuButton.setPreferredSize(new Dimension(100, 30));
        topPanel.add(menuButton, BorderLayout.WEST);
        menuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new IMDBMenuScreen(user);
            }
        });

        JPanel userInfoPanel = new JPanel();
        userInfoPanel.setLayout(new BoxLayout(userInfoPanel, BoxLayout.Y_AXIS));
        userInfoPanel.setBorder(BorderFactory.createTitledBorder("Your Information"));

        JLabel usernameLabel = new JLabel("Username: " + user.username);
        JLabel accountTypeLabel = new JLabel("Account Type: " + user.accountType);
        JLabel notificationLabel = new JLabel("Notifications: " + user.notifications.size());
        JLabel experienceLabel;
        if (user.accountType == AccountType.ADMIN) {
            experienceLabel = new JLabel("Experience: ∞");
        } else {
            experienceLabel = new JLabel("Experience: " + user.experience);
        }

        userInfoPanel.add(usernameLabel);
        userInfoPanel.add(experienceLabel);
        userInfoPanel.add(accountTypeLabel);
        userInfoPanel.add(notificationLabel);
        topPanel.add(userInfoPanel, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        // Second layer - Search bar in the center of the screen
        JTextField searchField = new JTextField("Here you can search a Movie/Series/Actor");
        searchField.setPreferredSize(new Dimension(600, 40));
        searchField.setHorizontalAlignment(JTextField.CENTER);

        JButton searchButton = new JButton("Search");
        searchButton.setPreferredSize(new Dimension(120, 40));
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchQuery = searchField.getText();
                if (searchQuery.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please enter a search query.");
                } else {
                    // Search for the production in the database
                    Production production = null;
                    for (Production p : IMDB.productionsList) {
                        if (p.title.equals(searchQuery)) {
                            production = p;
                            break;
                        }
                    }

                    if (production != null) {
                        dispose();
                        new IMDBProductionScreen(user, production);
                    }

                    // Search for the actor in the database
                    Actor actor = null;
                    for (Actor a : IMDB.actorsList) {
                        if (a.getName().equals(searchQuery)) {
                            actor = a;
                            break;
                        }
                    }

                    if (actor != null) {
                        dispose();
                        new IMDBActorScreen(user, actor);
                    }

                    // If the production or actor is not found, display an error message
                    if (production == null && actor == null) {
                        JOptionPane.showMessageDialog(null, "No results found.");
                    }
                }
            }
        });

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.setBorder(BorderFactory.createEmptyBorder(40, 20, 20, 20));
        add(searchPanel, BorderLayout.CENTER);

        // Third layer - Recommendations
        JPanel recommendationsPanel = new JPanel(new BorderLayout());
        recommendationsPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        JLabel recommendationsLabel = new JLabel("Our Recommendations");
        recommendationsLabel.setHorizontalAlignment(SwingConstants.CENTER);
        recommendationsPanel.add(recommendationsLabel, BorderLayout.NORTH);

        ArrayList<Production> recommendedProductions = new ArrayList<>(IMDB.productionsList);
        recommendedProductions.sort(new Comparator<Production>() {
            @Override
            public int compare(Production o1, Production o2) {
                return Double.compare(o2.getAverageRating(), o1.getAverageRating());
            }
        });

        JPanel productionsPanel = new JPanel(new GridLayout(2, 4, 20, 20));
        productionsPanel.setBorder(BorderFactory.createEmptyBorder(40, 20, 20, 20));

        for (int i = 1; i <= 8; i++) {
            Production recomandedProduction = recommendedProductions.get(i);

            JPanel productionPanel = new JPanel(new BorderLayout());
            productionPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

            ImageIcon icon = new ImageIcon(recomandedProduction.imagePath);
            JLabel productionImage = new JLabel(icon);
            productionImage.setHorizontalAlignment(SwingConstants.CENTER);
            productionImage.setPreferredSize(new Dimension(150, 160));
            productionPanel.add(productionImage, BorderLayout.CENTER);

            JButton productionButton = new JButton(recomandedProduction.title);
            productionButton.setPreferredSize(new Dimension(90, 40));
            productionButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dispose();
                    new IMDBProductionScreen(user, recomandedProduction);
                }
            });
            productionPanel.add(productionButton, BorderLayout.SOUTH);
            productionsPanel.add(productionPanel);
        }

        recommendationsPanel.add(productionsPanel, BorderLayout.CENTER);
        add(recommendationsPanel, BorderLayout.SOUTH);

        setVisible(true);
    }
}
