package domain;

import java.util.HashMap;

public class Epic extends Task {

    private HashMap <Integer, SubTask> subTasks = new HashMap<>();

    public Epic(String name, String description) {
        super(name, description);
    }

    public HashMap<Integer, SubTask> getSubTasks() {
        return subTasks;
    }

    public void setSubTasks(int ID, SubTask subTask) {
        subTasks.put(ID, subTask);
    }

    @Override
    public String toString() {
        return " [id: " + getID()
                + "] [status: " + getStatus()
                + "] [type: tasktracker.Epic"
                + "] [name: " + getName()
                + "] [description: " + getDescription()
                + "] [amount subtasks: " + subTasks.size() +"]";
    }
}


