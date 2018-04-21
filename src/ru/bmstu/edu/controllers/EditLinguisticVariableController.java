package ru.bmstu.edu.controllers;

import javafx.beans.property.ObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.controlsfx.control.textfield.CustomTextField;
import org.controlsfx.control.textfield.TextFields;
import org.json.simple.JSONObject;
import ru.bmstu.edu.DAO.PostgreSQLConnection;
import ru.bmstu.edu.objects.LinguisticVariable;
import ru.bmstu.edu.objects.utils.JSONParser;

import java.lang.reflect.Method;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class EditLinguisticVariableController implements Initializable {

  @FXML
  private TextArea txtValueVariable;
  @FXML
  private CustomTextField txtNameVariable;
  @FXML
  private LineChart chart;
  @FXML
  private CustomTextField txtParamMF;
  @FXML
  private CustomTextField txtMFName;


  private LinguisticVariable linguisticVariable;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    setupClearButtonField(txtMFName);
    setupClearButtonField(txtNameVariable);
    setupClearButtonField(txtParamMF);
  }
  public void setupClearButtonField(CustomTextField txtFunction){
    try {
      Method m = TextFields.class.getDeclaredMethod("setupClearButtonField", TextField.class, ObjectProperty.class);
      m.setAccessible(true);
      m.invoke(null, txtFunction, txtFunction.rightProperty());
    }catch (Exception e){
      e.printStackTrace();
    }
  }

  private void createLineChart(){
    XYChart.Series series = new XYChart.Series();
    series.getData().clear();

      chart.setTitle(linguisticVariable.getName());
      XYChart.Series series1 = new XYChart.Series();
      XYChart.Series series2 = new XYChart.Series();
//      labelB.setVisible(false);
//      txtB.setVisible(false);
//      series.setName("Функция распределения f(x)");
//
//      series1.setName("Плотность вероятности p(x)");
//      for (Data data : dataListImpl.getDataObservableList()) {
//        series.getData().add(new XYChart.Data(data.getRange(), data.getValue()));
//        series1.getData().add(new XYChart.Data(data.getRange(),data.getValue2()));
//      }

  }



  public void addParam(){
    JSONObject obj = new JSONObject();
//    obj.put(tx, "mkyong.com");
//    obj.put("age", new Integer(100));
//
//    JSONArray list = new JSONArray();
//    list.add("msg 1");
//    list.add("msg 2");
//    list.add("msg 3");
//
//    obj.put("messages", list);
//
//    try (FileWriter file = new FileWriter("f:\\test.json")) {
//
//      file.write(obj.toJSONString());
//      file.flush();
//
//    } catch (IOException e) {
//      e.printStackTrace();
//    }

    System.out.print(obj);
  }

  public void setLinguisticVariable(LinguisticVariable linguisticVariable){
    if(linguisticVariable==null){
      return;
    }

    this.linguisticVariable=linguisticVariable;
    txtNameVariable.setText(linguisticVariable.getName());
    txtValueVariable.setText(linguisticVariable.getValue());
    JSONParser.parseJSON(linguisticVariable.getValue());

  }



  public void actionClose(ActionEvent actionEvent) {
    Node source = (Node) actionEvent.getSource();
    Stage stage = (Stage) source.getScene().getWindow();
    stage.hide();
  }
  public void actionSave(ActionEvent actionEvent) throws SQLException {
    linguisticVariable.setName(txtNameVariable.getText());
    linguisticVariable.setValue(txtValueVariable.getText());

    if(linguisticVariable.getId()!=0){
      updateLinguisticVariable(linguisticVariable);
    }
    else{
      insertLinguisticVariable(linguisticVariable);
    }

    actionClose(actionEvent);
  }

  private void insertLinguisticVariable(LinguisticVariable linguisticVariable) throws SQLException {
    String query = "INSERT INTO cvdata.bmstu.linguisticvariables "
        + " (name, value, description) "
        + " VALUES (?, ?, ?);";
    try (PreparedStatement pstmt = PostgreSQLConnection.getConnection().prepareStatement(query)) {
      int i = 0;
      pstmt.setString(++i,linguisticVariable.getName());
      pstmt.setString(++i,linguisticVariable.getValue());
      pstmt.setString(++i,"");
      pstmt.executeUpdate();
    }
  }

  private void updateLinguisticVariable(LinguisticVariable linguisticVariable) throws SQLException {
    String query = "update cvdata.bmstu.linguisticvariables "
        + " set name = ?, value = ?, description = ?) ";
    try (PreparedStatement pstmt = PostgreSQLConnection.getConnection().prepareStatement(query)) {
      int i = 0;
      pstmt.setString(++i,linguisticVariable.getName());
      pstmt.setString(++i,linguisticVariable.getValue());
      pstmt.setString(++i,"");
      pstmt.executeUpdate();
    }
  }
  
  


  public LinguisticVariable getVariable(){
    return linguisticVariable;
  }



}
