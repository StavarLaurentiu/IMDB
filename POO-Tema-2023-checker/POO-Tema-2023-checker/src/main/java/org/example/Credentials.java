import java.util.Random;

public class Credentials {
    private String email;
    private String password;

    public Credentials() {}

    public Credentials(String email) {
        this.email = email;
        this.password = generatePassword();
    }

    public Credentials(String email, String password) {
        this.email = email;
        this.password = password;
    }

    private String generatePassword() {
        String password = "";
        Random random = new Random();
        int randomInt;
        char randomChar;
        boolean intOrChar;

        for (int i = 0; i < 10; i++) {
            randomInt = random.nextInt(9);
            randomChar = (char) (random.nextInt(26) + 'a');
            intOrChar = random.nextBoolean();

            if (intOrChar) {
                password = password + randomChar;
            } else {
                password = password + randomInt;
            }
        }

        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
