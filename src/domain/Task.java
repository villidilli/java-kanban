package domain;

public class Task {
    private int ID;
    private String name;
    private String description = "-";
    private Status status = Status.NEW;

    /*
        ЛЕГЕНДА:
        Первое вхождение объекта обязательно без ID
        Повторные вхождения всегда с ID
     */

    public Task(String name, String description){
        this.description = description;
        this.name = name;
    }

    public Task(int ID, String name, String description){
        this.ID = ID;
        this.name = name;
        this.description = description;
    }
    public Task(int ID, String name, String description, Status status){
        this.ID = ID;
        this.name = name;
        this.description = description;
        this.status = status;
    }


    public int getID() {
        return ID;
    }

    public void setID(int ID) { // сеттер необходим для работы метода create()
        if (this.ID == 0) { // защита от переустановки ID;
            this.ID = ID;
        } else {
            System.out.println("ID уже установлен => [" + this.ID +"]");
        }
    }
    //сеттер у имени не создается, т.к. переименование = создание нового таска
    public String getName() {
        return name;
    }


    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }



    @Override
    public String toString() {
        return "[id: " + getID()
                + "] [status: " + getStatus()
                + "] [type: Задача"
                + "] [name: " + getName()
                + "] [description: " + getDescription() + "]";
    }


}
