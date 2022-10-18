package domain;

public class SubTask extends Task{

    private int parentEpicID;

    public SubTask(String name, String description, int parentEpicID){
        super(name, description);
        this.parentEpicID = parentEpicID;
    }

    protected Epic getParentEpic() {
        return parentEpic;
    }


    @Override
    public String toString() {
        return " [id: " + getID()
                + "] [status: " + getStatus()
                + "] [type: Subtask"
                + "] [name: " + getName()
                + "] [description: " + getDescription()
                + "] [parent epic ID: " + parentEpic.getID()
                + "] [parent epic status: " + parentEpic.getStatus() + "]";
    }
}
