package ru.bmstu.edu.controllers;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.textfield.CustomTextField;
import org.json.simple.parser.ParseException;
import ru.bmstu.edu.DAO.PostgreSQLConnection;
import ru.bmstu.edu.objects.*;
import ru.bmstu.edu.objects.enums.MFname;
import ru.bmstu.edu.objects.enums.Variable;
import ru.bmstu.edu.objects.fuzzy.Mamdani;
import ru.bmstu.edu.objects.fuzzy.Sugeno;
import ru.bmstu.edu.objects.utils.DaoUtils;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class cvController {
  @FXML
  private TableColumn colPosition;
  @FXML
  private TableColumn colSalary;
  @FXML
  private TableColumn colExperience;
  @FXML
  private TableColumn colCategory;
  @FXML
  private TableView tableCV;
  @FXML
  private CustomTextField txtPositionName;
  @FXML
  private Button btnViewRules;
  @FXML
  private ComboBox comboTypeOutput;

  private Node nodesource;

  private ObservableList<CV> CVList = FXCollections.observableArrayList();
  private Parent fxmlEdit;
  private FXMLLoader fxmlLoader = new FXMLLoader();
  private ViewRulesController viewRulesController;
//  private ArrayList<LinguisticVariable> listInputVariables = DaoUtils.getInputVariables();
//  private ArrayList<LinguisticVariable> listOutputVariables = DaoUtils.getOutputVariables();
  private ArrayList<LinguisticVariable> listVariables = DaoUtils.getVariables();
  private Map<String,LinguisticVariable> mapInputVariables = DaoUtils.getMapInputVariables();
  private Map<String,LinguisticVariable> mapOutputVariables = DaoUtils.getMapOutputVariables();
  private Scene scene;
  private static Map<Integer,Rule> mapRules;
  private double accumulationResult;
  static {
    try {
      mapRules = DaoUtils.getMapRules();
    } catch (ParseException e) {
      e.printStackTrace();
    }
  }

  private Timeline timeline;
  private Map<String,Double> mapLabelValues = new LinkedHashMap<>();
  private static String totalRank = "";
  private static List<MembershipFunction> mfList;


  public cvController() throws ParseException {
  }

  @FXML
  private void initialize(){
    DaoUtils.setupClearButtonField(txtPositionName);
    colPosition.setCellValueFactory(new PropertyValueFactory<CV,String>("positionname"));
    colSalary.setCellValueFactory(new PropertyValueFactory<CV,Integer>("salary"));
    colExperience.setCellValueFactory(new PropertyValueFactory<CV,String>("experience"));
    colCategory.setCellValueFactory(new PropertyValueFactory<CV,String>("categoryName"));

    tableCV.setOnMouseClicked( event -> {
      if( event.getClickCount() == 2 ) {
        btnViewRules.fire();
      }});

    createComboBox();

    try {
      fxmlLoader.setLocation(getClass().getResource("../fxml/viewRules.fxml"));
      fxmlEdit = fxmlLoader.load();
      viewRulesController = fxmlLoader.getController();
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  private void createComboBox(){
    ObservableList<String> typeOutput = FXCollections.observableArrayList(
        "Mamdani",
        "Sugeno");
    comboTypeOutput.setItems(typeOutput);
    comboTypeOutput.setValue(typeOutput.get(0));
  }



  //Степень уверенности
  private double getStValue(MembershipFunction mf, double param, LinguisticVariable variable) {
    double stValue = 0;
    if(param>0) {
      stValue = Math.round(MFType.getTriangleMF(variable.getMfList(), param, mf.getCodeMF()) * 100.0) / 100.0;
    }
    return stValue;
  }


  private double getRank(CV cv) {
    double rank = 0;
    if (cv!=null) {
      for(int i = 0;i<listVariables.size();i++){

        LinguisticVariable linguisticVariable = listVariables.get(i);
        int variableID = linguisticVariable.getId();

        Variable variable1 = Variable.getVariableByName(linguisticVariable.getName());

        //Цикл по правилам
        for(Map.Entry<Integer, Rule> entryRule:mapRules.entrySet()){
          Rule rule = entryRule.getValue();
          int ruleID = rule.getIdRule();
          Map<String,Condition> mapIF = rule.getIFConditionMap();
          Map<String,Condition> mapAND = rule.getANDConditionMap();
          double param = 0;
          //Переменная IF
          for(Map.Entry<String, Condition> entry:mapIF.entrySet()){
            LinguisticVariable ifVariable =  mapInputVariables.get(entry.getKey());
            Condition ifCondition = entry.getValue();
            MembershipFunction ifMF = ifCondition.getMembershipFunction();
            Variable variable2 = Variable.getVariableByName(ifVariable.getName());

            if(variable1.equals(variable2)){
              switch (variable2) {
                case WORK_EXPERIENCE:
                  double stWorkExperience = getStValue(ifMF,cv.getExperience(),linguisticVariable);
                  mapLabelValues.put("label"+variableID + "_"+ruleID+"_"+ifMF.getCodeMF(),stWorkExperience);
                  break;
                case SALARY:
                  double stSalary = getStValue(ifMF,cv.getSalary(),linguisticVariable);
                  mapLabelValues.put("label"+variableID + "_"+ruleID+"_"+ifMF.getCodeMF(),stSalary);
                  break;
                case POSITION:
                  param = 0.5;
                  double stPosition = getStValue(ifMF,param,linguisticVariable);
                  break;
              }
            }
          }

          //Переменная AND
          for(Map.Entry<String, Condition> entry:mapAND.entrySet()){
            LinguisticVariable andVariable =  mapInputVariables.get(entry.getKey());
            Condition andCondition = entry.getValue();
            MembershipFunction andMF = andCondition.getMembershipFunction();
            Variable variable3 = Variable.getVariableByName(andVariable.getName());

            if(variable1.equals(variable3)){
              switch (variable3) {
                case WORK_EXPERIENCE:
                  double stWorkExperience = getStValue(andMF,cv.getExperience(),linguisticVariable);
                  mapLabelValues.put("label"+variableID + "_"+ruleID+"_"+andMF.getCodeMF(),stWorkExperience);
                  break;
                case SALARY:
                  double stSalary = getStValue(andMF,cv.getSalary(),linguisticVariable);
                  mapLabelValues.put("label"+variableID + "_"+ruleID+"_"+andMF.getCodeMF(),stSalary);
                  break;
                case POSITION:
                  param = 0.5;
                  double stPosition = getStValue(andMF,param,linguisticVariable);
                  break;
              }
            }
          }
        }
      }


      //Цикл по правилам
      for(Map.Entry<Integer, Rule> entryRule:mapRules.entrySet()) {
        Rule rule = entryRule.getValue();
        int ruleID = rule.getIdRule();
        double valueCategory = 0;

        ArrayList<Double> valueList = new ArrayList<Double>();
        Pattern pattern = Pattern.compile("_(.*?)_");
        for (Map.Entry<String, Double> entryLabel : mapLabelValues.entrySet()) {
          Matcher matcher = pattern.matcher(entryLabel.getKey());
          if (matcher.find()) {
            int id = Integer.valueOf(matcher.group(1));
            if (id == ruleID) {
              valueList.add(entryLabel.getValue());
            }
          }
        }

        // Расчет степени уверенности
        valueCategory = Mamdani.getAggregationResult(valueList);
        //Rule ruleOutput = mapRules.get(ruleID);
        if (rule != null) {
          rule.setValueOutput(valueCategory);
          mapRules.put(ruleID, rule);
        }

        //Вывод COG
        rank = Math.round(Mamdani.getCenterOfGravity(mapRules) * 100.0) / 100.0;

      }

    }
    return rank;
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
    for(int i =0;i<value.length;i++){
      series.getData().add(new XYChart.Data(Double.valueOf(value[i]),i%2));
    }
    linechart.getData().add(series);

    return linechart;
  }


  private AreaChart getEmptyAreaChart(){
    final NumberAxis xAxis = new NumberAxis(0,1,0) ;
    final NumberAxis yAxis = new NumberAxis(0,1,0) ;
    AreaChart<Number,Number> chart = new AreaChart<Number,Number>(xAxis, yAxis) ;
    chart.setMaxWidth(200);
    chart.setMaxHeight(150);
    chart.setMinHeight(100);
    chart.setMinWidth(100);
    chart.setCreateSymbols(false);
    chart.setAnimated(false);
    return chart;
  }

  private StackPane getAreaChart(MembershipFunction mf, double param, int ruleID, int variableID, LinguisticVariable variable){
    StackPane stack = new StackPane();
    String textFieldName = "#textField"+variableID;
    double maxValue = 0;

    Label stLabel = getLabel();
    String nameLabel ="label"+variableID + "_"+ruleID+"_"+mf.getCodeMF();
    stLabel.setId(nameLabel);
    stLabel.setMaxWidth(215);
    stLabel.setMinWidth(50);

//    chart.getXAxis().setTickMarkVisible(false);
//    chart.getYAxis().setTickMarkVisible(false);
//    chart.getXAxis().setTickLabelsVisible(false);
//    chart.getXAxis().setTickLength(0);
//    chart.getYAxis().setTickLabelsVisible(false);
//    chart.setVerticalGridLinesVisible(false);
    XYChart.Series<Number,Number> series = new XYChart.Series<Number,Number>();
    XYChart.Series<Number,Number> series1 = new XYChart.Series<Number,Number>();
    XYChart.Series<Number,Number> series2 = new XYChart.Series<Number,Number>();

    //Кол-во точек
    String value[] = mf.getParamValueMF().split(" ");
    series.setName(mf.getNameMF());
    switch (value.length){
      case 3:
        for(int i =0;i<value.length;i++){
          double val = Double.valueOf(value[i]);
          series.getData().add(new XYChart.Data<Number,Number>(Double.valueOf(value[i]),i%2));
          if(val>maxValue){
            maxValue = val;
          }
        }

        break;
      case 4:
        for(int i=0;i<value.length;i++){
          double val = Double.valueOf(value[i]);
          if(i==0 || i==3){
            series.getData().add(new XYChart.Data<Number,Number>(val,0));
          }else{
            series.getData().add(new XYChart.Data<Number,Number>(val,1));
          }
          if(val>maxValue){
            maxValue = val;
          }
        }
        break;
    }
    final NumberAxis xAxis;
    if(maxValue <= 1){
      xAxis = new NumberAxis(0,1,0) ;
    }else{
      xAxis = new NumberAxis();
    }

    final NumberAxis yAxis = new NumberAxis(0,1,0) ;
    AreaChart<Number,Number> chart = new AreaChart<Number,Number>(xAxis, yAxis) ;
    chart.setTitle(mf.getNameMF());
    chart.setMaxWidth(200);
    chart.setMaxHeight(150);
    chart.setMinHeight(100);
    chart.setMinWidth(100);
    chart.setCreateSymbols(false);
    chart.setId("chart"+variableID+ruleID+mf.getCodeMF());
    chart.setAnimated(false);

    //Красная линия
    series1.getData().add(new XYChart.Data<Number,Number>(param,0));
    series1.getData().add(new XYChart.Data<Number,Number>(param,1));

    timeline = new Timeline();
    timeline.getKeyFrames().add(new KeyFrame(Duration.millis(500), new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent actionEvent) {
        double newValue = 0;
        for (XYChart.Data<Number, Number> data : series1.getData()) {
          TextField textField = (TextField) scene.lookup(textFieldName);

          if(textField!=null) {
            newValue = Double.valueOf(textField.getText().replace(",", "."));
            data.setXValue(newValue);
          }
        }
        // Расчет степени уверенности

        Label label = (Label) scene.lookup("#label"+variableID+"_"+ruleID+"_"+mf.getCodeMF());
        series2.getData().clear();
        switch (value.length) {
          case 3:
            double stValue = Math.round(MFType.getTriangleMF(variable.getMfList(),newValue,mf.getCodeMF()) * 100.0) / 100.0;
            //double stValue = MFType.getTriangleMF(variable.getMfList(),newValue,mf.getCodeMF());
            label.setText(String.valueOf(stValue));
            mapLabelValues.put(nameLabel,stValue);

            //Заливка
            if (newValue >= Double.valueOf(value[0]) && newValue <= Double.valueOf(value[1])) {
              series2.getData().add(new XYChart.Data<Number, Number>(Double.valueOf(value[0]), 0));
              double y = getY(newValue, Double.valueOf(value[0]), Double.valueOf(value[1]), 0, 1);
              series2.getData().add(new XYChart.Data<Number, Number>(newValue, y));
              double x = getX(y, Double.valueOf(value[1]), Double.valueOf(value[2]), 1, 0);
              series2.getData().add(new XYChart.Data<Number, Number>(x, y));
              series2.getData().add(new XYChart.Data<Number, Number>(Double.valueOf(value[2]), 0));
            } else {
              if (newValue >= Double.valueOf(value[1]) && newValue <= Double.valueOf(value[2])) {
                series2.getData().add(new XYChart.Data<Number, Number>(Double.valueOf(value[2]), 0));
                double y = getY(newValue, Double.valueOf(value[1]), Double.valueOf(value[2]), 1, 0);
                series2.getData().add(new XYChart.Data(newValue, y));
                double x = getX(y, Double.valueOf(value[0]), Double.valueOf(value[1]), 0, 1);
                series2.getData().add(new XYChart.Data<Number, Number>(x, y));
                series2.getData().add(new XYChart.Data<Number, Number>(Double.valueOf(value[0]), 0));
              }
            }


            break;
          case 4:
            double stValueT = Math.round(MFType.getTrapMF(variable.getMfList(),newValue,mf.getCodeMF()) * 100.0) / 100.0;
            //double stValueT =MFType.getTrapMF(variable.getMfList(),newValue,mf.getCodeMF()) ;
            label.setText(String.valueOf(stValueT));
            mapLabelValues.put(nameLabel,stValueT);

            if(Double.valueOf(value[0])==0){
              //Заливка  0 0 0.1 0.2
              series2.getData().add(new XYChart.Data<Number, Number>(Double.valueOf(value[0]), 0));
              double y = stValueT;
              series2.getData().add(new XYChart.Data<Number, Number>(Double.valueOf(value[1]), y));
              double x = getX(y, Double.valueOf(value[2]), Double.valueOf(value[3]), 1, 0);
              series2.getData().add(new XYChart.Data<Number, Number>(x, y));
              series2.getData().add(new XYChart.Data<Number, Number>(Double.valueOf(value[3]), 0));
            }else{
              //Заливка  0.75 0.9 1 1
              series2.getData().add(new XYChart.Data<Number, Number>(Double.valueOf(value[0]), 0));
              double y = getY(newValue, Double.valueOf(value[0]), Double.valueOf(value[1]), 0, 1);
              series2.getData().add(new XYChart.Data<Number, Number>(newValue, y));
              double x = getX(y, Double.valueOf(value[2]), Double.valueOf(value[3]), 1, 0);
              series2.getData().add(new XYChart.Data<Number, Number>(x, y));
              series2.getData().add(new XYChart.Data<Number, Number>(Double.valueOf(value[3]), 0));
            }

            break;
        }
      }
    }));
    timeline.setCycleCount(Animation.INDEFINITE);
    timeline.setAutoReverse(true);
    timeline.play();
    chart.getData().addAll(series,series2,series1);


    stack.getChildren().addAll(chart,stLabel);
    return stack;
  }



  //Выходной график Сугено

  private StackPane getOutputAreaChartSugeno(MembershipFunction mf, double param, int ruleID, int variableID){
    StackPane stack = new StackPane();
    Label stLabel = getLabel();
    stLabel.setId("label"+variableID+ ruleID+mf.getCodeMF());
    stLabel.setMaxWidth(215);
    stLabel.setMinWidth(50);

    String textFieldName = "#textField"+variableID;
    double constantValue = mf.getConstantSugeno();

    System.out.println("Constant " + constantValue);
    XYChart.Series<Number,Number> series = new XYChart.Series<Number,Number>();
    XYChart.Series<Number,Number> series2 = new XYChart.Series<Number,Number>();
    XYChart.Series<Number,Number> series0 = new XYChart.Series<Number,Number>();
    XYChart.Series<Number,Number> series1 = new XYChart.Series<Number,Number>();

    final NumberAxis xAxis = new NumberAxis(0,1,0) ;
    final NumberAxis yAxis = new NumberAxis(0,1,0) ;

    AreaChart<Number,Number> chart = new AreaChart<Number,Number>(xAxis, yAxis) ;
    chart.setTitle(mf.getNameMF());
    chart.setMaxWidth(200);
    chart.setMaxHeight(150);
    chart.setMinHeight(100);
    chart.setMinWidth(100);
    chart.setCreateSymbols(false);
    chart.setId("chart"+variableID+ruleID+mf.getCodeMF());
    chart.setAnimated(false);


    //Синяя линия
    series.getData().add(new XYChart.Data<Number, Number>(constantValue, 0));
    series.getData().add(new XYChart.Data<Number, Number>(constantValue, 1));

    Timeline timeline = new Timeline();
    timeline.getKeyFrames().add(new KeyFrame(Duration.millis(500), new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent actionEvent) {
        double valueCategory = 0;

        ArrayList<Double> valueList = new ArrayList<Double>();
        Pattern pattern = Pattern.compile("_(.*?)_");
        for(Map.Entry<String,Double> entryLabel:mapLabelValues.entrySet()){
          Matcher matcher = pattern.matcher(entryLabel.getKey());
          if (matcher.find()) {
            int id = Integer.valueOf(matcher.group(1));
            if(id==ruleID){
              valueList.add(entryLabel.getValue());
            }
          }
        }

        // Расчет степени уверенности
        valueCategory = Sugeno.getAggregationResult(valueList);
        Rule rule = mapRules.get(ruleID);
        if(rule!=null){
          rule.setValueOutput(valueCategory);
          mapRules.put(ruleID,rule);
        }


        if(valueCategory>0){
          double offset=0.01;
          series1.getData().clear();
          series1.getData().add(new XYChart.Data<Number, Number>(constantValue-offset, valueCategory-offset));
          series1.getData().add(new XYChart.Data<Number, Number>(constantValue-offset, valueCategory+offset*2));
          series1.getData().add(new XYChart.Data<Number, Number>(constantValue+offset*2, valueCategory+offset*2));
          series1.getData().add(new XYChart.Data<Number, Number>(constantValue+offset*2, valueCategory-offset*2));
        }


        Label label = (Label) scene.lookup("#label"+variableID+ruleID+mf.getCodeMF());
        if(label!=null){
          label.setText(String.valueOf(valueCategory));
        }


        //Вывод COG
        accumulationResult = Math.round(Sugeno.getCenterOfGravitySingletons(mapRules)*100.0)/100.0;
        TextField textField = (TextField) scene.lookup(textFieldName);

        if(textField!=null){
          String categoryName = getCategoryName(accumulationResult);

          textField.setText(String.valueOf(categoryName + " ("+accumulationResult+")"));

        }



      }
    }));
    timeline.setCycleCount(Animation.INDEFINITE);
    timeline.setAutoReverse(true);
    timeline.play();

    chart.getData().addAll(series,series0,series1,series2);
    stack.getChildren().addAll(chart,stLabel);
    return stack;
  }

