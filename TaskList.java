import java.util.LinkedList;
class TaskList {
    private LinkedList<Task> taskList;

    //Constructor
    public TaskList(){
        taskList = new LinkedList<>();
    }

    //Adds a task to the list
    public void addTask(String title, String desc, int priority, String dueDate){
        taskList.add(new Task(title, desc, priority, dueDate));
    }

    //Removes a Task from the list based on title
    public void removeTask(String title){
        taskList.removeIf(tasklist -> tasklist.title.equals(title));
    }

    //Returns the task list
    public LinkedList<Task> getTasks(){
        return taskList;
    }
}
