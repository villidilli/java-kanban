import java.util.HashMap;

public class Epic extends Task{

    private HashMap <Integer, SubTask> subTasks;

    public Epic(String name, String description) {
        super(name, description);
        subTasks = new HashMap<>();
    }

    public HashMap<Integer, SubTask> getSubTasks() {
        return subTasks;
    }

    public void setSubTasks(int ID, SubTask subTask) {
        subTasks.put(ID, subTask);
    }



}


