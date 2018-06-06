package ru.bmstu.edu.controllers;

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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.controlsfx.control.textfield.CustomTextField;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.postgresql.util.PGobject;
import ru.bmstu.edu.DAO.PostgreSQLConnection;
import ru.bmstu.edu.objects.LinguisticVariable;
import ru.bmstu.edu.objects.MembershipFunction;
import ru.bmstu.edu.objects.enums.MFname;
import ru.bmstu.edu.objects.utils.DaoUtils;

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
  @FXML
  private ComboBox comboTypeVariable;
  @FXML
  private ComboBox comboCode;
  @FXML
  private TableColumn columnCodeMF;
  @FXML
  private AnchorPane root;
  @FXML
  private CustomTextField txtSugenoConstant;
  @FXML
  private TableColumn colConstant;

  private LinguisticVariable linguisticVariable;

  private ObservableList<MembershipFunction> mfList = FXCollections.observableArrayList();
  private Parent fxmlEdit;
  private FXMLLoader fxmlLoader = new FXMLLoader();
  private EditMembershipFunctionController editMembershipFunctionController;
  private Stage editMembershipFunctionStage;
  private Node nodesource;


  public void initialize() {
    DaoUtils.setupClearButtonField(txtNameMF);
    DaoUtils.setupClearButtonField(txtParamMF);
    DaoUtils.setupClearButtonField(txtNameVariable);

    createComboBox();
    colMFName.setCellValueFactory(new PropertyValueFactory<MembershipFunction,String>("nameMF"));
    colMFParamValue.setCellValueFactory(new PropertyValueFactory<MembershipFunction,String>("paramValueMF"));
    columnCodeMF.setCellValueFactory(new PropertyValueFactory<MembershipFunction,String>("codeMF"));
    colConstant.setCellValueFactory(new PropertyValueFactory<MembershipFunction,Double>("constantSugeno"));

    tableMF.setOnMouseClicked( event -> {
      if( event.getClickCount() == 1 ) {
        MembershipFunction mf = (MembershipFunction)tableMF.getSelectionModel().getSelectedItem();
        if(mf!=null){
          txtParamMF.setText(mf.getParamValueMF());
          txtNameMF.setText(mf.getNameMF());
          comboCode.setValue(mf.getmFname());
          txtSugenoConstant.setText(String.valueOf(mf.getConstantSugeno()));
        }
      }});

//    try {
//      fxmlLoader.setLocation(getClass().getResource("../fxml/editMembershipFunction.fxml"));
//      fxmlEdit = fxmlLoader.load();
//      editMembershipFunctionController = fxmlLoader.getController();
//    } catch (IOException e) {
//      e.printStackTrace();
//    }
  }

  private void createComboBox(){
    ObservableList<String> typeDistribution = FXCollections.observableArrayList(
        "INPUT",
        "OUTPUT");
    comboTypeVariable.setItems(typeDistribution);


    //Заполнение комбобокса comboCode
    ObservableList<MFname> codeVariablesList = FXCollections.observableArrayList();
    for(MFname mf:MFname.values()){
      codeVariablesList.add(mf);
    }
    comboCode.setItems(codeVariablesList);

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
        if(saveMF==null) return;
        saveMF.setNameMF(txtNameMF.getText());
        saveMF.setParamValueMF(txtParamMF.getText());
        saveMF.setmFname((MFname) comboCode.getSelectionModel().getSelectedItem());
        saveMF.setConstantSugeno(Double.valueOf(txtSugenoConstant.getText()));
        mfList.set(tableMF.getSelectionModel().getSelectedIndex(),saveMF);
        drawGraphMF();
//        editMembershipFunctionController.setMF((MembershipFunction)tableMF.getSelectionModel().getSelectedItem());
//        showDialog();
        break;

      case "btnDeleteMF":
        MembershipFunction delMF;
        delMF = (MembershipFunction)tableMF.getSelectionModel().getSelectedItem();
        mfList.remove(delMF);
        drawGraphMF();
        break;
    }

  }

  private void drawGraphMF() {

    chart1.getData().clear();
    chart1.setTitle(linguisticVariable.getName());

    for (int j = 0; j<mfList.size();j++) {
      MembershipFunction mf = mfList.get(j);

      double maxValue = 0;
      XYChart.Series series = new XYChart.Series();
      String value[] = mf.getParamValueMF().split(" ");
      series.setName(mf.getNameMF());
      switch (value.length){
        case 3:
          for(int i=0;i<value.length;i++){
            double val = Double.valueOf(value[i]);
            series.getData().add(new XYChart.Data(val,i%2));
          }
          break;
        case 4:
          for(int i=0;i<value.length;i++){
            double val = Double.valueOf(value[i]);
            if(i==0 || i==3){
              series.getData().add(new XYChart.Data(val,0));
            }else{
              series.getData().add(new XYChart.Data(val,1));
            }
            if(val>maxValue){
              maxValue = val;
            }
          }
      }
//      NumberAxis xAxis = new NumberAxis(0,maxValue,0);
//      NumberAxis yAxis = new NumberAxis();
//      AreaChart chart = new AreaChart(xAxis,yAxis);


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
    MFname codeMF = (MFname)comboCode.getSelectionModel().getSelectedItem();
    if(!nameMF.isEmpty() && !paramMF.isEmpty()){
      MembershipFunction mf = new MembershipFunction(nameMF,codeMF.getCode(),paramMF);
      if(!txtSugenoConstant.getText().isEmpty()){
        mf.setConstantSugeno(Double.valueOf(txtSugenoConstant.getText()));
      }
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
    txtNameMF.clear();
    txtParamMF.clear();
    txtSugenoConstant.clear();
    comboCode.getSelectionModel().clearSelection();
    comboTypeVariable.setValue(linguisticVariable.getType());
    if(linguisticVariable.getId()!=0){
      mfList = FXCollections.observableArrayList(DaoUtils.getMFList(linguisticVariable.getValue()));
      System.out.println("Получен список " + mfList.size());
      fillData();
    }else{
      txtNameVariable.clear();
      txtNameMF.clear();
      txtParamMF.clear();
      txtSugenoConstant.clear();
      mfList.clear();
      chart1.getData().clear();
      chart1.setTitle("");
    }

  }

  private void fillData(){
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
    if(comboTypeVariable.getValue()==null){
      Alert alert = new Alert(Alert.AlertType.WARNING);
      alert.setTitle("Ошибка");
      alert.setHeaderText(null);
      alert.setContentText("Не указан тип переменной");
      alert.showAndWait();
      return;
    }
    JSONObject obj = new JSONObject();
    JSONArray ar = new JSONArray();

    for(int i = 0;i<mfList.size();i++){
      JSONObject objMF = new JSONObject();
      MFname mFname = (MFname)comboCode.getSelectionModel().getSelectedItem();
      objMF.put("MFParamName",mfList.get(i).getNameMF());
      objMF.put("MFParamValue",mfList.get(i).getParamValueMF());
      objMF.put("MFCode",mfList.get(i).getCodeMF());
      objMF.put("MFSugenoConstant",mfList.get(i).getConstantSugeno());

      ar.add(objMF);
    }
    obj.put("MFParams",ar);
    obj.put("Variable", txtNameVariable.getText());
    System.out.println(obj.toString());
    linguisticVariable.setName(txtNameVariable.getText());
    linguisticVariable.setValue(obj.toJSONString());
    linguisticVariable.setType(comboTypeVariable.getValue().toString());

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
        + " (name, value, description, type) "
        + " VALUES (?, ?, ?, ?);";
    try (PreparedStatement pstmt = PostgreSQLConnection.getConnection().prepareStatement(query)) {
      int i = 0;
      pstmt.setString(++i,linguisticVariable.getName());
      PGobject jsonObject = new PGobject();
      jsonObject.setType("json");
      jsonObject.setValue(linguisticVariable.getValue());
      pstmt.setObject(++i, jsonObject);
      pstmt.setString(++i,"");
      pstmt.setString(++i,comboTypeVariable.getValue().toString());
      pstmt.executeUpdate();
    }
  }

  private void updateLinguisticVariable(LinguisticVariable linguisticVariable) throws SQLException {
    String query = "update cvdata.bmstu.linguisticvariables "
        + " set name = ?, value = ?, description = ?, type = ? WHERE id = ?";
    try (PreparedStatement pstmt = PostgreSQLConnection.getConnection().prepareStatement(query)) {
      int i = 0;
      pstmt.setString(++i,linguisticVariable.getName());

      PGobject jsonObject = new PGobject();
      jsonObject.setType("json");
      jsonObject.setValue(linguisticVariable.getValue());
      pstmt.setObject(++i, jsonObject);
      pstmt.setString(++i,"");
      pstmt.setString(++i,comboTypeVariable.getValue().toString());
      pstmt.setInt(++i,linguisticVariable.getId());

      pstmt.executeUpdate();
    }
  }

  public LinguisticVariable getVariable(){
    return linguisticVariable;
  }

}
