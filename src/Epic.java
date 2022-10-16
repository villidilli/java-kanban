import java.util.HashMap;

public class Epic extends Task {

    private HashMap <Integer, SubTask> subTasks;

    public Epic(String name, String description) {
        super(name, description);
        subTasks = new HashMap<>();
        this.setStatus(Status.NEW);
    }

    HashMap<Integer, SubTask> getSubTasks() {
        return subTasks;
    }

    void setSubTasks(int ID, SubTask subTask) {
        subTasks.put(ID, subTask);
    }

    @Override
    public String toString() {
        return "\n[Тип задачи] Эпик,\n[ID] " + this.ID +
                ",\n[Описание] " + this.description +
                ",\n[Статус] " + this.status +
                ",\n[Кол-во подзадач] " + this.subTasks.size() +
                "\n******************";
    }
}


