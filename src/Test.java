public class Test {

    public static void main(String[] args) {
    Manager manager = new Manager();

    Task task1 = new Task("Купить продукты", "Для ужина");
    Task task2 = new Task("Покормить кота", "после 18-00");

    manager.create(task1);
    manager.create(task2);

    Epic epic1 = new Epic("Стать программистом", "К 2024 году");
    SubTask subTask1 = new SubTask("Пройти обучение на ЯП", "чем быстрее тем лучше", epic1);
    SubTask subTask2 = new SubTask("Устроиться на работу", "эх, была не была", epic1);

    manager.create(epic1);
    manager.create(subTask1, epic1);
    manager.create(subTask2, epic1);

    Epic epic2 = new Epic("Приготовить ужин", "Самый вкусный в мире");
    SubTask subTask3 = new SubTask("Купить продукты", "Рыбу", epic2);

    manager.create(epic2);
    manager.create(subTask3, epic2);

    System.out.println(manager.getObjectsByType(Task.class));
    System.out.println(manager.getObjectsByType(SubTask.class));
    System.out.println(manager.getObjectsByType(Epic.class));




    }
}
