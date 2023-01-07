package ru.yandex.practicum.managers;

import ru.yandex.practicum.tasks.*;

import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static ru.yandex.practicum.tasks.Task.UNREACHEBLE_DATE;

public class InMemoryTaskManager implements TaskManager {

    protected final Map<Integer, Task> tasks = new HashMap<>();
    protected final Map<Integer, SubTask> subTasks = new HashMap<>();
    protected final Map<Integer, Epic> epics = new HashMap<>();
    protected final HistoryManager historyManager = Managers.getDefaultHistory();
    protected final Set<Task> prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime)
            .thenComparing(Task::getID));

    protected int generatorID = 1;

    private void checkDuplicateAndIntersections(Task task) {
        //UNREACHEBLE_DATE - эквивалентна отсутствию даты старта
        Task taskToRemove = null;
        ZonedDateTime incomingStartTime = task.getStartTime();
        ZonedDateTime incomingEndTime = task.getEndTime();
        for (Iterator<Task> it = prioritizedTasks.iterator(); it.hasNext(); ) {
            Task entry = it.next();
            ZonedDateTime existingStartTime = entry.getStartTime();
            ZonedDateTime existingEndTime = entry.getEndTime();
            //Условие нужно когда попытка добавить новый объект с тем же айди
            if (entry.getID() == task.getID()) {
                // и без даты, то проверка на пересечение не требуется, т.к. по ТЗ без даты добав.в конец
                if (incomingStartTime.isEqual(UNREACHEBLE_DATE)) {
                    it.remove();
                    return;
                } else {
                    taskToRemove = entry;
                    continue;
                }
                /*
                 * когда дата введена != UNREACHEBLE_DATE, нужно удалить старую версию, иначе бросит исключение
                 * по пересечению с самим собой
                 * НО! по новому времени может быть пересечение с другими задачами,
                 * тогда нужно перед удалением убедиться,
                 * что новый объект не пересекается с другими задачами (иначе старый будет удалён,
                 * а новый не добавлен, если пересечение есть, поэтому старый объект пока не удаляем,
                 * а сохраняем в переменную и переходим к сравнению к другой задачей
                 * если у задач в сравнении разные id и дата старта введена
                 */
            }
            if (!incomingStartTime.isEqual(UNREACHEBLE_DATE)) {
                // проверяем на пересечения
                if (!(incomingEndTime.isBefore(existingStartTime)
                        || incomingStartTime.isAfter(existingEndTime))) {
                    throw new TimeValueException(TimeValueException.INTERVAL_INTERSECTION);
                }
            }
        }
        if (taskToRemove != null) {
            prioritizedTasks.remove(taskToRemove);
        }
    }

    private void deleteAllTaskFromPrioritizedTasks(TaskTypes type) {
        prioritizedTasks.stream()
                .filter(task -> task.getType() == type)
                .collect(Collectors.toList())
                .forEach(prioritizedTasks::remove);
    }

    private void updateEpic(int ID) {
        updateEpicStatus(ID);
        updateEpicStartTimeAndDuration(ID);
    }

    public void updateEpicStartTimeAndDuration(int epicID) {
        Collection<SubTask> epicSubTasks = epics.get(epicID).getEpicSubTasks().values();
        if (!epicSubTasks.isEmpty()) {
            //блок обновления старттайм min
            ZonedDateTime firstDateTime = epicSubTasks.stream()
                    .min(Comparator.comparing(Task::getStartTime))
                    .get()
                    .getStartTime();
            epics.get(epicID).setStartTime(firstDateTime);
            //блок обновления продолжительности sum
            long sumSubTaskDurations = epicSubTasks.stream()
                    .mapToLong(Task::getDuration)
                    .sum();
            if (sumSubTaskDurations > 0) {
                epics.get(epicID).setDuration(sumSubTaskDurations);
            }
        }
    }

    private void updateEpicStatus(int epicID) {
        int countDone = 0;
        int countNew = 0;
        HashMap<Integer, SubTask> epicSubTasks = epics.get(epicID).getEpicSubTasks();

        if (epicSubTasks.size() == 0) {
            epics.get(epicID).setStatus(Status.NEW);
        } else {
            for (SubTask subTask : epicSubTasks.values()) {
                if (subTask.getStatus() == Status.NEW) {
                    countNew++;
                } else if (subTask.getStatus() == Status.DONE) {
                    countDone++;
                }
            }
            if (countNew == epicSubTasks.size()) {
                epics.get(epicID).setStatus(Status.NEW);
            } else if (countDone == epicSubTasks.size()) {
                epics.get(epicID).setStatus(Status.DONE);
            } else {
                epics.get(epicID).setStatus(Status.IN_PROGRESS);
            }
        }
    }

    @Override
    public void create(Task newTask) {
        if (newTask == null) {
            throw new ManagerNotFoundException(ManagerNotFoundException.NOT_TRANSFERRED_INPUT);
        }
        checkDuplicateAndIntersections(newTask);
        newTask.setID(generatorID);
        generatorID++;
        tasks.put(newTask.getID(), newTask);
        prioritizedTasks.add(newTask);
    }

    @Override
    public void create(SubTask newSubTask) {
        if (newSubTask == null) {
            throw new ManagerNotFoundException(ManagerNotFoundException.NOT_TRANSFERRED_INPUT);
        }
        Epic parentEpic = epics.get(newSubTask.getParentEpicID());
        if (parentEpic == null) {
            throw new ManagerNotFoundException(ManagerNotFoundException.NOT_FOUND_PARENT);
        }
//        try {
            checkDuplicateAndIntersections(newSubTask);
            newSubTask.setID(generatorID);
            subTasks.put(generatorID, newSubTask);
            parentEpic.getEpicSubTasks().put(generatorID, newSubTask);
            updateEpic(parentEpic.getID());
            prioritizedTasks.add(newSubTask);
            generatorID++;
//        } catch (TimeValueException e) {
//            throw new TimeValueException(TimeValueException.INTERVAL_INTERSECTION);
//        }
    }

    @Override
    public void create(Epic newEpic) {
        if (newEpic == null) {
            throw new ManagerNotFoundException(ManagerNotFoundException.NOT_TRANSFERRED_INPUT);
        }
        newEpic.setID(generatorID);
        generatorID++;
        epics.put(newEpic.getID(), newEpic);
    }

    @Override
    public void update(Task newTask) {
        if (newTask == null) {
            throw new ManagerNotFoundException(ManagerNotFoundException.NOT_TRANSFERRED_INPUT);
        }
        Task currentTask = tasks.get(newTask.getID());
        if (currentTask == null) {
            throw new ManagerNotFoundException(ManagerNotFoundException.NOT_FOUND_PREV_VERSION);
        }
            checkDuplicateAndIntersections(newTask);
            tasks.put(currentTask.getID(), newTask);
            prioritizedTasks.add(newTask);
    }

    @Override
    public void update(SubTask newSubTask) {
        if (newSubTask == null) {
            throw new ManagerNotFoundException(ManagerNotFoundException.NOT_TRANSFERRED_INPUT);
        }
        SubTask currentSubTask = subTasks.get(newSubTask.getID());
        if (currentSubTask == null) {
            throw new ManagerNotFoundException(ManagerNotFoundException.NOT_FOUND_PREV_VERSION);
        }
        Epic parentEpic = epics.get(currentSubTask.getParentEpicID());
        if (parentEpic == null) {
            throw new ManagerNotFoundException(ManagerNotFoundException.NOT_FOUND_PARENT);
        }

            checkDuplicateAndIntersections(newSubTask);
            parentEpic.getEpicSubTasks().put(currentSubTask.getID(), newSubTask);
            subTasks.put(currentSubTask.getID(), newSubTask);
            prioritizedTasks.add(newSubTask);
            updateEpic(parentEpic.getID());

    }

    @Override
    public void update(Epic newEpic) {
        if (newEpic == null) {
            throw new ManagerNotFoundException(ManagerNotFoundException.NOT_TRANSFERRED_INPUT);
        }
        Epic currentEpic = epics.get(newEpic.getID());
        if (currentEpic == null) {
            throw new ManagerNotFoundException(ManagerNotFoundException.NOT_FOUND_PREV_VERSION);
        }
        epics.put(currentEpic.getID(), newEpic);
        updateEpic(currentEpic.getID());
    }

    @Override
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<SubTask> getAllSubTasks() {
        return new ArrayList<>(subTasks.values());
    }

    @Override
    public List<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public void deleteAllTasks() {
        historyManager.deleteAllTasksByType(tasks);
        tasks.clear();
        deleteAllTaskFromPrioritizedTasks(TaskTypes.TASK);
    }

    @Override
    public void deleteAllSubTasks() {
        historyManager.deleteAllTasksByType(subTasks);
        subTasks.clear();
        epics.values() // удаляем все подзадачи из мапы эпиков
                .forEach(epic -> {
                    epic.getEpicSubTasks().clear();
                    updateEpic(epic.getID());
                });
        deleteAllTaskFromPrioritizedTasks(TaskTypes.SUBTASK);
    }

    @Override
    public void deleteAllEpics() {
        historyManager.deleteAllTasksByType(epics);
        historyManager.deleteAllTasksByType(subTasks);
        epics.clear();
        subTasks.clear(); // удаляем все подзадачи, т.к. они не являются самостоятельной сущностью программы
        deleteAllTaskFromPrioritizedTasks(TaskTypes.SUBTASK);
    }

    @Override
    public Task getTaskByID(int ID) {
        if (tasks.get(ID) == null) {
            throw new ManagerNotFoundException(ManagerNotFoundException.NOT_FOUND_BY_ID);
        }
        historyManager.add(tasks.get(ID));
        return tasks.get(ID);
    }

    @Override
    public SubTask getSubTaskByID(int ID) {
        if (subTasks.get(ID) == null) {
            throw new ManagerNotFoundException(ManagerNotFoundException.NOT_FOUND_BY_ID);
        }
        historyManager.add(subTasks.get(ID));
        return subTasks.get(ID);
    }

    @Override
    public Epic getEpicByID(int ID) {
        if (epics.get(ID) == null) {
            throw new ManagerNotFoundException(ManagerNotFoundException.NOT_FOUND_BY_ID);
        }
        historyManager.add(epics.get(ID));
        return epics.get(ID);
    }

    @Override
    public void deleteTaskByID(int ID) {
        Task task = tasks.get(ID);
        if (task == null) {
            throw new ManagerNotFoundException(ManagerNotFoundException.NOT_FOUND_BY_ID);
        }
        prioritizedTasks.remove(task);
        tasks.remove(ID);
        historyManager.remove(ID);
    }

    @Override
    public void deleteSubTaskByID(int ID) {
        SubTask subTask = subTasks.get(ID);
        if (subTask == null) {
            throw new ManagerNotFoundException(ManagerNotFoundException.NOT_FOUND_BY_ID);
        }

        Epic parentEpic = epics.get(subTask.getParentEpicID());
        if (parentEpic == null) {
            throw new ManagerNotFoundException(ManagerNotFoundException.NOT_FOUND_PARENT);
        }
        HashMap<Integer, SubTask> epicSubTasks = parentEpic.getEpicSubTasks();
        epicSubTasks.remove(ID); //удаляем из эпика
        prioritizedTasks.remove(subTask);
        subTasks.remove(ID); //удаляем из менеджера
        updateEpic(parentEpic.getID());
        historyManager.remove(ID);
    }

    @Override
    public void deleteEpicByID(int ID) {
        Epic epic = epics.get(ID);
        if (epic == null) {
            throw new ManagerNotFoundException(ManagerNotFoundException.NOT_FOUND_BY_ID);
        }
        //удаляем из коллекции собтасков менеджера сабтаски, имеющие отношение к эпику
        epic.getEpicSubTasks()
                .values()
                .forEach(subTask -> {
                    if (subTasks.get(subTask.getID()) != null) {
                        prioritizedTasks.remove(subTask);
                        historyManager.remove(subTask.getID());
                        subTasks.remove(subTask.getID());
                    }
                });
        //теперь удаляем сам эпик
        historyManager.remove(ID);
        epics.remove(ID);
    }

    @Override
    public List<SubTask> getAllSubTasksByEpic(int ID) {
        if (epics.get(ID) == null) {
            throw new ManagerNotFoundException(ManagerNotFoundException.NOT_FOUND_BY_ID);
        }
        return new ArrayList<>(epics.get(ID).getEpicSubTasks().values());
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }
}