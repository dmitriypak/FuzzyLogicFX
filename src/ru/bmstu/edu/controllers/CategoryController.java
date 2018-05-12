package ru.bmstu.edu.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.controlsfx.control.RangeSlider;
import org.controlsfx.control.textfield.CustomTextField;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.postgresql.util.PGobject;
import ru.bmstu.edu.DAO.PostgreSQLConnection;
import ru.bmstu.edu.objects.Category;
import ru.bmstu.edu.objects.LinguisticVariable;
import ru.bmstu.edu.objects.MembershipFunction;
import ru.bmstu.edu.objects.utils.DaoUtils;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class CategoryController {
  @FXML
  private TableView tableCategory;
  @FXML
  private TableColumn colNameCategory;
  @FXML
  private TableColumn colValueCategory;


  private Parent fxmlEdit;
  private FXMLLoader fxmlLoader = new FXMLLoader();
  private Node nodesource;
  private EditCategoryController editCategoryController;
  private ArrayList<LinguisticVariable> listInputVariables = DaoUtils.getInputVariables();
  private ArrayList<LinguisticVariable> listOutputVariables = DaoUtils.getOutputVariables();
  private Map<LinguisticVariable,String> mapVariables = new LinkedHashMap<>();

  private Scene scene;
  @FXML
  private void initialize(){
    try {
      fxmlLoader.setLocation(getClass().getResource("../fxml/editCategory.fxml"));
      fxmlEdit = fxmlLoader.load();
      editCategoryController = fxmlLoader.getController();

      colNameCategory.setCellValueFactory(new PropertyValueFactory<LinguisticVariableController,String>("name"));
      colValueCategory.setCellValueFactory(new PropertyValueFactory<LinguisticVariableController,String>("value"));

      fillData();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private Region createHorizontalSlider(int min, int max, int minValue, int maxValue, String idVariable) {
    final TextField minField = new TextField();
    minField.setPrefColumnCount(5);
    final TextField maxField = new TextField();
    maxField.setPrefColumnCount(5);
    double increment = 0;

    if (max==1){
      increment = 0.1;
    }
    if(max<=100){
      increment = 10;
    }

    if(max<=1000){
      increment = 100;
    }

    if(max<=10000){
      increment = 1000;
    }

    if(max<=100000){
      increment = 10000;
    }

    if(max<=1000000){
      increment = 100000;
    }

    final RangeSlider hSlider = new RangeSlider(min, max, minValue, maxValue);
    hSlider.setShowTickMarks(true);
    hSlider.setShowTickLabels(true);
    hSlider.setBlockIncrement(increment);
    hSlider.setMajorTickUnit(increment);
    hSlider.setMinorTickCount(100);
    hSlider.setPrefWidth(200);

//    hSlider.setLabelFormatter(new StringConverter<Number>() {
//
//      @Override
//      public String toString(Number object) {
//        switch (object.intValue()) {
//          case 0:
//            return "very low";
//          case 1:
//            return "low";
//          case 2:
//            return "middle";
//          case 3:
//            return "high";
//          case 10:
//            return "very high";
//        }
//        return object.toString();
//      }
//
//      @Override
//      public Number fromString(String string) {
//        return Double.valueOf(string);
//      }
//    });


    minField.setId("MinValue"+idVariable);
    minField.setText("" + hSlider.getLowValue());
    maxField.setId("MaxValue"+idVariable);
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

  private Button getButton(String nameButton){
    Button button = new Button(nameButton);
    button.setMinWidth(75);

    return button;
  }

  private Label getLabel(String name){
    Label label = new Label(name);
    label.setFont(new Font("Verdana", 14));
    label.setTextAlignment(TextAlignment.CENTER);
    label.setTextFill(Paint.valueOf(String.valueOf(Color.BLUE)));
    label.setPadding(new Insets(25));
    //label.setMaxWidth(100);
    return label;
  }

  private void fillData(){
    ObservableList<Category> categoriesList = FXCollections.observableArrayList(DaoUtils.getCategoriesList());
    if(categoriesList.size()>0){
      tableCategory.setItems(categoriesList);
    }
  }

  private void showDialog(Category category) {
    int lastRow = 1;

    if (category == null) return;
    Stage viewCategoryStage = new Stage();
    GridPane root = new GridPane();
    root.setPadding(new Insets(25,0,0,0));
    root.setHgap(10);
    viewCategoryStage.setTitle("Редактирование категорий");
    viewCategoryStage.setMinHeight(300);
    viewCategoryStage.setMinWidth(400);
    viewCategoryStage.setResizable(true);
    root.setMinWidth(300);

    ScrollPane scrollPane = new ScrollPane();

    scrollPane.setContent(root);
    scrollPane.setPannable(true);
    scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
    scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);


    //Кнопка Сохранить
    Button buttonSave = getButton("Сохранить");
    buttonSave.setOnAction(e -> {
      try {
        actionSave(category);
      } catch (SQLException e1) {
        e1.printStackTrace();
      }
    });

    //Кнопка Отмена
//    Button buttonCancel = getButton("Отмена");
//    buttonCancel.setOnAction(e -> {
//      Node source = (Node) e.getSource();
//      Stage stage = (Stage) source.getScene().getWindow();
//      stage.hide();
//    });


    //Название категории
    Label nameCategory = getLabel("Наименование");
    //nameCategory.setPadding(new Insets(0,0,0,25));
    CustomTextField textFieldCategory = new CustomTextField();
    textFieldCategory.setId("txtCategoryName");
    root.add(nameCategory,0,lastRow);
    root.add(textFieldCategory,1,lastRow);
    lastRow +=1;



    for(int i = 0;i<listInputVariables.size();i++){
      LinguisticVariable linguisticVariable = listInputVariables.get(i);

      ArrayList<MembershipFunction>mfList = linguisticVariable.getMfList();
      int minValue = 0;
      int maxValue = 0;
      int min = 0;
      int max = 0;

      for(int j = 0;j<mfList.size();j++){
        MembershipFunction mf = mfList.get(j);
        String[] params = mf.getParamValueMF().split(" ");

        for(int k = 0;k<params.length;k++){
          int paramValue = Integer.valueOf(params[k]);
          System.out.println("Params " + paramValue);

          if(paramValue < minValue){
            min =paramValue;
            minValue = paramValue;
          }
          if(paramValue > maxValue){
            max = paramValue;
            maxValue = paramValue;
          }
        }
      }

      //Название переменной
      StringBuilder nameVariable = new StringBuilder(linguisticVariable.getName());
      Label label = getLabel(nameVariable.toString());

      //Слайдер
      Region slider = createHorizontalSlider(min, max, minValue, maxValue, String.valueOf(linguisticVariable.getId()));
      slider.setId("Slider"+i);

      lastRow +=i;
      root.add(label,0,lastRow);
      root.add(slider,1,lastRow);

      category.setLinguisticVariableStringMap(mapVariables);
    }

    root.add(buttonSave,2,lastRow+5);
    //root.add(buttonCancel,2,lastRow+5);

    scene = new Scene(scrollPane);

    viewCategoryStage.setScene(scene);
    viewCategoryStage.initModality(Modality.WINDOW_MODAL);
    viewCategoryStage.initOwner((Stage) nodesource.getScene().getWindow());
    viewCategoryStage.showAndWait();
  }


  //Сохранение изменений
  private void actionSave(Category category) throws SQLException {
    if(category ==null) return;
    TextField categoryName = (TextField) scene.lookup("#txtCategoryName");
    category.setName(categoryName.getText());

    int idCategory = category.getId();
    if(idCategory==0){
      for(LinguisticVariable linguisticVariable:listInputVariables){
        TextField minTextField = (TextField) scene.lookup("#MinValue"+String.valueOf(linguisticVariable.getId()));
        System.out.println("#MinValue"+linguisticVariable.getId());
        TextField maxTextField = (TextField) scene.lookup("#MaxValue"+String.valueOf(linguisticVariable.getId()));
        StringBuilder stringBuilder = new StringBuilder(minTextField.getText().replace(",","."));
        stringBuilder.append(" ").append(maxTextField.getText().replace(",","."));
        if(stringBuilder.toString()==null) return;
        mapVariables.put(linguisticVariable,stringBuilder.toString());
      }

      category.setLinguisticVariableStringMap(mapVariables);
      insertCategory(category);

    }else{
      updateCategory(category);
    }
  }

  private void insertCategory(Category category) throws SQLException {
    Map<LinguisticVariable,String> map= category.getLinguisticVariableStringMap();
    JSONObject obj = new JSONObject();
    JSONArray array = new JSONArray();
    for (Map.Entry<LinguisticVariable,String> entry : map.entrySet()) {
      JSONObject obj1 = new JSONObject();
      String[]values = entry.getValue().split(" ");
      obj1.put("idVariable",entry.getKey().getId());
      obj1.put("minValue",values[0]);
      obj1.put("maxValue",values[1]);
      array.add(obj1);
    }
    obj.put("values",array);


    String query = "INSERT INTO cvdata.bmstu.categories "
        + " (name, value) "
        + " VALUES (?, ?)";
    try(PreparedStatement st = PostgreSQLConnection.getConnection().prepareStatement(query)){
      int i=0;
      st.setString(++i,category.getName());
      PGobject jsonObject = new PGobject();
      jsonObject.setType("json");
      jsonObject.setValue(obj.toString());
      st.setObject(++i,jsonObject);
      st.executeUpdate();
    }
  }

  private void updateCategory(Category category) throws SQLException {
    Map<LinguisticVariable,String> map= category.getLinguisticVariableStringMap();
    JSONObject obj = new JSONObject();
    for (Map.Entry<LinguisticVariable,String> entry : map.entrySet()) {
      String[]values = entry.getValue().split(" ");
      obj.put("idVariable",entry.getKey().getId());
      obj.put("minValue",values[0]);
      obj.put("maxValue",values[1]);
    }
    String query = "UPDATE cvdata.bmstu.categories SET"
        + " name = ?, value = ? WHERE id = ? ";
    try(PreparedStatement st = PostgreSQLConnection.getConnection().prepareStatement(query)){
      int i=0;
      st.setString(++i,category.getName());
      st.setString(++i,obj.toString());
      st.setInt(++i,category.getId());
      st.executeUpdate();
    }
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
