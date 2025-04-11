import java.io.Serializable;

class Task implements Serializable{
    public String title;
    public String desc;
    public int priority;
    public String dueDate;

    //Constructor
    public Task(String title, String desc, int priority, String dueDate){
        this.title = title;
        this.desc = desc;
        this.priority = priority;
        this.dueDate = dueDate;
    }
}