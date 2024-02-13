import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class IMDBDeleteRating extends JFrame {
    private JComboBox<Rating> ratingsComboBox;
    private JButton deleteButton;
    private User<?> currentUser;

    public IMDBDeleteRating(User<?> user) {
        this.currentUser = user;

        setTitle("Delete Rating");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 250);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new GridLayout(2, 1));

        JLabel selectLabel = new JLabel("Select a Rating to Delete:");
        mainPanel.add(selectLabel);

        ratingsComboBox = new JComboBox<>();
        populateRatingsComboBox();
        mainPanel.add(ratingsComboBox);

        deleteButton = new JButton("Delete Rating");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Rating rating = (Rating) ratingsComboBox.getSelectedItem();
                Production production = null;

                if (rating == null) {
                    JOptionPane.showMessageDialog(null, "No rating selected.");
                    return;
                }

                for (Production p : IMDB.productionsList) {
                    for (Rating r : p.ratings) {
                        if (r.equals(rating)) {
                            production = p;
                            break;
                        }
                    }
                }

                if (production != null) {
                    production.removeRating(rating);
                    populateRatingsComboBox();
                    JOptionPane.showMessageDialog(null, "Rating deleted successfully.");
                } else {
                    JOptionPane.showMessageDialog(null, "Rating not found.");
                }
            }
        });
        mainPanel.add(deleteButton);

        // Add return home button
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

    private void populateRatingsComboBox() {
        ratingsComboBox.removeAllItems();

        for (Production production : IMDB.productionsList) {
            for (Rating rating : production.ratings) {
                if (rating.getUsername().equals(currentUser.username)) {
                    ratingsComboBox.addItem(rating);
                }
            }
        }
    }
}
