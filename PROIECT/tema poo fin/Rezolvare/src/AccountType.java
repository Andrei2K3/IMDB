public enum AccountType {
    REGULAR,
    CONTRIBUTOR,
    ADMIN,
    NULL;

    public static AccountType fromString(String value) {
        switch (value) {
            case "Regular":
                return REGULAR;
            case "Contributor":
                return CONTRIBUTOR;
            case "Admin":
                return ADMIN;
            default:
                throw new IllegalArgumentException("Valoarea JSON nu poate fi mapata la AccountType: " + value);
        }
    }

    @Override
    public String toString() {
        switch (this) {
            case REGULAR:
                return "Regular";
            case CONTRIBUTOR:
                return "Contributor";
            case ADMIN:
                return "Admin";
            case NULL:
                return "NULL";
            default:
                throw new IllegalArgumentException("Valoarea enumului nu poate fi convertita in sir: " + this);
        }
    }
}