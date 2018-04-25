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
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.controlsfx.control.textfield.CustomTextField;
import org.controlsfx.control.textfield.TextFields;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.postgresql.util.PGobject;
import ru.bmstu.edu.DAO.PostgreSQLConnection;
import ru.bmstu.edu.objects.LinguisticVariable;
import ru.bmstu.edu.objects.MembershipFunction;
import ru.bmstu.edu.objects.utils.DaoUtils;

import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class EditLinguisticVariableController{

  @FXML
  private CustomTextField txtNameVariable;
  @FXML
  private TableColumn colMFName;
  @FXML
  private TableColumn colMFParamValue;
  @FXML
  private TableView tableMF;
  @FXML
  private Button btnSaveMF;
  @FXML
  private CustomTextField txtNameMF;
  @FXML
  private CustomTextField txtParamMF;
  @FXML
  private AreaChart chart1;
  @FXML
  private Button btnSave;

  private LinguisticVariable linguisticVariable;

  private ObservableList<MembershipFunction> mfList = FXCollections.observableArrayList();
  private Parent fxmlEdit;
  private FXMLLoader fxmlLoader = new FXMLLoader();
  private EditMembershipFunctionController editMembershipFunctionController;
  private Stage editMembershipFunctionStage;
  private Node nodesource;


  public void initialize() {
    setupClearButtonField(txtNameMF);
    setupClearButtonField(txtParamMF);
    setupClearButtonField(txtNameVariable);

    colMFName.setCellValueFactory(new PropertyValueFactory<MembershipFunction,String>("MFname"));
    colMFParamValue.setCellValueFactory(new PropertyValueFactory<MembershipFunction,String>("MFParamValue"));
    tableMF.setOnMouseClicked( event -> {
      if( event.getClickCount() == 1 ) {
        MembershipFunction mf = (MembershipFunction)tableMF.getSelectionModel().getSelectedItem();
        txtParamMF.setText(mf.getMFParamValue());
        txtNameMF.setText(mf.getMFname());

      }});

//    try {
//      fxmlLoader.setLocation(getClass().getResource("../fxml/editMembershipFunction.fxml"));
//      fxmlEdit = fxmlLoader.load();
//      editMembershipFunctionController = fxmlLoader.getController();
//    } catch (IOException e) {
//      e.printStackTrace();
//    }
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

  public void actionButtonPressed(ActionEvent actionEvent) {
    Object source = actionEvent.getSource();
    if (!(source instanceof Button)) {
      return;
    }
    nodesource = (Node) actionEvent.getSource();
    Button clickedButton = (Button) source;

    switch (clickedButton.getId()) {
      case "btnAddMF":
//        MembershipFunction membershipFunction = new MembershipFunction();
//        editMembershipFunctionController.setMF(membershipFunction);
//        membershipFunction = editMembershipFunctionController.getMF();
//        showDialog();
        addMFParam();
        break;

      case "btnSaveMF":
        MembershipFunction saveMF = (MembershipFunction)tableMF.getSelectionModel().getSelectedItem();
        saveMF.setMFname(txtNameMF.getText());
        saveMF.setMFParamValue(txtParamMF.getText());
        mfList.set(tableMF.getSelectionModel().getSelectedIndex(),saveMF);
        drawGraphMF();
//        editMembershipFunctionController.setMF((MembershipFunction)tableMF.getSelectionModel().getSelectedItem());
//        showDialog();
        break;

      case "btnDeleteMF":
        MembershipFunction delMF;
        delMF = (MembershipFunction)tableMF.getSelectionModel().getSelectedItem();
        mfList.remove(delMF);
        break;
    }

  }

  private void drawGraphMF() {
    chart1.getData().clear();
    chart1.setTitle(linguisticVariable.getName());
    for (MembershipFunction mf : mfList) {
      XYChart.Series series = new XYChart.Series();
      String value[] = mf.getMFParamValue().split(" ");
      for(int i =0;i<value.length;i++){
        series.setName(mf.getMFname());
        series.getData().add(new XYChart.Data(Double.valueOf(value[i]),i%2));
        //series.getData().add(new XYChart.Data(1,19));
        System.out.println(value[i]);
      }
      chart1.getData().add(series);
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
    String nameMF = txtNameMF.getText();
    String paramMF = txtParamMF.getText();
    if(!nameMF.isEmpty() && !paramMF.isEmpty()){
      MembershipFunction mf = new MembershipFunction(nameMF,paramMF);
      mfList.add(mf);
      ObservableList<MembershipFunction> mfList2 = FXCollections.observableArrayList(mfList);
      tableMF.setItems(mfList2);
      txtNameMF.clear();
      txtParamMF.clear();
      drawGraphMF();
    }
    //System.out.print(obj);
  }

  public void setLinguisticVariable(LinguisticVariable linguisticVariable){
    if(linguisticVariable==null){
      return;
    }
    System.out.println("Получена переменная id: " + linguisticVariable.getId());
    this.linguisticVariable=linguisticVariable;
    txtNameVariable.setText(linguisticVariable.getName());
    if(linguisticVariable.getId()!=0){
      mfList = DaoUtils.parseJSON(linguisticVariable.getValue());
      System.out.println("Получен список " + mfList.size());
      fillData();
    }else{
      txtNameVariable.setText("");
      txtNameMF.setText("");
      txtParamMF.setText("");
      mfList.clear();
      chart1.getData().clear();
    }

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
      drawGraphMF();
    }


  }


  public void actionClose(ActionEvent actionEvent) {
    Node source = (Node) actionEvent.getSource();
    Stage stage = (Stage) source.getScene().getWindow();
    stage.hide();
  }
  public void actionSave(ActionEvent actionEvent) throws SQLException {
    if(mfList.size()==0) return;
    JSONObject obj = new JSONObject();
    JSONArray ar = new JSONArray();

    for(int i = 0;i<mfList.size();i++){
      JSONObject objMF = new JSONObject();
      objMF.put("MFParamName",mfList.get(i).getMFname());
      objMF.put("MFParamValue",mfList.get(i).getMFParamValue());
      ar.add(objMF);
    }
    obj.put("MFParams",ar);
    obj.put("Variable", txtNameVariable.getText());
    System.out.println(obj.toString());
    linguisticVariable.setName(txtNameVariable.getText());
    linguisticVariable.setValue(obj.toJSONString());

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
      PGobject jsonObject = new PGobject();
      jsonObject.setType("json");
      jsonObject.setValue(linguisticVariable.getValue());
      pstmt.setObject(++i, jsonObject);
      pstmt.setString(++i,"");
      pstmt.executeUpdate();
    }
  }

  private void updateLinguisticVariable(LinguisticVariable linguisticVariable) throws SQLException {
    String query = "update cvdata.bmstu.linguisticvariables "
        + " set name = ?, value = ?, description = ? WHERE id = ?";
    try (PreparedStatement pstmt = PostgreSQLConnection.getConnection().prepareStatement(query)) {
      int i = 0;
      pstmt.setString(++i,linguisticVariable.getName());

      PGobject jsonObject = new PGobject();
      jsonObject.setType("json");
      jsonObject.setValue(linguisticVariable.getValue());
      pstmt.setObject(++i, jsonObject);
      pstmt.setString(++i,"");
      pstmt.setInt(++i,linguisticVariable.getId());

      pstmt.executeUpdate();
    }
  }

  public LinguisticVariable getVariable(){
    return linguisticVariable;
  }

}