// Выходные переменные

  private StackPane getOutputAreaChartMamdani(MembershipFunction mf, double param, int ruleID, int variableID){
    StackPane stack = new StackPane();
    double maxValue = 0;
    Label stLabel = getLabel();
    stLabel.setId("label"+variableID+ ruleID+mf.getCodeMF());
    stLabel.setMaxWidth(215);
    stLabel.setMinWidth(50);

    String textFieldName = "#textField"+variableID;


    XYChart.Series<Number,Number> series = new XYChart.Series<Number,Number>();
    XYChart.Series<Number,Number> series2 = new XYChart.Series<Number,Number>();
    XYChart.Series<Number,Number> series0 = new XYChart.Series<Number,Number>();
    XYChart.Series<Number,Number> series1 = new XYChart.Series<Number,Number>();

    //Кол-во точек
    String value[] = mf.getParamValueMF().split(" ");
    series.setName(mf.getNameMF());
    switch (value.length){
      case 3:
        for(int i =0;i<value.length;i++){
          double val = Double.valueOf(value[i]);
          series.getData().add(new XYChart.Data<Number,Number>(Double.valueOf(value[i]),i%2));
          if(val>maxValue){
            maxValue = val;
          }
        }

        break;
      case 4:
        for(int i=0;i<value.length;i++){
          double val = Double.valueOf(value[i]);
          if(i==0 || i==3){
            series.getData().add(new XYChart.Data<Number,Number>(val,0));
          }else{
            series.getData().add(new XYChart.Data<Number,Number>(val,1));
          }
          if(val>maxValue){
            maxValue = val;
          }
        }
        break;
    }

    final NumberAxis xAxis;
    if(maxValue <= 1){
      xAxis = new NumberAxis(0,1,0) ;
    }else{
      xAxis = new NumberAxis();
    }

    final NumberAxis yAxis = new NumberAxis(0,1,0) ;
    AreaChart<Number,Number> chart = new AreaChart<Number,Number>(xAxis, yAxis) ;
    chart.setTitle(mf.getNameMF());
    chart.setMaxWidth(200);
    chart.setMaxHeight(150);
    chart.setMinHeight(100);
    chart.setMinWidth(100);
    chart.setCreateSymbols(false);
    chart.setId("chart"+variableID+ruleID+mf.getCodeMF());
    chart.setAnimated(false);

    Timeline timeline = new Timeline();
    timeline.getKeyFrames().add(new KeyFrame(Duration.millis(500), new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent actionEvent) {
        double valueCategory = 0;

        ArrayList<Double> valueList = new ArrayList<Double>();
        Pattern pattern = Pattern.compile("_(.*?)_");
        for(Map.Entry<String,Double> entryLabel:mapLabelValues.entrySet()){
           Matcher matcher = pattern.matcher(entryLabel.getKey());
          if (matcher.find()) {
            int id = Integer.valueOf(matcher.group(1));
            if(id==ruleID){
              valueList.add(entryLabel.getValue());
            }
          }
        }

        // Расчет степени уверенности
        valueCategory = Mamdani.getAggregationResult(valueList);
        Rule rule = mapRules.get(ruleID);
        if(rule!=null){
          rule.setValueOutput(valueCategory);
          mapRules.put(ruleID,rule);
        }

        double XX = getX(valueCategory,Double.valueOf(value[0]),Double.valueOf(value[1]),0,1);

        Label label = (Label) scene.lookup("#label"+variableID+ruleID+mf.getCodeMF());
        if(label!=null){
          label.setText(String.valueOf(valueCategory));

        }

        series2.getData().clear();
        switch (value.length) {
          case 3:

            //Заливка
            if (XX >= Double.valueOf(value[0]) && XX <= Double.valueOf(value[1])) {
              series2.getData().add(new XYChart.Data<Number, Number>(Double.valueOf(value[0]), 0));
              double y = getY(XX, Double.valueOf(value[0]), Double.valueOf(value[1]), 0, 1);
              series2.getData().add(new XYChart.Data<Number, Number>(XX, y));
              double x = getX(y, Double.valueOf(value[1]), Double.valueOf(value[2]), 1, 0);
              series2.getData().add(new XYChart.Data<Number, Number>(x, y));
              series2.getData().add(new XYChart.Data<Number, Number>(Double.valueOf(value[2]), 0));
            } else {
              if (XX >= Double.valueOf(value[1]) && XX <= Double.valueOf(value[2])) {
                series2.getData().add(new XYChart.Data<Number, Number>(Double.valueOf(value[2]), 0));
                double y = getY(XX, Double.valueOf(value[1]), Double.valueOf(value[2]), 1, 0);
                series2.getData().add(new XYChart.Data(XX, y));
                double x = getX(y, Double.valueOf(value[0]), Double.valueOf(value[1]), 0, 1);
                series2.getData().add(new XYChart.Data<Number, Number>(x, y));
                series2.getData().add(new XYChart.Data<Number, Number>(Double.valueOf(value[0]), 0));
              }
            }
            break;
          case 4:
            if(Double.valueOf(value[0])==0){
              //Заливка  0 0 0.1 0.2
              series2.getData().add(new XYChart.Data<Number, Number>(Double.valueOf(value[0]), 0));
              double y = valueCategory;
              series2.getData().add(new XYChart.Data<Number, Number>(Double.valueOf(value[1]), y));
              double x = getX(y, Double.valueOf(value[2]), Double.valueOf(value[3]), 1, 0);
              series2.getData().add(new XYChart.Data<Number, Number>(x, y));
              series2.getData().add(new XYChart.Data<Number, Number>(Double.valueOf(value[3]), 0));
            }else{
              //Заливка  0.75 0.9 1 1
              series2.getData().add(new XYChart.Data<Number, Number>(Double.valueOf(value[0]), 0));
              double y = getY(XX, Double.valueOf(value[0]), Double.valueOf(value[1]), 0, 1);
              series2.getData().add(new XYChart.Data<Number, Number>(XX, y));
              double x = getX(y, Double.valueOf(value[2]), Double.valueOf(value[3]), 1, 0);
              series2.getData().add(new XYChart.Data<Number, Number>(x, y));
              series2.getData().add(new XYChart.Data<Number, Number>(Double.valueOf(value[3]), 0));
            }


            break;
        }

        //Вывод COG
        accumulationResult = Math.round(Mamdani.getCenterOfGravity(mapRules)*100.0)/100.0;
        TextField textField = (TextField) scene.lookup(textFieldName);

        if(textField!=null){
          String categoryName = getCategoryName(accumulationResult);

          textField.setText(String.valueOf(categoryName + " ("+accumulationResult+")"));

        }



      }
    }));
    timeline.setCycleCount(Animation.INDEFINITE);
    timeline.setAutoReverse(true);
    timeline.play();

    chart.getData().addAll(series,series0,series1,series2);
    stack.getChildren().addAll(chart,stLabel);
    return stack;
  }

