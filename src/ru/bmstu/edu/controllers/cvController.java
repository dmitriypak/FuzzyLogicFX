package ru.bmstu.edu.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import ru.bmstu.edu.DAO.PostgreSQLConnection;
import ru.bmstu.edu.objects.CV;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class cvController {
  @FXML
  private TableColumn colPosition;
  @FXML
  private TableColumn colSalary;
  @FXML
  private TableColumn colCity;
  @FXML
  private TableColumn colAge;
  @FXML
  private TableColumn colExperience;
  @FXML
  private TableView tableCV;

  private ObservableList<CV> CVList = FXCollections.observableArrayList();

  @FXML
  private void initialize(){
    colPosition.setCellValueFactory(new PropertyValueFactory<CV,String>("positionname"));
    colSalary.setCellValueFactory(new PropertyValueFactory<CV,Integer>("salary"));
    colExperience.setCellValueFactory(new PropertyValueFactory<CV,String>("experience"));
    fillData();
  }

  private void fillData(){
    try(PreparedStatement statement = PostgreSQLConnection.getConnection().prepareStatement("select positionname, salary, experience" +
        " from cvdata.bmstu.CV order by id limit 1000; ")) {
      ResultSet rs = statement.executeQuery();
      while (rs.next()){
        CV cv = new CV();
        cv.setPositionname(rs.getString("positionname"));
        cv.setSalary(rs.getInt("salary"));
        cv.setExperience(rs.getInt("experience"));
        CVList.add(cv);
      }
      tableCV.setItems(CVList);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }


}
