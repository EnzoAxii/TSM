import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

public class TaskManagerApp extends Application{
    //The save file for the tasks
    private String SAVE_FILE = "task.dat";

    //Creates a new tasklist object
    TaskList taskList = new TaskList();

    //Creates a set of VBoxes for the 3 different columns of tasks
    VBox todoBox =  new VBox(10);
    VBox inProgressBox = new VBox(10);
    VBox completedBox = new VBox(10);

    @Override
    public void start(Stage primStage){
        //Loads the tasks from the save file
        taskList.loadData(SAVE_FILE);
        //Displays the tasks from the save file
        for(Task task : taskList.getTasks()){
            addCardToColumn(createTaskCardFromSaved(task), task.status);
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

        //Creates a scroll pane for the tasks in the todo category
        ScrollPane todoPane = new ScrollPane(todoBox);
        todoPane.setFitToWidth(true);
        todoPane.setMaxWidth(400);

        //Creates a scroll pane for the tasks in the inProgress category
        ScrollPane inProgressPane = new ScrollPane(inProgressBox);
        inProgressPane.setFitToWidth(true);
        inProgressPane.setMaxWidth(400);

        //Creates a scroll pane for the tasks in the completed category
        ScrollPane completedPane = new ScrollPane(completedBox);
        completedPane.setFitToWidth(true);
        completedPane.setMaxWidth(400);

        HBox taskBoard = new HBox(10, todoPane, inProgressPane, completedPane);

        //Creates a new VBox object for the layout
        VBox layout =  new VBox(10, sortBtnBox, addBtn, taskBoard);

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

        //UI for the status combo box
        ComboBox<TaskStatus> statusBox = new ComboBox<>();
        statusBox.getItems().addAll(TaskStatus.values());
        statusBox.setPromptText("Status");

        Button saveBtn = new Button("Save Task");

        saveBtn.setOnAction(event -> {
            //Gets the task info, fills with temp date if empty
            String title = titleField.getText().isEmpty() ? "No Title" : titleField.getText();
            String desc = descArea.getText().isEmpty() ? "No Desc" : descArea.getText();
            String priorString = priorField.getText();
            String date = datePicker.getValue() == null ? "No Date" : datePicker.getValue().toString();
            TaskStatus status = statusBox.getValue();

            //Sets a default value for priority
            int priority = 0;

            //if status is null then set a default value
            if(status == null) status = TaskStatus.ToDo;

            try{
                //Checks if the priority string is empty
                if (!priorString.isEmpty()) {
                    //Sets priority to the numbers in piorString
                    priority = Integer.parseInt(priorString);

                    //Checks if priority is outside of range
                    if(priority < 1 || priority > 3){
                        showAlert("Priority must be a number 1-3");
                        return;
                    }
                }

            }
            catch(NumberFormatException error){
                showAlert("Priority must be a number 1-3");
                return;
            }

            //Adds task to list
            taskList.addTask(title, desc, priority, date, status);

            //clear the card
            card.getChildren().clear();

            //Locks the data in
            Label titLabel = new Label(title);
            Label descLabel = new Label(desc);
            Label priorLabel = new Label("Priority: " + priority);
            Label dateLabel = new Label("Due: " + date);
            Label statusLabel = new Label("Status: " + status);

            //Creates a new button object for the options for the card
            Button optionsBtn = new Button("\u22EE");
            //Sets the style of the button
            optionsBtn.setStyle("-fx-background-color: transparent; -fx-font-size: 25px; -fx-padding: 5; -fx-border-color: transparent;");

            //Creates context menu and its items
            ContextMenu optionMenu =  new ContextMenu();
            MenuItem editItem = new MenuItem("Edit");
            MenuItem deleteItem =  new MenuItem("Delete");

            //adds the menu items to the menu
            optionMenu.getItems().addAll(editItem,deleteItem);

            //Opens the context menu on button press
            optionsBtn.setOnAction(e -> {
                optionMenu.show(optionsBtn, Side.BOTTOM, 0, 0);
            });

            //handles the creation and format of the top bar of the card
            HBox topBar = new HBox();
            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);
            topBar.getChildren().addAll(spacer, optionsBtn);

            //Adds the labels to the card
            card.getChildren().addAll(topBar, titLabel, dateLabel, priorLabel, statusLabel, descLabel);
        });

        //Adds the fields and button to the card
        card.getChildren().addAll(titleField, descArea, priorField, datePicker, statusBox, saveBtn);
        addCardToColumn(card, statusBox.getValue());
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
        Label statusLabel =  new Label("Status: " + task.status);

        //Creates a new button object for the options for the card
        Button optionsBtn = new Button("\u22EE");
        //Sets the style of the button
        optionsBtn.setStyle("-fx-background-color: transparent; -fx-font-size: 25px; -fx-padding: 5; -fx-border-color: transparent;");

        //Creates context menu and its items
        ContextMenu menu =  new ContextMenu();
        MenuItem editItem = new MenuItem("Edit");
        MenuItem deleteItem =  new MenuItem("Delete");
        //adds the menu items to the menu
        menu.getItems().addAll(editItem,deleteItem);

        //Opens the context menu on button press
        optionsBtn.setOnAction(event -> {
            menu.show(optionsBtn, Side.BOTTOM, 0, 0);
        });

        //handles the creation and format of the top bar of the card
        HBox topBar = new HBox();
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        topBar.getChildren().addAll(spacer, optionsBtn);

        card.getChildren().addAll(topBar, titLabel, dateLabel, priorLabel, statusLabel, descLabel);
        return card;
    }

    //Adds Card to its designated column based on its status
    private void addCardToColumn(VBox card, TaskStatus status){
        switch (status) {
            case ToDo -> todoBox.getChildren().add(card);
            case InProgress -> inProgressBox.getChildren().add(card);
            case Completed -> completedBox.getChildren().add(card);
        }
    }

    //Alerts user of invalid input
    private void showAlert(String message){
        //Creates a new alert object
        Alert alert = new Alert(Alert.AlertType.WARNING);

        //Sets the title of the alert
        alert.setTitle("Invalid Input");

        //sets the header text
        alert.setHeaderText(null);

        //sets the message of the alert
        alert.setContentText(message);

        //shows the alert and waits
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}