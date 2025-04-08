import java.util.PriorityQueue;

public class TaskPriorityManager {
    private PriorityQueue<Task> priorityQueue;
    
    //Constructor
    public TaskPriorityManager(){
        priorityQueue = new PriorityQueue<>((t1, t2) -> Integer.compare(t1.priority, t2.priority));
    }

    //Adds a task to the priority queue
    public void addTask(Task task){
        priorityQueue.offer(task);
    }

    //Gets the next highest priority task from the queue
    public Task getNextTask(){
        return priorityQueue.poll();
    }
}
