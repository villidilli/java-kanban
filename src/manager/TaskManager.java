package manager;

import domain.*;

import java.util.Collections;
import java.util.HashMap;


public class TaskManager {
     private int countObjects = 1;
     private HashMap <Integer, Task> tasks  = new HashMap<>();
     private HashMap <Integer, SubTask> subTasks = new HashMap<>();
     private HashMap <Integer, Epic> epics = new HashMap<>();

    //создает сингл - объекты (эпики и таски)
    public void create (Task newTask) {
        if (newTask == null){
            System.out.println("[Ошибка] Объект задачи равен *null*");
        } else {
            newTask.setID(countObjects);
            countObjects++;
            System.out.println("Задача: [" + newTask.getName() + "] [ID: " + newTask.getID()+ "] создана!");
            tasks.put(newTask.getID(), newTask);
        }
    }

    public void create (SubTask newSubTask) {
        if (newSubTask == null){
            System.out.println("[Ошибка] Объект задачи равен *null*");
        } else if(epics.containsKey(newSubTask.getParentEpicID())) { // если родительский эпик существует, создаем задачу
            newSubTask.setID(countObjects);
            countObjects++;
            System.out.println("Подзадача: [" + newSubTask.getName() + "] [ID: "
                    + newSubTask.getID()+ "] [ID эпика: "
                    + newSubTask.getParentEpicID() + "] создана!");
            subTasks.put(newSubTask.getID(), newSubTask);
            epics.get(newSubTask.getParentEpicID()).getEpicSubTasks().put(newSubTask.getID(), newSubTask);
        } else {
            System.out.println("Эпик с ID [" + newSubTask.getParentEpicID() + "] не найден. Подзадача не может быть создана");
        }
    }

    public void create (Epic newEpic){
        if(newEpic == null){
            System.out.println("[Ошибка] Объект задачи равен *null*");
        } else {
            newEpic.setID(countObjects);
            countObjects++;
            epics.put(newEpic.getID(), newEpic);
            System.out.println("Эпик: [" + newEpic.getName() + "] [ID: " + newEpic.getID()+ "] создан!");
        }
    }

    public void update (Task newTask){
        if(newTask == null){
            System.out.println("[Ошибка] Объект задачи равен *null*");
        } else if (tasks.containsKey(newTask.getID())){ // проверяем есть ли в программе объект с таким id
            System.out.println("Задача: [" + tasks.get(newTask.getID()).getName() + "] [ID: " + newTask.getID() + "] обновлена!");
            tasks.put(newTask.getID(), newTask);
        } else {
            System.out.println("Задача с ID: " + newTask.getID() + " не найдена! Создайте новую задачу");
        }
    }

    public void update (SubTask newSubTask){
        if(newSubTask == null){
            System.out.println("[Ошибка] Объект задачи равен *null*");
        } else if (!subTasks.containsKey(newSubTask.getID())){ // если в программе нет сабтаска с таким ID
            System.out.println("[Ошибка] Подзадача [ID: " + newSubTask.getID() +"] не найдена!");
        } else if (!epics.containsKey(newSubTask.getParentEpicID())){ // если в программе нет эпика с таким ID
            System.out.println("[Ошибка] Эпик [ID: " + newSubTask.getParentEpicID() +"] не найден!");
        } else {
            System.out.println("Подзадача: [" + subTasks.get(newSubTask.getID()).getName() + "]" +
                    " [ID: " + newSubTask.getID() + "] обновлена!");
            subTasks.put(newSubTask.getID(), newSubTask); // перезаписали в мапе подзадач новый объект
            epics.get(newSubTask.getParentEpicID()).getEpicSubTasks().put(newSubTask.getID(), newSubTask); // перезаписали в мапе эпика его подзадачу
            reCheckEpicStatus(newSubTask.getParentEpicID()); // проверили и изменили статус эпика
        }
    }

