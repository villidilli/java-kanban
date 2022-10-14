public class Main {

    public static void main(String[] args) {
        Manager manager = new Manager();
        manager.createTask(new Task());
        System.out.println(manager.tasks.toString());
    }
}
