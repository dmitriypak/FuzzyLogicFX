package ru.bmstu.edu.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.controlsfx.control.RangeSlider;
import ru.bmstu.edu.DAO.PostgreSQLConnection;
import ru.bmstu.edu.objects.LinguisticVariable;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class EditLinguisticVariableController {

  @FXML
  private TextArea txtValueVariable;
  @FXML
  private TextField txtNameVariable;
  @FXML
  private RangeSlider rangeSlider1;

  private LinguisticVariable linguisticVariable;

  @FXML
  public void initialize() {
    rangeSlider1.
  }


  public void setLinguisticVariable(LinguisticVariable linguisticVariable){
    if(linguisticVariable==null){
      return;
    }
    this.linguisticVariable=linguisticVariable;
    txtNameVariable.setText(linguisticVariable.getName());
    txtValueVariable.setText(linguisticVariable.getValue());
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
