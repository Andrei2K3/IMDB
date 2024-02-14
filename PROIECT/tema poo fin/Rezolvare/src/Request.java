import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Request {
    private RequestType type;
    private LocalDateTime localDateTime;//(*) se ia automat
    private String titleOrName;//(*) se ia automat
    private String description;
    private String userUsername;//(*) se ia din sistem automat
    private String resolverUsername;//(*)automat

    public Request(RequestType type, LocalDateTime localDateTime, String titleOrName, String description, String userUsername, String resolverUsername) {
        this.type = type;
        this.localDateTime = localDateTime;
        this.titleOrName = titleOrName;
        this.description = description;
        this.userUsername = userUsername;
        this.resolverUsername = resolverUsername;
    }

    public RequestType getType() {
        return type;
    }
    public void setType(RequestType type) {
        this.type = type;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }
    public String getTitleOrName() {
        return titleOrName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUserUsername() {
        return userUsername;
    }

    public String getResolverUsername() {
        return resolverUsername;
    }

    public String getFormattedCreationDate() {
        if(localDateTime == null)
            return null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return localDateTime.format(formatter);
    }

    public String getFormattedDateToJSON()
    {
        if(localDateTime == null)
            return null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        return localDateTime.format(formatter);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(type.toString()).append("\n");
        stringBuilder.append(getFormattedCreationDate()).append("\n");
        stringBuilder.append(titleOrName).append("\n");
        stringBuilder.append(description).append("\n");
        stringBuilder.append(userUsername).append("\n");
        stringBuilder.append(resolverUsername).append("\n");
        return stringBuilder.toString();
    }
}
