import java.util.HashMap;

public class Task {
    private Status status; // хранит текущий статус задачи
    private int id; // уникальный идентификатор для любого типа задач


    public Task(){
        status = Status.NEW;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Task{" +
                "status=" + status +
                ", id=" + id +
                '}';
    }
}
