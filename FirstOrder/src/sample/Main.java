package sample;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.util.ArrayList;
import java.util.Objects;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import  javafx.scene.text.Text;

public class Main extends Application {

    @Override
    /**
     * Here we declare all the global variables that our program needs
     */
    public void start(Stage primaryStage) {
        final String [] entriesLogIn = {"",""};
        final Image[] imageChanging = {new Image(getClass().getResource("countDooku.jpg").toExternalForm())};
        final Image[] imageChoosen = {new Image(getClass().getResource("tieFighter.jpg").toExternalForm())};
        final int [] actualUser = {0}, selectedUser = {0};
        Scene [] scenes = new Scene[8];
        GridPane [] grid = new GridPane[8];
        Button btnLogIn = new Button();
        Button [] confirm = new Button[2];
        confirm[0] = new Button("Next");
        confirm[1] = new Button("Next");
        
        Button [] btnLogOut = new Button[5];
        Button [] back = new Button[5];
        Button [] viewPilot = new Button[2];
        Button [] viewShip = new Button[2];
        for (int i = 0; i <viewPilot.length; i++){
            viewPilot[i] = new Button();
            viewShip[i] = new Button();
        }
        for(int actualBtn = 0; actualBtn < back.length -2; actualBtn++){
            back[actualBtn] = new Button();
            btnLogOut[actualBtn] = new Button();
            back[actualBtn].setText("Back");
            btnLogOut[actualBtn].setText("Log Out");
        }
        back[3] = new Button();
        back[3].setText("Log Out"); 
        back[4] = new Button();
        back[4].setText("Back");         
        Button [] imageChoosing = new Button[4];
        for (int i = 0; i < imageChoosing.length; i++){
            imageChoosing[i] = new Button();
            imageChoosing[i].setText("Choose image");
        }

        ButtonType OK  = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
        Button [] buttonsAdmin = new Button[3];
        Button[] buttonsManager = new Button[9];
        for(int i = 0; i< buttonsManager.length; i++){
            buttonsManager[i] = new Button();
        }

        TextField [] txt = new TextField[2];
        TextField [] txtAdmin = new TextField[5];
        TextField [] unitName = new TextField[2];
        TextField [] pilotName = new TextField[2];

        Label[] labelsAdmin = new Label[3];
        Label[] labelsVehicles = new Label[4];
        Label [] labelsPilots = new Label[4];

        ChoiceBox [] status = new ChoiceBox[4];
        ComboBox [] vehiclesAvailable = new ComboBox[3];
        ComboBox [] pilotsAvailable = new ComboBox[3];
        ComboBox <String> [] usersAvailable = new ComboBox[2];
        ComboBox [] typeOfUser = new ComboBox[2];

        ImageView iv = new ImageView();
        FileChooser fileC = new FileChooser();

        ArrayList<User> users = new ArrayList<User>();
        ArrayList <Unit> pilots = new ArrayList<Unit>();
        ArrayList <Unit> vehicles = new ArrayList<Unit>();
        /**
         * Variable declaration ends, now we initialice most of the scenes that are declared below in our code
         */
        creatingDefaultUsers(users);
        creatingDefaultUnits(pilots, vehicles);
        logInScene(grid, btnLogIn, txt, scenes);
        showingScene(primaryStage, scenes, 0, "Log In");
        optionsBox(status, typeOfUser);
        usersAvailable(users, usersAvailable);
        unitsAvailable(pilots, vehicles, vehiclesAvailable, pilotsAvailable);
        adminScene(grid, buttonsAdmin, btnLogOut, scenes, labelsAdmin, usersAvailable, txtAdmin, typeOfUser);
        managerScene1(buttonsManager, grid, btnLogOut, scenes, viewPilot, viewShip);
        viewerScene(grid, viewPilot, viewShip, "Please select one option", scenes, back);
        /**
         * Scene initialization ends, now we set the log in scenes for the three exiting type of users 
         */
        btnLogIn.setOnAction((ActionEvent event) -> {
            boolean valid = comparingWithDatabase(entriesLogIn, users, actualUser, txt);
            if(valid){
                primaryStage.close();
                if(users.get(actualUser[0]).getType() == "Admin"){//going to the admin window
                    showingScene(primaryStage, scenes, 1,"Admin");
                    usersAvailable[0].setOnAction(event_2 -> {
                        gettingUserPassword(txtAdmin, usersAvailable, users, selectedUser);
                    });
                }
                if(users.get(actualUser[0]).getType() == "Manager")//going to the manager view
                    showingScene(primaryStage,scenes, 2, "Manager");
                if(users.get(actualUser[0]).getType() == "Viewer"){//going to the viewer scene
                    showingScene(primaryStage, scenes, 7, "Viewer");
                }
            }
            else
                alertMessage(OK, "Ooops, there was an error!\nPlease try again","Error Dialog");
        });
        /**
         * Here we state all the button events for all the scenes
         */
        btnLogOut[0].setOnAction(event -> {//going to the LogIn Scene
            showingScene(primaryStage, scenes, 0, "Log In");
        });
        btnLogOut[1].setOnAction(event -> {
            showingScene(primaryStage, scenes, 0, "Log In");
        });
        buttonsAdmin[0].setOnAction(event -> {
            changingUserInfo(txtAdmin, usersAvailable, users, typeOfUser, OK);
        });
        buttonsAdmin[1].setOnAction(event -> {//button of confirming the adding of user
            addingUser(txtAdmin, typeOfUser, users, usersAvailable, OK);
        });
        buttonsAdmin[2].setOnAction(event -> {//button of confirming the removing user
            remove(usersAvailable, users, actualUser, OK);
        });
        buttonsManager[0].setOnAction(event -> {//button of selection of modification
            managerScene2(buttonsManager, grid, scenes, labelsVehicles, status, back, unitName, vehiclesAvailable, pilotsAvailable, imageChoosing);
            showingScene(primaryStage, scenes, 3, "Vehicles");
        });
        buttonsManager[1].setOnAction(event -> {//button of selection of modification
            managerScene3(buttonsManager, grid, scenes, labelsPilots, status, back, pilotName, pilotsAvailable, imageChoosing);
            showingScene(primaryStage, scenes, 4, "Pilots");
        });
        buttonsManager[2].setOnAction(event -> {//button of selection of files
            changingUnitInfo(unitName, status, vehicles, vehiclesAvailable, imageChanging[0], 0);
            alertNewUser("Vehicle eddited", "Vehicle was eddited successfully", OK);
        });
        buttonsManager[3].setOnAction(event -> {//button of confirmation of adding the new p
            addingUnit(unitName, status, vehicles, vehiclesAvailable, imageChoosen[0], 1);
            alertNewUser("Vehicle added", "Vehicle was added with success", OK);
        });
        buttonsManager[4].setOnAction(event -> {//button of removing the selected vehicle
            removeUnit(vehiclesAvailable, vehicles, OK);
        });
        buttonsManager[5].setOnAction(event -> {//button of confirmation of editing user
            changingUnitInfo(pilotName, status, pilots, pilotsAvailable, imageChanging[0], 2);
            alertNewUser("Pilot eddited", "Pilot was eddited successfully", OK);
        });
        buttonsManager[6].setOnAction(event -> {//button of confirmation of adding pilot
            addingUnit(pilotName, status, pilots, pilotsAvailable, imageChoosen[0],3);
            alertNewUser("Pilot added", "Pilot was added with success", OK);
        });
        buttonsManager[7].setOnAction(event -> {//button of removing the selected pilot
            removeUnit(pilotsAvailable, pilots, OK);
        });
        buttonsManager[8].setOnAction(event -> {
            assigningPilotToVehicle(pilotsAvailable, vehiclesAvailable, vehicles, pilots, OK);
        });
        imageChoosing[0].setOnAction((ActionEvent event) -> {//button of selection of file
            try{
                File fileSelected = createSelectionWindow(fileC, primaryStage);
                imageChanging[0] = new Image(fileSelected.toURI().toString());
            }catch(Error e){}

        });
        imageChoosing[1].setOnAction(event -> {//button of selection of files
            try{
                File fileSelected = createSelectionWindow(fileC, primaryStage);
                imageChoosen[0] = new Image(fileSelected.toURI().toString());
            }catch(Error e){}
        });
        imageChoosing[2].setOnAction(event -> {//button of selection of files
            try{
                File fileSelected = createSelectionWindow(fileC, primaryStage);
                imageChanging[0] = new Image(fileSelected.toURI().toString());
            }catch(Error e){}
        });
        imageChoosing[3].setOnAction(event -> {//button of selection of files
            try{
                File fileSelected = createSelectionWindow(fileC, primaryStage);
                imageChoosen[1] = new Image(fileSelected.toURI().toString());
            }catch(Error e){}
        });
        /**
         * In these type of buttons (back) we compare the scene title with a string to 
         * be sure what scene to send depending on the type of user
         */
        back[0].setOnAction(event -> {//returning to the selection window
            if((primaryStage.getTitle().equals("Vehicles Viewer")) || (primaryStage.getTitle().equals("Pilots Viewer"))){
                showingScene(primaryStage, scenes, 7,"Manager");
            }else{
                showingScene(primaryStage, scenes, 2,"Manager");
            }        });
        back[1].setOnAction(event -> {
            if((primaryStage.getTitle().equals("Vehicles Viewer")) || (primaryStage.getTitle().equals("Pilots Viewer"))){
                showingScene(primaryStage, scenes, 7,"Manager");
            }else{
                showingScene(primaryStage, scenes, 2,"Manager");
            }
        });
        /**
         * In the scenes to view pilots or ships we send the title according to the type of user trying to access
        */
        viewPilot[0].setOnAction(event -> {
            sceneView1(scenes, grid, confirm, back, "Please choose the pilot that you want to see", pilotsAvailable);
            showingScene(primaryStage, scenes, 5,"Pilots Manager");
        });
        viewShip[0].setOnAction(event -> {
            sceneView1(scenes, grid, confirm, back, "Please choose the space ship that you want to see", vehiclesAvailable);
            showingScene(primaryStage,  scenes, 5,"Vehicles Manager");
        });
        viewPilot[1].setOnAction(event -> {
            sceneView1(scenes, grid, confirm, back, "Please choose the pilot that you want to see", pilotsAvailable);
            showingScene(primaryStage, scenes, 5,"Pilots Viewer");
        });
        viewShip[1].setOnAction(event -> {
            sceneView1(scenes, grid, confirm, back, "Please choose the space ship that you want to see", vehiclesAvailable);
            showingScene(primaryStage, scenes, 5,"Vehicles Viewer");
        });
        confirm[0].setOnAction(event -> {//if the user have selected something, so show the information that the user wants
            if(!vehiclesAvailable[1].getSelectionModel().isEmpty() || !pilotsAvailable[1].getSelectionModel().isEmpty()){
                if((primaryStage.getTitle().equals("Vehicles Viewer"))){
                    int selected = gettingSelectedUnit(vehiclesAvailable, 1);
                    sceneView2(grid, scenes, back, selected, vehicles);
                    showingScene(primaryStage, scenes, 6,"Vehicles Viewer");
                }else if((primaryStage.getTitle().equals("Pilots Viewer"))) {
                    int selected = gettingSelectedUnit(pilotsAvailable, 1);
                    sceneView2(grid, scenes, back, selected, pilots);
                    showingScene(primaryStage, scenes, 6, "Pilots Viewer");
                }else if((primaryStage.getTitle().equals("Pilots Manager"))){
                    int selected = gettingSelectedUnit(pilotsAvailable, 1);
                    sceneView2(grid, scenes, back, selected, pilots);
                    showingScene(primaryStage, scenes, 6, "Pilots Manager");
                }else if((primaryStage.getTitle().equals("Vehicles Manager"))){
                    int selected = gettingSelectedUnit(vehiclesAvailable, 1);
                    sceneView2(grid, scenes, back, selected, vehicles);
                    showingScene(primaryStage, scenes, 6, "Vehicles Manager");
                }
            }

        });
        back[3].setOnAction(event ->{
            primaryStage.close();
            showingScene(primaryStage,scenes, 0, "LogIn");
        } );
        back[4].setOnAction(event -> {
            if((primaryStage.getTitle().equals("Vehicles Viewer"))){
                sceneView1(scenes, grid, confirm, back, "Please choose the space ship that you want to see", vehiclesAvailable);
                showingScene(primaryStage, scenes, 5,"Pilots Viewer");
            }else if ((primaryStage.getTitle().equals("Pilots Viewer"))){
                sceneView1(scenes, grid, confirm, back, "Please choose the pilot that you want to see", pilotsAvailable);
                showingScene(primaryStage,  scenes, 5, "Vehicles Viewer");
            }else if((primaryStage.getTitle().equals("Pilots Manager"))){
                showingScene(primaryStage,scenes, 5, "Pilots Manager");
            }else if((primaryStage.getTitle().equals("Vehicles Manager"))){
                showingScene(primaryStage,scenes, 5, "Vehicles Manager");
            }
        });

    }

