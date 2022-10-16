import java.util.HashMap;

public class Epic extends Task {

    private HashMap <Integer, SubTask> subTasks;

    public Epic(String name, String description) {
        super(name, description);
        subTasks = new HashMap<>();
        this.setStatus(Status.NEW);
    }

    protected HashMap<Integer, SubTask> getSubTasks() {
        return subTasks;
    }

    protected void setSubTasks(int ID, SubTask subTask) {
        subTasks.put(ID, subTask);
    }

    @Override
    public String toString() {
        return "[id] " + getID()
                + "[status] " + getStatus()
                + "[type] Epic"
                + "[name] " + getName()
                + "[description] " + getDescription()
                + "[amount subtasks] " + subTasks.size();
    }
}


