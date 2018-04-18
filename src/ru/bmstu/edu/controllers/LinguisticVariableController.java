package ru.bmstu.edu.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import ru.bmstu.edu.DAO.PostgreSQLConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LinguisticVariableController {

  @FXML
  private TableView tableViewVariables;

  @FXML
  public void initialize(){

  }

  private void createVariablesList(){

    try(PreparedStatement statement = PostgreSQLConnection.getConnection().prepareStatement("select name, VALUE from cvdata.bmstu.linguisticvariables")) {
      ResultSet rs = statement.executeQuery();
      while (rs.next()){

      }

    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

}
