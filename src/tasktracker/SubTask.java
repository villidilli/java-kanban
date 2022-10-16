package tasktracker;

public class SubTask extends Task{

    private Epic parentEpic;

    public SubTask(String name, String description, Epic epic){
        super(name, description);
        this.parentEpic = epic;
    }

    protected Epic getParentEpic() {
        return parentEpic;
    }

    protected void setParentEpic(Epic parentEpic) {
        this.parentEpic = parentEpic;
    }

    @Override
    public String toString() {
        return " [id: " + getID()
                + "] [status: " + getStatus()
                + "] [type: Subtask"
                + "] [name: " + getName()
                + "] [description: " + getDescription()
                + "] [parent epic ID: " + parentEpic.getID()
                + "] [parent epic status: " + parentEpic.getStatus() + "]";
    }
}
