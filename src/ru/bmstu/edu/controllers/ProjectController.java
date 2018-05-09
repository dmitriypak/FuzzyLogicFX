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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ru.bmstu.edu.objects.Project;
import ru.bmstu.edu.objects.utils.DaoUtils;

import java.io.IOException;

/**
 * Created by HP on 21.05.2017.
 */
public class ProjectController {
    @FXML
    private TableView tableProjects;
    @FXML
    private TableColumn colProjectId;
    @FXML
    private TableColumn colProjectName;
    @FXML
    private TableColumn colProjectDescr;
    @FXML
    private TableColumn colVacancyList;

    @FXML
    private Button btnAddProject;
    @FXML
    private Button btnEditProject;
    @FXML
    private Button btnDeleteProject;

    private Parent fxmlEdit;
    private FXMLLoader fxmlLoader = new FXMLLoader();
    private EditProjectController editProjectController;
    private Stage editProjectStage;
    private Node nodesource;
    private ObservableList<Project> projectsList;

    @FXML
    public void initialize() {

      initLoader();
      tableProjects.setOnMouseClicked( event -> {
        if( event.getClickCount() == 2 ) {
          btnEditProject.fire();
        }});
    }

    private void initLoader(){
        try {
            fxmlLoader.setLocation(getClass().getResource("../fxml/editProject.fxml"));
            fxmlEdit = fxmlLoader.load();
            editProjectController = fxmlLoader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }

        colProjectName.setCellValueFactory(new PropertyValueFactory<Project,String>("name"));
        colProjectDescr.setCellValueFactory(new PropertyValueFactory<Project,String>("descr"));

        fillData();

    }


public void actionButtonPressed(ActionEvent actionEvent) {


    Object source = actionEvent.getSource();
    if (!(source instanceof Button)) {
        return;
    }
    nodesource = (Node) actionEvent.getSource();
    Button clickedButton = (Button) source;

    switch (clickedButton.getId()) {
        case "btnAddProject":
          Project project = new Project();
          editProjectController.setProject(project);
          project = editProjectController.getProject();
          projectsList.add(project);

          showDialog();
          break;
        case "btnEditProject":
          showDialog();
          break;
        case "btnDeleteProject":
            break;

    }
}

//        Object source = actionEvent.getSource();
//        if (!(source instanceof Button)) {
//            return;
//        }
//        nodesource = (Node) actionEvent.getSource();
//        Button clickedButton = (Button) source;
//
//        switch (clickedButton.getId()) {
//            case "btnAddProject":

//                showDialog();
//                break;
//
//            case "btnEditProject":
//                editProjectController.setProject((Project) tableProjects.getSelectionModel().getSelectedItem());
//                showDialog();
//                break;
//
//            case "btnDeleteProject":
//                Project delproject;
//                delproject = (Project) tableProjects.getSelectionModel().getSelectedItem();
//                comboProjectArrayList.remove(tableProjects.getSelectionModel().getSelectedIndex());
//                projectArrayList.remove(delproject);
//                projectsListImpl.delete(delproject);
//                deleteProject(delproject);
//                break;
//        }
//


    private void showDialog() {
        if (editProjectStage==null) {
            editProjectStage = new Stage();
            editProjectStage.setTitle("Редактирование записи");
            editProjectStage.setMinHeight(150);
            editProjectStage.setMinWidth(300);
            editProjectStage.setResizable(false);
            editProjectStage.setScene(new Scene(fxmlEdit));
            editProjectStage.initModality(Modality.WINDOW_MODAL);
            editProjectStage.initOwner((Stage) nodesource.getScene().getWindow());
        }
        editProjectStage.showAndWait();
    }
//
//    private void deleteProject(Project project){
//        try {
//            CallableStatement call = MSSQLConnection.getConnection().prepareCall("{call dbo.deleteProject(?)}");
//            call.setString("id",project.getId());
//            call.executeUpdate();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
    private void fillData(){
//      projectsList.clear();
      projectsList = FXCollections.observableArrayList(DaoUtils.getProjectsList());
      tableProjects.setItems(projectsList);
    }

}