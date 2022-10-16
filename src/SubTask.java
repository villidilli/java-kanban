public class SubTask extends Task{

    private Epic parentEpic;

    public SubTask(String name, String description, Epic epic){
        super(name, description);
        this.parentEpic = epic;
        this.setStatus(Status.NEW);
    }

    protected Epic getParentEpic() {
        return parentEpic;
    }

    protected void setParentEpic(Epic parentEpic) {
        this.parentEpic = parentEpic;
    }

    @Override
    public String toString() {
        return "\n[Тип задачи] Подзадача,\n[ID] " + this.getID() +
                ",\n[Описание] " + this.getDescription() +
                ",\n[Статус] " + this.getStatus() +
                ",\n[Эпик] " + this.parentEpic +
                "\n-------------------";
    }
}
