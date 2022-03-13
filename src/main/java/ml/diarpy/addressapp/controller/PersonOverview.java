package ml.diarpy.addressapp.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import ml.diarpy.addressapp.MainApp;
import ml.diarpy.addressapp.model.Person;
import ml.diarpy.addressapp.util.DateUtil;

import java.net.URL;
import java.util.ResourceBundle;

public class PersonOverview implements Initializable {
    @FXML
    private TableView<Person> tvPerson;
    @FXML
    private TableColumn<Person, String> tcFirstName;
    @FXML
    private TableColumn<Person, String> tcLastName;

    @FXML
    private Label lbFirstName;
    @FXML
    private Label lbLastName;
    @FXML
    private Label lbStreet;
    @FXML
    private Label lbPostalCode;
    @FXML
    private Label lbCity;
    @FXML
    private Label lbBirthday;

    // Reference to the main application.
    private MainApp mainApp;

    /**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
    public PersonOverview() {
    }
/*
    *//**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     *//*
    @FXML
    private void initialize() {
        // Initialize the person table with the two columns.
        tcFirstName.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
        tcLastName.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());
    }*/

    /**
     * Is called by the main application to give a reference back to itself.
     *
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;

        // Add observable list data to the table
        tvPerson.setItems(mainApp.getPersonData());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize the person table with the two columns
        tcFirstName.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
        tcLastName.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());

        showPersonDetails(null);

        // Listen for selection changes and show the person details when changed
        tvPerson.getSelectionModel().selectedItemProperty().addListener(
                (observableValue, oldPerson, newPerson) -> showPersonDetails(newPerson)
        );
    }

    private void showPersonDetails(Person person) {
        if (person != null) {
            lbFirstName.setText(person.getFirstName());
            lbLastName.setText(person.getLastName());
            lbStreet.setText(person.getStreet());
            lbPostalCode.setText(String.valueOf(person.getPostalCode()));
            lbCity.setText(person.getCity());

            lbBirthday.setText(DateUtil.format(person.getBirthday()));
        } else {
            // Person is null, remove all the text.
            lbFirstName.setText("");
            lbLastName.setText("");
            lbStreet.setText("");
            lbPostalCode.setText("");
            lbCity.setText("");
            lbBirthday.setText("");
        }
    }

    @FXML
    private void handleDeletePerson() {
        int selectedIndex = tvPerson.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            tvPerson.getItems().remove(selectedIndex);
        } else { // Nothing selected
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("No selection");
            alert.setHeaderText("No Person Selected");
            alert.setContentText("Please select a person in the table.");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleNewPerson(ActionEvent event) {
        Person tempPerson =new Person();
        boolean okClicked = mainApp.showPersonEditDialog(tempPerson);
        if (okClicked) {
            mainApp.getPersonData().add(tempPerson);
        }
    }

    @FXML
    private void handleEditPerson(ActionEvent event) {
        Person selectedPerson = tvPerson.getSelectionModel().getSelectedItem();
        if (selectedPerson != null) {
            boolean okClicked = mainApp.showPersonEditDialog(selectedPerson);
            if (okClicked) {
                showPersonDetails(selectedPerson);
            }
        } else { // Nothing selected.
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("No Selection");
            alert.setHeaderText("No Person Selected");
            alert.setContentText("Please select a person in the table.");

            alert.showAndWait();
        }
    }

}
