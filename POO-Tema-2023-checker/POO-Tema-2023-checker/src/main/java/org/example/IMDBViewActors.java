import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class IMDBViewActors extends JFrame {
    private User<?> user;
    private ArrayList<Actor> actors;

    public IMDBViewActors(User<?> user) {
        this.user = user;
        this.actors = (ArrayList<Actor>) IMDB.actorsList;

        setTitle("View Actors");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        JPanel actorsPanel = new JPanel(new GridLayout(0, 4, 20, 20));
        actorsPanel.setBorder(BorderFactory.createEmptyBorder(40, 20, 20, 20)); // Adding padding

        for (Actor actor : actors) {
            JPanel actorPanel = new JPanel(new BorderLayout());
            actorPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

            ImageIcon icon = new ImageIcon(actor.getPhotoPath());
            JLabel actorImage = new JLabel(icon);
            actorImage.setHorizontalAlignment(SwingConstants.CENTER);
            actorImage.setPreferredSize(new Dimension(150, 160));
            actorPanel.add(actorImage, BorderLayout.CENTER);

            JButton actorButton = new JButton(actor.getName());
            actorButton.setPreferredSize(new Dimension(90, 40));
            actorButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dispose();
                    new IMDBActorScreen(user, actor);
                }
            });
            actorPanel.add(actorButton, BorderLayout.SOUTH);
            actorsPanel.add(actorPanel);
        }

        JScrollPane scrollPane = new JScrollPane(actorsPanel);
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        setVisible(true);
    }
}
