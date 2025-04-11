import javafx.application.Application;
import javafx.geometry.Insets;
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

    //Creates a new VBox object to hold the task cards
    VBox taskDisplayBox = new VBox(10);

    @Override
    public void start(Stage primStage){
        //Loads the tasks from the save file
        taskList.loadData(SAVE_FILE);

        for(Task task : taskList.getTasks()){
            taskDisplayBox.getChildren().add(createTaskCardFromSaved(task));
        }

        //Creates the UI button for adding task
        Button addBtn = new Button("Add Task");

        //Creates the UI buttons for sorting 
        Button sortByPriorBtn = new Button("Sort By Priority");
        Button sortByDateBtn =  new Button("Sort By Date");
        HBox sortBtnBox = new HBox(5, sortByPriorBtn, sortByDateBtn);


        //Adds task on button press
        addBtn.setOnAction(event -> {
            addTaskCard();
        });

        //Creates a scroll plane for the tasks
        ScrollPane scrollPane = new ScrollPane(taskDisplayBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(500);

        //Creates a new VBox object for the layout
        VBox layout =  new VBox(10, sortBtnBox, addBtn, scrollPane);

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

    private void addTaskCard(){
        //Creates a new VBox for the card
        VBox card = new VBox(10);

        //Sets the margin of the card
        VBox.setMargin(card, new Insets(5, 5, 5, 5));

        //Sets the style of the card
        card.getStyleClass().add("task-card");

        //UI for the title section of the task
        TextField titleField = new TextField();
        titleField.setPromptText("Title: ");

        //UI for the description section of the task
        TextArea descArea = new TextArea();
        descArea.setPromptText("Description: ");
        descArea.setPrefRowCount(2);

        //UI for the priority section of the task
        TextField priorField = new TextField();
        priorField.setPromptText("Priority (1-3): ");


        //UI for the date section of the task
        DatePicker datePicker = new DatePicker();
        datePicker.setPromptText("Due Date: ");

        Button saveBtn = new Button("Save Task");

        saveBtn.setOnAction(event -> {
            String title = titleField.getText();
            String desc = descArea.getText();
            int priority = Integer.parseInt(priorField.getText());
            String date = datePicker.getValue().toString();

            //Adds task to list
            taskList.addTask(title, desc, priority, date);

            //clear the card
            card.getChildren().clear();

            //Locks the data in
            Label titLabel = new Label(title);
            Label descLabel = new Label(desc);
            Label priorLabel = new Label("Priority: " + priority);
            Label dateLabel = new Label("Due: " + date);

            card.getChildren().addAll(titLabel, dateLabel, priorLabel, descLabel);
        });

        card.getChildren().addAll(titleField, descArea, priorField, datePicker, saveBtn);
        taskDisplayBox.getChildren().addAll(card);
    }

    //Creates task cards from the save file
    private VBox createTaskCardFromSaved(Task task){
        //Creates a new VBox for the card
        VBox card = new VBox(10);

        //Sets the margin of the card
        VBox.setMargin(card, new Insets(5, 5, 5, 5));

        //Sets the style of the card
        card.getStyleClass().add("task-card");

        //Loads the labels of the card
        Label titLabel = new Label(task.title);
        Label descLabel = new Label(task.desc);
        Label priorLabel = new Label("Priority: " + task.priority);
        Label dateLabel = new Label("Due: " + task.dueDate);

        card.getChildren().addAll(titLabel, descLabel, priorLabel, dateLabel);
        return card;
    }

    public static void main(String[] args) {
        launch(args);
    }
}