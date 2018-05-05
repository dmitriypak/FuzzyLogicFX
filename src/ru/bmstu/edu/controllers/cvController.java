package ru.bmstu.edu.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.controlsfx.control.textfield.CustomTextField;
import ru.bmstu.edu.DAO.PostgreSQLConnection;
import ru.bmstu.edu.objects.CV;
import ru.bmstu.edu.objects.LinguisticVariable;
import ru.bmstu.edu.objects.MembershipFunction;
import ru.bmstu.edu.objects.utils.DaoUtils;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


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
  @FXML
  private CustomTextField txtPositionName;
  private Node nodesource;

  private ObservableList<CV> CVList = FXCollections.observableArrayList();
  private Parent fxmlEdit;
  private FXMLLoader fxmlLoader = new FXMLLoader();
  private ViewRulesController viewRulesController;
  private Stage viewRulesStage;
  private ArrayList<LinguisticVariable> listInputVariables = DaoUtils.getInputVariables();
  private ArrayList<LinguisticVariable> listOutputVariables = DaoUtils.getOutputVariables();
  private GridPane root = new GridPane();
  @FXML
  private void initialize(){
    DaoUtils.setupClearButtonField(txtPositionName);
    colPosition.setCellValueFactory(new PropertyValueFactory<CV,String>("positionname"));
    colSalary.setCellValueFactory(new PropertyValueFactory<CV,Integer>("salary"));
    colExperience.setCellValueFactory(new PropertyValueFactory<CV,String>("experience"));

    try {
      fxmlLoader.setLocation(getClass().getResource("../fxml/ViewRules.fxml"));
      fxmlEdit = fxmlLoader.load();
      viewRulesController = fxmlLoader.getController();
    } catch (IOException e) {
      e.printStackTrace();
    }

  }
  private LineChart getLineChart(MembershipFunction mf){
    NumberAxis xAxis = new NumberAxis() ;
    NumberAxis yAxis = new NumberAxis() ;
    LineChart linechart = new LineChart(xAxis, yAxis) ;
    linechart.setTitle(mf.getMFname());
    linechart.setMaxWidth(100);
    linechart.setMaxHeight(100);

    XYChart.Series series = new XYChart.Series();
    String value[] = mf.getMFParamValue().split(" ");
    series.setName(mf.getMFname());
    System.out.println("MFname:" + mf.getMFname());
    for(int i =0;i<value.length;i++){
      series.getData().add(new XYChart.Data(Double.valueOf(value[i]),i%2));
      //series.getData().add(new XYChart.Data(1,19));
      System.out.println(value[i]);
    }
    linechart.getData().add(series);

    return linechart;
  }

  private Label getLabel(String name){
    Label label = new Label(name);
    label.setFont(new Font("Verdana", 14));
    label.setTextAlignment(TextAlignment.CENTER);
    label.setTextFill(Paint.valueOf(String.valueOf(Color.BLUE)));
    label.setPadding(new Insets(25));
    return label;
  }
  private void showDialog(CV cv) {
    if (viewRulesStage==null) {
      viewRulesStage = new Stage();
      viewRulesStage.setTitle("Мамдани");
      viewRulesStage.setMinHeight(600);
      viewRulesStage.setMinWidth(800);
      viewRulesStage.setResizable(true);
      //FlowPane root = new FlowPane();

      ScrollPane scrollPane = new ScrollPane();

      if(cv.getId()!=0){
        //Входные переменные

        int j = 0;
        for(int i = 0;i<listInputVariables.size();i++){
          LinguisticVariable linguisticVariable = listInputVariables.get(i);
          Label label = getLabel(linguisticVariable.getName());
          ArrayList<LineChart> listCharts = drawMFGraph(linguisticVariable);
          System.out.println("Получен список графиков: " + listCharts.size());
          root.add(label,i,0);

          if(listCharts.size()>0){
            for(int k = 0;k<listCharts.size();k++){
              try{
                root.add(listCharts.get(k),i,k+1);
              }catch (Exception ex){
                ex.printStackTrace();
              }

            }
          }
          j = i+1;
        }

        //Выходные переменные
        for(int i = 0;i<listOutputVariables.size();i++){
          Label label = getLabel(listOutputVariables.get(i).getName());

          root.add(label,i+j,0);
         // root.add(lineChart1,i+j,1);
          //root.getChildren().add(label);
        }

        scrollPane.setContent(root);
        scrollPane.setPannable(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        Scene scene = new Scene(scrollPane);

        viewRulesStage.setScene(scene);
      }

      viewRulesStage.initModality(Modality.WINDOW_MODAL);
      viewRulesStage.initOwner((Stage) nodesource.getScene().getWindow());
    }
    viewRulesStage.showAndWait();
  }

  private ArrayList<LineChart> drawMFGraph(LinguisticVariable linguisticVariable) {
    ArrayList<LineChart> listCharts = new ArrayList<>();
    ArrayList<MembershipFunction> listMF = linguisticVariable.getMfList();
    System.out.println("Список MF: " + listMF.size());
    System.out.println("Переменная: " + linguisticVariable.getName());

    if(listMF.size()>0){
      for(MembershipFunction mf:listMF){
        LineChart lineChart = getLineChart(mf);
        listCharts.add(lineChart);
      }

    }
    return listCharts;
  }


  private void fillData(){
    StringBuilder query = new StringBuilder("select id, positionname, salary, experience from cvdata.bmstu.CV");
    StringBuilder where = new StringBuilder("");
    if(!txtPositionName.getText().isEmpty()){
      if(where.toString().isEmpty()){
        where.append(" where ");
      }
      where.append(" positionname like '%"+ txtPositionName.getText()+"%'");
    }
    if(!where.toString().isEmpty()){
      query.append(where.toString());
    }

    query.append("order by id limit 1000;");
    System.out.println(query.toString());

    try(PreparedStatement statement = PostgreSQLConnection.getConnection().prepareStatement(query.toString())) {
      ResultSet rs = statement.executeQuery();
      while (rs.next()){
        CV cv = new CV();
        cv.setId(rs.getInt("id"));
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
  public void actionButtonPressed(ActionEvent actionEvent) {
    Object source = actionEvent.getSource();
    if (!(source instanceof Button)) {
      return;
    }
    nodesource = (Node) actionEvent.getSource();
    Button clickedButton = (Button) source;

    switch (clickedButton.getId()) {
      case "btnSearch":
        fillData();
        break;

      case "btnViewRules":
        CV cv = (CV) tableCV.getSelectionModel().getSelectedItem();




        viewRulesController.setCV((CV) tableCV.getSelectionModel().getSelectedItem());
        //viewRulesController.setStage((Stage) nodesource.getScene().getWindow());
        showDialog(cv);
        break;
    }

  }

}
