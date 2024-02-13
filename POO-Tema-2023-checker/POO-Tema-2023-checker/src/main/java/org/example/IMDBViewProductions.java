import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class IMDBViewProductions extends JFrame {
    private User<?> user;
    private ArrayList<Production> productions;

    private JPanel productionsPanel;
    private JComboBox<String> genreFilter;
    private JSlider ratingsSlider;

    public IMDBViewProductions(User<?> user) {
        this.user = user;
        this.productions = (ArrayList<Production>) IMDB.productionsList;

        setTitle("View Productions");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        JPanel filterPanel = createFilterPanel();

        productionsPanel = new JPanel(new GridLayout(0, 4, 20, 20));
        productionsPanel.setBorder(BorderFactory.createEmptyBorder(40, 20, 20, 20));

        displayAllProductions();

        JScrollPane scrollPane = new JScrollPane(productionsPanel);
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(filterPanel, BorderLayout.NORTH);
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        setVisible(true);
    }

    private JPanel createFilterPanel() {
        JPanel filterPanel = new JPanel();
        filterPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));

        String[] genreOptions = createGenreOptions();
        genreFilter = new JComboBox<>(genreOptions);
        filterPanel.add(new JLabel("Filter by Genre: "));
        filterPanel.add(genreFilter);

        JLabel ratingsLabel = new JLabel("Filter by Number of Ratings: ");
        ratingsSlider = new JSlider(JSlider.HORIZONTAL, 0, 10, 0);
        ratingsSlider.setMajorTickSpacing(1);
        ratingsSlider.setPaintTicks(true);
        ratingsSlider.setPaintLabels(true);
        filterPanel.add(ratingsLabel);
        filterPanel.add(ratingsSlider);

        // Add listener to apply filters
        genreFilter.addActionListener(e -> filterProductions());
        ratingsSlider.addChangeListener(e -> filterProductions());

        return filterPanel;
    }

    private String[] createGenreOptions() {
        Genre[] genres = Genre.values();
        String[] genreOptions = new String[genres.length + 1];
        for (int i = 0; i < genres.length; i++) {
            genreOptions[i] = genres[i].name();
        }
        return genreOptions;
    }

    private void displayAllProductions() {
        productionsPanel.removeAll();

        for (Production production : productions) {
            JPanel productionPanel = createProductionPanel(production);
            productionsPanel.add(productionPanel);
        }

        productionsPanel.revalidate();
        productionsPanel.repaint();
    }

    private void filterProductions() {
        String selectedGenreName = (String) genreFilter.getSelectedItem();
        Genre selectedGenre = Genre.valueOf(selectedGenreName);
        int selectedRatings = ratingsSlider.getValue();

        productionsPanel.removeAll();

        for (Production production : productions) {
            boolean matchesGenre = selectedGenre.equals(Genre.ALL) || production.genres.contains(selectedGenre);
            boolean matchesRatings = production.ratings.size() >= selectedRatings;

            if (matchesGenre && matchesRatings) {
                JPanel productionPanel = createProductionPanel(production);
                productionsPanel.add(productionPanel);
            }
        }

        productionsPanel.revalidate();
        productionsPanel.repaint();
    }

    private JPanel createProductionPanel(Production production) {
        JPanel productionPanel = new JPanel(new BorderLayout());

        // JLabel for the image path of the production
        ImageIcon icon = new ImageIcon(production.imagePath);
        JLabel productionImage = new JLabel(icon);
        productionImage.setHorizontalAlignment(SwingConstants.CENTER);
        productionImage.setPreferredSize(new Dimension(150, 160));
        productionPanel.add(productionImage, BorderLayout.CENTER);

        // JButton for the title of the production
        JButton productionButton = new JButton(production.title);
        productionButton.setPreferredSize(new Dimension(150, 40));
        productionButton.addActionListener(e -> {
            dispose();
            new IMDBProductionScreen(user, production);
        });
        productionPanel.add(productionButton, BorderLayout.SOUTH);

        return productionPanel;
    }

}
