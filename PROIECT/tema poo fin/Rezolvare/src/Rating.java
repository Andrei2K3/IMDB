public class Rating {
    private String username;
    private int score; // Nota nr intreg intreg 1 și 10
    private String comment;

    public Rating(String username, int score, String comment) {
        this.username = username;
        this.score = score;
        this.comment = comment;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    // Alte metode necesare
    @Override
    public String toString() {
        return "{\n" +
                "  \"username\": \"" + username + "\",\n" +
                "  \"rating\": " + score + ",\n" +
                "  \"comment\": \"" + comment + "\"\n" +
                "}";
    }

}
