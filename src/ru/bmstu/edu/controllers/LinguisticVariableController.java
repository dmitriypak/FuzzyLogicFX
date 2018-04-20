package ru.bmstu.edu.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import ru.bmstu.edu.DAO.PostgreSQLConnection;
import ru.bmstu.edu.interfaces.CollectionImpl.LinguisticVariableList;
import ru.bmstu.edu.interfaces.Collections.LinguisticVariableCollection;
import ru.bmstu.edu.objects.LinguisticVariable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LinguisticVariableController {

  @FXML
  private TableView tableViewVariables;
  @FXML
  private TableColumn colNameVariable;
  @FXML
  private TableColumn colValueVariable;
  @FXML
  private TableColumn colIDVariable;

  private LinguisticVariableCollection variableList = new LinguisticVariableList();

  @FXML
  public void initialize(){
    colIDVariable.setCellValueFactory(new PropertyValueFactory<LinguisticVariableController,String>("id"));
    colValueVariable.setCellValueFactory(new PropertyValueFactory<LinguisticVariableController,String>("value"));
    colNameVariable.setCellValueFactory(new PropertyValueFactory<LinguisticVariableController,String>("name"));

    fillData();


  }

  private void fillData(){

    try(PreparedStatement statement = PostgreSQLConnection.getConnection().prepareStatement("select id, name, VALUE from cvdata.bmstu.linguisticvariables")) {
      ResultSet rs = statement.executeQuery();
      while (rs.next()){
        LinguisticVariable linguisticVariable = new LinguisticVariable(rs.getInt("id"), rs.getString("name"), rs.getString("value"));
        variableList.add(linguisticVariable);
      }
      tableViewVariables.setItems(variableList.getVariablesList());
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

}
