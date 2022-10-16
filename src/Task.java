public class Task {
    protected int ID;
    protected String name;
    protected String description;
    protected Status status;

    public Task(String name, String description){
        this.name = name;
        this.description = description;
        this.status = Status.NEW;
    }

    protected int getID() {
        return ID;
    }

    protected void setID(int ID) {
        this.ID = ID;
    }

    protected String getName() {
        return name;
    }


    protected Status getStatus() {
        return status;
    }

    protected void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "\n[Тип задачи] Задача,\n[ID] " + this.ID +
                ",\n[Описание] " + this.description +
                "\n[Статус] " + this.status +
                "\n******************";
    }
}
