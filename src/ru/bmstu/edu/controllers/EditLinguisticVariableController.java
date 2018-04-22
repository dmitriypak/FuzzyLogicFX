package ru.bmstu.edu.controllers;

import javafx.beans.property.ObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.controlsfx.control.textfield.CustomTextField;
import org.controlsfx.control.textfield.TextFields;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import ru.bmstu.edu.DAO.PostgreSQLConnection;
import ru.bmstu.edu.objects.LinguisticVariable;
import ru.bmstu.edu.objects.MembershipFunction;

import java.io.IOException;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class EditLinguisticVariableController{

  @FXML
  private TextArea txtValueVariable;
  @FXML
  private CustomTextField txtNameVariable;
  @FXML
  private LineChart chart;
  @FXML
  private TableColumn colMFName;
  @FXML
  private TableColumn colMFParamValue;
  @FXML
  private TableView tableMF;
  @FXML
  private Button btnAdd;

  private LinguisticVariable linguisticVariable;
  private ObservableList<MembershipFunction> mfList = FXCollections.observableArrayList();
  private Parent fxmlEdit;
  private FXMLLoader fxmlLoader = new FXMLLoader();
  private EditMembershipFunctionController editMembershipFunctionController;
  private Stage editMembershipFunctionStage;
  private Node nodesource;


  public void initialize() {
    setupClearButtonField(txtNameVariable);

    colMFName.setCellValueFactory(new PropertyValueFactory<MembershipFunction,String>("MFname"));
    colMFParamValue.setCellValueFactory(new PropertyValueFactory<MembershipFunction,String>("MFParamValue"));

    try {
      fxmlLoader.setLocation(getClass().getResource("../fxml/editMembershipFunction.fxml"));
      fxmlEdit = fxmlLoader.load();
      editMembershipFunctionController = fxmlLoader.getController();
    } catch (IOException e) {
      e.printStackTrace();
    }
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
  public void actionButtonPressed(ActionEvent actionEvent) {
    Object source = actionEvent.getSource();
    if (!(source instanceof Button)) {
      return;
    }
    nodesource = (Node) actionEvent.getSource();
    Button clickedButton = (Button) source;

    switch (clickedButton.getId()) {
      case "btnAddMF":
        MembershipFunction membershipFunction = new MembershipFunction();
        editMembershipFunctionController.setMF(membershipFunction);
        membershipFunction = editMembershipFunctionController.getMF();
        showDialog();
        break;

      case "btnEditMF":
        editMembershipFunctionController.setMF((MembershipFunction)tableMF.getSelectionModel().getSelectedItem());
        showDialog();
        break;

      case "btnDeleteMF":
//        User deluser;
//        deluser = (User)tableUsers.getSelectionModel().getSelectedItem();
//        usersListImpl.delete(deluser);
//        deleteUser(deluser);
        break;
    }

  }
  private void showDialog() {
    if (editMembershipFunctionStage==null) {
      editMembershipFunctionStage = new Stage();
      editMembershipFunctionStage.setTitle("Функция принадлежности");
      editMembershipFunctionStage.setMinHeight(150);
      editMembershipFunctionStage.setMinWidth(300);
      editMembershipFunctionStage.setResizable(false);
      editMembershipFunctionStage.setScene(new Scene(fxmlEdit));
      editMembershipFunctionStage.initModality(Modality.WINDOW_MODAL);
      editMembershipFunctionStage.initOwner((Stage) nodesource.getScene().getWindow());
    }
    editMembershipFunctionStage.showAndWait();
  }

  public void addMFParam(){
//    String nameMF = txtMFName.getText();
//    String paramMF = txtParamMF.getText();
//    if(!nameMF.isEmpty() && !paramMF.isEmpty()){
//      MembershipFunction mf = new MembershipFunction(nameMF,paramMF);
//      mfList.add(mf);
//      tableMF.setItems(mfList);
//    }


//    JSONObject obj = new JSONObject();
//    JSONArray ar = new JSONArray();
//    obj.put("Variable", txtNameVariable.getText());
//
//    ar.add("MFParamName");
//    JSONObject objMF = new JSONObject();
//    objMF.put("MFParamName",)
//    ar.add(txtMFName.getText());
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

    //System.out.print(obj);
  }

  public void setLinguisticVariable(LinguisticVariable linguisticVariable){
    if(linguisticVariable==null){
      return;
    }

    this.linguisticVariable=linguisticVariable;
    txtNameVariable.setText(linguisticVariable.getName());
    txtValueVariable.setText(linguisticVariable.getValue());
    mfList = parseJSON(linguisticVariable.getValue());



    System.out.println("Получен список " + mfList.size());
    fillData();


  }

  private void fillData(){
//    try(PreparedStatement statement = PostgreSQLConnection.getConnection().prepareStatement("select id, name, VALUE from cvdata.bmstu.linguisticvariables")) {
//      ResultSet rs = statement.executeQuery();
//      while (rs.next()){
//        LinguisticVariable linguisticVariable = new LinguisticVariable(rs.getInt("id"), rs.getString("name"), rs.getString("value"));
//        MFList.add(linguisticVariable);
//      }
//      tableViewVariables.setItems(variableList.getVariablesList());
//    } catch (SQLException e) {
//      e.printStackTrace();
//    }


    //System.out.println("Список" + mfList.size());
    if(mfList.size()>0){
      tableMF.setItems(mfList);
    }

  }


  public ObservableList parseJSON(String value){
    ObservableList list = FXCollections.observableArrayList();
    org.json.simple.parser.JSONParser parser = new org.json.simple.parser.JSONParser();
    try {

      Object obj = parser.parse(value);

      JSONObject jsonObject = (JSONObject) obj;
      System.out.println("Парсинг JSON: " + jsonObject.toJSONString());
      String nameMF = (String) jsonObject.get("Variable");
      System.out.println(nameMF);
      JSONArray mf = (JSONArray) jsonObject.get("MFParams");
      for(int i = 0;i<mf.size();i++){
        JSONObject mfParamName = (JSONObject) mf.get(i);
        String mfParam = mfParamName.get("MFParamName").toString();
        System.out.println(mfParam);
        String mfParamValue =  mfParamName.get("MFParamValue").toString();
        System.out.println(mfParamValue);
        MembershipFunction membershipFunction = new MembershipFunction(mfParam, mfParamValue);
        list.add(membershipFunction);
      }
//      Iterator<JSONArray> iterator = mf.iterator();
//      while (iterator.hasNext()) {
//        System.out.println(iterator.next());
//
//
//      }
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return list;
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
