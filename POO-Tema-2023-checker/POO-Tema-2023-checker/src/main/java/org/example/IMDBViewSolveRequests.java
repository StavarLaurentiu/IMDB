import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class IMDBViewSolveRequests extends JFrame {

    private JComboBox<Request> userRequestsComboBox;
    private JButton solveButton;
    private JButton rejectButton;
    private JButton returnButton;

    private User<?> currentUser;

    public IMDBViewSolveRequests(User<?> user) {
        this.currentUser = user;

        setTitle("Solve or Reject Request");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 400);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(5, 1));

        JLabel selectRequestLabel = new JLabel("Select Request:");
        userRequestsComboBox = new JComboBox<>();
        updateRequestsComboBox();
        mainPanel.add(selectRequestLabel);
        mainPanel.add(userRequestsComboBox);

        solveButton = new JButton("Solve");
        solveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (userRequestsComboBox.getItemCount() == 0) {
                    JOptionPane.showMessageDialog(null, "No requests to solve.");
                    return;
                }

                Request selectedRequest = (Request) userRequestsComboBox.getSelectedItem();
                RequestsHolder.solveRequest(selectedRequest, "Solved");
                JOptionPane.showMessageDialog(null, "Request solved.");
                updateRequestsComboBox();
            }
        });
        mainPanel.add(solveButton);

        rejectButton = new JButton("Reject");
        rejectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (userRequestsComboBox.getItemCount() == 0) {
                    JOptionPane.showMessageDialog(null, "No requests to reject.");
                    return;
                }

                Request selectedRequest = (Request) userRequestsComboBox.getSelectedItem();
                RequestsHolder.solveRequest(selectedRequest, "Rejected");
                JOptionPane.showMessageDialog(null, "Request rejected.");
                updateRequestsComboBox();
            }
        });
        mainPanel.add(rejectButton);

        returnButton = new JButton("Return to Home Screen");
        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new IMDBMenuScreen(currentUser);
            }
        });
        mainPanel.add(returnButton);

        getContentPane().add(mainPanel);
        setVisible(true);
    }

    private void updateRequestsComboBox() {
        userRequestsComboBox.removeAllItems();

        if (RequestsHolder.getRequestsList(currentUser) == null) {
            return;
        }

        for (Request request : RequestsHolder.getRequestsList(currentUser)) {
            userRequestsComboBox.addItem(request);
        }
    }
}
