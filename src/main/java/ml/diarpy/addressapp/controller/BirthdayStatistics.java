package ml.diarpy.addressapp.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.XYChart;
import ml.diarpy.addressapp.model.Person;

import java.net.URL;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class BirthdayStatistics implements Initializable {
    @FXML
    private BarChart<String, Integer> bcStatistic;
    @FXML
    private CategoryAxis xAxis;

    private ObservableList<String> monthNames = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String[] months = DateFormatSymbols.getInstance(Locale.FRANCE).getMonths();
        monthNames.addAll(Arrays.asList(months));
        xAxis.setCategories(monthNames);
    }

    public void setPersonData(List<Person> personData) {
        // Count the number of people having their birthday in a specific month
        int[] monthCounter = new int[12];
        for (Person p: personData) {
            int month = p.getBirthday().getMonthValue();
            monthCounter[month - 1]++;
        }

        XYChart.Series<String, Integer> series = new XYChart.Series<>();

        // Create a XYChart.Data object for each month. Add it to the series
        for (int i = 0; i < monthCounter.length; i++) {
            series.getData().add(new XYChart.Data<>(monthNames.get(i), monthCounter[i]));
        }

        bcStatistic.getData().add(series);
    }
}
