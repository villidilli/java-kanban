import java.util.HashMap;

public class Manager {
    final private int countID; //считает количество созданных задач любого типа для ID

    HashMap <Integer,Task> tasks; // K - id , V - task
    HashMap <Integer, SubTask> subtasks; // K - id , V - subtask
    HashMap <Integer, HashMap<Integer, SubTask>> epics; // K - ID epic, K - ID subtask V - list SubTasks

    public Manager(){
        tasks = new HashMap<>();
        subtasks = new HashMap<>();
        epics = new HashMap<>();
        countID = 1;
    }

    boolean checkSubtasksForComplete(){ // метод проверяет выполнены ли все подзачачи у эпика
        return true;
    }

    void changeStatus(){ // метод изменяет статус для любого типа задачи

    }

    void getAllTasks(){ // получение списка всех задач

    }

    void createTask(Object obj){ //сохранение задачи
        Task task = (Task) obj;
        task.setId(countID);
        tasks.put(task.getId(), task);
    }


    void updateTask(Object obj){ //обновление задачи TODO: DELETE - Новая версия объекта с верным идентификатором передаётся в виде параметра

    }

    void deleteTaskByID(int id){ //удаление по ID

    }

    void getTaskByID(int id){ //получение по ID

    }

    void getTasksByEpic(Epic epic){ //получение всех подзадач эпика

    }

}
