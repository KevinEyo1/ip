public class Task {
    protected String description;
    protected boolean isDone;

    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    public String getStatusIcon() {
        return (isDone ? "X" : " "); // mark done task with X
    }

    public String outputMarked() {
        isDone = true;
        return "Well done. I've marked this tasked as done:\n" + display();
    }

    public String outputUnmarked() {
        isDone = false;
        return "Noted. I've marked this tasked as undone:\n" + display();
    }

    public String display() {
        return "[" + getStatusIcon() + "] " + description;
    }
}
