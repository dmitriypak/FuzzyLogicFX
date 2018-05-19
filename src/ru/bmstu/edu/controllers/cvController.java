package ru.bmstu.edu.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.controlsfx.control.textfield.CustomTextField;
import org.json.simple.parser.ParseException;
import ru.bmstu.edu.DAO.PostgreSQLConnection;
import ru.bmstu.edu.objects.*;
import ru.bmstu.edu.objects.enums.Variable;
import ru.bmstu.edu.objects.utils.DaoUtils;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;


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
  @FXML
  private Button btnViewRules;


  private Node nodesource;

  private ObservableList<CV> CVList = FXCollections.observableArrayList();
  private Parent fxmlEdit;
  private FXMLLoader fxmlLoader = new FXMLLoader();
  private ViewRulesController viewRulesController;
  private ArrayList<LinguisticVariable> listInputVariables = DaoUtils.getInputVariables();
  private ArrayList<LinguisticVariable> listOutputVariables = DaoUtils.getOutputVariables();
  private Map<String,LinguisticVariable> mapInputVariables = DaoUtils.getMapInputVariables();
  private Map<String,LinguisticVariable> mapOutputVariables = DaoUtils.getMapOutputVariables();

  private ArrayList<Rule> listRules = DaoUtils.getRules();

  public cvController() throws ParseException {
  }

  @FXML
  private void initialize(){
    DaoUtils.setupClearButtonField(txtPositionName);
    colPosition.setCellValueFactory(new PropertyValueFactory<CV,String>("positionname"));
    colSalary.setCellValueFactory(new PropertyValueFactory<CV,Integer>("salary"));
    colExperience.setCellValueFactory(new PropertyValueFactory<CV,String>("experience"));

    tableCV.setOnMouseClicked( event -> {
      if( event.getClickCount() == 2 ) {
        btnViewRules.fire();
      }});



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
    double maxValue = 0;
//    chart.getXAxis().setTickMarkVisible(false);
//    chart.getYAxis().setTickMarkVisible(false);
//    chart.getXAxis().setTickLabelsVisible(false);
//    chart.getXAxis().setTickLength(0);
//    chart.getYAxis().setTickLabelsVisible(false);
//    chart.setVerticalGridLinesVisible(false);
    XYChart.Series series = new XYChart.Series();
    XYChart.Series series1 = new XYChart.Series();
    XYChart.Series series2 = new XYChart.Series();
    //Кол-во точек
    String value[] = mf.getParamValueMF().split(" ");
    System.out.println("Получен массив длины " + value.length + " " + mf.getParamValueMF());
    series.setName(mf.getNameMF());
    System.out.println("MFname:" + mf.getNameMF());
    switch (value.length){
      case 3:
        for(int i =0;i<value.length;i++){
          double val = Double.valueOf(value[i]);
          series.getData().add(new XYChart.Data(Double.valueOf(value[i]),i%2));
          if(val>maxValue){
            maxValue = val;
          }
        }

        break;
      case 4:
        for(int i=0;i<value.length;i++){
          double val = Double.valueOf(value[i]);
          System.out.println("val " + val);
          if(i==0 || i==3){
            series.getData().add(new XYChart.Data(val,0));
          }else{
            series.getData().add(new XYChart.Data(val,1));
          }
          if(val>maxValue){
            maxValue = val;
          }
        }
        break;
    }
    NumberAxis xAxis;
    if(maxValue <= 1){
      xAxis = new NumberAxis(0,1,0) ;
    }else{
      xAxis = new NumberAxis();
    }

    NumberAxis yAxis = new NumberAxis(0,1,0) ;
    AreaChart chart = new AreaChart(xAxis, yAxis) ;
    chart.setTitle(mf.getNameMF());
    chart.setMaxWidth(200);
    chart.setMaxHeight(150);
    chart.setMinHeight(100);
    chart.setMinWidth(100);
    chart.setCreateSymbols(false);

    series1.getData().add(new XYChart.Data(param,0));
    series1.getData().add(new XYChart.Data(param,1));

    //Красная линия
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

  private TextArea getTextArea(String name){
    TextArea textArea = new TextArea(name);
    textArea.setFont(new Font("Verdana", 14));
    textArea.setPadding(new Insets(25));
    //label.setMaxWidth(100);
    return textArea;
  }


  private Slider getSlider(int min,int max, double value){
    Slider slider = new Slider();

    double increment = 0;

    if (max==1){
      increment = 0.1;
    }
    if(max<=100){
      increment = 10;
    }

    if(max<=1000){
      increment = 100;
    }

    if(max<=10000){
      increment = 1000;
    }

    if(max<=100000){
      increment = 10000;
    }

    if(max<=1000000){
      increment = 100000;
    }

    slider.setMaxWidth(150);
    slider.setMinWidth(150);
    slider.setMin(min);
    slider.setMax(max);
    slider.setValue(value);
    slider.setShowTickLabels(true);
    slider.setShowTickMarks(true);
    slider.setMajorTickUnit(increment);
    slider.setBlockIncrement(increment);

    return slider;
  }


//  private Region getSlider(int min,int max, double value){
//    HBox box = new HBox(10);
//    box.setAlignment(Pos.CENTER);
//    //box.setPadding(new Insets(20,0,0,20));
//    box.setFillHeight(false);
//
//
//    Slider slider = new Slider();
//
//    final TextField valueField = new TextField();
//    valueField.textProperty().bind(slider.valueProperty().asString("%.2f"));
//
//    double increment = 0;
//
//    if (max==1){
//      increment = 0.1;
//    }
//    if(max<=100){
//      increment = 10;
//    }
//
//    if(max<=1000){
//      increment = 100;
//    }
//
//    if(max<=10000){
//      increment = 1000;
//    }
//
//    if(max<=100000){
//      increment = 10000;
//    }
//
//    if(max<=1000000){
//      increment = 100000;
//    }
//
//    slider.setMaxWidth(150);
//    slider.setMinWidth(150);
//    slider.setMin(min);
//    slider.setMax(max);
//    slider.setValue(value);
//    slider.setShowTickLabels(true);
//    slider.setShowTickMarks(true);
//    slider.setMajorTickUnit(increment);
//    slider.setBlockIncrement(increment);
//    box.getChildren().addAll(valueField, slider);
//    return box;
//  }


  private Region getLabel(String name){
    HBox box = new HBox(10);
    box.setAlignment(Pos.CENTER);
    //box.setPadding(new Insets(20,0,0,20));
    box.setFillHeight(false);

    Label label = new Label(name);
    label.setFont(new Font("Verdana", 14));
    label.setTextAlignment(TextAlignment.CENTER);
    label.setTextFill(Paint.valueOf(String.valueOf(Color.BLUE)));
    label.setPadding(new Insets(10,0,10,0));
    label.setMaxWidth(250);
    box.getChildren().addAll(label);
    return box;
  }
  private void showDialog(CV cv) {

    if (cv!=null) {
      Stage viewRulesStage = new Stage();
      GridPane root = new GridPane();
      root.setAlignment(Pos.CENTER);
      //root.setGridLinesVisible(true);
      viewRulesStage.setTitle("Мамдани");
      viewRulesStage.setMinHeight(600);
      viewRulesStage.setMinWidth(800);
      viewRulesStage.setResizable(true);
      //FlowPane root = new FlowPane();
//      root.setMaxHeight(500);
      root.setMinWidth(300);
      ScrollPane scrollPane = new ScrollPane();

      if(cv.getId()==0) return;
      int rowIndex = 0;
      //Основной цикл по переменным
      for(int i = 0;i<listInputVariables.size();i++){

        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setMinWidth(250);
        columnConstraints.setMaxWidth(250);
        root.getColumnConstraints().add(columnConstraints);

        LinguisticVariable linguisticVariable = listInputVariables.get(i);
        Variable variable1 = Variable.getVariableByName(linguisticVariable.getName());
        //Имя переменной
        Region labelNameVariable = getLabel(linguisticVariable.getName());
        root.add(labelNameVariable,i,rowIndex);

        double min = 0;
        double max = 0;
        double value = 0;

        ArrayList<MembershipFunction> mfList = linguisticVariable.getMfList();
        for(int k = 0;k<mfList.size();k++){
          MembershipFunction mf = mfList.get(k);
          String[] params = mf.getParamValueMF().split(" ");

          for(int l = 0;l<params.length;l++){
            double paramValue = Double.valueOf(params[l]);
            System.out.println("Params " + paramValue);
            if(paramValue < min){
              min = paramValue;
            }
            if(paramValue > max){
              max = paramValue;
            }
          }
        }
        double param1 = 0;
        rowIndex += 1;
        switch (variable1){
          case WORK_EXPERIENCE:
            param1 = cv.getExperience();
            Region labelWorkExperience = getLabel(String.valueOf(param1));
            root.add(labelWorkExperience,i,rowIndex);
            break;
          case SALARY:
            param1 = cv.getSalary();
            Region labelSalary = getLabel(String.valueOf(param1));
            root.add(labelSalary,i,rowIndex);
            break;
          case POSITION:
            param1 = 0.5;
            Region labelPosition = getLabel(String.valueOf(param1));
            root.add(labelPosition,i,rowIndex);

            break;
        }

        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        Slider slider = getSlider((int)min,(int) max,param1);
        hBox.getChildren().add(slider);
        TextField valueField = new TextField();
        valueField.setFont(new Font("Verdana", 14));
        valueField.setAlignment(Pos.CENTER);
        valueField.setId("slider"+linguisticVariable.getName());
        valueField.textProperty().bind(slider.valueProperty().asString("%.2f"));
        root.add(valueField,i,++rowIndex);
        root.add(hBox,i,++rowIndex);

        //Цикл по правилам
        for(int j = 0;j<listRules.size();j++){
          rowIndex += 1;
          Rule rule = listRules.get(j);

          System.out.println("idRule: " + rule.getIdRule());
          Map<String,Condition> mapIF = rule.getIFConditionMap();
          Map<String,Condition> mapAND = rule.getANDConditionMap();
          Map<String,Condition> mapTHEN = rule.getTHENConditionMap();
          double param = 0;
          //Переменная IF
          for(Map.Entry<String, Condition> entry:mapIF.entrySet()){
            LinguisticVariable ifVariable =  mapInputVariables.get(entry.getKey());
            System.out.println("Получена переменная if " + ifVariable.getName());
            Condition ifCondition = entry.getValue();
            MembershipFunction ifMF = ifCondition.getMembershipFunction();
            System.out.println("Получено condition if " + ifMF.getNameMF());

            StringBuilder params = new StringBuilder();
            Variable variable2 = Variable.getVariableByName(ifVariable.getName());

            if(variable1.equals(variable2)){

              switch (variable2) {
                case WORK_EXPERIENCE:

                  //Построение графика
                  AreaChart chartExperience = getAreaChart(ifMF,param);
                  root.add(chartExperience,i,rowIndex+1);
                  break;
                case SALARY:

                  //Построение графика
                  AreaChart chartSalary = getAreaChart(ifMF,param);
                  root.add(chartSalary,i,rowIndex+1);

                  break;
                case POSITION:
                  //Построение графика
                  AreaChart chartPosition = getAreaChart(ifMF,param);
                  root.add(chartPosition,i,rowIndex+1);
                  break;
              }
            }
          }
        }
        rowIndex = 0;

      }


      //Выходные переменные
//      for(int i = 0;i<listOutputVariables.size();i++){
//        double param = 0;
//        LinguisticVariable linguisticVariable = listOutputVariables.get(i);
//        Label label = getLabel(linguisticVariable.getName());
//        ArrayList<AreaChart> listAreaCharts = drawMFAreaGraph(linguisticVariable,param);
//        root.add(label,i+j,0);
//
//        if(listAreaCharts.size()>0){
//          for(int k = 0;k<listAreaCharts.size();k++){
//            root.add(listAreaCharts.get(k),i+j,k+2);
//          }
//        }
//      }


      scrollPane.setContent(root);
      scrollPane.setPannable(true);
      scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
      scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
      Scene scene = new Scene(scrollPane);
      scene.getStylesheets().add("ru/bmstu/edu/resources/css/chart.css");

      viewRulesStage.setScene(scene);


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
