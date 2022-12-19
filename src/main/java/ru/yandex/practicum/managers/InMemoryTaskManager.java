package ru.yandex.practicum.managers;

import ru.yandex.practicum.tasks.*;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.function.Consumer;

public class InMemoryTaskManager implements TaskManager {

	protected final Map<Integer, Task> tasks = new HashMap<>();
	protected final Map<Integer, SubTask> subTasks = new HashMap<>();
	protected final Map<Integer, Epic> epics = new HashMap<>();
	protected final HistoryManager historyManager = Managers.getDefaultHistory();
	protected final Set<Task> prioritizedTasks = new TreeSet<>((o1, o2) -> {
		if (o1.getStartTime().isBefore(o2.getStartTime())) {
			return -1;
		}
		if (o1.getStartTime().isAfter(o2.getStartTime())) {
			return 1;
		}
		return 0;
	});

	protected int generatorID = 1;

	//проверка пересечений интервалов времени выполнения задач
	private void checkIntersectionOnDateTime(Task task) {
		//проверка нужна чтобы создать задачу без указания времени,
		// если поля не инициализированы пользователем (== системные константы)
		if (task.getStartTime() == Task.UNREACHEBLE_DATE || task.getDuration() == Duration.ZERO.toMinutes()) {
			return;
		}
		//если поля инициализированы не константами (введены пользователем), проверяем пересечения
		ZonedDateTime checkStart = task.getStartTime();
		ZonedDateTime checkEnd = task.getEndTime();

		boolean isHaveIntersection = prioritizedTasks.stream()
				.anyMatch(atask -> (
						checkStart.isBefore(atask.getEndTime())
						|| checkStart.isEqual(atask.getEndTime())
				) && (
						checkEnd.isAfter(atask.getStartTime())
						|| checkEnd.isEqual(atask.getStartTime())
				));
		if (isHaveIntersection) {
			throw new TimeValueException("\nОТМЕНА СОЗДАНИЯ -> [пересечение интервалов выполнения]");
		}
	}

	private void deleteAllTaskFromPrioritizedTasks(TaskTypes type) {
		prioritizedTasks.stream()
				.filter(task -> task.getTaskType() == type)
				.forEach(prioritizedTasks::remove);
	}

	private void updateEpic(int ID) {
		updateEpicStatus(ID);
		updateEpicStartTime(ID);
		updateEpicDuration(ID);
	}

	// startTime эпика = наименьшему значению startTime его сабтасков
	private void updateEpicStartTime(int epicID) {
		Collection <SubTask> epicSubTasks = epics.get(epicID).getEpicSubTasks().values();
		if (!epicSubTasks.isEmpty()) {
			ZonedDateTime firstDateTime = epicSubTasks
					.stream()
					.min((o1, o2) -> {
						if (o1.getStartTime().isBefore(o2.getStartTime())) {
							return -1;
						}
						if (o1.getStartTime().isAfter(o2.getStartTime())) {
							return 1;
						}
						return 0;
					})
					.get()
					.getStartTime();
			epics.get(epicID).setStartTime(firstDateTime);
		}
	}

	// duration эпика = наименьшему значению duration его сабтасков
	private void updateEpicDuration(int epicID) {
		HashMap<Integer, SubTask> epicSubTasks = epics.get(epicID).getEpicSubTasks();
		if (!epicSubTasks.isEmpty()) {
			long sumSubTaskDurations = epicSubTasks.values()
					.stream()
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
			System.out.println("ОТМЕНА СОЗДАНИЯ -> [объект не передан]");
			return;
		}
		try {
			checkIntersectionOnDateTime(newTask);
			newTask.setID(generatorID);
			generatorID++;
			tasks.put(newTask.getID(), newTask);
			prioritizedTasks.add(newTask);
			System.out.println("УСПЕШНО -> [" + newTask.getName() + "] [ID: " + newTask.getID() + "]");
		} catch (TimeValueException e) {
			System.out.println(e.getMessage());
		}
	}

	@Override
	public void create(SubTask newSubTask) {
		if (newSubTask == null) {
			System.out.println("ОТМЕНА СОЗДАНИЯ -> [объект не передан]");
			return;
		}

		Epic parentEpic = epics.get(newSubTask.getParentEpicID());
		if (parentEpic == null) {
			System.out.println("ОТМЕНА СОЗДАНИЯ -> [не найден эпик с ID: " + newSubTask.getParentEpicID() + "]");
			return;
		}

		try {
			checkIntersectionOnDateTime(newSubTask);
			newSubTask.setID(generatorID);
			subTasks.put(generatorID, newSubTask);
			parentEpic.getEpicSubTasks().put(generatorID, newSubTask);
			updateEpic(parentEpic.getID());
			prioritizedTasks.add(newSubTask);
			generatorID++;
			System.out.println("УСПЕШНО -> [" + newSubTask.getName() + "] [ID: " + newSubTask.getID() + "]");
		} catch (TimeValueException e) {
			System.out.println(e.getMessage());
		}
	}

