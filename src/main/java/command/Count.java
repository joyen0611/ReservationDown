package command;

public class Count {
    private int count;
    public Count(int count) {
        this.count = count;
    }
    public int get() {
        return count;
    }
    public void decrease() {
        count--;
    }
}
