package ml.diarpy.addressapp;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ml.diarpy.addressapp.controller.PersonEditDialog;
import ml.diarpy.addressapp.controller.PersonOverview;
import ml.diarpy.addressapp.controller.RootLayout;
import ml.diarpy.addressapp.model.Person;
import ml.diarpy.addressapp.model.PersonListWrapper;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;
import java.util.prefs.Preferences;

/**
 * @author Mack_TB
 * @since 11/03/2022
 * @version 1.0.4
 */

public class MainApp extends Application {
    private Stage primaryStage;
    private BorderPane rootLayout;
    private ObservableList<Person> personData = FXCollections.observableArrayList();

    public MainApp() {
        // Add some sample data
        personData.add(new Person("Alpha", "BAH"));
        personData.add(new Person("Djibril", "COULIBALY"));
        personData.add(new Person("Maïmouna", "KEÎTA"));
        personData.add(new Person("Bassirou", "SOW"));
        personData.add(new Person("Aïssata", "WADIDIE"));
        personData.add(new Person("Hawa", "SANGARE"));
        personData.add(new Person("Japhet", "DIARRA"));
        personData.add(new Person("Will", "SMITH"));
        personData.add(new Person("Bob", "ATTO"));
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public ObservableList<Person> getPersonData() {
        return personData;
    }

    @Override
    public void start(Stage stage) throws Exception {
        this.primaryStage = stage;
        this.primaryStage.setTitle("AddressApp");
        this.primaryStage.getIcons().add(new Image("file:src/main/resources/ml/diarpy/addressapp/image/address_book_icon.png"));
//        this.primaryStage.getIcons().add(new Image(String.valueOf(getClass().getResource("ml/diarpy/addressapp/image/address_book_icon.png"))));

        initRootLayout();

        showPersonOverview();
    }

    public boolean showPersonEditDialog(Person person) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("view/personEditDialog.fxml"));
            AnchorPane personEditDialog = loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Person");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(personEditDialog);
            dialogStage.setScene(scene);
            dialogStage.getIcons().add(new Image("file:src/main/resources/ml/diarpy/addressapp/image/address_book_icon.png"));


            // Set the person into the controller.
            PersonEditDialog controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setPerson(person);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void showPersonOverview() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/ml/diarpy/addressapp/view/personOverview.fxml"));
            AnchorPane personOverview = loader.load();

            rootLayout.setCenter(personOverview);

            PersonOverview controller = loader.getController();
            controller.setMainApp(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initRootLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("view/rootLayout.fxml"));
            rootLayout = loader.load();

            RootLayout rootLayoutController = loader.getController();
            rootLayoutController.setMainApp(this);

            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

        File file = getPersonFilePath();
        if (file != null) {
            loadPersonDataFromFile(file);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    public File getPersonFilePath() {
        Preferences preferences = Preferences.userNodeForPackage(MainApp.class);
        String filePath = preferences.get("filePath", null);
        if (filePath != null) {
            return new File(filePath);
        } else {
            return null;
        }
    }

    public void setPersonFilePath(File file) {
        Preferences preferences = Preferences.userNodeForPackage(MainApp.class);
        if (file != null) {
            preferences.put("filePath", file.getPath());
            this.primaryStage.setTitle("AddressApp - " + file.getName());
        } else {
            preferences.remove("filePath");
            this.primaryStage.setTitle("AddressApp");
        }
    }

    public void loadPersonDataFromFile(File file) {
        try {
            JAXBContext context = JAXBContext.newInstance(PersonListWrapper.class);
            Unmarshaller um = context.createUnmarshaller();

            // Reading XML from the file and unmarshalling
            PersonListWrapper wrapper = (PersonListWrapper) um.unmarshal(file);


            personData.clear();
            personData.addAll(wrapper.getPersons());

            // Save the file path to the registry
            setPersonFilePath(file);
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Couldn't load data");
            alert.setContentText("Couldn't load data from file:\n" + file.getPath());

            alert.showAndWait();
        }
    }

    public void savePersonDataToFile(File file) {
        try {
            JAXBContext context = JAXBContext.newInstance(PersonListWrapper.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            PersonListWrapper wrapper = new PersonListWrapper();
            wrapper.setPersons(personData);

            // Marshalling and saving XML to the file
            m.marshal(wrapper, file);

            // Save the person file to the registry
            setPersonFilePath(file);
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Couldn't save data");
            alert.setContentText("Couldn't save data from file:\n" + file.getPath());

            alert.showAndWait();
        }
    }
}
