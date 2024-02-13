public class UserFactory {

    public UserFactory() {}

    public User getInstance(AccountType accountType) {
        switch (accountType) {
            case REGULAR:
                return new Regular();
            case CONTRIBUTOR:
                return new Contributor();
            case ADMIN:
                return new Admin();
            default:
                return null;
        }
    }
}
