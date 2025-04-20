import java.io.Serializable;

class Task implements Serializable{
    public String title;
    public String desc;
    public int priority;
    public String dueDate;
    public TaskStatus status;

    //Constructor
    public Task(String title, String desc, int priority, String dueDate, TaskStatus status){
        this.title = title;
        this.desc = desc;
        this.priority = priority;
        this.dueDate = dueDate;
        this.status = status;
    }
}