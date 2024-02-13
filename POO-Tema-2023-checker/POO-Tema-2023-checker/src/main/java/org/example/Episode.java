public class Episode {
    String name;
    int duration;

    public Episode() {}

    public Episode(String name, int duration) {
        this.name = name;
        this.duration = duration;
    }

    public void displayInfo() {
        if (name != null) System.out.print("      Episode Name: " + name);
        if (duration > 0) System.out.println(", duration: " + duration + " minutes");
    }

    public String toString() {
        return name;
    }
}
