package ru.bmstu.edu.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.stage.Stage;
import org.controlsfx.control.textfield.CustomTextField;
import ru.bmstu.edu.DAO.PostgreSQLConnection;
import ru.bmstu.edu.objects.Vacancy;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class EditVacancyController {

  @FXML
  private CustomTextField txtVacancyName;
  @FXML
  private CustomTextField txtWages;
  @FXML
  private CustomTextField txtTotalAmount;

  private Vacancy vacancy;

  public Vacancy getVacancy(){
    return vacancy;
  }
  public void setVacancy(Vacancy vacancy){
    if(vacancy==null){
      return;
    }
    System.out.println("Получена вакансия id: " + vacancy.getId());
    this.vacancy=vacancy;

    if(vacancy.getId()!=0){
      txtVacancyName.setText(vacancy.getName());
      txtWages.setText(String.valueOf(vacancy.getWages()));
      txtTotalAmount.setText(String.valueOf(vacancy.getAmountTotal()));
    }else{
      txtWages.clear();
      txtVacancyName.clear();
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
    String query = "UPDATE cvdata.bmstu.vacancies "
        + " set name = ?, idproject = ?, wages = ?, VALUE = ?,totalamount = ?  "
        + " WHERE id = ?;";
    try (PreparedStatement pstmt = PostgreSQLConnection.getConnection().prepareStatement(query)) {
      int i = 0;
      pstmt.setString(++i,vacancy.getName());
      pstmt.setObject(++i, vacancy.getIdProject());
      pstmt.setObject(++i, vacancy.getWages());
      pstmt.setObject(++i, vacancy.getValue());
      pstmt.setObject(++i, vacancy.getAmountTotal());
      pstmt.setObject(++i, vacancy.getId());
      pstmt.executeUpdate();
    }
  }


  private void insertVacancy(Vacancy vacancy) throws SQLException {
    vacancy.setName(txtVacancyName.getText());
    vacancy.setWages(Integer.valueOf(txtWages.getText()));
    vacancy.setAmountTotal(Integer.valueOf(txtTotalAmount.getText()));
    String query = "INSERT INTO cvdata.bmstu.vacancies "
        + " (name, idproject, wages, VALUE,totalamount ) "
        + " VALUES (?, ?, ?, ?, ? );";
    try (PreparedStatement pstmt = PostgreSQLConnection.getConnection().prepareStatement(query)) {
      int i = 0;
      pstmt.setString(++i,vacancy.getName());
      pstmt.setObject(++i, vacancy.getIdProject());
      pstmt.setObject(++i, vacancy.getWages());
      pstmt.setObject(++i, vacancy.getValue());
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
