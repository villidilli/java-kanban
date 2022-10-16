package tasktracker;

public class Test {
    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();
        Task task1 = new Task("Task1", "");
        Task task2 = new Task("Task2", "");
        taskManager.create(task1);
        taskManager.create(task2);
        Epic epic1 = new Epic("Epic1", "");
        taskManager.create(epic1);
        SubTask subTask1 = new SubTask("Subtask1", "", epic1);
        SubTask subTask2 = new SubTask("Subtask2", "", epic1);
        taskManager.create(subTask1,epic1);
        taskManager.create(subTask2,epic1);
        Epic epic2 = new Epic("Epic2", "");
        taskManager.create(epic2);
        SubTask subTask3 = new SubTask("Subtask3", "", epic2);
        taskManager.create(subTask3,epic2);

        System.out.println(taskManager.getObjectsByType(Task.class));
        System.out.println(taskManager.getObjectsByType(SubTask.class));
        System.out.println(taskManager.getObjectsByType(Epic.class));

        task1.setStatus(Status.IN_PROGRESS);
        task2.setStatus(Status.DONE);
        taskManager.update(task1);
        taskManager.update(task2);
        System.out.println(taskManager.getObjectsByType(Task.class));

        System.out.println("-----------------");
        subTask1.setStatus(Status.DONE);
        subTask2.setStatus(Status.DONE);
        taskManager.update(subTask1);
        taskManager.update(subTask2);
        taskManager.update(epic1);
        System.out.println(taskManager.getObjectsByType(SubTask.class));

        System.out.println("-----------------");
        System.out.println(taskManager.deleteObjectByID(1));

    }
}
