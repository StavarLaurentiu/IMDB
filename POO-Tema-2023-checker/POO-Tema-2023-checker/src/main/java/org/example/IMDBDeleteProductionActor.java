import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class IMDBDeleteProductionActor extends JFrame {

    private JComboBox<String> entryToDeleteComboBox;
    private JButton deleteButton;
    private JButton returnButton;

    private User<?> currentUser;

    public IMDBDeleteProductionActor(User<?> user) {
        this.currentUser = user;

        setTitle("Delete Production/Actor");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 400);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(4, 1));

        JLabel entryToDeleteLabel = new JLabel("Select Production/Actor to Delete:");
        entryToDeleteComboBox = new JComboBox<>();
        refreshComboBox();
        mainPanel.add(entryToDeleteLabel);
        mainPanel.add(entryToDeleteComboBox);

        deleteButton = new JButton("Delete");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedEntry = (String) entryToDeleteComboBox.getSelectedItem();

                for (Production production : IMDB.productionsList) {
                    if (production.title.equals(selectedEntry)) {
                        ((Staff <?>) currentUser).removeProductionSystem(production);
                        break;
                    }
                }

                for (Actor actor : IMDB.actorsList) {
                    if (actor.getName().equals(selectedEntry)) {
                        ((Staff <?>) currentUser).removeActorSystem(actor);
                        break;
                    }
                }

                refreshComboBox();
                JOptionPane.showMessageDialog(null, "Entry deleted successfully!");
            }
        });
        mainPanel.add(deleteButton);

        returnButton = new JButton("Return to Home Screen");
        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new IMDBHomeScreen(currentUser);
            }
        });
        mainPanel.add(returnButton, BorderLayout.SOUTH);

        getContentPane().add(mainPanel);
        setVisible(true);
    }

    private void refreshComboBox() {
        entryToDeleteComboBox.removeAllItems();

        if (currentUser.accountType == AccountType.ADMIN) {
            for (Production production : IMDB.productionsList)
                    entryToDeleteComboBox.addItem(production.title);

            for (Actor actor : IMDB.actorsList)
                if (actor.getContributor().equals(currentUser.username))
                    entryToDeleteComboBox.addItem(actor.getName());
        } else {
            for (Production production : IMDB.productionsList) {
                if (production.contributor.equals(currentUser.username)) {
                    entryToDeleteComboBox.addItem(production.title);
                }
            }

            for (Actor actor : IMDB.actorsList) {
                if (actor.getContributor().equals(currentUser.username)) {
                    entryToDeleteComboBox.addItem(actor.getName());
                }
            }
        }
    }
}
