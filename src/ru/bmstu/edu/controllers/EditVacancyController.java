package ru.bmstu.edu.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import org.controlsfx.control.textfield.CustomTextField;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.postgresql.util.PGobject;
import ru.bmstu.edu.DAO.PostgreSQLConnection;
import ru.bmstu.edu.objects.LinguisticVariable;
import ru.bmstu.edu.objects.MembershipFunction;
import ru.bmstu.edu.objects.Vacancy;
import ru.bmstu.edu.model.DaoUtils;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class EditVacancyController {

  @FXML
  private CustomTextField txtVacancyName;
  @FXML
  private CustomTextField txtWages;
  @FXML
  private CustomTextField txtTotalAmount;
  @FXML
  private ComboBox comboCategory;

  private Vacancy vacancy;
  private ArrayList<LinguisticVariable> listOutputVariables = DaoUtils.getOutputVariables();
  private ArrayList<MembershipFunction> mfList;
  private LinguisticVariable outputVariable;

  @FXML
  private void initialize(){
    createComboBox();
  }

  private void createComboBox(){
    if(listOutputVariables.size()>0){
      outputVariable = listOutputVariables.get(0);
      mfList = outputVariable.getMfList();

    }

    //Заполнение комбобокса
    ObservableList<String> fnamesList = FXCollections.observableArrayList();
    for(MembershipFunction mf:mfList){
      fnamesList.add(mf.getNameMF());
    }
    comboCategory.setItems(fnamesList);
  }

  public Vacancy getVacancy(){
    return vacancy;
  }
  public void setVacancy(Vacancy vacancy) throws ParseException {
    if(vacancy==null){
      return;
    }
    System.out.println("Получена вакансия id: " + vacancy.getId());
    this.vacancy=vacancy;

    if(vacancy.getId()!=0){
      txtVacancyName.setText(vacancy.getName());
      txtWages.setText(String.valueOf(vacancy.getWages()));
      txtTotalAmount.setText(String.valueOf(vacancy.getAmountTotal()));
      org.json.simple.parser.JSONParser parser = new org.json.simple.parser.JSONParser();
      //Парсинг JSON
      Object obj = parser.parse(vacancy.getValue());
      JSONObject jsonObject = (JSONObject) obj;
      comboCategory.setValue(jsonObject.get("nameCategory").toString());
    }else{
      txtWages.clear();
      txtVacancyName.clear();
      txtTotalAmount.clear();
      comboCategory.setValue("");
    }

  }
  public void actionSave(ActionEvent actionEvent) throws SQLException {

    if(vacancy.getId()!=0){
      updateVacancy(vacancy);
    }
    else{
      insertVacancy(vacancy);
    }
    actionClose(actionEvent);
  }

  private void updateVacancy(Vacancy vacancy) throws SQLException {
    vacancy.setName(txtVacancyName.getText());
    vacancy.setWages(Integer.valueOf(txtWages.getText()));
    vacancy.setAmountTotal(Integer.valueOf(txtTotalAmount.getText()));

    String nameCategory = comboCategory.getValue().toString();
    vacancy.setNameCategory(nameCategory);
    String codeCategory = "";
    for(MembershipFunction membershipFunction:mfList){
      if (membershipFunction.getNameMF().equals(nameCategory)) {
        codeCategory = membershipFunction.getCodeMF();
      }
    }

    JSONObject obj = new JSONObject();
    obj.put("idvariable",outputVariable.getId());
    obj.put("nameCategory",nameCategory);
    obj.put("codeCategory",codeCategory);

    String query = "UPDATE cvdata.bmstu.vacancies "
        + " set name = ?, idproject = ?, wages = ?, VALUE = ?,totalamount = ?  "
        + " WHERE id = ?;";
    try (PreparedStatement pstmt = PostgreSQLConnection.getConnection().prepareStatement(query)) {
      int i = 0;
      pstmt.setString(++i,vacancy.getName());
      pstmt.setObject(++i, vacancy.getIdProject());
      pstmt.setObject(++i, vacancy.getWages());

      PGobject jsonObject = new PGobject();
      jsonObject.setType("json");
      jsonObject.setValue(obj.toString());

      pstmt.setObject(++i, jsonObject);
      pstmt.setObject(++i, vacancy.getAmountTotal());
      pstmt.setObject(++i, vacancy.getId());
      pstmt.executeUpdate();
    }
  }


  private void insertVacancy(Vacancy vacancy) throws SQLException {
    vacancy.setName(txtVacancyName.getText());
    vacancy.setWages(Integer.valueOf(txtWages.getText()));
    vacancy.setAmountTotal(Integer.valueOf(txtTotalAmount.getText()));
    vacancy.setAmountFree(Integer.valueOf(txtTotalAmount.getText()));
    String nameCategory = comboCategory.getValue().toString();
    String codeCategory = "";
    for(MembershipFunction membershipFunction:mfList){
      if (membershipFunction.getNameMF().equals(nameCategory)) {
        codeCategory = membershipFunction.getCodeMF();
      }
    }

    JSONObject obj = new JSONObject();
    obj.put("idvariable",outputVariable.getId());
    obj.put("nameCategory",nameCategory);
    obj.put("codeCategory",codeCategory);

    vacancy.setNameCategory(nameCategory);

    String query = "INSERT INTO cvdata.bmstu.vacancies "
        + " (name, idproject, wages, VALUE,totalamount ) "
        + " VALUES (?, ?, ?, ?, ? );";
    try (PreparedStatement pstmt = PostgreSQLConnection.getConnection().prepareStatement(query)) {
      int i = 0;
      pstmt.setString(++i,vacancy.getName());
      pstmt.setObject(++i, vacancy.getIdProject());
      pstmt.setObject(++i, vacancy.getWages());

      PGobject jsonObject = new PGobject();
      jsonObject.setType("json");
      jsonObject.setValue(obj.toString());

      pstmt.setObject(++i, jsonObject);
      pstmt.setObject(++i, vacancy.getAmountTotal());

      pstmt.executeUpdate();
    }
  }

  public void actionClose(ActionEvent actionEvent){
    Node source = (Node) actionEvent.getSource();
    Stage stage = (Stage) source.getScene().getWindow();
    stage.hide();

  }
}
