import java.util.HashMap;

public class Manager {
    private int countObjects;
     HashMap <Integer, Task> tasks;
     HashMap <Integer, SubTask> subTasks;
     HashMap <Integer, Epic> epics;

    public Manager(){
        countObjects = 1;
        tasks = new HashMap<>();
        subTasks = new HashMap<>();
        epics = new HashMap<>();
    }

    //создает сингл - объекты (эпики и таски)
    public Object create(Object obj){
        Object objectToReturn ="";
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
            objectToReturn = "Задача не создана, объект не передан в метод";
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
            objectToReturn = "Задача не создана, объект не передан в метод";
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
            objectToReturn = "Не указан тип задачи для выполнения";
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
            System.out.println("Не указан тип задачи для выполнения");
        }
    }

    public Object getObjectByID (int ID){
        Object objectToReturn = "";
        if (ID == 0 || ID > countObjects) {
            objectToReturn = "Объект с ID [" + ID + "] отсутствует";
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
            objectToReturn = "Объект с ID [" + ID + "] отсутствует";
        } else {
            if (tasks.containsKey(ID)) {
                objectToReturn = "Удалена задача: [название] " + tasks.get(ID).getName() + " [ID] " + tasks.get(ID).getID();
                tasks.remove(ID);
            } else if (subTasks.containsKey(ID)) {
                objectToReturn = "Удалена задача: [название] " + subTasks.get(ID).getName() + " [ID] " + subTasks.get(ID).getID();
                subTasks.remove(ID);
            } else if (epics.containsKey(ID)) {
                objectToReturn = "Удалена задача: [название] " + epics.get(ID).getName() + " [ID] " + epics.get(ID).getID();
                epics.remove(ID);
            }
        }
        return objectToReturn;
    }

    public Object update (Object obj){
        Object objectToReturn = "";
        if (obj != null) {
            if (obj.getClass() == Task.class){
                Task task = (Task) obj;
                tasks.put(task.getID(), task);
            } else if (obj.getClass() == SubTask.class){
                SubTask subTask = (SubTask) obj;
                HashMap<Integer, SubTask> epicSubTasks = subTask.getParentEpic().getSubTasks();
                boolean isAllSubTasksDone = true;
                for (SubTask epicSubTask : epicSubTasks.values()) {
                    if (epicSubTask.getStatus() != Status.DONE) {
                        isAllSubTasksDone = false;
                        break;
                    }
                }
                for (SubTask epicSubTask : epicSubTasks.values()){
                    if (epicSubTask.status == Status.IN_PROGRESS){

                    }
                }

                if (isAllSubTasksDone){
                    subTask.getParentEpic().status = Status.DONE;
                }
                subTasks.put(subTask.getID(), subTask);
            } else if (obj.getClass() == Epic.class) {
                Epic epic = (Epic) obj;
                epics.put(epic.getID(), epic);
            }
        } else {
            objectToReturn = "Обновление не завершено, объект не передан в метод";
        }
        return objectToReturn;
    }



    private void checkAndChangeStatus(Epic epic){

    }





}