	@Override
	public void create(Epic newEpic) {
		if (newEpic == null) {
			System.out.println("ОТМЕНА СОЗДАНИЯ -> [объект не передан]");
			return;
		}
		newEpic.setID(generatorID);
		generatorID++;
		epics.put(newEpic.getID(), newEpic);
		System.out.println("УСПЕШНО -> [" + newEpic.getName() + "] [ID: " + newEpic.getID() + "]");
	}

	@Override
	public void update(Task newTask) {
		if (newTask == null) {
			System.out.println("ОТМЕНА ОБНОВЛЕНИЯ -> [объект не передан]");
			return;
		}

		Task currentTask = tasks.get(newTask.getID());
		if (currentTask == null) {
			System.out.println("ОТМЕНА ОБНОВЛЕНИЯ -> [предыдущая версия задачи не найдена]");
			return;
		}

		try {
			checkIntersectionOnDateTime(newTask);
			tasks.put(currentTask.getID(), newTask);
			prioritizedTasks.add(newTask);
			System.out.println("УСПЕШНО -> [" + newTask.getName() + "] [ID: " + newTask.getID() + "]");
		} catch (TimeValueException e) {
			System.out.println(e.getMessage());
		}
	}

	@Override
	public void update(SubTask newSubTask) {
		if (newSubTask == null) {
			System.out.println("ОТМЕНА ОБНОВЛЕНИЯ -> [объект не передан]");
			return;
		}

		SubTask currentSubTask = subTasks.get(newSubTask.getID());
		if (currentSubTask == null) {
			System.out.println("ОТМЕНА ОБНОВЛЕНИЯ -> [предыдущая версия задачи не найдена]");
			return;
		}

		Epic parentEpic = epics.get(currentSubTask.getParentEpicID());
		if (parentEpic == null) {
			System.out.println("ОТМЕНА ОБНОВЛЕНИЯ -> [родительский эпик не найден]");
			return;
		}

		try {
			checkIntersectionOnDateTime(newSubTask);
			// сохраняем по ID новый объект-подзадачу
			parentEpic.getEpicSubTasks().put(currentSubTask.getID(), newSubTask);
			subTasks.put(currentSubTask.getID(), newSubTask);
			updateEpic(parentEpic.getID());
			prioritizedTasks.add(newSubTask);
			System.out.println("УСПЕШНО -> [" + currentSubTask.getName() + "] " + "[ID: " + currentSubTask.getID()+"]");
		} catch (TimeValueException e) {
			System.out.println(e.getMessage());
		}
	}

	@Override
	public void update(Epic newEpic) {
		if (newEpic == null) {
			System.out.println("ОТМЕНА ОБНОВЛЕНИЯ -> [объект не передан]");
			return;
		}

		Epic currentEpic = epics.get(newEpic.getID());
		if (currentEpic == null) {
			System.out.println("ОТМЕНА ОБНОВЛЕНИЯ -> [предыдущая версия задачи не найдена]");
			return;
		}

		epics.put(currentEpic.getID(), newEpic);
		updateEpic(currentEpic.getID());
		System.out.println("УСПЕШНО -> [" + epics.get(newEpic.getID()).getName() + " ]" +
				"[ID: " + newEpic.getID() + "] обновлена!");
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
		System.out.println("УСПЕШНО -> [задачи удалены]");
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
		System.out.println("УСПЕШНО -> [подзадачи удалены]");
	}

	@Override
	public void deleteAllEpics() {
		historyManager.deleteAllTasksByType(epics);
		historyManager.deleteAllTasksByType(subTasks);
		epics.clear();
		subTasks.clear(); // удаляем все подзадачи, т.к. они не являются самостоятельной сущностью программы
		System.out.println("Все эпики удалены");
	}

	@Override
	public Task getTaskByID(int ID) {
		historyManager.add(tasks.get(ID));
		return tasks.get(ID);
	}

	@Override
	public SubTask getSubTaskByID(int ID) {
		historyManager.add(subTasks.get(ID));
		return subTasks.get(ID);
	}

	@Override
	public Epic getEpicByID(int ID) {
		historyManager.add(epics.get(ID));
		return epics.get(ID);
	}

	@Override
	public void deleteTaskByID(int ID) {
		prioritizedTasks.remove(tasks.get(ID));
		tasks.remove(ID);
		historyManager.remove(ID);
		System.out.println("УСПЕШНО -> [задача удалена]");
	}

	@Override
	public void deleteSubTaskByID(int ID) {
		Epic parentEpic = epics.get(subTasks.get(ID).getParentEpicID());
		HashMap<Integer, SubTask> epicSubTasks = parentEpic.getEpicSubTasks();
		epicSubTasks.remove(ID); //удаляем из эпика
		prioritizedTasks.remove(subTasks.get(ID));
		subTasks.remove(ID); //удаляем из менеджера
		updateEpic(parentEpic.getID());
		historyManager.remove(ID);
		System.out.println("УСПЕШНО -> [подзадача удалена]");
	}

	@Override
	public void deleteEpicByID(int ID) {
		//удаляем из коллекции собтасков менеджера сабтаски, имеющие отношение к эпику
		epics.get(ID).getEpicSubTasks()
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
		System.out.println("УСПЕШНО -> [подзадача удален]");
	}

	@Override
	public List<SubTask> getAllSubTasksByEpic(int ID) {
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