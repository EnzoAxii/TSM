import java.util.LinkedList;

public class TaskSorter {
    //Sorts the tasks by due date
    public static void sortByDate(TaskList taskList){
        //Sorts the tasklist by due date
        LinkedList<Task> sorted = mergeSort(taskList.getTasks(), "dueDate");

        //Clears the task list
        taskList.getTasks().clear();

        //Fill the tasklist in the sorted order
        taskList.getTasks().addAll(sorted);
    }

    //Sorts the tasks by priority
    public static void sortByPriority(TaskList taskList){
        //Sorts the tasklist by priority 
        LinkedList<Task> sorted = mergeSort(taskList.getTasks(), "priority");

        //Clears the task list
        taskList.getTasks().clear();

        //Fill the tasklist in the sorted order
        taskList.getTasks().addAll(sorted);
    }

    //Merge sorts the task list 
    private static LinkedList<Task> mergeSort(LinkedList<Task> list, String sortType){
        //if the list is empty or has only 1 task then return list
        if(list.size() <= 1) return list;

        //calculates the middle of the list
        int mid = list.size() / 2;

        //gets the left side of the list
        LinkedList<Task> left = new LinkedList<>(list.subList(0, mid));

        //gets the left side of the list
        LinkedList<Task> right = new LinkedList<>(list.subList(mid, list.size()));

        //merges the lists 
        return merge(mergeSort(left, sortType), mergeSort(right, sortType), sortType);
    }

    //Merges the lists based off of the sort type
    private static LinkedList<Task> merge(LinkedList<Task> left, LinkedList<Task> right, String sortType){
        //creates a new list
        LinkedList<Task> result = new LinkedList<>();

        while(!left.isEmpty() && !right.isEmpty()){
            boolean takeLeft;

            //Checks if the sort type is by due date
            if(sortType.equals("dueDate")){
                //Compares the lefts due date to the rights due date
                takeLeft = left.getFirst().dueDate.compareTo(right.getFirst().dueDate) <= 0;
            }
            //Checks if the sort type is by priority
            else if(sortType.equals("priority")){
                //Compares the lefts priority to the rights priority
                takeLeft = left.getFirst().priority <= right.getFirst().priority;
            }
            else{
                //Throws an exception if the sorting type is unknown
                throw new IllegalArgumentException("Unknown sort type: " + sortType);
            }

            //Adds the removed element to the result list
            result.add(takeLeft ? left.removeFirst() : right.removeFirst());
        }

        //Adds the left and right list to the result list
        result.addAll(left);
        result.addAll(right);

        //returns the result
        return result;
    }
}