//Итоговый график
private StackPane getTotalOutputAreaChartSugeno(){
  StackPane stack = new StackPane();

  Map <MembershipFunction,Double> mapGraph = new LinkedHashMap<>();
  for(Map.Entry<String, LinguisticVariable> entry:mapOutputVariables.entrySet()){
    LinguisticVariable variable =  mapOutputVariables.get(entry.getKey());
    Variable var= Variable.getVariableByName(variable.getName());
    switch (var) {
      case RANK:
        mfList = variable.getMfList();
        break;
    }
  }

  for(int i = 0;i<mfList.size();i++){
    mapGraph.put(mfList.get(i),0.0);
  }


  final NumberAxis xAxis = new NumberAxis(0,1,0) ;
  final NumberAxis yAxis = new NumberAxis(0,1,0) ;

  AreaChart<Number,Number> chart = new AreaChart<Number,Number>(xAxis, yAxis) ;

  chart.setTitle("");
  chart.setMaxWidth(200);
  chart.setMaxHeight(150);
  chart.setMinHeight(100);
  chart.setMinWidth(100);
  chart.setCreateSymbols(false);
  chart.setId("chartTotalOutput");
  chart.setAnimated(false);

  XYChart.Series<Number,Number> seriesOutput1 = new XYChart.Series<Number,Number>();
  seriesOutput1.getData().add(new XYChart.Data<Number, Number>(0,0));

  XYChart.Series<Number,Number> seriesOutput2 = new XYChart.Series<Number,Number>();
  seriesOutput2.getData().add(new XYChart.Data<Number, Number>(0,0));


  //Красная линия
  XYChart.Series<Number,Number> seriesOutput3 = new XYChart.Series<Number,Number>();
  seriesOutput3.getData().add(new XYChart.Data<Number,Number>(accumulationResult,0));
  seriesOutput3.getData().add(new XYChart.Data<Number,Number>(accumulationResult,1));

  XYChart.Series<Number,Number> seriesOutput4 = new XYChart.Series<Number,Number>();
  XYChart.Series<Number,Number> seriesOutput5 = new XYChart.Series<Number,Number>();
  XYChart.Series<Number,Number> seriesOutput6 = new XYChart.Series<Number,Number>();
  XYChart.Series<Number,Number> seriesOutput7 = new XYChart.Series<Number,Number>();
  XYChart.Series<Number,Number> seriesOutput8 = new XYChart.Series<Number,Number>();
  XYChart.Series<Number,Number> seriesOutput9 = new XYChart.Series<Number,Number>();



  //Map<String,Double>
  Timeline timeline = new Timeline();
  timeline.getKeyFrames().add(new KeyFrame(Duration.millis(500), new EventHandler<ActionEvent>() {
    @Override
    public void handle(ActionEvent actionEvent) {

      seriesOutput4.getData().clear();
      seriesOutput5.getData().clear();
      seriesOutput6.getData().clear();
      seriesOutput7.getData().clear();
      seriesOutput8.getData().clear();
      seriesOutput9.getData().clear();
      for (XYChart.Data<Number, Number> data : seriesOutput3.getData()) {
        data.setXValue(accumulationResult);
      }
      for (XYChart.Data<Number, Number> data : seriesOutput2.getData()) {
        data.setXValue(0.1);
      }
      for (XYChart.Data<Number, Number> data : seriesOutput1.getData()) {
        data.setXValue(0.1);
      }


      for(int i = 0;i<mfList.size();i++){
        mapGraph.put(mfList.get(i),0.0);
      }
      for(Map.Entry<Integer,Rule> r:mapRules.entrySet()){
        Rule outputRule = r.getValue();

        double ruleValueOutput = outputRule.getValueOutput();
        if(ruleValueOutput>0){
          Map<String,Condition> mapTHEN = outputRule.getTHENConditionMap();

          for(Map.Entry<String, Condition> entry:mapTHEN.entrySet()){
            Condition condition = entry.getValue();
            MembershipFunction mfOut = condition.getMembershipFunction();
            //Проверка на большее значение
            for(int i = 0;i<mfList.size();i++){
              MembershipFunction m = mfList.get(i);
              if(mfOut.getCodeMF().equals(m.getCodeMF())){
                Double val = mapGraph.get(m);
                if(val<ruleValueOutput){
                  mapGraph.put(m,ruleValueOutput);
                }
              }
            }
          }
        }
      }

      for(Map.Entry<MembershipFunction,Double> m :mapGraph.entrySet()){
        String value[] = m.getKey().getParamValueMF().split(" ");
        //System.out.println("Graph " + m.getKey().getCodeMF() + "  " + m.getValue() + " size " + mapGraph.size());

        double valueCategory = m.getValue();
        String mfCode = m.getKey().getCodeMF();
        MFname mFname = MFname.getMFnameByCode(mfCode);
        //System.out.println("MFCode " + mfCode );



      }

    }
  }));
  timeline.setCycleCount(Animation.INDEFINITE);
  timeline.setAutoReverse(true);
  timeline.play();
  chart.getData().addAll(seriesOutput1,seriesOutput2,seriesOutput3);
  chart.getData().addAll(seriesOutput4,seriesOutput5,seriesOutput6,seriesOutput7,seriesOutput8,seriesOutput9);
  //chart.getData().addAll(seriesOutput1,seriesOutput2,seriesOutput3);



  // seriesOutput9,seriesOutput8,seriesOutput7,seriesOutput6,seriesOutput5,seriesOutput4);
  stack.getChildren().addAll(chart);
  return stack;
}


  private StackPane getTotalOutputAreaChartMamdani(){
    StackPane stack = new StackPane();

    Map <MembershipFunction,Double> mapGraph = new LinkedHashMap<>();
    for(Map.Entry<String, LinguisticVariable> entry:mapOutputVariables.entrySet()){
      LinguisticVariable variable =  mapOutputVariables.get(entry.getKey());
      Variable var= Variable.getVariableByName(variable.getName());
      switch (var) {
        case RANK:
        mfList = variable.getMfList();
        break;
      }
    }

    for(int i = 0;i<mfList.size();i++){
      mapGraph.put(mfList.get(i),0.0);
    }


    final NumberAxis xAxis = new NumberAxis(0,1,0) ;
    final NumberAxis yAxis = new NumberAxis(0,1,0) ;

    AreaChart<Number,Number> chart = new AreaChart<Number,Number>(xAxis, yAxis) ;

    chart.setTitle("");
    chart.setMaxWidth(200);
    chart.setMaxHeight(150);
    chart.setMinHeight(100);
    chart.setMinWidth(100);
    chart.setCreateSymbols(false);
    chart.setId("chartTotalOutput");
    chart.setAnimated(false);

    XYChart.Series<Number,Number> seriesOutput1 = new XYChart.Series<Number,Number>();
    seriesOutput1.getData().add(new XYChart.Data<Number, Number>(0,0));

    XYChart.Series<Number,Number> seriesOutput2 = new XYChart.Series<Number,Number>();
    seriesOutput2.getData().add(new XYChart.Data<Number, Number>(0,0));


    //Красная линия
    XYChart.Series<Number,Number> seriesOutput3 = new XYChart.Series<Number,Number>();
    seriesOutput3.getData().add(new XYChart.Data<Number,Number>(accumulationResult,0));
    seriesOutput3.getData().add(new XYChart.Data<Number,Number>(accumulationResult,1));

    XYChart.Series<Number,Number> seriesOutput4 = new XYChart.Series<Number,Number>();
    XYChart.Series<Number,Number> seriesOutput5 = new XYChart.Series<Number,Number>();
    XYChart.Series<Number,Number> seriesOutput6 = new XYChart.Series<Number,Number>();
    XYChart.Series<Number,Number> seriesOutput7 = new XYChart.Series<Number,Number>();
    XYChart.Series<Number,Number> seriesOutput8 = new XYChart.Series<Number,Number>();
    XYChart.Series<Number,Number> seriesOutput9 = new XYChart.Series<Number,Number>();



    //Map<String,Double>
    Timeline timeline = new Timeline();
    timeline.getKeyFrames().add(new KeyFrame(Duration.millis(500), new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent actionEvent) {

        seriesOutput4.getData().clear();
        seriesOutput5.getData().clear();
        seriesOutput6.getData().clear();
        seriesOutput7.getData().clear();
        seriesOutput8.getData().clear();
        seriesOutput9.getData().clear();
        for (XYChart.Data<Number, Number> data : seriesOutput3.getData()) {
            data.setXValue(accumulationResult);
        }
        for (XYChart.Data<Number, Number> data : seriesOutput2.getData()) {
          data.setXValue(0.1);
        }
        for (XYChart.Data<Number, Number> data : seriesOutput1.getData()) {
          data.setXValue(0.1);
        }


        for(int i = 0;i<mfList.size();i++){
          mapGraph.put(mfList.get(i),0.0);
        }
        for(Map.Entry<Integer,Rule> r:mapRules.entrySet()){
          Rule outputRule = r.getValue();

          double ruleValueOutput = outputRule.getValueOutput();
          if(ruleValueOutput>0){
            Map<String,Condition> mapTHEN = outputRule.getTHENConditionMap();

            for(Map.Entry<String, Condition> entry:mapTHEN.entrySet()){
              Condition condition = entry.getValue();
              MembershipFunction mfOut = condition.getMembershipFunction();
              //Проверка на большее значение
              for(int i = 0;i<mfList.size();i++){
                MembershipFunction m = mfList.get(i);
                if(mfOut.getCodeMF().equals(m.getCodeMF())){
                  Double val = mapGraph.get(m);
                  if(val<ruleValueOutput){
                    mapGraph.put(m,ruleValueOutput);
                  }
                }
              }
            }
          }
        }

        for(Map.Entry<MembershipFunction,Double> m :mapGraph.entrySet()){
          String value[] = m.getKey().getParamValueMF().split(" ");
          //System.out.println("Graph " + m.getKey().getCodeMF() + "  " + m.getValue() + " size " + mapGraph.size());

          double valueCategory = m.getValue();
          String mfCode = m.getKey().getCodeMF();
          MFname mFname = MFname.getMFnameByCode(mfCode);
          //System.out.println("MFCode " + mfCode );
          switch (mFname){
            case PLS:
              if(valueCategory>0){
                getDataSeries(seriesOutput4,value,valueCategory);
              }
              break;
            case PS:
              if(valueCategory>0){
                getDataSeries(seriesOutput5,value,valueCategory);
              }
              break;
            case PLM:
              if(valueCategory>0){
                getDataSeries(seriesOutput6,value,valueCategory);
              }
              break;
            case PM:
              if(valueCategory>0){
                getDataSeries(seriesOutput7,value,valueCategory);
              }
              break;
            case PLB:
              if(valueCategory>0){
                getDataSeries(seriesOutput8,value,valueCategory);
              }
              break;
            case PB:
              if(valueCategory>0){
                getDataSeries(seriesOutput9,value,valueCategory);

              }
              break;
          }

        }

      }
    }));
    timeline.setCycleCount(Animation.INDEFINITE);
    timeline.setAutoReverse(true);
    timeline.play();
    chart.getData().addAll(seriesOutput1,seriesOutput2,seriesOutput3);
    chart.getData().addAll(seriesOutput4,seriesOutput5,seriesOutput6,seriesOutput7,seriesOutput8,seriesOutput9);
    //chart.getData().addAll(seriesOutput1,seriesOutput2,seriesOutput3);



        // seriesOutput9,seriesOutput8,seriesOutput7,seriesOutput6,seriesOutput5,seriesOutput4);
    stack.getChildren().addAll(chart);
    return stack;
  }

  private void getDataSeries(XYChart.Series<Number,Number> series, String value[], Double valueCategory){

    double XX = getX(valueCategory,Double.valueOf(value[0]),Double.valueOf(value[1]),0,1);
    switch (value.length){
      case 3:
        //Заливка
        if (XX >= Double.valueOf(value[0]) && XX <= Double.valueOf(value[1])) {
          series.getData().add(new XYChart.Data<Number, Number>(Double.valueOf(value[0]), 0));
          double y = getY(XX, Double.valueOf(value[0]), Double.valueOf(value[1]), 0, 1);
          series.getData().add(new XYChart.Data<Number, Number>(XX, y));
          double x = getX(y, Double.valueOf(value[1]), Double.valueOf(value[2]), 1, 0);
          series.getData().add(new XYChart.Data<Number, Number>(x, y));
          series.getData().add(new XYChart.Data<Number, Number>(Double.valueOf(value[2]), 0));
        } else {
          if (XX >= Double.valueOf(value[1]) && XX <= Double.valueOf(value[2])) {
            series.getData().add(new XYChart.Data<Number, Number>(Double.valueOf(value[2]), 0));
            double y = getY(XX, Double.valueOf(value[1]), Double.valueOf(value[2]), 1, 0);
            series.getData().add(new XYChart.Data(XX, y));
            double x = getX(y, Double.valueOf(value[0]), Double.valueOf(value[1]), 0, 1);
            series.getData().add(new XYChart.Data<Number, Number>(x, y));
            series.getData().add(new XYChart.Data<Number, Number>(Double.valueOf(value[0]), 0));
          }
        }

        break;
      case 4:

        if(Double.valueOf(value[0])==0){
          //Заливка  0 0 0.1 0.2
          series.getData().add(new XYChart.Data<Number, Number>(Double.valueOf(value[0]), 0));
          double y = valueCategory;
          series.getData().add(new XYChart.Data<Number, Number>(Double.valueOf(value[1]), y));
          double x = getX(y, Double.valueOf(value[2]), Double.valueOf(value[3]), 1, 0);
          series.getData().add(new XYChart.Data<Number, Number>(x, y));
          series.getData().add(new XYChart.Data<Number, Number>(Double.valueOf(value[3]), 0));
        }else{
          //Заливка  0.75 0.9 1 1
          series.getData().add(new XYChart.Data<Number, Number>(Double.valueOf(value[0]), 0));
          double y = getY(XX, Double.valueOf(value[0]), Double.valueOf(value[1]), 0, 1);
          series.getData().add(new XYChart.Data<Number, Number>(XX, y));
          double x = getX(y, Double.valueOf(value[2]), Double.valueOf(value[3]), 1, 0);
          series.getData().add(new XYChart.Data<Number, Number>(x, y));
          series.getData().add(new XYChart.Data<Number, Number>(Double.valueOf(value[3]), 0));
        }

        break;

    }



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


  private Label getLabel(){
    Label label = new Label();
    label.setFont(new Font("Verdana", 12));
    label.setPadding(new Insets(0,0,0,5));
    label.setTextFill(Paint.valueOf(String.valueOf(Color.RED)));
    return label;
  }

  private Label getRedLabel(String name){
    Label label = new Label(name);
    label.setFont(new Font("Verdana", 16));
    label.setTextAlignment(TextAlignment.CENTER);
    label.setTextFill(Paint.valueOf(String.valueOf(Color.RED)));
    label.setPadding(new Insets(0,0,0,0));
    label.setMaxWidth(250);
    return label;
  }

  private Region getLabel(String name){
    HBox box = new HBox(10);
    box.setAlignment(Pos.CENTER);
    //box.setPadding(new Insets(20,0,0,20));
    box.setFillHeight(false);

    Label label = new Label(name);
    label.setFont(new Font("Verdana", 14));
    label.setTextAlignment(TextAlignment.CENTER);
    label.setTextFill(Paint.valueOf(String.valueOf(Color.BLUE)));
    label.setPadding(new Insets(0,0,0,0));
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
      int columnIndex = 0;
      //Основной цикл по переменным
      for(int i = 0;i<listVariables.size();i++){

        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setMinWidth(250);
        columnConstraints.setMaxWidth(250);
        root.getColumnConstraints().add(columnConstraints);

        LinguisticVariable linguisticVariable = listVariables.get(i);
        int variableID = linguisticVariable.getId();
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

        //Slider + Listener
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        Slider slider = getSlider((int) min, (int) max, param1);
        slider.valueProperty().addListener(new ChangeListener<Number>() {
          public void changed(ObservableValue<? extends Number> obsVal,
                              Number oldVal, Number newVal) {
            System.out.println(newVal.doubleValue());
          }
        });
        hBox.getChildren().add(slider);

        if(variable1.equals(Variable.RANK)) {
          slider.setVisible(false);
        }

          //Текстовое поле
        TextField valueField = new TextField();
        valueField.setFont(new Font("Verdana", 14));
        valueField.setAlignment(Pos.CENTER);
        valueField.setId("textField"+variableID);
        if(!variable1.equals(Variable.RANK)){
          valueField.textProperty().bind(slider.valueProperty().asString("%.2f"));
        }
        rowIndex+=1;
        root.add(valueField,i,rowIndex);
        rowIndex+=1;
        root.add(hBox,i,rowIndex);

        //Цикл по правилам
        int j = 0;
        for(Map.Entry<Integer, Rule> entryRule:mapRules.entrySet()){
          j+=1;
          rowIndex += 1;
          Rule rule = entryRule.getValue();
          int ruleID = rule.getIdRule();
          Map<String,Condition> mapIF = rule.getIFConditionMap();
          Map<String,Condition> mapAND = rule.getANDConditionMap();
          double param = 0;
          //Переменная IF
          for(Map.Entry<String, Condition> entry:mapIF.entrySet()){
            LinguisticVariable ifVariable =  mapInputVariables.get(entry.getKey());
            Condition ifCondition = entry.getValue();
            MembershipFunction ifMF = ifCondition.getMembershipFunction();
            Variable variable2 = Variable.getVariableByName(ifVariable.getName());

            if(variable1.equals(variable2)){

              switch (variable2) {
                case WORK_EXPERIENCE:
                  //Построение графика
                  Region chartExperience1 = getAreaChart(ifMF,cv.getExperience(),ruleID , variableID,linguisticVariable);
                  root.add(chartExperience1,i,rowIndex+1);
                  break;
                case SALARY:
                  //Построение графика
                  Region chartSalary1 = getAreaChart(ifMF,cv.getSalary(), ruleID, variableID,linguisticVariable);
                  root.add(chartSalary1,i,rowIndex+1);
                  break;
                case POSITION:
                  param = 0.5;
                  Region chartPosition1 = getAreaChart(ifMF,param, ruleID, variableID,linguisticVariable);
                  root.add(chartPosition1,i,rowIndex+1);

                  break;
              }
            }
          }

          //Переменная AND
          for(Map.Entry<String, Condition> entry:mapAND.entrySet()){
            LinguisticVariable andVariable =  mapInputVariables.get(entry.getKey());
            Condition andCondition = entry.getValue();
            MembershipFunction andMF = andCondition.getMembershipFunction();
            Variable variable3 = Variable.getVariableByName(andVariable.getName());

            if(variable1.equals(variable3)){
              switch (variable3) {
                case WORK_EXPERIENCE:
                  //Построение графика
                  Region chartExperience2 = getAreaChart(andMF,cv.getExperience(), ruleID, variableID,linguisticVariable);
                  root.add(chartExperience2,i,rowIndex+1);
                  break;
                case SALARY:
                  //Построение графика
                  Region chartSalary2 = getAreaChart(andMF,cv.getSalary(), ruleID, variableID,linguisticVariable);
                  root.add(chartSalary2,i,rowIndex+1);

                  break;
                case POSITION:
                  //Построение графика
                  Region chartPosition2 = getAreaChart(andMF,param,ruleID, variableID,linguisticVariable);
                  root.add(chartPosition2,i,rowIndex+1);
                  break;
              }
            }
          }
        }
        if(i==listVariables.size()-1){
          rowIndex = 3;
        }
        //Цикл по правилам
        for(Map.Entry<Integer,Rule>entryRulesOutput:mapRules.entrySet()) {
          rowIndex += 1;
          Rule rule = entryRulesOutput.getValue();
          Map<String,Condition> mapTHEN = rule.getTHENConditionMap();
          int ruleID = rule.getIdRule();
          //Переменная THEN
          for(Map.Entry<String, Condition> entry:mapTHEN.entrySet()){
            LinguisticVariable thenVariable =  mapOutputVariables.get(entry.getKey());
            Condition thenCondition = entry.getValue();
            MembershipFunction thenMF = thenCondition.getMembershipFunction();
            Variable variable4 = Variable.getVariableByName(thenVariable.getName());

            if(variable1.equals(variable4)){
              switch (variable4) {
                case RANK:
                  Region rank = null;
                  switch(comboTypeOutput.getSelectionModel().getSelectedIndex()){
                    //Mamdani
                    case 0:
                      rank = getOutputAreaChartMamdani(thenMF,0,ruleID, variableID);
                      break;

                      //Sugeno
                    case 1:
                      rank = getOutputAreaChartSugeno(thenMF,0,ruleID, variableID);
                      break;
                  }
                  root.add(rank,i,rowIndex+1);
                  break;
              }
            }
          }
        }
        columnIndex = i;
        rowIndex = 0;
      }
//      Region label = getRedLabel("");
//      label.setId("TotalRank");
//      root.add(label,columnIndex,3);

      //String categoryName = getCategoryName();


      //Построение итогового графика выходных переменных
      Region outputAreaChart = null;
      switch(comboTypeOutput.getSelectionModel().getSelectedIndex()) {
        //Mamdani
        case 0:
          outputAreaChart = getTotalOutputAreaChartMamdani();
          break;
        case 1:
          outputAreaChart = getTotalOutputAreaChartSugeno();
          break;
      }
      root.add(outputAreaChart,columnIndex,mapRules.size()+5);

      scrollPane.setContent(root);
      scrollPane.setPannable(true);
      scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
      scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
      scene = new Scene(scrollPane);
      scene.getStylesheets().add("ru/bmstu/edu/resources/css/chart.css");

      viewRulesStage.setScene(scene);

      viewRulesStage.initModality(Modality.WINDOW_MODAL);
      viewRulesStage.initOwner((Stage) nodesource.getScene().getWindow());
      viewRulesStage.showAndWait();
    }
  }

  private String getCategoryName(double value){
    String name = null;
    ArrayList<LinguisticVariable>listOutputVariables = DaoUtils.getOutputVariables();
    for(LinguisticVariable linguisticVariable:listOutputVariables){
      ArrayList<MembershipFunction>mfList = linguisticVariable.getMfList();
      for(MembershipFunction m:mfList){
        String values[] = m.getParamValueMF().split(" ");
        double minValue = 0;
        double maxValue = 0;
        switch (values.length){
          case 3:
            minValue = Double.valueOf(values[0]);
            maxValue = Double.valueOf(values[2]);
            break;
          case 4:
            minValue = Double.valueOf(values[0]);
            maxValue = Double.valueOf(values[3]);
            break;
        }

        if(value>=minValue&&value<=maxValue){
          name = m.getNameMF();
          return name;
        }
      }
    }


    return name;
  }


  private ArrayList<LineChart> drawMFLineGraph(LinguisticVariable linguisticVariable) {
    ArrayList<LineChart> listCharts = new ArrayList<>();
    ArrayList<MembershipFunction> listMF = linguisticVariable.getMfList();

    if(listMF.size()>0){
      for(MembershipFunction mf:listMF){
        LineChart lineChart = getLineChart(mf);
        listCharts.add(lineChart);
      }

    }
    return listCharts;
  }

  private void fillData(){
    CVList.clear();
    StringBuilder query = new StringBuilder("select id, positionname, salary, experience from cvdata.bmstu.CV");
    StringBuilder where = new StringBuilder("");
    if(txtPositionName.getText().isEmpty()) return;
    if(where.toString().isEmpty()){
      where.append(" where ");
    }
    where.append(" positionname like '%"+ txtPositionName.getText()+"%'");

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
        cv.setCategoryName(getRank(cv));
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
        AreaChart chart = (AreaChart) scene.lookup("#chartTotalOutput");
        if(chart!=null){
          chart.getData().clear();
        }
        break;

      case "btnStop":
        timeline.stop();
        break;
    }

  }

}
