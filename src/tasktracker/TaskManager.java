package tasktracker;

import java.util.HashMap;

public class TaskManager {
    private int countObjects;
     HashMap <Integer, Task> tasks;
     HashMap <Integer, SubTask> subTasks;
     HashMap <Integer, Epic> epics;

    public TaskManager(){
        countObjects = 1;
        tasks = new HashMap<>();
        subTasks = new HashMap<>();
        epics = new HashMap<>();
    }

    //создает сингл - объекты (эпики и таски)
    public Object create(Object obj){
        Object objectToReturn = "";
        if (obj != null) {
            if(obj.getClass() == Task.class){
                Task task = (Task) obj;
                task.setID(countObjects);
                countObjects++;
                tasks.put(task.getID(),task);
                objectToReturn = task;
            } else if (obj.getClass() == Epic.class){
                Epic epic = (Epic) obj;
                epic.setID(countObjects);
                countObjects++;
                epics.put(epic.getID(), epic);
            }
        } else {
            objectToReturn = "=> О Ш И Б К А <= Передаваемый объект = null";
        }
        return objectToReturn;
    }
    //перегрузка метода, при создании сабтаска передается второй параметр - эпик
    public Object create(SubTask subTask, Epic epic){
        Object objectToReturn;
        if(subTask != null && epic != null){
            subTask.setID(countObjects);
            countObjects++;
            subTask.setParentEpic(epic);
            epic.setSubTasks(subTask.getID(), subTask);
            subTasks.put(subTask.getID(),subTask);
            objectToReturn = subTask;
        } else {
            objectToReturn = "=> О Ш И Б К А <= Передаваемый объект = null";
        }
        return objectToReturn;
    }
    public Object getObjectsByType (Class aClass){
        Object objectToReturn = "";
        if (aClass != null){
            if (aClass == Task.class){
                objectToReturn = tasks;
            } else if (aClass == SubTask.class){
                objectToReturn = subTasks;
            } else if (aClass == Epic.class) {
                objectToReturn = epics;
            }
        } else {
            objectToReturn = "=> О Ш И Б К А <= Передаваемый объект = null";
        }
        return objectToReturn;
    }
    public void deleteObjectsByType (Class aClass){
        if (aClass != null){
            if (aClass == Task.class){
                tasks.clear();
            } else if (aClass == SubTask.class){
                subTasks.clear();
            } else if (aClass == Epic.class) {
                epics.clear();
            }
        } else {
            System.out.println("=> О Ш И Б К А <= Передаваемый объект = null");
        }
    }
    public Object getObjectByID (int ID){
        Object objectToReturn = "";
        if (ID == 0 || ID > countObjects) {
            objectToReturn = "=> О Ш И Б К А <= Объект с ID [" + ID + "] не найден";
        } else {
            if (tasks.containsKey(ID)) {
                objectToReturn = tasks.get(ID);
            } else if (subTasks.containsKey(ID)) {
                objectToReturn = subTasks.get(ID);
            } else if (epics.containsKey(ID)) {
                objectToReturn = epics.get(ID);
            }
        }
        return  objectToReturn;
    }
    public Object deleteObjectByID (int ID){
        Object objectToReturn = "";
        if (ID == 0 || ID > countObjects) {
            objectToReturn = "=> О Ш И Б К А <= Объект с ID [" + ID + "] не найден";
        } else {
            if (tasks.containsKey(ID)) {
                objectToReturn = tasks.get(ID);
                tasks.remove(ID);
            } else if (subTasks.containsKey(ID)) {
                objectToReturn = subTasks.get(ID);
                subTasks.remove(ID);
            } else if (epics.containsKey(ID)) {
                objectToReturn = epics.get(ID);
                epics.remove(ID);
            }
        }
        return objectToReturn;
    }

    private void reCheckEpicStatus(Epic epic){
        HashMap <Integer, SubTask> epicSubTasks = epic.getSubTasks();
        for (SubTask epicSubTask : epicSubTasks.values()){
            if (epicSubTask.getStatus() == Status.IN_PROGRESS){
                epic.setStatus(Status.IN_PROGRESS);
                break;
            }
        }
        boolean isAllSubTasksComplete = true;
        for (SubTask epicSubTask : epicSubTasks.values()){
            if(epicSubTask.getStatus() != Status.DONE){
                isAllSubTasksComplete = false;
            }
        }
        if(isAllSubTasksComplete){
            epic.setStatus(Status.DONE);
        }
    }
    public Object update (Object obj){
        Object objectToReturn = "";
        if (obj != null) {
            if (obj.getClass() == Task.class){
                Task Task = (Task) obj;
                tasks.put(Task.getID(), Task);
                objectToReturn = Task;
            } else if (obj.getClass() == SubTask.class){
                SubTask subTask = (SubTask) obj;
                reCheckEpicStatus(subTask.getParentEpic());
                subTasks.put(subTask.getID(), subTask);
                objectToReturn = subTask;
            } else if (obj.getClass() == Epic.class) {
                Epic epic = (Epic) obj;
                epics.put(epic.getID(), epic);
                objectToReturn = epic;
            }
        } else {
            objectToReturn = "=> О Ш И Б К А <= Передаваемый объект = null";
        }
        return objectToReturn;
    }
    public Object getSubTasksByEpic(Epic epic){
        Object objectToReturn = "";
        if(epic == null){
            objectToReturn = "=> О Ш И Б К А <= Передаваемый объект = null";
        } else {
            objectToReturn = epic.getSubTasks();
        }
        return objectToReturn;
    }
}