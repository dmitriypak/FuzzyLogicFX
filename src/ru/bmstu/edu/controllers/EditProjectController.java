package ru.bmstu.edu.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.json.simple.parser.ParseException;
import ru.bmstu.edu.DAO.PostgreSQLConnection;
import ru.bmstu.edu.objects.MembershipFunction;
import ru.bmstu.edu.objects.Project;
import ru.bmstu.edu.objects.Vacancy;
import ru.bmstu.edu.model.DaoUtils;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class EditProjectController {

  @FXML
  private TextField txtProjectName;
  @FXML
  private TextField txtProjectDescr;
  @FXML
  private Button btnClose;
  @FXML
  private Button btnEditVacancy;
  @FXML
  private TableView tableVacancy;
  @FXML
  private TableColumn colVacancyName;
  @FXML
  private TableColumn colVacancyWages;
  @FXML
  private TableColumn colAmountFree;
  @FXML
  private TableColumn colAmountTotal;
  @FXML
  private TableColumn colCategory;
  @FXML
  private Button btnSave;

  private Node nodesource;
  private Project project;
  private Parent fxmlEdit;
  private FXMLLoader fxmlLoader = new FXMLLoader();
  private Stage editVacancyStage;
  private EditVacancyController editVacancyController;
  private ObservableList<Vacancy> vacancyList = FXCollections.observableArrayList();

  @FXML
  public void initialize(){
    colVacancyName.setCellValueFactory(new PropertyValueFactory<MembershipFunction,String>("name"));
    colVacancyWages.setCellValueFactory(new PropertyValueFactory<MembershipFunction,String>("wages"));
    colAmountFree.setCellValueFactory(new PropertyValueFactory<MembershipFunction,String>("amountFree"));
    colAmountTotal.setCellValueFactory(new PropertyValueFactory<MembershipFunction,String>("amountTotal"));
    colCategory.setCellValueFactory(new PropertyValueFactory<MembershipFunction,String>("nameCategory"));

    tableVacancy.setOnMouseClicked( event -> {
      if( event.getClickCount() == 2 ) {
        btnEditVacancy.fire();
      }});


    try {
      fxmlLoader.setLocation(getClass().getResource("../fxml/editVacancy.fxml"));
      fxmlEdit = fxmlLoader.load();
      editVacancyController = fxmlLoader.getController();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void setProject(Project project) throws ParseException {
    if(project==null){
      return;
    }
    this.project=project;
    txtProjectName.setText(project.getName());
    txtProjectDescr.setText(project.getDescr());
    System.out.println("Получен проект: " + project.getId());

    if(project.getId()!=0){
      fillData();
    }else{
      vacancyList.clear();
    }
  }

  public Project getProject(){
    return project;
  }

  private void showDialog() {
    if (editVacancyStage==null) {
      editVacancyStage = new Stage();
      editVacancyStage.setTitle("Редактирование вакансии");
      editVacancyStage.setMinHeight(150);
      editVacancyStage.setMinWidth(300);
      editVacancyStage.setResizable(false);
      editVacancyStage.setScene(new Scene(fxmlEdit));
      editVacancyStage.initModality(Modality.WINDOW_MODAL);
      editVacancyStage.initOwner((Stage) nodesource.getScene().getWindow());
    }
    editVacancyStage.showAndWait();
  }



  public void actionButtonPressed(ActionEvent actionEvent) throws SQLException, ParseException {
    Object source = actionEvent.getSource();
    if (!(source instanceof Button)) {
      return;
    }
    nodesource = (Node) actionEvent.getSource();
    Button clickedButton = (Button) source;
    switch (clickedButton.getId()) {
      case "btnSaveProject":
        btnSave.fire();
        break;
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
        Vacancy vacancy = new Vacancy();
        vacancy.setId(0);
        vacancy.setIdProject(project.getId());
        editVacancyController.setVacancy(vacancy);
        showDialog();
        vacancy = editVacancyController.getVacancy();
        vacancyList.add(vacancy);
        //tableVacancy.setItems(vacancyList);
        break;
      case "btnEditVacancy":
        editVacancyController.setVacancy((Vacancy)tableVacancy.getSelectionModel().getSelectedItem());
        showDialog();
        break;
      case "btnDeleteVacancy":
        break;
    }

  }

  public void actionSave(ActionEvent actionEvent) {

  }


  private void fillData() throws ParseException {
    if(project==null) return;
    ArrayList<Vacancy> list = DaoUtils.getVacanciesList(project.getId());
    if(list!=null || list.size()>0 ){
      vacancyList = FXCollections.observableArrayList(list);
      tableVacancy.setItems(vacancyList);
    }
  }

  private void updateProject(Project project) throws SQLException {
    String query = "update cvdata.bmstu.projects "
        + " set name = ?, descr = ?  WHERE id = ?";
    try (PreparedStatement pstmt = PostgreSQLConnection.getConnection().prepareStatement(query)) {
      int i = 0;
      pstmt.setString(++i,txtProjectName.getText());
      pstmt.setString(++i,txtProjectDescr.getText());
      pstmt.setInt(++i,project.getId());

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
