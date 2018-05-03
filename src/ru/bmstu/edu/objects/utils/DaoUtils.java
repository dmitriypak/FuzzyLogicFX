package ru.bmstu.edu.objects.utils;

import javafx.beans.property.ObjectProperty;
import javafx.scene.control.TextField;
import org.controlsfx.control.textfield.CustomTextField;
import org.controlsfx.control.textfield.TextFields;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.postgresql.util.PGobject;
import ru.bmstu.edu.DAO.PostgreSQLConnection;
import ru.bmstu.edu.objects.LinguisticVariable;
import ru.bmstu.edu.objects.MembershipFunction;
import ru.bmstu.edu.objects.Rule;

import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class DaoUtils {

  public static ArrayList parseJSON(String value){
    ArrayList list = new ArrayList();
    org.json.simple.parser.JSONParser parser = new org.json.simple.parser.JSONParser();
    try {

      Object obj = parser.parse(value);

      JSONObject jsonObject = (JSONObject) obj;
      System.out.println("Парсинг JSON: " + jsonObject.toJSONString());
      String nameMF = (String) jsonObject.get("Variable");
      System.out.println(nameMF);
      JSONArray mf = (JSONArray) jsonObject.get("MFParams");
      for(int i = 0;i<mf.size();i++){
        JSONObject mfParamName = (JSONObject) mf.get(i);
        String mfParam = mfParamName.get("MFParamName").toString();
        System.out.println(mfParam);
        String mfParamValue =  mfParamName.get("MFParamValue").toString();
        System.out.println(mfParamValue);
        MembershipFunction membershipFunction = new MembershipFunction(mfParam, mfParamValue);
        list.add(membershipFunction);
      }
//      Iterator<JSONArray> iterator = mf.iterator();
//      while (iterator.hasNext()) {
//        System.out.println(iterator.next());
//
//
//      }
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return list;
  }



  public static ArrayList<LinguisticVariable> getVariables(){
    ArrayList<LinguisticVariable> listVariables = new ArrayList<>();
    try(PreparedStatement statement = PostgreSQLConnection.getConnection().prepareStatement("select id, name, VALUE, type from cvdata.bmstu.linguisticvariables")) {
      ResultSet rs = statement.executeQuery();
      while (rs.next()){
        LinguisticVariable linguisticVariable = new LinguisticVariable(rs.getInt("id"), rs.getString("name"),
            rs.getString("value"),rs.getString("type"));
        listVariables.add(linguisticVariable);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return listVariables;
  }

  public static LinkedHashMap<String,LinguisticVariable> getMapInputVariables(){
    LinkedHashMap<String,LinguisticVariable> mapVariables = new LinkedHashMap<>();
    try(PreparedStatement statement = PostgreSQLConnection.getConnection().prepareStatement("select id, name, VALUE, type" +
        " from cvdata.bmstu.linguisticvariables WHERE TYPE = 'INPUT'")) {
      ResultSet rs = statement.executeQuery();
      while (rs.next()){
        LinguisticVariable linguisticVariable = new LinguisticVariable(rs.getInt("id"), rs.getString("name"),
            rs.getString("value"), rs.getString("type"));
        linguisticVariable.setMfList(DaoUtils.parseJSON(linguisticVariable.getValue()));
        mapVariables.put(linguisticVariable.getName(),linguisticVariable);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return mapVariables;
  }

  public static LinkedHashMap<String,LinguisticVariable> getMapOutputVariables(){
    LinkedHashMap<String,LinguisticVariable> mapVariables = new LinkedHashMap<>();
    try(PreparedStatement statement = PostgreSQLConnection.getConnection().prepareStatement("select id, name, VALUE, type" +
        " from cvdata.bmstu.linguisticvariables WHERE TYPE = 'OUTPUT'")) {
      ResultSet rs = statement.executeQuery();
      while (rs.next()){
        LinguisticVariable linguisticVariable = new LinguisticVariable(rs.getInt("id"), rs.getString("name"),
            rs.getString("value"), rs.getString("type"));
        linguisticVariable.setMfList(DaoUtils.parseJSON(linguisticVariable.getValue()));
        mapVariables.put(linguisticVariable.getName(),linguisticVariable);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return mapVariables;
  }


  public static void insertRule(Rule rule, int idVariable) throws SQLException {
    String query = "INSERT INTO cvdata.bmstu.rules "
        + " (idvariable, value) "
        + " VALUES (?, ?);";
    try (PreparedStatement pstmt = PostgreSQLConnection.getConnection().prepareStatement(query)) {
      int i = 0;
      pstmt.setInt(++i,idVariable);
      PGobject jsonObject = new PGobject();
      jsonObject.setType("json");
      jsonObject.setValue(rule.getValue());
      pstmt.setObject(++i, jsonObject);
      pstmt.executeUpdate();
    }
  }

  public static ArrayList<Rule> getRules(){
    ArrayList<Rule> listRules = new ArrayList<>();
    try(PreparedStatement statement = PostgreSQLConnection.getConnection().prepareStatement("select id, idvariable, VALUE from cvdata.bmstu.rules")) {
      ResultSet rs = statement.executeQuery();
      while (rs.next()){
        Rule rule = new Rule(rs.getInt("id"), rs.getString("value"));
        listRules.add(rule);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return listRules;
  }
  public static ArrayList<Rule> getRulesByVariable(int idVariable){
    ArrayList<Rule> listRules = new ArrayList<>();
    try(PreparedStatement statement = PostgreSQLConnection.getConnection().prepareStatement("select id, idvariable, VALUE from cvdata.bmstu.rules WHERE idvariable" +
        " = ?")) {
      statement.setInt(1,idVariable);
      ResultSet rs = statement.executeQuery();
      while (rs.next()){
        Rule rule = new Rule(rs.getInt("id"), rs.getString("value"));
        listRules.add(rule);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return listRules;
  }


  public static void setupClearButtonField(CustomTextField txtFunction){
    try {
      Method m = TextFields.class.getDeclaredMethod("setupClearButtonField", TextField.class, ObjectProperty.class);
      m.setAccessible(true);
      m.invoke(null, txtFunction, txtFunction.rightProperty());
    }catch (Exception e){
      e.printStackTrace();
    }
  }
}
