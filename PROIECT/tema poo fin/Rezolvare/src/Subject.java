public interface Subject {
    void addObserver(Observer o);
    void removeObserver(Observer o);
    void notifyObservers(String notification);
    void notifyObserver(String notification, Observer o);
}
