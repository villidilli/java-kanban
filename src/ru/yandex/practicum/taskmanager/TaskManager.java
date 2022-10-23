package ru.yandex.practicum.taskmanager;

import ru.yandex.practicum.tasks.*;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private int generatorID = 1;
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, SubTask> subTasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();

    //создает сингл - объекты (эпики и таски)
    public void create(Task newTask) {
        if (newTask == null) {
            System.out.println("[Ошибка] Объект задачи равен *null*");
        } else {
            newTask.setID(generatorID);
            generatorID++;
            System.out.println("Задача: [" + newTask.getName() + "] [ID: " + newTask.getID() + "] создана!");
            tasks.put(newTask.getID(), newTask);
        }
    }

    public void create(SubTask newSubTask) {
        if (newSubTask != null) {
            Epic parentEpic = epics.get(newSubTask.getParentEpicID()); //может быть null

            if (parentEpic != null) {
                newSubTask.setID(generatorID);
                subTasks.put(generatorID, newSubTask);
                parentEpic.getEpicSubTasks().put(generatorID, newSubTask);
                reCheckEpicStatus(parentEpic.getID());
                generatorID++;
                System.out.println("Подзадача: [" + newSubTask.getName() + "] [ID: " +
                                    newSubTask.getID() + "] [ID эпика: " +
                                    newSubTask.getParentEpicID() + "] создана!");
            } else {
                System.out.println("Эпик с ID [" + newSubTask.getParentEpicID() + "] не найден. " +
                                   "Подзадача не может быть создана");
            }
        } else {
            System.out.println("[Ошибка] Объект задачи равен *null*");
        }
    }

    public void create(Epic newEpic) {
        if (newEpic == null) {
            System.out.println("[Ошибка] Объект задачи равен *null*");
        } else {
            newEpic.setID(generatorID);
            generatorID++;
            epics.put(newEpic.getID(), newEpic);
            System.out.println("Эпик: [" + newEpic.getName() + "] [ID: " + newEpic.getID() + "] создан!");
        }
    }

    public void update(Task newTask) {
        if (newTask == null) {
            System.out.println("[Ошибка] Объект задачи равен *null*");
        } else if (tasks.containsKey(newTask.getID())) { // проверяем есть ли в программе объект с таким id
            System.out.println("Задача: [" + tasks.get(newTask.getID()).getName() + "] [ID: " + newTask.getID() + "] обновлена!");
            tasks.put(newTask.getID(), newTask);
        } else {
            System.out.println("Задача с ID: " + newTask.getID() + " не найдена! Создайте новую задачу");
        }
    }

    public void update(SubTask newSubTask) {
        if (newSubTask == null) {
            System.out.println("[Ошибка] Объект задачи равен *null*");
        } else if (!subTasks.containsKey(newSubTask.getID())) { // если в программе нет сабтаска с таким ID
            System.out.println("[Ошибка] Подзадача [ID: " + newSubTask.getID() + "] не найдена!");
        } else if (!epics.containsKey(newSubTask.getParentEpicID())) { // если в программе нет эпика с таким ID
            System.out.println("[Ошибка] Эпик [ID: " + newSubTask.getParentEpicID() + "] не найден!");
        } else {
            System.out.println("Подзадача: [" + subTasks.get(newSubTask.getID()).getName() + "]" +
                    " [ID: " + newSubTask.getID() + "] обновлена!");
            epics.get(newSubTask.getParentEpicID()).getEpicSubTasks().put(newSubTask.getID(), newSubTask); // перезаписали в мапе эпика его подзадачу
            subTasks.put(newSubTask.getID(), newSubTask); // перезаписали в мапе подзадач новый объект
            reCheckEpicStatus(newSubTask.getParentEpicID()); // проверили и изменили статус эпика
        }
    }

    public void update(Epic newEpic) {
        if (newEpic == null) {
            System.out.println("[Ошибка] Объект задачи равен *null*");
        } else if (epics.containsKey(newEpic.getID())) {
            System.out.println("Задача: [" + epics.get(newEpic.getID()).getName() + "] [ID: " + newEpic.getID() + "] обновлена!");
            epics.put(newEpic.getID(), newEpic);
            reCheckEpicStatus(newEpic.getID());
        } else {
            System.out.println("Задача с ID: " + newEpic.getID() + " не найдена! Создайте новую задачу");
        }
    }

    private void reCheckEpicStatus(int epicID) {
        int countDone = 0;
        int countNew = 0;
        for (SubTask subTask : epics.get(epicID).getEpicSubTasks().values()) {
            if (subTask.getStatus() == Status.NEW) {
                countNew++;
            } else if (subTask.getStatus() == Status.DONE) {
                countDone++;
            }
        }
        if (countNew == epics.get(epicID).getEpicSubTasks().size()) {
            epics.get(epicID).setStatus(Status.NEW);
        } else if (countDone == epics.get(epicID).getEpicSubTasks().size()) {
            epics.get(epicID).setStatus(Status.DONE);
        } else {
            epics.get(epicID).setStatus(Status.IN_PROGRESS);
        }
    }

    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    public ArrayList<SubTask> getAllSubTasks() {
        return new ArrayList<>(subTasks.values());
    }

    public ArrayList<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    public void deleteAllTasks() {
        tasks.clear();
        System.out.println("Все задачи удалены");
    }

    public void deleteAllSubTasks() {
        subTasks.clear();
        for (Epic epic : epics.values()) { // удаляем все подзадачи из мапы эпиков
            epic.getEpicSubTasks().clear();
        }
        System.out.println("Все подзадачи удалены");
    }

    public void deleteAllEpics() {
        epics.clear();
        subTasks.clear(); // удаляем все подзадачи, т.к. они не являются самостоятельной сущностью программы
        System.out.println("Все эпики удалены");
    }

    public Task getTaskByID(int ID) {
        return tasks.get(ID);
    }

    public SubTask getSubTaskByID(int ID) {
        return subTasks.get(ID);
    }

    public Epic getEpicByID(int ID) {
        return epics.get(ID);
    }

    public void deleteTaskByID(int ID) {
        if (tasks.containsKey(ID)) {
            tasks.remove(ID);
            System.out.println("Задача с ID [" + ID + "] успешно удалена!");
        } else {
            System.out.println("Задача с ID [" + ID + "] не найдена!");
        }
    }

    public void deleteSubTaskByID(int ID) {
        // передается ID сабтаска, проверка есть ли объект с таким ID в мапе менеджера и в мапе эпика-родителя
        Epic parentEpic = epics.get(subTasks.get(ID).getParentEpicID());
        HashMap<Integer, SubTask> epicSubTasks = parentEpic.getEpicSubTasks();
        if (subTasks.containsKey(ID) && epicSubTasks.containsKey(ID)) {
            epicSubTasks.remove(ID); //удаляем из эпика
            subTasks.remove(ID); //удаляем из менеджера
            System.out.println("Подзадача с ID [" + ID + "] успешно удалена!");
        } else {
            System.out.println("Подзадача с ID [" + ID + "] не найдена!");
        }
    }

    public void deleteEpicByID(int ID) {
        if (epics.containsKey(ID)) {
            //удаляем и коллекции собтасков менеджера сабтаски, имеющие отношение к эпику
            for (SubTask subTask : epics.get(ID).getEpicSubTasks().values()) {
                if (subTasks.containsKey(subTask.getID())) {
                    subTasks.remove(subTask.getID());
                }
            }
            //теперь удаляем сам эпик
            epics.remove(ID);
            System.out.println("Эпик с ID [" + ID + "] успешно удален!");
        } else {
            System.out.println("Эпик с ID [" + ID + "] не найден!");
        }
    }

    public ArrayList<SubTask> getAllSubTasksByEpic(int ID) {
        return new ArrayList<>(epics.get(ID).getEpicSubTasks().values());
    }
}