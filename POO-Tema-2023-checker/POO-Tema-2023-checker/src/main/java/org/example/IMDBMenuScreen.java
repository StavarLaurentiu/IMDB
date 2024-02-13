import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class IMDBMenuScreen extends JFrame implements ActionListener {
    private User<?> currentUser;

    public IMDBMenuScreen(User<?> user) {
        setTitle("Menu Screen");
        currentUser = user;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 800);
        setLocationRelativeTo(null);

        // Build all the buttons
        JButton productionsButton = new JButton("View Productions");
        productionsButton.addActionListener(this);

        JButton actorsButton = new JButton("View Actors");
        actorsButton.addActionListener(this);

        JButton notificationsButton = new JButton("View Notifications");
        notificationsButton.addActionListener(this);

        JButton addDeleteRequestButton = new JButton("Add/Delete a request");
        addDeleteRequestButton.addActionListener(this);

        JButton deleteRatingButton = new JButton("Delete a review you wrote");
        deleteRatingButton.addActionListener(this);

        JButton addProductionActorButton = new JButton("Add a Production/Actor");
        addProductionActorButton.addActionListener(this);

        JButton deleteProductionActorButton = new JButton("Delete a Production/Actor");
        deleteProductionActorButton.addActionListener(this);

        JButton viewSolveRequestsButton = new JButton("View/Solve Requests");
        viewSolveRequestsButton.addActionListener(this);

        JButton modifyProductionActorButton = new JButton("Update a Production/Actor");
        modifyProductionActorButton.addActionListener(this);

        JButton addDeleteUserButton = new JButton("Add/Delete a User");
        addDeleteUserButton.addActionListener(this);

        JButton homeButton = new JButton("Return to Home Screen");
        homeButton.addActionListener(this);

        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(this);

        // Add the buttons to the menu panel depending on the user's account type
        JPanel menuPanel = null;
        switch (user.accountType) {
            case REGULAR:
                menuPanel = new JPanel(new GridLayout(7, 1));
                menuPanel.add(productionsButton);
                menuPanel.add(actorsButton);
                menuPanel.add(notificationsButton);
                menuPanel.add(addDeleteRequestButton);
                menuPanel.add(deleteRatingButton);
                menuPanel.add(homeButton);
                menuPanel.add(logoutButton);

                break;
            case CONTRIBUTOR:
                menuPanel = new JPanel(new GridLayout(10, 1));
                menuPanel.add(productionsButton);
                menuPanel.add(actorsButton);
                menuPanel.add(notificationsButton);
                menuPanel.add(addDeleteRequestButton);
                menuPanel.add(addProductionActorButton);
                menuPanel.add(deleteProductionActorButton);
                menuPanel.add(viewSolveRequestsButton);
                menuPanel.add(modifyProductionActorButton);
                menuPanel.add(homeButton);
                menuPanel.add(logoutButton);

                break;
            case ADMIN:
                menuPanel = new JPanel(new GridLayout(10, 1));
                menuPanel.add(productionsButton);
                menuPanel.add(actorsButton);
                menuPanel.add(notificationsButton);
                menuPanel.add(addProductionActorButton);
                menuPanel.add(deleteProductionActorButton);
                menuPanel.add(viewSolveRequestsButton);
                menuPanel.add(modifyProductionActorButton);
                menuPanel.add(addDeleteUserButton);
                menuPanel.add(homeButton);
                menuPanel.add(logoutButton);

                break;
        }

        // Add the menu panel to the frame
        add(menuPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton clickedButton = (JButton) e.getSource();

            // Close the current frame
            dispose();

            // Perform actions based on the clicked button
            switch (clickedButton.getText()) {
                case "View Productions":
                    new IMDBViewProductions(currentUser);
                    break;
                case "View Actors":
                    new IMDBViewActors(currentUser);
                    break;
                case "View Notifications":
                    new IMDBViewNotifications(currentUser);
                    break;
                case "Add/Delete a request":
                    new IMDBAddDeleteRequest(currentUser);
                    break;
                case "Delete a review you wrote":
                    new IMDBDeleteRating(currentUser);
                    break;
                case "Add a Production/Actor":
                    new IMDBAddProductionActor(currentUser);
                    break;
                case "Delete a Production/Actor":
                    new IMDBDeleteProductionActor(currentUser);
                    break;
                case "View/Solve Requests":
                    new IMDBViewSolveRequests(currentUser);
                    break;
                case "Update a Production/Actor":
                    new IMDBUpdateProductionActor(currentUser);
                    break;
                case "Add/Delete a User":
                    new IMDBAddDeleteUser(currentUser);
                    break;
                case "Logout":
                    new IMDBLoginScreen();
                    break;
                case "Return to Home Screen":
                    new IMDBHomeScreen(currentUser);
                    break;
            }
        }
    }
}