    public static void main(String[] args) {
        launch(args);
    }
    /*
    creating and setting the items that the status and typeOfUser ComboBox needs to restringed what the
    user can selected
     */
    private static void optionsBox(ChoiceBox [] status, ComboBox [] typeOfUser){
        for(int box = 0; box < typeOfUser.length; box++){
            typeOfUser[box] = new ComboBox();
            typeOfUser[box].setItems(FXCollections.observableArrayList("Manager", "Administrator", "Viewer"));
        }
        for(int boxStatus = 0; boxStatus < status.length; boxStatus++)
            status[boxStatus] = new ChoiceBox();
        for(int actualBox = 0; actualBox < status.length/2; actualBox++){
            status[actualBox].setItems(FXCollections.observableArrayList("Available", "Maintenance", "Occupied", "Flying"));
            status[actualBox+2].setItems(FXCollections.observableArrayList("Available", "Recovering", "Occupied", "Dead"));
        }

    }
    /*
    saving in a ComboBox the default users that exists
     */
    private static void usersAvailable(ArrayList<User> users, ComboBox <String> [] usersAvailable){
        for(int current = 0; current < usersAvailable.length; current++){
            usersAvailable[current] = new ComboBox<String>();
            for(int user = 0; user < users.size(); user++){//having all users that are currently available
                usersAvailable[current].getItems().add(users.get(user).getUserName());
            }
        }
    }
    /*
    saving in a ComboBox all the vehicles and pilots that are created by default
     */
    private static void unitsAvailable(ArrayList<Unit> pilots, ArrayList<Unit>vehicles, ComboBox[] vehiclesAvailable, ComboBox [] pilotsAvailable){
        for(int currentItem = 0; currentItem < vehiclesAvailable.length; currentItem++){
            pilotsAvailable[currentItem] = new ComboBox();
            vehiclesAvailable[currentItem] = new ComboBox();
            for(int unit = 0; unit < pilots.size(); unit++){
                vehiclesAvailable[currentItem].getItems().add(vehicles.get(unit).getName());
                pilotsAvailable[currentItem].getItems().add(pilots.get(unit).getName());
            }
        }
    }
    /**
     *this is for allowing user to introduce images files in to their units and pilots
     */
    private static File createSelectionWindow(FileChooser fileC, Stage primaryStage){
        fileC.setTitle("Select image you want to add to profile");
        fileC.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Images Files","*.png","*.jpg","*.gif"));
        File file = selectedFileScene(fileC, primaryStage);
        return file;
    }
    /*
    creationg of the users that will be by default
     */
    private static void creatingDefaultUsers(ArrayList<User> users){
        users.add(new User("Admin","admin","Admin"));
    }

