package main.java.lab.task2;

public class PriorityItem implements Comparable<PriorityItem> {

    private final int value;
    private final int priority;

    public PriorityItem(int value, int priority) {
        this.value = value;
        this.priority = priority;
    }

    public int getValue() {
        return value;
    }

    public int getPriority() {
        return priority;
    }

    @Override
    public int compareTo(PriorityItem other) {
        return Integer.compare(other.priority, this.priority);
    }

    @Override
    public String toString() {
        return "(" + value + ", p=" + priority + ")";
    }
}
