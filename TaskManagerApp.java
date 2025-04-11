import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class TaskManagerApp extends Application{
    //The save file for the tasks
    private String SAVE_FILE = "task.dat";

    //Creates a new tasklist object
    TaskList taskList = new TaskList();

    @Override
    public void start(Stage primStage){
        //Loads the tasks from the save file
        taskList.loadData(SAVE_FILE);

        //UI for the title section of the task
        Label titLabel = new Label("Title: ");
        TextField titleField = new TextField();
        VBox titleBox = new VBox(titLabel, titleField);

        //UI for the description section of the task
        Label descLabel = new Label("Description: ");
        TextArea descArea = new TextArea();
        descArea.setPrefSize(200, 200);
        VBox descBox = new VBox(descLabel, descArea);

        //UI for the priority section of the task
        Label priorLabel =  new Label("Priority: ");
        TextField priorField = new TextField();
        VBox priorBox = new VBox(priorLabel, priorField);

        //UI for the date section of the task
        Label dataLabel =  new Label("Due Date: ");
        DatePicker datePicker = new DatePicker();
        datePicker.setPromptText("Due Date: ");
        VBox dateBox =  new VBox(dataLabel, datePicker);

        //Creates the UI buttons for adding and removing tasks
        Button addBtn = new Button("Add Task");
        Button removeBtn =  new Button("Remove Task");
        HBox inOutBox = new HBox(5, addBtn, removeBtn);

        //Creates the UI buttons for sorting 
        Button sortByPriorBtn = new Button("Sort By Priority");
        Button sortByDateBtn =  new Button("Sort By Date");
        HBox sortBtnBox = new HBox(5, sortByPriorBtn, sortByDateBtn);


        //Adds task on button press
        addBtn.setOnAction(event -> {
            //Adds task to task list
            taskList.addTask(
                titleField.getText(),
                descArea.getText(),
                Integer.parseInt(priorField.getText()),
                datePicker.getValue().toString()
            );
        });

        //Removes a task from the list by title
        removeBtn.setOnAction(event -> {
            taskList.removeTask(titleField.getText());
        });

        //Creates a Vbox object for the tasks
        VBox taskBoxes = new VBox(titleBox, priorBox, dateBox, descBox);

        //Creates a new VBox object for the layout
        VBox layout =  new VBox(10, sortBtnBox, taskBoxes, inOutBox);

        //Creates a new scene object using the layout
        Scene scene = new Scene(layout, 400, 500);

        //Changes the style of the scene so its not BLINDING
        scene.getStylesheets().add(getClass().getResource("Theme.css").toExternalForm());

        //Sets the stage to the scene
        primStage.setScene(scene);

        //Sets the title of the window/stage 
        primStage.setTitle("T.M.S.A");

        //Shows the stage
        primStage.show();

        //Save tasks to save file before closing
        primStage.setOnCloseRequest(event -> taskList.saveData(SAVE_FILE));
    }

    public static void main(String[] args) {
        launch(args);
    }
}