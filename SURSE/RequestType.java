public enum RequestType {
    DELETE_ACCOUNT,
    ACTOR_ISSUE,
    MOVIE_ISSUE,
    OTHERS,
    NULL;
    public static RequestType fromString(String value) {
        switch (value) {
            case "DELETE_ACCOUNT":
                return DELETE_ACCOUNT;
            case "ACTOR_ISSUE":
                return ACTOR_ISSUE;
            case "MOVIE_ISSUE":
                return MOVIE_ISSUE;
            case "OTHERS":
                return OTHERS;
            default:
                throw new IllegalArgumentException("Valoarea JSON nu poate fi mapata la RequestType: " + value);
        }
    }

    @Override
    public String toString() {
        switch (this) {
            case DELETE_ACCOUNT:
                return "DELETE_ACCOUNT";
            case ACTOR_ISSUE:
                return "ACTOR_ISSUE";
            case MOVIE_ISSUE:
                return "MOVIE_ISSUE";
            case OTHERS:
                return "OTHERS";
            case NULL:
                return "NULL";
            default:
                throw new IllegalArgumentException("Valoarea enumului nu poate fi convertita in sir: " + this);
        }
    }
}
