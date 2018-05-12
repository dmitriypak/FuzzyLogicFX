package ru.bmstu.edu.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.controlsfx.control.RangeSlider;
import ru.bmstu.edu.objects.Category;

import java.io.IOException;

public class CategoryController {
  @FXML
  private TableView tableCategory;
  private Parent fxmlEdit;
  private FXMLLoader fxmlLoader = new FXMLLoader();
  private Node nodesource;
  private EditCategoryController editCategoryController;


  @FXML
  private void initialize(){
    try {
      fxmlLoader.setLocation(getClass().getResource("../fxml/editCategory.fxml"));
      fxmlEdit = fxmlLoader.load();
      editCategoryController = fxmlLoader.getController();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  Region createHorizontalSlider() {
    final TextField minField = new TextField();
    minField.setPrefColumnCount(5);
    final TextField maxField = new TextField();
    maxField.setPrefColumnCount(5);

    final RangeSlider hSlider = new RangeSlider(0, 100, 10, 90);
    hSlider.setShowTickMarks(true);
    hSlider.setShowTickLabels(true);
    hSlider.setBlockIncrement(10);
    hSlider.setPrefWidth(200);

    hSlider.setLabelFormatter(new StringConverter<Number>() {

      @Override
      public String toString(Number object) {
        switch (object.intValue()) {
          case 0:
            return "very low";
          case 25:
            return "low";
          case 50:
            return "middle";
          case 75:
            return "high";
          case 100:
            return "very high";
        }
        return object.toString();
      }

      @Override
      public Number fromString(String string) {
        return Double.valueOf(string);
      }
    });



    minField.setText("" + hSlider.getLowValue());
    maxField.setText("" + hSlider.getHighValue());

    minField.setEditable(false);
    minField.setPromptText("Min");

    maxField.setEditable(false);
    maxField.setPromptText("Max");

    minField.textProperty().bind(hSlider.lowValueProperty().asString("%.2f"));
    maxField.textProperty().bind(hSlider.highValueProperty().asString("%.2f"));

    HBox box = new HBox(10);
    box.getChildren().addAll(minField, hSlider, maxField);
    box.setPadding(new Insets(20,0,0,20));
    box.setFillHeight(false);

    return box;
  }



  Region createLabelSlider() {
    final RangeSlider hSlider = new RangeSlider(0, 100, 10, 90);
    hSlider.setShowTickMarks(true);
    hSlider.setShowTickLabels(true);
    hSlider.setLabelFormatter(new StringConverter<Number>() {

      @Override
      public String toString(Number object) {
        switch (object.intValue()) {
          case 0:
            return "very low";
          case 25:
            return "low";
          case 50:
            return "middle";
          case 75:
            return "high";
          case 100:
            return "very high";
        }
        return object.toString();
      }

      @Override
      public Number fromString(String string) {
        return Double.valueOf(string);
      }
    });
    hSlider.setBlockIncrement(10);
    hSlider.setPrefWidth(300);

    HBox box = new HBox(10);
    box.getChildren().addAll(hSlider);
    box.setPadding(new Insets(20,0,0,20));
    box.setFillHeight(false);

    return box;
  }




  private void showDialog(Category category) {

    if (category == null) return;
    Stage viewCategoryStage = new Stage();
    GridPane root = new GridPane();
    root.setHgap(50);
    viewCategoryStage.setTitle("Редактирование категорий");
    viewCategoryStage.setMinHeight(600);
    viewCategoryStage.setMinWidth(800);
    viewCategoryStage.setResizable(true);
    root.setMinWidth(300);
    ScrollPane scrollPane = new ScrollPane();

    Region slider = createHorizontalSlider();

    root.add(slider,1,1);



    scrollPane.setContent(root);
    scrollPane.setPannable(true);
    scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
    scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);



    Scene scene = new Scene(scrollPane);

    viewCategoryStage.setScene(scene);
    viewCategoryStage.initModality(Modality.WINDOW_MODAL);
    viewCategoryStage.initOwner((Stage) nodesource.getScene().getWindow());
    viewCategoryStage.showAndWait();
  }

  public void actionButtonPressed(ActionEvent actionEvent) {
    Object source = actionEvent.getSource();
    if (!(source instanceof Button)) {
      return;
    }
    nodesource = (Node) actionEvent.getSource();
    Button clickedButton = (Button) source;

    switch (clickedButton.getId()) {
      case "btnAddCategory":
        Category category = new Category();
        category.setId(0);
        showDialog(category);
        break;

      case "btnEditCategory":
        Category cat = (Category) tableCategory.getSelectionModel().getSelectedItem();
        editCategoryController.setCategory(cat);
        showDialog(cat);
        break;
      case "btnDeleteCategory":
        break;
    }
  }

}
