package ru.bmstu.edu.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ru.bmstu.edu.DAO.PostgreSQLConnection;
import ru.bmstu.edu.interfaces.CollectionImpl.LinguisticVariableList;
import ru.bmstu.edu.interfaces.Collections.LinguisticVariableCollection;
import ru.bmstu.edu.objects.LinguisticVariable;

import java.io.IOException;
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
  @FXML
  private Button btnAddVariable;
  @FXML
  private Button btnEditVariable;
  @FXML
  private Button btnDeleteVariable;




  private LinguisticVariableCollection variableList = new LinguisticVariableList();
  private CvWebViewController cvWebViewController = new CvWebViewController();
  private Parent fxmlEdit;
  private FXMLLoader fxmlLoader = new FXMLLoader();
  private EditLinguisticVariableController editLinguisticVariableController;
  private Stage editLinguisticVariableStage;
  private Node nodesource;



  public void initialize(){
    colIDVariable.setCellValueFactory(new PropertyValueFactory<LinguisticVariableController,String>("id"));
    colValueVariable.setCellValueFactory(new PropertyValueFactory<LinguisticVariableController,String>("value"));
    colNameVariable.setCellValueFactory(new PropertyValueFactory<LinguisticVariableController,String>("name"));

    tableViewVariables.setOnMouseClicked( event -> {
      if( event.getClickCount() == 2 ) {
        btnEditVariable.fire();
      }});

    try {
      fxmlLoader.setLocation(getClass().getResource("../fxml/editLinguisticVariable.fxml"));
      fxmlEdit = fxmlLoader.load();
      editLinguisticVariableController = fxmlLoader.getController();
    } catch (IOException e) {
      e.printStackTrace();
    }

    fillData();


  }

  private void showDialog() {
    if (editLinguisticVariableStage==null) {
      editLinguisticVariableStage = new Stage();
      editLinguisticVariableStage.setTitle("Редактирование записи");
      editLinguisticVariableStage.setMinHeight(150);
      editLinguisticVariableStage.setMinWidth(300);
      editLinguisticVariableStage.setResizable(false);
      editLinguisticVariableStage.setScene(new Scene(fxmlEdit));
      editLinguisticVariableStage.initModality(Modality.WINDOW_MODAL);
      editLinguisticVariableStage.initOwner((Stage) nodesource.getScene().getWindow());
    }
    editLinguisticVariableStage.showAndWait();
  }

  public void actionButtonPressed(ActionEvent actionEvent) throws SQLException {
    Object source = actionEvent.getSource();
    if (!(source instanceof Button)) {
      return;
    }
    nodesource = (Node) actionEvent.getSource();
    Button clickedButton = (Button) source;

    switch (clickedButton.getId()) {
      case "btnAddVariable":
        LinguisticVariable linguisticVariable = new LinguisticVariable();
        linguisticVariable.setId(0);
        editLinguisticVariableController.setLinguisticVariable(linguisticVariable);
//        linguisticVariable = editLinguisticVariableController.getVariable();
//        variableList.add(linguisticVariable);
        showDialog();
        variableList.clear();
        tableViewVariables.getItems().clear();
        fillData();
        break;

      case "btnEditVariable":
        editLinguisticVariableController.setLinguisticVariable((LinguisticVariable) tableViewVariables.getSelectionModel().getSelectedItem());
        //variableList.clear();
        //fillData();
        showDialog();
        break;

      case "btnDeleteVariable":
        LinguisticVariable delVariable;
        delVariable = (LinguisticVariable)tableViewVariables.getSelectionModel().getSelectedItem();
        variableList.delete(delVariable);
        deleteVariable(delVariable);
        break;

        //webViewCV( "https://eclipse.org");
    }

  }

  private void deleteVariable(LinguisticVariable delVariable) throws SQLException {
    String query = "delete from cvdata.bmstu.linguisticvariables WHERE id = ?";
    try (PreparedStatement pstmt = PostgreSQLConnection.getConnection().prepareStatement(query)) {
      pstmt.setInt(1,delVariable.getId());
      pstmt.executeUpdate();
    }


  }


  private void fillData(){
    try(PreparedStatement statement = PostgreSQLConnection.getConnection().prepareStatement("select id, name, VALUE from cvdata.bmstu.linguisticvariables order by id")) {
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