    private void creatingDefaultUnits(ArrayList<Unit> pilots, ArrayList<Unit> vehicles){
        vehicles.add(new Vehicle("Boeing 787",new Image(getClass().getResource("tieFighter.jpg").toExternalForm()), "Occupied","None"));
        pilots.add(new Pilot("Count Dooku", new Image(getClass().getResource("countDooku.jpg").toExternalForm()), "Dead"));
    }
    /*
    displaying the selected scene
     */
    private static void showingScene(Stage primaryStage,Scene [] scenes, int index, String title){//showing all scenes
        primaryStage.setTitle(title);
        primaryStage.setScene(scenes[index]);
        primaryStage.show();
    }
    /*
    giving the padding and "air" to the pane
     */
    private static void styleScene(GridPane [] grid, int index){
        grid[index].setPadding(new Insets(10));
        grid[index].setVgap(10);
        grid[index].setHgap(10);
    }
    /**
     * 
     * @param grid
     * @param btnLogIn
     * @param txt
     * @param scenes 
     *how is going to be displayed the scene of LogIn
     */
    private  void logInScene(GridPane [] grid, Button btnLogIn, TextField[] txt, Scene [] scenes){
        grid[0] = new GridPane();
        txt[0] = new TextField();
        txt[1] = new PasswordField();
        BackgroundImage back = new BackgroundImage(new Image(getClass().getResource("Firstorder.jpg").toExternalForm()), BackgroundRepeat.NO_REPEAT,BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        Background background = new Background(back);
        btnLogIn.setText("Log In");
        txt[0].setPromptText("Type here your username");
        txt[1].setPromptText("Type here your password");
        styleScene(grid, 0);
        grid[0].setBackground(background);
        grid[0].add(txt[0], 0, 0);
        grid[0].add(txt[1], 0, 1);
        grid[0].add(btnLogIn, 0, 2);
        scenes[0] = new Scene(grid[0], 200, 200);
    }
    /**
     * obtain what are in the textfields
     * @param txt
     * @param entries 
     */
    private static void gettingLogInfo(TextField[] txt, final String[] entries){
        for(int i = 0; i < txt.length; i++) {
            entries[i] = txt[i].getText();
        }
    }
    /**
     * found if the data entered are in our ArrayList of Users
     * @param entriesLogIn
     * @param users
     * @param actualUser
     * @param txt
     * @return 
     */
    private static boolean comparingWithDatabase(final String [] entriesLogIn, ArrayList<User> users, final int [] actualUser, TextField [] txt) {
        gettingLogInfo(txt, entriesLogIn);
        boolean validData = false;
        for (int account = 0; account < users.size(); account++) {
            if (Objects.equals(entriesLogIn[0], users.get(account).getUserName())) {
                if (Objects.equals(entriesLogIn[1], users.get(account).getUserPassword())) {
                    validData = true;
                    actualUser[0] = account;
                }
            }
        }
        return validData;
    }
    /**
     * Message to inform the user that he/she what have he/she done already
     * @param title
     * @param message
     * @param OK 
     */
    private static void alertNewUser(String title, String message, ButtonType OK) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message,OK);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
    /**
     * Message when something is done wrong
     * @param OK
     * @param message
     * @param title 
     */
    private static void alertMessage(ButtonType OK, String message, String title){
        Alert alert = new Alert(Alert.AlertType.ERROR, message, OK);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
    /**
     * set all textfields to null
     * @param txtAdmin 
     */
    private static void clearAdmin(TextField[] txtAdmin) {
        txtAdmin[0].setText("");
        txtAdmin[1].setText("");
        txtAdmin[2].setText("");
        txtAdmin[3].setText("");
        txtAdmin[4].setText("");
    }
    /**
     * this opens a dialog to allow user to select a image
     * @param fileC
     * @param primaryStage
     * @return 
     */
    private static File selectedFileScene(FileChooser fileC, Stage primaryStage){
        return fileC.showOpenDialog(primaryStage);//
    }
    /**
     * This scene displays all that the operations the admin is allowed to do
     * @param grid
     * @param buttonsAdmin
     * @param btnLogOut
     * @param scenes
     * @param labelsAdmin
     * @param usersAvailable
     * @param txtAdmin
     * @param typeOfUser 
     */
    private  void adminScene(GridPane [] grid, Button [] buttonsAdmin, Button [] btnLogOut, Scene [] scenes, Label [] labelsAdmin, ComboBox [] usersAvailable, TextField [] txtAdmin, ComboBox [] typeOfUser){
        BackgroundImage back = new BackgroundImage(new Image(getClass().getResource("stars.jpg").toExternalForm()), BackgroundRepeat.NO_REPEAT,BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        Background background = new Background(back);        
        txtAdmin[0] = new TextField();
        txtAdmin[1] = new PasswordField();
        txtAdmin[2] = new PasswordField();
        txtAdmin[3] = new TextField();
        txtAdmin[4] = new PasswordField();
        grid[1] = new GridPane();
        grid[1].setBackground(background);
        styleScene(grid, 1);
        for(int actualBtn = 0; actualBtn < 3; actualBtn++){
            buttonsAdmin[actualBtn] = new Button();
            buttonsAdmin[actualBtn].setText("Confirm");
            labelsAdmin[actualBtn] = new Label();
        }
        labelsAdmin[0].setText("Edit user");     
        labelsAdmin[1].setText("Add user");
        labelsAdmin[2].setText("Remove user");
        labelsAdmin[0].setTextFill(Color.web("#FFFFFF"));
        labelsAdmin[1].setTextFill(Color.web("#FFFFFF"));
        labelsAdmin[2].setTextFill(Color.web("#FFFFFF"));  
        labelsAdmin[0].setFont(new Font("Serif Gothic ", 20));
        labelsAdmin[1].setFont(new Font("Serif Gothic ", 20));
        labelsAdmin[2].setFont(new Font("Serif Gothic ", 20));        
        txtAdmin[0].setPromptText("New username");
        txtAdmin[1].setPromptText("Current password");
        txtAdmin[2].setPromptText("New password");
        txtAdmin[3].setPromptText("New username");
        txtAdmin[4].setPromptText("New password");
        grid[1].add(labelsAdmin[0], 0,0);
        grid[1].add(usersAvailable[0], 0, 1);
        grid[1].add(txtAdmin[0],0,2);
        grid[1].add(txtAdmin[1], 0, 3);
        grid[1].add(txtAdmin[2], 0, 4);
        grid[1].add(typeOfUser[0], 1, 3);
        grid[1].add(buttonsAdmin[0], 1, 4);
        grid[1].add(labelsAdmin[1], 0, 6);
        grid[1].add(txtAdmin[3], 0, 7);
        grid[1].add(txtAdmin[4], 0, 8);
        grid[1].add(typeOfUser[1], 1, 7);
        grid[1].add(buttonsAdmin[1], 1, 8);
        grid[1].add(labelsAdmin[2], 0, 10);
        grid[1].add(usersAvailable[1], 0, 11);
        grid[1].add(buttonsAdmin[2], 1, 11);
        grid[1].add(btnLogOut[0], 0, 13);
        scenes[1] = new Scene(grid[1], -1, -1);
    }
    /**
     * When the user selected a user, the password fields contains the password of the selected user
     * @param txtAdmin
     * @param usersAvailable
     * @param users
     * @param selectedUser 
     */
    private static void gettingUserPassword(TextField [] txtAdmin, ComboBox [] usersAvailable, ArrayList <User> users, final int [] selectedUser){
        if(!usersAvailable[0].getSelectionModel().isEmpty())//checking if there's an user selected
            selectedUser[0] = usersAvailable[0].getSelectionModel().getSelectedIndex();
        for(int index = 1; index < txtAdmin.length - 2; index++)
            txtAdmin[index].setText(users.get(selectedUser[0]).getUserPassword());
    }
    /**
     * This function checks if the user wants to erase it own account
     * @param user
     * @param actualUser
     * @return 
     */
    private static boolean checkingIfIsTheCurrentUser(int user, final int [] actualUser){
        if(actualUser[0] == user) return false;
        else return true;
    }
    /**
     * This function changes the information of the user if there's something different to the actual information
     * @param txtAdmin
     * @param usersAvailable
     * @param users
     * @param typeOfUser
     * @param OK 
     */
    private static void changingUserInfo(TextField [] txtAdmin, ComboBox [] usersAvailable, ArrayList <User> users, ComboBox [] typeOfUser, ButtonType OK){
        if(!usersAvailable[0].getSelectionModel().isEmpty()) {
            int option = usersAvailable[0].getSelectionModel().getSelectedIndex();
            if (txtAdmin[0].getText() != usersAvailable[0].getValue().toString() && txtAdmin[0].getText() != null) {
                users.get(option).setUserName(txtAdmin[0].getText());
                for(int item = 0; item < usersAvailable.length; item++)
                    usersAvailable[item].getItems().set(option, users.get(option).getUserName());
            }
            if (txtAdmin[1].getText() != txtAdmin[2].getText())
                users.get(option).setUserPassword(txtAdmin[2].getText());

            if (!typeOfUser[0].getSelectionModel().isEmpty() && typeOfUser[0].getValue().toString() != usersAvailable[0].getValue())
                users.get(option).setType(usersAvailable[0].getValue().toString());

            clearAdmin(txtAdmin);
            alertNewUser("User edited", "The user was eddited with succes", OK);
        }
    }
    /**
     * This function changes the information of the unit if there's something different to the actual information
     * @param name
     * @param status
     * @param units
     * @param unitsAvailable
     * @param image
     * @param index 
     */
    private static void changingUnitInfo(TextField [] name, ChoiceBox [] status, ArrayList<Unit>units, ComboBox [] unitsAvailable, Image image, int index) {
        if (!unitsAvailable[0].getSelectionModel().isEmpty()) {
            int selectedUnit = unitsAvailable[0].getSelectionModel().getSelectedIndex();

            if (!name[0].getText().isEmpty() && name[0].getText() != units.get(selectedUnit).getName())
                units.get(selectedUnit).setName(name[0].getText());
            for(int item = 0; item < unitsAvailable.length; item++)
                unitsAvailable[item].getItems().set(selectedUnit, units.get(selectedUnit).getName());

            if (!status[index].getSelectionModel().isEmpty() && status[index].getValue().toString() != units.get(selectedUnit).getStatus())
                units.get(selectedUnit).setStatus(status[index].getValue().toString());

            if(image!= units.get(selectedUnit).getImage())
                units.get(selectedUnit).setImage(image);
        }
    }
    /**
     * This function allows the admin to add a new user and password of the type of his choice
     * @param txtAdmin
     * @param typeOfUser
     * @param users
     * @param usersAvailable
     * @param OK 
     */
    private static void addingUser(TextField [] txtAdmin, ComboBox [] typeOfUser, ArrayList<User> users, ComboBox [] usersAvailable, ButtonType OK){
        if(txtAdmin[3].getText() != null && txtAdmin[4].getText() != null && !typeOfUser[1].getSelectionModel().isEmpty()){
            users.add(new User(txtAdmin[3].getText(),txtAdmin[4].getText(), typeOfUser[1].getValue().toString()));
            clearAdmin(txtAdmin);
            alertNewUser("User added", "User added with success", OK);
            for(int item = 0; item < usersAvailable.length; item++)
                usersAvailable[item].getItems().add(users.get(users.size()-1).getUserName());
        }
        else
            alertMessage(OK, "Please selected everything and then press in Confirm","ERROR: Missing");
    }
    /**
     * This function allows the manager to add a new unit with its name, image and status
     * @param name
     * @param status
     * @param units
     * @param unitsAvailable
     * @param image
     * @param index 
     */
    private static void addingUnit(TextField [] name, ChoiceBox [] status, ArrayList<Unit>units, ComboBox [] unitsAvailable, Image image, int index){
        if(unitsAvailable[0].getSelectionModel().isEmpty() && name[1].getText()!= null && !status[index].getSelectionModel().isEmpty()){
            if(units.get(0) instanceof Vehicle){
                units.add(new Vehicle(name[1].getText(), image, status[index].getValue().toString(),"none"));
            }
            else{
                units.add(new Pilot(name[1].getText(), image, status[index].getValue().toString()));
            }
            for(int item = 0; item <unitsAvailable.length; item++)
                unitsAvailable[item].getItems().add(units.get(units.size()-1).getName());
        }
    }
    /**
     * This function is to remove a user (only Admin type users can access it)
     * @param usersAvailable
     * @param users
     * @param actualUser
     * @param OK 
     */
    private static void remove(ComboBox [] usersAvailable, ArrayList<User> users, final int [] actualUser, ButtonType OK){
        if(!usersAvailable[1].getSelectionModel().isEmpty()){
            int userToRemove = usersAvailable[1].getSelectionModel().getSelectedIndex();
            boolean noCurrentAdmin = checkingIfIsTheCurrentUser(userToRemove, actualUser);
            if(noCurrentAdmin){
                users.remove(userToRemove);
                alertNewUser("User removed", "User was removed correctly", OK);
                for(int people = 0; people < 2; people++){
                    usersAvailable[people].getItems().remove(userToRemove);
                    usersAvailable[people].getSelectionModel().clearAndSelect(0);
                }
            }
            else alertMessage(OK, "You cannot remove the current user", "Same admin");
        }
    }
    /**
     * This function allows a manager to remove a pilot/vehicle
     * @param unitsAvailable
     * @param units
     * @param OK 
     */
    private static void removeUnit (ComboBox [] unitsAvailable, ArrayList<Unit> units, ButtonType OK){
        if(!unitsAvailable[1].getSelectionModel().isEmpty()){
            int unitToRemove = unitsAvailable[1].getSelectionModel().getSelectedIndex();
            units.remove(unitToRemove);
            for(int unit = 0; unit < unitsAvailable.length; unit++){
                unitsAvailable[unit].getSelectionModel().clearSelection();
                unitsAvailable[unit].getItems().remove(unitToRemove);
            }
        }
    }
    /**
     * This function is to asign a pilot to a specific vehicle (only a manager can do this)
     * @param pilotsAvailable
     * @param vehiclesAvailable
     * @param vehicles
     * @param pilots
     * @param OK 
     */
    private static void assigningPilotToVehicle(ComboBox [] pilotsAvailable, ComboBox [] vehiclesAvailable, ArrayList<Unit> vehicles, ArrayList<Unit>pilots, ButtonType OK){
        if(!pilotsAvailable[0].getSelectionModel().isEmpty() && !vehiclesAvailable[0].getSelectionModel().isEmpty()) {
            int index = vehiclesAvailable[0].getSelectionModel().getSelectedIndex();
            if(pilots.get(index).getStatus()=="Available")
            ((Vehicle) vehicles.get(index)).setAssignToPilot(pilotsAvailable[0].getValue().toString());
            if(pilots.get(index).getStatus()=="Dead")
                alertMessage(OK, "You cannot assigned this vehicle to an dead pilot","Dead pilot");
            else
                alertMessage(OK, "You cannot assigned this vehicle to the selected pilot","No available pilot");
        }
    }
    /**
     * This is the first manager scene here we add a menu of the acctions that a manager can do
     * @param buttonsManager
     * @param grid
     * @param btnLogOut
     * @param scenes
     * @param viewPilot
     * @param viewShip 
     */
    private  void managerScene1(Button[] buttonsManager, GridPane[] grid, Button [] btnLogOut, Scene[] scenes, Button [] viewPilot, Button [] viewShip) {
        BackgroundImage back = new BackgroundImage(new Image(getClass().getResource("stars.jpg").toExternalForm()), BackgroundRepeat.NO_REPEAT,BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        Background background = new Background(back);  
        grid[2] = new GridPane();
        grid[2].setBackground(background);
        styleScene(grid, 2);
        buttonsManager[0].setText("Vehicles");
        buttonsManager[1].setText("Pilots");
        viewPilot[0].setText("View Pilots");
        viewShip[0].setText("View Vehicles");
        grid[2].add(buttonsManager[0], 0, 0);
        grid[2].add(buttonsManager[1], 1, 0);
        grid[2].add(viewShip[0], 0, 1);
        grid[2].add(viewPilot[0], 1, 1);
        grid[2].add(btnLogOut[1], 2, 2);
        scenes[2] = new Scene(grid[2], -1, -1);
    }
    /**
     * This is the second manager function and it displays when the manager want to add/edit/remove a ship
     * @param buttonsManager
     * @param grid
     * @param scenes
     * @param labelShips
     * @param status
     * @param back
     * @param unitName
     * @param vehiclesAvailable
     * @param pilotsAvailable
     * @param imageChoosing 
     */
    private void managerScene2(Button[] buttonsManager, GridPane[] grid, Scene[] scenes, Label[] labelShips, ChoiceBox[] status, Button [] back, TextField [] unitName, ComboBox [] vehiclesAvailable, ComboBox [] pilotsAvailable, Button [] imageChoosing) {
        BackgroundImage back2 = new BackgroundImage(new Image(getClass().getResource("stars.jpg").toExternalForm()), BackgroundRepeat.NO_REPEAT,BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        Background background = new Background(back2);          
        grid[3] = new GridPane();
        grid[3].setBackground(background);
        styleScene(grid, 3);
        for (int actualBtn = 0; actualBtn < 3; actualBtn++) {
            if(actualBtn < 2) {
                unitName[actualBtn] = new TextField();
            }
            else
                labelShips[3] = new Label();
            labelShips[actualBtn] = new Label();
        }
        for(int i = 0; i < 3; i++){
            buttonsManager[i + 2].setText("Confirm");
        }
        buttonsManager[8].setText("Confirm");
        Label asignPilot = new Label("Assign pilot");
        labelShips[0].setText("Edit Ship");
        labelShips[1].setText("Add Ship");
        labelShips[2].setText("Remove Ship");
        labelShips[3].setText("Amount of vehicles");
        labelShips[0].setTextFill(Color.web("#FFFFFF"));
        labelShips[1].setTextFill(Color.web("#FFFFFF"));
        labelShips[2].setTextFill(Color.web("#FFFFFF"));  
        labelShips[3].setTextFill(Color.web("#FFFFFF"));  
        labelShips[0].setFont(new Font("Serif Gothic ", 20));
        labelShips[1].setFont(new Font("Serif Gothic ", 20));
        labelShips[2].setFont(new Font("Serif Gothic ", 20));        
        labelShips[3].setFont(new Font("Serif Gothic ", 20));  
        asignPilot.setTextFill(Color.web("#FFFFFF"));  
        asignPilot.setFont(new Font("Serif Gothic ", 20));        
        unitName[0].setPromptText("New name");
        grid[3].add(labelShips[0], 0, 0);
        grid[3].add(vehiclesAvailable[0], 0, 1);
        grid[3].add(unitName[0], 0, 2);
        grid[3].add(imageChoosing[0],1,2);
        grid[3].add(status[0], 0,3);
        grid[3].add(buttonsManager[2],1,3);
        grid[3].add(asignPilot, 0, 5);
        grid[3].add(pilotsAvailable[0], 0, 7);
        grid[3].add(buttonsManager[8], 1, 7);
        grid[3].add(labelShips[1], 0, 9);
        grid[3].add(unitName[1], 0, 10);
        grid[3].add(imageChoosing[1],1,10);
        grid[3].add(status[1], 0, 11);
        grid[3].add(buttonsManager[3], 1, 11);
        grid[3].add(labelShips[2], 0, 13);
        grid[3].add(vehiclesAvailable[1], 0,15);
        grid[3].add(buttonsManager[4], 1, 15);
        grid[3].add(back[0], 0, 17);
        scenes[3] = new Scene(grid[3], -1, -1);
    }
    /**
     * This is the third manager scene, it is for when the manager wants to add/edit/remove a pilot
     * @param buttonsManager
     * @param grid
     * @param scenes
     * @param labelPilots
     * @param status
     * @param back
     * @param pilotName
     * @param pilotsAvailable
     * @param imageChoosing 
     */
    private  void managerScene3(Button[] buttonsManager, GridPane[] grid, Scene[] scenes, Label[] labelPilots, ChoiceBox[] status, Button [] back, TextField [] pilotName, ComboBox [] pilotsAvailable, Button [] imageChoosing) {
        BackgroundImage back2 = new BackgroundImage(new Image(getClass().getResource("stars.jpg").toExternalForm()), BackgroundRepeat.NO_REPEAT,BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        Background background = new Background(back2);           
        grid[4] = new GridPane();
        grid[4].setBackground(background);
        styleScene(grid, 4);
        for(int actualTxtPilot = 0; actualTxtPilot < pilotName.length; actualTxtPilot++){
            labelPilots[actualTxtPilot] = new Label();
            pilotName[actualTxtPilot] = new TextField();
            pilotName[actualTxtPilot].setPromptText("New name");
            if(actualTxtPilot ==1){
                labelPilots[actualTxtPilot+1] = new Label();
            }
        }
        for(int i = 0; i < 3; i++){
            buttonsManager[i + 5].setText("Confirm");
        }
        labelPilots[0].setText("Edit Pilot");
        labelPilots[1].setText("Add Pilot");
        labelPilots[2].setText("Remove Pilot");
        labelPilots[0].setTextFill(Color.web("#FFFFFF"));
        labelPilots[1].setTextFill(Color.web("#FFFFFF"));
        labelPilots[2].setTextFill(Color.web("#FFFFFF"));
        labelPilots[0].setFont(new Font("Serif Gothic ", 20));
        labelPilots[1].setFont(new Font("Serif Gothic ", 20));
        labelPilots[2].setFont(new Font("Serif Gothic ", 20));
        grid[4].add(labelPilots[0], 0, 0);
        grid[4].add(pilotsAvailable[0], 0,1);
        grid[4].add(pilotName[0], 0, 2);
        grid[4].add(imageChoosing[2],1, 2);
        grid[4].add(status[2], 0, 3);
        grid[4].add(buttonsManager[5], 1, 3);
        grid[4].add(labelPilots[1], 0, 5);
        grid[4].add(pilotName[1], 0, 6);
        grid[4].add(imageChoosing[3],1, 6);
        grid[4].add(status[3], 0, 7);
        grid[4].add(buttonsManager[6], 1, 7);
        grid[4].add(labelPilots[2], 0, 9);
        grid[4].add(pilotsAvailable[1], 0,10);
        grid[4].add(buttonsManager[7], 1, 10);
        grid[4].add(back[1], 0, 12);
        scenes[4] = new Scene(grid[4], -1, -1);
    }
    /**
     * This scene is for both manager and viewer user types, here the user can choose which pilot/ship does he want to see
     * @param scenes
     * @param grid
     * @param confirm
     * @param back
     * @param instructions
     * @param selection 
     */
    public  void sceneView1(Scene[] scenes, GridPane[] grid, Button[] confirm, Button [] back, String instructions, ComboBox [] selection){
        BackgroundImage back2 = new BackgroundImage(new Image(getClass().getResource("vader.jpg").toExternalForm()), BackgroundRepeat.NO_REPEAT,BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        Background background = new Background(back2);          
        grid[5] = new GridPane();
        grid[5].setBackground(background);
        styleScene(grid, 5);
        Label select = new Label(instructions);
        select.setTextFill(Color.web("#FFFFFF"));
        select.setFont(new Font("Serif Gothic ", 15));
        grid[5].add(select, 0, 0);
        grid[5].add(selection[1], 0, 1);
        grid[5].add(back[1], 0, 2);
        grid[5].add(confirm[0], 1, 2);
        scenes[5] = new Scene(grid[5], -1, -1);
    }
    /**
     * This is a function that returns an int of the selection of pilot or ship that the user wants to see, later on it will allow us to show pilot/vehicle name
     * @param unitsAvailable
     * @param index
     * @return 
     */
    public static int gettingSelectedUnit(ComboBox [] unitsAvailable, int index){
        return unitsAvailable[index].getSelectionModel().getSelectedIndex();
    }
    /**
     * This scene is where the image, name and status of the selected pilot/vehicle appear, it is for both manager and viewer
     * @param grid
     * @param scenes
     * @param back
     * @param index
     * @param units 
     */
    public  void sceneView2(GridPane[] grid, Scene[] scenes, Button[] back, int index, ArrayList<Unit>units){
        BackgroundImage back2 = new BackgroundImage(new Image(getClass().getResource("black.jpg").toExternalForm()), BackgroundRepeat.NO_REPEAT,BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        Background background = new Background(back2);          
        grid[6] = new GridPane();
        grid[6].setBackground(background);
        styleScene(grid, 6);
        Label name = new Label(units.get(index).getName());
        Label role = new Label(units.get(index).getStatus());
        if(units.get(index) instanceof Vehicle){
            Label assign = new Label(((Vehicle) units.get(index)).getAsignToPilot());
            assign.setTextFill(Color.web("#FFFFFF"));
            assign.setFont(new Font("Serif Gothic ", 12));
            grid[6].add(assign, 1, 2);
        }

        name.setTextFill(Color.web("#FFFFFF"));
        name.setFont(new Font("Serif Gothic ", 15));
        role.setTextFill(Color.web("#FFFFFF"));
        role.setFont(new Font("Serif Gothic ", 12));

        ImageView imageView = new ImageView(units.get(index).getImage());
        grid[6].add(imageView, 0 ,0, 1, 3);
        grid[6].add(name, 1, 0);
        grid[6].add(role, 1,1);

        grid[6].add(back[4], 0, 4);
        scenes[6] = new Scene(grid[6], -1, -1);
    }
    /**
     * This scene is the menu of actions a viewer type user can choose
     * @param grid
     * @param viewPilot
     * @param viewShip
     * @param instructions
     * @param scenes
     * @param back 
     */
    public  void viewerScene(GridPane[] grid, Button[] viewPilot, Button[] viewShip, String instructions, Scene[] scenes, Button[] back ){
        BackgroundImage back2 = new BackgroundImage(new Image(getClass().getResource("stars.jpg").toExternalForm()), BackgroundRepeat.NO_REPEAT,BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        Background background = new Background(back2);         
        grid[7] = new GridPane();
        grid[7].setBackground(background);
        styleScene(grid, 7);
        Label select2 = new Label(instructions);
        select2.setTextFill(Color.web("#FFFFFF"));
        select2.setFont(new Font("Serif Gothic ", 12));  
        viewPilot[1].setText("View Pilots");
        viewShip[1].setText("View Vehicles");

        grid[7].add(select2, 0 ,0);
        grid[7].add(viewShip[1], 0, 1);
        grid[7].add(viewPilot[1], 0, 2);
        grid[7].add(back[3],0,3);
        scenes[7] = new Scene(grid[7], -1, -1);
    }
}
