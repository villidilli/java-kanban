public class Main {

    public static void main(String[] args) {
        Manager manager = new Manager();
        Task task = new Task("Ремонт", "Сделать ремонт в квартире");
        Task task1 = new Task("Дача", "Съездить на дачу");
        Epic epic =new Epic("Посадка огорода", "Посадить огород на даче");
        SubTask subTask = new SubTask("Саженцы", "Нужны саженцы клубники",epic);
        manager.create(task);
        manager.create(task1);
        manager.create(epic);
        manager.create(subTask, epic);

        System.out.println(manager.getObjectsByType(SubTask.class));
        System.out.println(manager.getObjectsByType(Task.class));
        System.out.println(manager.getObjectsByType(Epic.class));
        manager.deleteObjectsByType(SubTask.class);
 //       System.out.println(manager.deleteObjectsByType(Task.class));
 //       System.out.println(manager.deleteObjectsByType(Epic.class));
 //       System.out.println(manager.getObjectsByType(SubTask.class));
        System.out.println(manager.getObjectByID(0));
        System.out.println(manager.deleteObjectByID(2));

    }
}
