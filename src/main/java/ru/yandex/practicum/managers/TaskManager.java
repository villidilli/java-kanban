package ru.yandex.practicum.managers;

import ru.yandex.practicum.tasks.*;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

public interface TaskManager {

	boolean create(Task newTask);

	boolean create(SubTask newSubTask);

	boolean create(Epic newEpic);

	boolean update(Task newTask);

	boolean update(SubTask newSubTask);

	boolean update(Epic newEpic);

	List<Task> getAllTasks();

	List<SubTask> getAllSubTasks();

	List<Epic> getAllEpics();

	boolean deleteAllTasks();

	boolean deleteAllSubTasks();

	boolean deleteAllEpics();

	Task getTaskByID(int ID);

	SubTask getSubTaskByID(int ID);

	Epic getEpicByID(int ID);

	boolean deleteTaskByID(int ID);

	boolean deleteSubTaskByID(int ID);

	boolean deleteEpicByID(int ID);

	List<SubTask> getAllSubTasksByEpic(int ID);

	List<Task> getHistory();

	List<Task> getPrioritizedTasks();

	public Map<ZonedDateTime, Task> getMap ();
}