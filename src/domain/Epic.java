package domain;

import java.util.HashMap;

public class Epic extends Task {

    private HashMap <Integer, SubTask> epicSubTasks = new HashMap<>();

    /*
        ЛЕГЕНДА:
        Первое вхождение объекта обязательно без ID
        Повторные вхождения всегда с ID, но всегда без указания STATUS, т.к. рассчитывается на основании
        статусов подзадач
     */
    public Epic (String name, String description) {
        super(name, description);
    }

    public Epic (int ID, String name, String description){
        super(ID, name, description);
    }


    public HashMap<Integer, SubTask> getEpicSubTasks() {
        return epicSubTasks;
    }

    public void setEpicSubTasks(HashMap<Integer, SubTask> epicSubTasks) {
        this.epicSubTasks = epicSubTasks;
    }

    @Override
    public String toString() {
        return " [id: " + getID()
                + "] [status: " + getStatus()
                + "] [type: Эпик"
                + "] [name: " + getName()
                + "] [description: " + getDescription()
                + "] [amount subtasks: " + epicSubTasks.size() +"]";
    }
}


