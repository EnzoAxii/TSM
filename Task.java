class Task{
    private String title;
    private String desc;
    private int priority;
    private String dueDate;
    private Task subTask;

    //Constructor
    public Task(String title, String desc, int priority, String dueDate){
        this.title = title;
        this.desc = desc;
        this.priority = priority;
        this.dueDate = dueDate;
        subTask = null;
    }
}