package tasktracker;

public class Task {
    private int ID;
    private String name;
    private String description;
    private Status status = Status.NEW;

    public Task(String name, String description){
        this.name = name;
        this.description = description;
    }

    protected int getID() {
        return ID;
    }

    protected void setID(int ID) {
        this.ID = ID;
    }

    protected String getName() {
        return name;
    }

    protected void setName(String name) {
        this.name = name;
    }

    protected Status getStatus() {
        return status;
    }

    protected void setStatus(Status status) {
        this.status = status;
    }

    protected String getDescription() {
        return description;
    }
    protected void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return " [id: " + getID()
                + "] [status: " + getStatus()
                + "] [type: tasktracker.Task"
                + "] [name: " + getName()
                + "] [description: " + getDescription() + "]";
    }
}
