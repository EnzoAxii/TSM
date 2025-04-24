import java.time.LocalDate;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.geometry.Pos;

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
        Button addBtn = new Button("+");

        //Creates the UI button for sorting 
        Button sortBtn = new Button("\u21C5 Sort");

        //Creates a context menu for the sorting options
        ContextMenu sortMenu =  new ContextMenu();
        MenuItem dateSortItem = new MenuItem("Sort By Date");
        MenuItem prioritySortItem =  new MenuItem("Sort By Priority");
        //adds the menu items to the menu
        sortMenu.getItems().addAll(dateSortItem, prioritySortItem);

        //Opens the sorting menu on button press
        sortBtn.setOnAction(event -> {
            if(sortMenu.isShowing()){
                sortMenu.hide();
            }
            sortMenu.show(sortBtn, Side.BOTTOM, 0, 0);
        });

        //Adds task on button press
        addBtn.setOnAction(event -> {
            addTaskCard();
        });

        //Creates a HBox for the top bar of the window
        HBox topBar = new HBox(5, addBtn, sortBtn);
        topBar.setPadding(new Insets(5));

        //Creates a scroll pane for the tasks in the todo category
        ScrollPane todoPane = new ScrollPane(todoBox);
        //Fits the scroll pane to the window
        todoPane.setFitToWidth(true);
        todoPane.setMaxWidth(Double.MAX_VALUE);

        //Creates a scroll pane for the tasks in the inProgress category
        ScrollPane inProgressPane = new ScrollPane(inProgressBox);
        //Fits the scroll pane to the window
        inProgressPane.setFitToWidth(true);
        inProgressPane.setMaxWidth(Double.MAX_VALUE);

        //Creates a scroll pane for the tasks in the completed category
        ScrollPane completedPane = new ScrollPane(completedBox);
        //Fits the scroll pane to the window
        completedPane.setFitToWidth(true);
        completedPane.setMaxWidth(Double.MAX_VALUE);

        //Labels for the columns
        Label todoLabel = new Label("To Do");
        Label inProgressLabel = new Label("In Progress");
        Label completedLabel = new Label("Completed");

        //Styles the labels for the columns
        todoLabel.setStyle("-fx-font-size: 25px; -fx-font-weight: bold;");
        inProgressLabel.setStyle("-fx-font-size: 25px; -fx-font-weight: bold;");
        completedLabel.setStyle("-fx-font-size: 25px; -fx-font-weight: bold;");


        //Creates HBoxes for the column headers
        HBox todoHeader = new HBox(todoLabel);
        HBox inProgressHeader = new HBox(inProgressLabel);
        HBox completedHeader = new HBox(completedLabel);

        //Centers the column headers
        todoHeader.setAlignment(Pos.CENTER);
        inProgressHeader.setAlignment(Pos.CENTER);
        completedHeader.setAlignment(Pos.CENTER);

        //Creates VBoxes for the columns
        VBox todoColumn = new VBox(5, todoHeader, todoPane);
        VBox inProgressColumn = new VBox(5, inProgressHeader, inProgressPane);
        VBox completedColumn = new VBox(5, completedHeader, completedPane);

        //Styles the columns
        todoColumn.setStyle("-fx-border-color: #666666; -fx-border-radius: 10; -fx-background-radius: 10;");
        inProgressColumn.setStyle("-fx-border-color: #666666; -fx-border-radius: 10; -fx-background-radius: 10;");
        completedColumn.setStyle("-fx-border-color: #666666; -fx-border-radius: 10; -fx-background-radius: 10;");

        //Fits the columns to the window
        HBox.setHgrow(todoColumn, Priority.ALWAYS);
        HBox.setHgrow(inProgressColumn, Priority.ALWAYS);
        HBox.setHgrow(completedColumn, Priority.ALWAYS);

        //Creates a HBox to contain the columns
        HBox taskBoard = new HBox(10, todoColumn, inProgressColumn, completedColumn);

        //Creates a new VBox object for the layout
        VBox layout =  new VBox(10, topBar, taskBoard);

        //Creates a new scene object using the layout
        Scene scene = new Scene(layout, 1200, 600);

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
            int priority = 3;

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

            Task newTask = new Task(title, desc, priority, date, status);
            //Adds task to list
            taskList.addTask(newTask);

            //Removes the editable card
            VBox parent = (VBox) card.getParent();
            if(parent != null){
                parent.getChildren().remove(card);
            }

            //Creates a new VBox for the finalized card
            VBox lockedCard =  createTaskCardFromSaved(new Task(title, desc, priority, date, status));
            //attaches the task id to the card
            lockedCard.setUserData(newTask.id);
            //Adds the locked card to the column
            addCardToColumn(lockedCard, status);
        });

        //Adds the fields and button to the card 
        card.getChildren().addAll(titleField, descArea, priorField, datePicker, statusBox, saveBtn);
        //displays the editable card in the todo box
        todoBox.getChildren().add(card);
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
        ContextMenu optionMenu =  new ContextMenu();
        MenuItem editItem = new MenuItem("Edit");
        MenuItem deleteItem =  new MenuItem("Delete");
        //adds the menu items to the menu
        optionMenu.getItems().addAll(editItem,deleteItem);

        //Opens the context menu on button press
        optionsBtn.setOnAction(event -> {
            optionMenu.show(optionsBtn, Side.BOTTOM, 0, 0);
        });

        //handles the creation and format of the top bar of the card
        HBox topBarCard = new HBox();
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        topBarCard.getChildren().addAll(spacer, optionsBtn);

        card.getChildren().addAll(topBarCard, titLabel, dateLabel, priorLabel, statusLabel, descLabel);

        editItem.setOnAction(e -> {
            //Clear the card
            card.getChildren().clear();

            //UI for the title section of the task
            TextField titleField = new TextField(task.title);
            titleField.setPromptText("Title: ");

            //UI for the description section of the task
            TextArea descArea = new TextArea(task.desc);
            descArea.setPromptText("Description: ");
            descArea.setPrefRowCount(2);

            //UI for the priority section of the task
            TextField priorField = new TextField(Integer.toString(task.priority));
            priorField.setPromptText("Priority (1-3): ");

            //UI for the date section of the task
            DatePicker datePicker = new DatePicker();
            datePicker.setPromptText("Due Date: ");
            if(!task.dueDate.equals("No Date")){
                datePicker.setValue(LocalDate.parse(task.dueDate));
            }

            //UI for the status combo box
            ComboBox<TaskStatus> statusBox = new ComboBox<>();
            statusBox.getItems().addAll(TaskStatus.values());
            statusBox.setValue(task.status);
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
                int priority = 3;

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

            //Removes the editable card
            VBox parent = (VBox) card.getParent();
            if(parent != null){
                parent.getChildren().remove(card);
            }

                //Creates a new VBox for the finalized card
                VBox updatedCard =  createTaskCardFromSaved(new Task(title, desc, priority, date, status));
                //Adds the locked card to the column
                addCardToColumn(updatedCard, status);
            });
            //adds the children to the car
            card.getChildren().addAll(topBarCard,titleField, descArea, priorField, datePicker, statusBox,saveBtn);
        });

        //removes the card
        deleteItem.setOnAction(r ->{
            //gets the task id
            String taskId = (String) card.getUserData();

            //removes the task from the task list
            taskList.removeTask(taskId);

            //gets the parent of the card
            VBox parent = (VBox) card.getParent();

            //checks if the parent is null
            if(parent != null){
                //removes the card
                parent.getChildren().remove(card);
            }
        });

        //returns the created card
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