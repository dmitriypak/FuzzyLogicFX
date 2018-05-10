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
import javafx.scene.chart.AreaChart;
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
import ru.bmstu.edu.objects.enums.Variable;
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
  private ArrayList<LinguisticVariable> listInputVariables = DaoUtils.getInputVariables();
  private ArrayList<LinguisticVariable> listOutputVariables = DaoUtils.getOutputVariables();

  @FXML
  private void initialize(){
    DaoUtils.setupClearButtonField(txtPositionName);
    colPosition.setCellValueFactory(new PropertyValueFactory<CV,String>("positionname"));
    colSalary.setCellValueFactory(new PropertyValueFactory<CV,Integer>("salary"));
    colExperience.setCellValueFactory(new PropertyValueFactory<CV,String>("experience"));

    try {
      fxmlLoader.setLocation(getClass().getResource("../fxml/viewRules.fxml"));
      fxmlEdit = fxmlLoader.load();
      viewRulesController = fxmlLoader.getController();
    } catch (IOException e) {
      e.printStackTrace();
    }

  }
  private LineChart getLineChart(MembershipFunction mf){
    NumberAxis xAxis = new NumberAxis() ;
    NumberAxis yAxis = new NumberAxis(0,1,0) ;
    LineChart linechart = new LineChart(xAxis, yAxis) ;
    linechart.setTitle(mf.getNameMF());
    linechart.setMaxWidth(200);
    linechart.setMaxHeight(200);
    linechart.setMinHeight(100);
    linechart.setMinWidth(100);
    linechart.setOpacity(0.5);
    XYChart.Series series = new XYChart.Series();
    String value[] = mf.getParamValueMF().split(" ");
    series.setName(mf.getNameMF());
    System.out.println("MFname:" + mf.getNameMF());
    for(int i =0;i<value.length;i++){
      series.getData().add(new XYChart.Data(Double.valueOf(value[i]),i%2));
      //series.getData().add(new XYChart.Data(1,19));
      System.out.println(value[i]);
    }
    linechart.getData().add(series);

    return linechart;
  }


  private AreaChart getAreaChart(MembershipFunction mf, double param){
    NumberAxis xAxis = new NumberAxis() ;
    NumberAxis yAxis = new NumberAxis(0,1,0) ;
    AreaChart chart = new AreaChart(xAxis, yAxis) ;
    chart.setTitle(mf.getNameMF());
    chart.setMaxWidth(200);
    chart.setMaxHeight(200);
    chart.setMinHeight(100);
    chart.setMinWidth(100);
    chart.setCreateSymbols(false);
//    chart.getXAxis().setTickMarkVisible(false);
//    chart.getYAxis().setTickMarkVisible(false);
//    chart.getXAxis().setTickLabelsVisible(false);
//    chart.getXAxis().setTickLength(0);
//    chart.getYAxis().setTickLabelsVisible(false);
//    chart.setVerticalGridLinesVisible(false);
    XYChart.Series series = new XYChart.Series();
    XYChart.Series series1 = new XYChart.Series();
    XYChart.Series series2 = new XYChart.Series();
    String value[] = mf.getParamValueMF().split(" ");
    series.setName(mf.getNameMF());
    System.out.println("MFname:" + mf.getNameMF());
    for(int i =0;i<value.length;i++){
      series.getData().add(new XYChart.Data(Double.valueOf(value[i]),i%2));
    }
    series1.getData().add(new XYChart.Data(param,0));
    series1.getData().add(new XYChart.Data(param,1));


    if(param>=Double.valueOf(value[0]) && param<=Double.valueOf(value[1])){
      series2.getData().add(new XYChart.Data(Double.valueOf(value[0]),0));
      double y = getY(param,Double.valueOf(value[0]),Double.valueOf(value[1]),0,1);
      series2.getData().add(new XYChart.Data(param,y));
      double x = getX(y,Double.valueOf(value[1]),Double.valueOf(value[2]),1,0);
      series2.getData().add(new XYChart.Data(x,y));
      series2.getData().add(new XYChart.Data(Double.valueOf(value[2]),0));
    }else{
      if(param>=Double.valueOf(value[1]) && param<=Double.valueOf(value[2])){
        series2.getData().add(new XYChart.Data(Double.valueOf(value[2]),0));
        double y = getY(param,Double.valueOf(value[1]),Double.valueOf(value[2]),1,0);
        series2.getData().add(new XYChart.Data(param,y));
        double x = getX(y,Double.valueOf(value[0]),Double.valueOf(value[1]),0,1);
        series2.getData().add(new XYChart.Data(x,y));
        series2.getData().add(new XYChart.Data(Double.valueOf(value[0]),0));
      }
    }

    chart.getData().addAll(series,series2,series1);
    return chart;
  }


  private double getX(double y, double x1, double x2, double y1, double y2){
    double x = 0;
    x = (-y*(x2-x1)-(x1*y2-x2*y1))/(y1-y2);
    return x;
  }

  private double getY(double x, double x1, double x2, double y1, double y2){
    double y = 0;
    y = (-(x1*y2-x2*y1)-x*(y1-y2))/(x2-x1);
    return y;
  }

  private Label getLabel(String name){
    Label label = new Label(name);
    label.setFont(new Font("Verdana", 14));
    label.setTextAlignment(TextAlignment.CENTER);
    label.setTextFill(Paint.valueOf(String.valueOf(Color.BLUE)));
    label.setPadding(new Insets(25));
    //label.setMaxWidth(100);
    return label;
  }
  private void showDialog(CV cv) {

    if (cv!=null) {
      Stage viewRulesStage = new Stage();
      GridPane root = new GridPane();
      viewRulesStage.setTitle("Мамдани");
      viewRulesStage.setMinHeight(600);
      viewRulesStage.setMinWidth(800);
      viewRulesStage.setResizable(true);
      //FlowPane root = new FlowPane();
//      root.setMaxHeight(500);
      root.setMinWidth(300);
      ScrollPane scrollPane = new ScrollPane();

      if(cv.getId()!=0){
        //Входные переменные

        int j = 0;
        for(int i = 0;i<listInputVariables.size();i++){
          LinguisticVariable linguisticVariable = listInputVariables.get(i);
          //Название переменной
          StringBuilder nameVariable = new StringBuilder(linguisticVariable.getName());

          double param = 0;
          Variable variable = Variable.getVariableByName(nameVariable.toString());
          switch (variable) {
            case WORK_EXPERIENCE:
              param = cv.getExperience();
              break;
            case SALARY:
              param = cv.getSalary();
              break;

          }
          nameVariable.append("\n").append(param);
          Label label = getLabel(nameVariable.toString());
          Label label1 = getLabel(String.valueOf(param));
          //Построение графиков
          ArrayList<AreaChart> listAreaCharts = drawMFAreaGraph(linguisticVariable, param);
          System.out.println("Получен список графиков: " + listAreaCharts.size());
          root.add(label,i,0);

          if(listAreaCharts.size()>0){
            for(int k = 0;k<listAreaCharts.size();k++){
              root.add(listAreaCharts.get(k),i,k+2);
            }
          }

          j = i+1;
        }

        //Выходные переменные
        for(int i = 0;i<listOutputVariables.size();i++){
          double param = 0;
          LinguisticVariable linguisticVariable = listOutputVariables.get(i);
          Label label = getLabel(linguisticVariable.getName());
          ArrayList<AreaChart> listAreaCharts = drawMFAreaGraph(linguisticVariable,param);
          root.add(label,i+j,0);

          if(listAreaCharts.size()>0){
            for(int k = 0;k<listAreaCharts.size();k++){
              root.add(listAreaCharts.get(k),i+j,k+2);
            }
          }
        }


        scrollPane.setContent(root);
        scrollPane.setPannable(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        Scene scene = new Scene(scrollPane);
        scene.getStylesheets().add("ru/bmstu/edu/resources/css/chart.css");

        viewRulesStage.setScene(scene);
      }

      viewRulesStage.initModality(Modality.WINDOW_MODAL);
      viewRulesStage.initOwner((Stage) nodesource.getScene().getWindow());
      viewRulesStage.showAndWait();
    }

  }

  private ArrayList<LineChart> drawMFLineGraph(LinguisticVariable linguisticVariable) {
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

  private ArrayList<AreaChart> drawMFAreaGraph(LinguisticVariable linguisticVariable, double param) {
    ArrayList<AreaChart> listCharts = new ArrayList<>();
    ArrayList<MembershipFunction> listMF = linguisticVariable.getMfList();
    System.out.println("Список MF: " + listMF.size());
    System.out.println("Переменная: " + linguisticVariable.getName());

    if(listMF.size()>0){
      for(MembershipFunction mf:listMF){
        //Определение принадлежности
        String nameVariable = linguisticVariable.getName();
        AreaChart chart = getAreaChart(mf,param);
        listCharts.add(chart);
      }

    }
    return listCharts;
  }
  private void fillData(){
    CVList.clear();
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
