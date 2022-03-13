package ml.diarpy.addressapp.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ml.diarpy.addressapp.model.Person;
import ml.diarpy.addressapp.util.DateUtil;

import java.net.URL;
import java.util.ResourceBundle;

public class PersonEditDialog implements Initializable {

    @FXML
    private TextField tfBirthday;

    @FXML
    private TextField tfCity;

    @FXML
    private TextField tfFirstName;

    @FXML
    private TextField tfLastName;

    @FXML
    private TextField tfPostalCode;

    @FXML
    private TextField tfStreet;

    private Stage dialogStage;
    private Person person;
    private boolean okClicked = false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setPerson(Person person) {
        this.person = person;

        tfFirstName.setText(person.getFirstName());
        tfLastName.setText(person.getLastName());
        tfStreet.setText(person.getStreet());
        tfPostalCode.setText(String.valueOf(person.getPostalCode()));
        tfCity.setText(person.getCity());

        tfBirthday.setText(DateUtil.format(person.getBirthday()));
    }

    public boolean isOkClicked() {
        return okClicked;
    }

    @FXML
    private void handleOk() {
        if (isInputValid()) {
            person.setFirstName(tfFirstName.getText());
            person.setLastName(tfLastName.getText());
            person.setStreet(tfStreet.getText());
            person.setStreet(tfStreet.getText());
            person.setPostalCode(Integer.parseInt(tfPostalCode.getText()));
            person.setCity(tfCity.getText());
            person.setBirthday(DateUtil.parse(tfBirthday.getText()));

            okClicked = true;
            dialogStage.close();
        }
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    private boolean isInputValid() {
        String errorMessage = "";

        if (tfFirstName.getText() == null || tfFirstName.getText().length() == 0) {
            errorMessage += "No valid first name!\n";
        }
        if (tfLastName.getText() == null || tfLastName.getText().length() == 0) {
            errorMessage += "No valid last name!\n";
        }
        if (tfStreet.getText() == null || tfStreet.getText().length() == 0) {
            errorMessage += "No valid street!\n";
        }

        if (tfPostalCode.getText() == null || tfPostalCode.getText().length() == 0) {
            errorMessage += "No valid postal code!\n";
        } else {
            // try to parse the postal code into an int.
            try {
                Integer.parseInt(tfPostalCode.getText());
            } catch (NumberFormatException e) {
                errorMessage += "No valid postal code (must be an integer)!\n";
            }
        }

        if (tfCity.getText() == null || tfCity.getText().length() == 0) {
            errorMessage += "No valid city!\n";
        }

        if (tfBirthday.getText() == null || tfBirthday.getText().length() == 0) {
            errorMessage += "No valid birthday!\n";
        } else {
            if (!DateUtil.validDate(tfBirthday.getText())) {
                errorMessage += "No valid birthday. Use the format dd.mm.yyyy!\n";
            }
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            // Show the error message.
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Please correct invalid fields");
            alert.setContentText(errorMessage);

            alert.showAndWait();

            return false;
        }
    }
}