    private void reCheckEpicStatus(int epicID) { //TODO
        HashMap <Integer, SubTask> epicSubTasks = epics.get(epicID).getEpicSubTasks();

        int freq = Collections.frequency(epicSubTasks.values()., Status.DONE);
    }


//            if (subTask.getStatus() == Status.IN_PROGRESS){ //если нашли хотя бы у одной подзадачи статус IN PROGRESS
//                epics.get(epicID).setStatus(Status.IN_PROGRESS); // меняем у эпика статус на IN PROGRESS
//                System.out.println("Статус Эпика [" + epics.get(epicID).getName() + "]" +
//                        " [ID: " + epicID + "] изменен на " +
//                        "[" + epics.get(epicID).getStatus() + "]");
//                break;
//            }


//
//    public Object getObjectsByType (Class aClass){
//        Object objectToReturn = "";
//        if (aClass != null){
//            if (aClass == Task.class){
//                objectToReturn = tasks;
//            } else if (aClass == SubTask.class){
//                objectToReturn = subTasks;
//            } else if (aClass == Epic.class) {
//                objectToReturn = epics;
//            }
//        } else {
//            objectToReturn = "=> О Ш И Б К А <= Передаваемый объект = null";
//        }
//        return objectToReturn;
//    }
//    public void deleteObjectsByType (Class aClass){
//        if (aClass != null){
//            if (aClass == Task.class){
//                tasks.clear();
//            } else if (aClass == SubTask.class){
//                subTasks.clear();
//            } else if (aClass == Epic.class) {
//                epics.clear();
//            }
//        } else {
//            System.out.println("=> О Ш И Б К А <= Передаваемый объект = null");
//        }
//    }
//    public Object getObjectByID (int ID){
//        Object objectToReturn = "";
//        if (ID == 0 || ID > countObjects) {
//            objectToReturn = "=> О Ш И Б К А <= Объект с ID [" + ID + "] не найден";
//        } else {
//            if (tasks.containsKey(ID)) {
//                objectToReturn = tasks.get(ID);
//            } else if (subTasks.containsKey(ID)) {
//                objectToReturn = subTasks.get(ID);
//            } else if (epics.containsKey(ID)) {
//                objectToReturn = epics.get(ID);
//            }
//        }
//        return  objectToReturn;
//    }
//    public Object deleteObjectByID (int ID){
//        Object objectToReturn = "";
//        if (ID == 0 || ID > countObjects) {
//            objectToReturn = "=> О Ш И Б К А <= Объект с ID [" + ID + "] не найден";
//        } else {
//            if (tasks.containsKey(ID)) {
//                objectToReturn = tasks.get(ID);
//                tasks.remove(ID);
//            } else if (subTasks.containsKey(ID)) {
//                objectToReturn = subTasks.get(ID);
//                subTasks.remove(ID);
//            } else if (epics.containsKey(ID)) {
//                objectToReturn = epics.get(ID);
//                epics.remove(ID);
//            }
//        }
//        return objectToReturn;
//    }
//
//
//    public Object update (Object obj){
//        Object objectToReturn = "";
//        if (obj != null) {
//            if (obj.getClass() == Task.class){
//                Task Task = (Task) obj;
//                tasks.put(Task.getID(), Task);
//                objectToReturn = Task;
//            } else if (obj.getClass() == SubTask.class){
//                SubTask subTask = (SubTask) obj;
//                reCheckEpicStatus(subTask.getParentEpic());
//                subTasks.put(subTask.getID(), subTask);
//                objectToReturn = subTask;
//            } else if (obj.getClass() == Epic.class) {
//                Epic epic = (Epic) obj;
//                epics.put(epic.getID(), epic);
//                objectToReturn = epic;
//            }
//        } else {
//            objectToReturn = "=> О Ш И Б К А <= Передаваемый объект = null";
//        }
//        return objectToReturn;
//    }
//    public Object getSubTasksByEpic(Epic epic){
//        Object objectToReturn = "";
//        if(epic == null){
//            objectToReturn = "=> О Ш И Б К А <= Передаваемый объект = null";
//        } else {
//            objectToReturn = epic.getSubTasks();
//        }
//        return objectToReturn;
//    }
}