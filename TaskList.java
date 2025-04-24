import java.io.*;
import java.util.LinkedList;

class TaskList {
    private LinkedList<Task> taskList;

    //Constructor
    public TaskList(){
        taskList = new LinkedList<>();
    }

    //Adds a task to the list
    public void addTask(Task task){
        taskList.add(task);
    }

    //Removes a Task from the list based on title
    public void removeTask(String id){
        taskList.removeIf(task -> task.id.equals(id));
    }

    //Returns the task list
    public LinkedList<Task> getTasks(){
        return taskList;
    }

    //Saves the task list to a file so it can loaded again later
    public void saveData(String fileName){
        //Tries to write to the file 
        try(ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName))){
            out.writeObject(taskList);
        }
        //Catches any exceptions and prints out the error
        catch(IOException error){
            error.printStackTrace();
        }
    }

    //Loads the task list from the file
    public void loadData(String fileName){
        //Tries to read the data from the file and save it in tasklist
        try(ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName))){
            taskList = (LinkedList<Task>) in.readObject();
        }
        //Catches any exceptions and sets the task list to an empty fallback
        catch(IOException | ClassNotFoundException error){
            taskList = new LinkedList<>();
        }
    }
}
