public class Rating {
    private String username;
    private int value;
    private String comments;

    public Rating() {}

    public Rating(String username, int value, String comments) {
        this.username = username;
        this.value = value;
        this.comments = comments;
    }

    public String getUsername() {
        return username;
    }

    public int getValue() {
        return value;
    }

    public String getComments() {
        return comments;
    }

    public void updateRating(int newScore, String newComments) {
        // Check if the new score is within the valid range [1, 10]
        if (newScore < 1 || newScore > 10) {
            System.out.println("Invalid score. The score should be between 1 and 10.");
            return;
        }

        // Update the rating if the conditions are met
        this.value = newScore;
        this.comments = newComments;
        System.out.println("Rating updated successfully.");
    }

    public void displayInfo() {
        System.out.println("    Rating: " + value + " by " + username + ", Comment: " + comments);
    }

    public boolean equals(Object o) {
        if (!(o instanceof Rating)) return false;

        return username.equals((((Rating) o).username))
                && value == (((Rating) o).value);
    }

    public String toString() {
        return "Rating: " + value + " by " + username + ", Comment: " + comments;
    }

}
