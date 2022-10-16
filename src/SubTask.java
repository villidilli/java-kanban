public class SubTask extends Task{

    private Epic parentEpic;

    public SubTask(String name, String description, Epic epic){
        super(name, description);
        this.parentEpic = epic;
        this.setStatus(Status.NEW);
    }

    Epic getParentEpic() {
        return parentEpic;
    }

    void setParentEpic(Epic parentEpic) {
        this.parentEpic = parentEpic;
    }

    @Override
    public String toString() {
        return "\n[Тип задачи] Подзадача,\n[ID] " + this.ID +
                ",\n[Описание] " + this.description +
                ",\n[Статус] " + this.status +
                ",\n[Эпик] " + this.parentEpic +
                "\n-------------------";
    }
}
