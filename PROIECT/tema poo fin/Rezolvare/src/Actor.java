import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Actor implements ProductionAndActor {
    private String name;
    private List<FilmAppearance> filmography;
    private String biography;

    public Actor(String name, String biography) {
        this.name = name;
        this.biography = biography;
        this.filmography = new ArrayList<>();
    }

    public int compareTo(ProductionAndActor other) {
        if (other instanceof Actor) {
            Actor otherActor = (Actor) other;
            return this.name.compareTo(otherActor.name);
        }
        return this.getClass().getName().compareTo(other.getClass().getName());
    }

    public void addFilmAppearance(String filmName, String filmType) {
        filmography.add(new FilmAppearance(filmName, filmType));
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<FilmAppearance> getFilmography() {
        return filmography;
    }

    public void setFilmography(List<FilmAppearance> filmography)
    {
        this.filmography = filmography;
    }

    public void updateFilmName(String oldName, String newName) {
        for (FilmAppearance fa : filmography) {
            if (oldName.equals(fa.getName())) {
                fa.setName(newName);
                break;
            }
        }
    }

    public void removeFilmAppearanceByName(String name) {
        Iterator<FilmAppearance> iterator = filmography.iterator();
        while (iterator.hasNext()) {
            FilmAppearance fa = iterator.next();
            if (name.equals(fa.getName())) {
                iterator.remove();
                break;
            }
        }
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public static class FilmAppearance {
        private String name;
        private String type;

        public FilmAppearance(String name, String type) {
            this.name = name;
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(name).append("\n");
        stringBuilder.append(biography).append("\n");
        if (!filmography.isEmpty())
            for (FilmAppearance filmAppearance : filmography)
                stringBuilder.append(filmAppearance.name).append("---").append(filmAppearance.type).append("\n");
        else
            stringBuilder.append("performances is empty").append("\n");
        stringBuilder.append("\n\n");
        return stringBuilder.toString();
    }
}
