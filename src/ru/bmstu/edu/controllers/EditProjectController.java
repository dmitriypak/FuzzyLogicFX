package ru.bmstu.edu.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ru.bmstu.edu.DAO.PostgreSQLConnection;
import ru.bmstu.edu.objects.Project;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class EditProjectController {

  @FXML
  private TextField txtProjectName;
  @FXML
  private TextField txtProjectDescr;
  @FXML
  private Button btnClose;
  @FXML
  private Button btnAddVacancy;

  private Node nodesource;

  private Project project;

  @FXML
  public void initialize(){

  }

  public void setProject(Project project){
    if(project==null){
      return;
    }
    this.project=project;
    txtProjectName.setText(project.getName());
    txtProjectDescr.setText(project.getDescr());
    System.out.println("Получен проект: " + project.getId());
  }

  public Project getProject(){
    return project;
  }


  public void actionButtonPressed(ActionEvent actionEvent) throws SQLException {
    Object source = actionEvent.getSource();
    if (!(source instanceof Button)) {
      return;
    }
    nodesource = (Node) actionEvent.getSource();
    Button clickedButton = (Button) source;
    switch (clickedButton.getId()) {
      case "btnSave":

        project.setName(txtProjectName.getText());
        project.setDescr(txtProjectDescr.getText());

        if (project.getId() > 0) {
          updateProject(project);
        } else {
          insertProject(project);
        }
        btnClose.fire();
        break;
      case "btnClose":
        Stage stage = (Stage) nodesource.getScene().getWindow();
        stage.hide();
        break;

      case "btnAddVacancy":
        break;
      case "btnEditVacancy":
        break;
      case "btnDeleteVacancy":
        break;
    }

  }

//    public void actionSave(ActionEvent actionEvent) {
//      project.setName(txtProjectName.getText());
//      project.setDescr(txtProjectDescr.getText());
//      project.setStartDate(String.valueOf(txtStartDate.getValue()));
//      project.setEndDate(String.valueOf(txtEndDate.getValue()));
//      System.out.println(project.getId());
//      if(project.getId()!=""){
//        updateProject(project);
//      }
//      else{
//        insertProject(project);
//
//        PreparedStatement call = null;
//        try {
//          call = MSSQLConnection.getConnection().prepareStatement("select id from projects where name = ?");
//          call.setString(1,project.getName());
//          ResultSet rs = call.executeQuery();
//
//          while (rs.next()){
//            project.setId(rs.getString(1));
//          }
//        } catch (SQLException e) {
//          e.printStackTrace();
//        }
//        projectArrayList.add(project);
//        comboProjectArrayList.add(project.getName());
//      }
//
//      actionClose(actionEvent);
//    }
//
//
  private void updateProject(Project project) throws SQLException {
    String query = "update cvdata.bmstu.linguisticvariables "
        + " set name = ?, value = ?, description = ?, type = ? WHERE id = ?";
    try (PreparedStatement pstmt = PostgreSQLConnection.getConnection().prepareStatement(query)) {
      int i = 0;
      pstmt.setString(++i,project.getName());
      pstmt.setString(++i,project.getDescr());
      pstmt.executeUpdate();
    }
  }

  private void insertProject(Project project) throws SQLException {
    String query = "INSERT INTO cvdata.bmstu.projects "
        + " (name, descr) "
        + " VALUES (?, ?);";
    try (PreparedStatement pstmt = PostgreSQLConnection.getConnection().prepareStatement(query)) {
      int i = 0;
      pstmt.setString(++i,project.getName());
      pstmt.setObject(++i, project.getDescr());
      pstmt.executeUpdate();
    }

  }

}
