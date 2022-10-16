public class SubTask extends Task{

    private Epic parentEpic;

    public SubTask(String name, String description, Epic epic){
        super(name, description);
        this.parentEpic = epic;
    }

    public Epic getParentEpic() {
        return parentEpic;
    }

    public void setParentEpic(Epic parentEpic) {
        this.parentEpic = parentEpic;
    }



}
