package ru.bmstu.edu.model;

import javafx.beans.property.ObjectProperty;
import javafx.scene.control.TextField;
import org.controlsfx.control.textfield.CustomTextField;
import org.controlsfx.control.textfield.TextFields;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.postgresql.util.PGobject;
import ru.bmstu.edu.DAO.PostgreSQLConnection;
import ru.bmstu.edu.objects.*;
import ru.bmstu.edu.objects.enums.Variable;

import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class DaoUtils {

  public static void updateVariable(LinguisticVariable linguisticVariable) throws SQLException {
    String query = "UPDATE cvdata.bmstu.linguisticvariables set isactive = ? where id = ?";
    try (PreparedStatement pstmt = PostgreSQLConnection.getConnection().prepareStatement(query)) {
      int i = 0;
      pstmt.setObject(++i,linguisticVariable.getIsactive());
      pstmt.setObject(++i, linguisticVariable.getId());
      pstmt.executeUpdate();
    }
  }


  public static void deleteVariable(LinguisticVariable delVariable) throws SQLException {
    String query = "delete from cvdata.bmstu.linguisticvariables WHERE id = ?";
    try (PreparedStatement pstmt = PostgreSQLConnection.getConnection().prepareStatement(query)) {
      pstmt.setInt(1,delVariable.getId());
      pstmt.executeUpdate();
    }
  }


  public static void insertLinguisticVariable(LinguisticVariable linguisticVariable) throws SQLException {
    String query = "INSERT INTO cvdata.bmstu.linguisticvariables "
        + " (name, value, description, type) "
        + " VALUES (?, ?, ?, ?);";
    try (PreparedStatement pstmt = PostgreSQLConnection.getConnection().prepareStatement(query)) {
      int i = 0;
      pstmt.setString(++i,linguisticVariable.getName());
      PGobject jsonObject = new PGobject();
      jsonObject.setType("json");
      jsonObject.setValue(linguisticVariable.getValue());
      pstmt.setObject(++i, jsonObject);
      pstmt.setString(++i,"");
      pstmt.setString(++i,linguisticVariable.getType());
      pstmt.executeUpdate();
    }
  }

  public static void updateLinguisticVariable(LinguisticVariable linguisticVariable) throws SQLException {
    String query = "update cvdata.bmstu.linguisticvariables "
        + " set name = ?, value = ?, description = ?, type = ? WHERE id = ?";
    try (PreparedStatement pstmt = PostgreSQLConnection.getConnection().prepareStatement(query)) {
      int i = 0;
      pstmt.setString(++i,linguisticVariable.getName());

      PGobject jsonObject = new PGobject();
      jsonObject.setType("json");
      jsonObject.setValue(linguisticVariable.getValue());
      pstmt.setObject(++i, jsonObject);
      pstmt.setString(++i,"");
      pstmt.setString(++i,linguisticVariable.getType());
      pstmt.setInt(++i,linguisticVariable.getId());

      pstmt.executeUpdate();
    }
  }



  public static ArrayList getMFList(String value){
    ArrayList list = new ArrayList();
    org.json.simple.parser.JSONParser parser = new org.json.simple.parser.JSONParser();
    try {

      Object obj = parser.parse(value);

      JSONObject jsonObject = (JSONObject) obj;
      //System.out.println("Парсинг JSON: " + jsonObject.toJSONString());
      //String nameMF = (String) jsonObject.get("Variable");
      JSONArray mf = (JSONArray) jsonObject.get("MFParams");
      for(int i = 0;i<mf.size();i++){
        JSONObject mfParamName = (JSONObject) mf.get(i);
        String mfParam = mfParamName.get("MFParamName").toString();
        String mfParamValue =  mfParamName.get("MFParamValue").toString();
        String mfCode =  mfParamName.get("MFCode").toString();
        String mfSugenoConstant = mfParamName.get("MFSugenoConstant").toString();
        MembershipFunction membershipFunction = new MembershipFunction(mfParam,mfCode,mfParamValue);
        if(mfSugenoConstant!=null && !mfSugenoConstant.isEmpty()){
          membershipFunction.setConstantSugeno(Double.valueOf(mfSugenoConstant));
        }
        list.add(membershipFunction);
      }
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return list;
  }


  public static ArrayList<LinguisticVariable> getVariables(){
    ArrayList<LinguisticVariable> listVariables = new ArrayList<>();
    try(PreparedStatement statement = PostgreSQLConnection.getConnection().prepareStatement
        ("select id, name, VALUE, type, isactive from cvdata.bmstu.linguisticvariables WHERE isactive = 'true' order by type;")) {
      ResultSet rs = statement.executeQuery();
      while (rs.next()){
        LinguisticVariable linguisticVariable = new LinguisticVariable(rs.getInt("id"), rs.getString("name"),
            rs.getString("value"),rs.getString("type"), rs.getBoolean("isactive"));
        linguisticVariable.setMfList(DaoUtils.getMFList(linguisticVariable.getValue()));
        linguisticVariable.setVariable(Variable.getVariableByName(rs.getString("name")));
        listVariables.add(linguisticVariable);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return listVariables;
  }



  public static ArrayList<LinguisticVariable> getInputVariables(){
    ArrayList<LinguisticVariable> listVariables = new ArrayList<>();
    try(PreparedStatement statement = PostgreSQLConnection.getConnection().prepareStatement
        ("select id, name, VALUE, type, isactive from cvdata.bmstu.linguisticvariables WHERE type = 'INPUT' and isactive = 'true'")) {
      ResultSet rs = statement.executeQuery();
      while (rs.next()){
        LinguisticVariable linguisticVariable = new LinguisticVariable(rs.getInt("id"), rs.getString("name"),
            rs.getString("value"),rs.getString("type"), rs.getBoolean("isactive"));
        linguisticVariable.setMfList(DaoUtils.getMFList(linguisticVariable.getValue()));
        linguisticVariable.setVariable(Variable.getVariableByName(rs.getString("name")));
        listVariables.add(linguisticVariable);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return listVariables;
  }

  public static ArrayList<LinguisticVariable> getOutputVariables(){
    ArrayList<LinguisticVariable> listVariables = new ArrayList<>();
    try(PreparedStatement statement = PostgreSQLConnection.getConnection().prepareStatement
        ("select id, name, VALUE, type, isactive from cvdata.bmstu.linguisticvariables WHERE TYPE ='OUTPUT'")) {
      ResultSet rs = statement.executeQuery();
      while (rs.next()){
        LinguisticVariable linguisticVariable = new LinguisticVariable(rs.getInt("id"), rs.getString("name"),
            rs.getString("value"),rs.getString("type"), rs.getBoolean("isactive"));
        linguisticVariable.setMfList(DaoUtils.getMFList(linguisticVariable.getValue()));
        linguisticVariable.setVariable(Variable.getVariableByName(rs.getString("name")));
        listVariables.add(linguisticVariable);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return listVariables;
  }



  public static LinkedHashMap<String,LinguisticVariable> getMapInputVariables(){
    LinkedHashMap<String,LinguisticVariable> mapVariables = new LinkedHashMap<>();
    try(PreparedStatement statement = PostgreSQLConnection.getConnection().prepareStatement("select id, name, VALUE, type, isactive" +
        " from cvdata.bmstu.linguisticvariables WHERE TYPE = 'INPUT' and isactive = 'true'")) {
      ResultSet rs = statement.executeQuery();
      while (rs.next()){
        LinguisticVariable linguisticVariable = new LinguisticVariable(rs.getInt("id"), rs.getString("name"),
            rs.getString("value"), rs.getString("type"), rs.getBoolean("isactive"));
        linguisticVariable.setMfList(DaoUtils.getMFList(linguisticVariable.getValue()));
        linguisticVariable.setVariable(Variable.getVariableByName(rs.getString("name")));
        mapVariables.put(linguisticVariable.getName(),linguisticVariable);

      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return mapVariables;
  }

  public static LinkedHashMap<String,LinguisticVariable> getMapOutputVariables(){
    LinkedHashMap<String,LinguisticVariable> mapVariables = new LinkedHashMap<>();
    try(PreparedStatement statement = PostgreSQLConnection.getConnection().prepareStatement("select id, name, VALUE, type, isactive " +
        " from cvdata.bmstu.linguisticvariables WHERE TYPE = 'OUTPUT' and isactive = 'true'")) {
      ResultSet rs = statement.executeQuery();
      while (rs.next()){
        LinguisticVariable linguisticVariable = new LinguisticVariable(rs.getInt("id"), rs.getString("name"),
            rs.getString("value"), rs.getString("type"), rs.getBoolean("isactive"));
        linguisticVariable.setMfList(DaoUtils.getMFList(linguisticVariable.getValue()));
        linguisticVariable.setVariable(Variable.getVariableByName(rs.getString("name")));
        mapVariables.put(linguisticVariable.getName(),linguisticVariable);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return mapVariables;
  }


  public static void insertRule(Rule rule) throws SQLException {
    String query = "INSERT INTO cvdata.bmstu.rules "
        + " (value) "
        + " VALUES (?);";
    try (PreparedStatement pstmt = PostgreSQLConnection.getConnection().prepareStatement(query)) {
      int i = 0;
      PGobject jsonObject = new PGobject();
      jsonObject.setType("json");
      jsonObject.setValue(rule.getValue());
      pstmt.setObject(++i, jsonObject);
      pstmt.executeUpdate();
    }catch(Exception ex){
      ex.printStackTrace();
    }
  }

  public static void updateRule(Rule rule, int idRule) throws SQLException {
    String query = "UPDATE cvdata.bmstu.rules "
        + " SET value = ? WHERE id = ?;" ;
    try (PreparedStatement pstmt = PostgreSQLConnection.getConnection().prepareStatement(query)) {
        int i = 0;
        PGobject jsonObject = new PGobject();
        jsonObject.setType("json");
        jsonObject.setValue(rule.getValue());
        pstmt.setObject(++i, jsonObject);
        pstmt.setInt(++i,rule.getIdRule());
        pstmt.executeUpdate();
      }catch (Exception ex){
      ex.printStackTrace();
    }
  }

  public static String getRuleDescr(Map<String, Condition>map,String param){
    StringBuilder descr = new StringBuilder();
    for(Map.Entry<String, Condition> entry:map.entrySet()){
      String condition = entry.getKey();
      Condition cond = entry.getValue();
      descr.append(param).append(condition).append(" ").append(cond.getMembershipFunction().getNameMF());
    }
    return descr.toString();
  }


  public static Map<Integer,Rule> getMapRules() throws ParseException {
    Map<Integer,Rule> mapRules = new LinkedHashMap<>();
    org.json.simple.parser.JSONParser parser = new org.json.simple.parser.JSONParser();
    try(PreparedStatement statement = PostgreSQLConnection.getConnection().prepareStatement
        ("select id, VALUE, isactive from cvdata.bmstu.rules WHERE isactive = 'true' ORDER by id")) {
      ResultSet rs = statement.executeQuery();
      while (rs.next()){
        Map<String,Condition> ifMap = new LinkedHashMap<>();
        Map<String,Condition> andMap = new LinkedHashMap<>();
        Map<String,Condition> thenMap = new LinkedHashMap<>();

        Rule rule = new Rule(rs.getInt("id"), rs.getString("value"),rs.getBoolean("isactive"));

        //Парсинг JSON
        Object obj = parser.parse(rule.getValue());
        JSONObject jsonObject = (JSONObject) obj;
        System.out.println("Парсинг JSON: " + jsonObject.toJSONString());
        //IF
        JSONArray IFarray = (JSONArray) jsonObject.get("IF");
        System.out.println("IF: " + IFarray);
        for(int i =0;i<IFarray.size();i++){
          JSONObject ifParam =  (JSONObject) IFarray.get(i);
          int idVariable = Integer.valueOf(ifParam.get("idvariable").toString());
          //String nameMF = ifParam.get("nameMF").toString();
          String codeMF = ifParam.get("codeMF").toString();
          try(PreparedStatement pstm = PostgreSQLConnection.getConnection().prepareStatement
              ("select name, value from cvdata.bmstu.linguisticvariables where id = ?")){
            pstm.setInt(1,idVariable);
            ResultSet rs2 = pstm.executeQuery();
            if (rs2.next()){
              ArrayList<MembershipFunction> mfList = DaoUtils.getMFList(rs2.getString("value"));
              for(int j =0;j<mfList.size();j++){
                MembershipFunction m = mfList.get(j);
                if(m.getCodeMF().equals(codeMF)){
                  String nameVariable = rs2.getString("name");
                  Condition condition = new Condition(idVariable,nameVariable);
                  condition.setMembershipFunction(m);
                  ifMap.put(nameVariable,condition);
                }
              }
            }
          }
        }

        //AND
        JSONArray ANDarray = (JSONArray) jsonObject.get("AND");
        System.out.println("AND: " + ANDarray);
        for(int i =0;i<ANDarray.size();i++){
          JSONObject andParam =  (JSONObject) ANDarray.get(i);
          int idVariable = Integer.valueOf(andParam.get("idvariable").toString());
          String codeMF = andParam.get("codeMF").toString();
          try(PreparedStatement pstm = PostgreSQLConnection.getConnection().prepareStatement
              ("select name, value from cvdata.bmstu.linguisticvariables where id = ?")){
            pstm.setInt(1,idVariable);
            ResultSet rs2 = pstm.executeQuery();
            if (rs2.next()){
              ArrayList<MembershipFunction> mfList = DaoUtils.getMFList(rs2.getString("value"));
              for(int j =0;j<mfList.size();j++){
                MembershipFunction m = mfList.get(j);
                if(m.getCodeMF().equals(codeMF)){
                  String nameVariable = rs2.getString("name");
                  Condition condition = new Condition(idVariable,nameVariable);
                  condition.setMembershipFunction(m);
                  andMap.put(nameVariable,condition);
                }
              }
            }
          }
        }

        //THEN
        JSONArray THENarray = (JSONArray) jsonObject.get("THEN");
        System.out.println("THEN: " + THENarray);
        for(int i =0;i<THENarray.size();i++){
          JSONObject thenParam =  (JSONObject) THENarray.get(i);
          int idVariable = Integer.valueOf(thenParam.get("idvariable").toString());
          String codeMF = thenParam.get("codeMF").toString();
          try(PreparedStatement pstm = PostgreSQLConnection.getConnection().prepareStatement
              ("select name, value from cvdata.bmstu.linguisticvariables where id = ?")){
            pstm.setInt(1,idVariable);
            ResultSet rs2 = pstm.executeQuery();
            if (rs2.next()){
              ArrayList<MembershipFunction> mfList = DaoUtils.getMFList(rs2.getString("value"));
              for(int j =0;j<mfList.size();j++){
                MembershipFunction m = mfList.get(j);
                if(m.getCodeMF().equals(codeMF)){
                  String nameVariable = rs2.getString("name");
                  Condition condition = new Condition(idVariable,nameVariable);
                  condition.setMembershipFunction(m);
                  thenMap.put(nameVariable,condition);
                }
              }
            }
          }
        }
        rule.setIFConditionMap(ifMap);
        rule.setANDConditionMap(andMap);
        rule.setTHENConditionMap(thenMap);
        mapRules.put(rule.getIdRule(),rule);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return mapRules;
  }


  public static ArrayList<Rule> getRules() throws ParseException {
    ArrayList<Rule> listRules = new ArrayList<>();
    org.json.simple.parser.JSONParser parser = new org.json.simple.parser.JSONParser();
    try(PreparedStatement statement = PostgreSQLConnection.getConnection().prepareStatement
        ("select id, VALUE, isactive from cvdata.bmstu.rules WHERE isactive = 'true'")) {
      ResultSet rs = statement.executeQuery();
      while (rs.next()){
        Map<String,Condition> ifMap = new LinkedHashMap<>();
        Map<String,Condition> andMap = new LinkedHashMap<>();
        Map<String,Condition> thenMap = new LinkedHashMap<>();

        Rule rule = new Rule(rs.getInt("id"), rs.getString("value"),rs.getBoolean("isactive"));

        //Парсинг JSON
        Object obj = parser.parse(rule.getValue());
        JSONObject jsonObject = (JSONObject) obj;
        System.out.println("Парсинг JSON: " + jsonObject.toJSONString());
        //IF
        JSONArray IFarray = (JSONArray) jsonObject.get("IF");
        System.out.println("IF: " + IFarray);
        for(int i =0;i<IFarray.size();i++){
          JSONObject ifParam =  (JSONObject) IFarray.get(i);
          int idVariable = Integer.valueOf(ifParam.get("idvariable").toString());
          String codeMF = ifParam.get("codeMF").toString();
          try(PreparedStatement pstm = PostgreSQLConnection.getConnection().prepareStatement
              ("select name, value from cvdata.bmstu.linguisticvariables where id = ?")){
            pstm.setInt(1,idVariable);
            ResultSet rs2 = pstm.executeQuery();
            if (rs2.next()){
              ArrayList<MembershipFunction> mfList = DaoUtils.getMFList(rs2.getString("value"));
              for(int j =0;j<mfList.size();j++){
                MembershipFunction m = mfList.get(j);
                if(m.getCodeMF().equals(codeMF)){
                  String nameVariable = rs2.getString("name");
                  Condition condition = new Condition(idVariable,nameVariable);
                  condition.setMembershipFunction(m);
                  ifMap.put(nameVariable,condition);
                }
              }
            }
          }
        }

        //AND
        JSONArray ANDarray = (JSONArray) jsonObject.get("AND");
        System.out.println("AND: " + ANDarray);
        for(int i =0;i<ANDarray.size();i++){
          JSONObject andParam =  (JSONObject) ANDarray.get(i);
          int idVariable = Integer.valueOf(andParam.get("idvariable").toString());
          String codeMF = andParam.get("codeMF").toString();
          try(PreparedStatement pstm = PostgreSQLConnection.getConnection().prepareStatement
              ("select name, value from cvdata.bmstu.linguisticvariables where id = ?")){
            pstm.setInt(1,idVariable);
            ResultSet rs2 = pstm.executeQuery();
            if (rs2.next()){
              ArrayList<MembershipFunction> mfList = DaoUtils.getMFList(rs2.getString("value"));
              for(int j =0;j<mfList.size();j++){
                MembershipFunction m = mfList.get(j);
                if(m.getCodeMF().equals(codeMF)){
                  String nameVariable = rs2.getString("name");
                  Condition condition = new Condition(idVariable,nameVariable);
                  condition.setMembershipFunction(m);
                  andMap.put(nameVariable,condition);
                }
              }
            }
          }
        }

        //THEN
        JSONArray THENarray = (JSONArray) jsonObject.get("THEN");
        System.out.println("THEN: " + THENarray);
        for(int i =0;i<THENarray.size();i++){
          JSONObject thenParam =  (JSONObject) THENarray.get(i);
          int idVariable = Integer.valueOf(thenParam.get("idvariable").toString());
          String codeMF = thenParam.get("codeMF").toString();
          try(PreparedStatement pstm = PostgreSQLConnection.getConnection().prepareStatement
              ("select name, value from cvdata.bmstu.linguisticvariables where id = ?")){
            pstm.setInt(1,idVariable);
            ResultSet rs2 = pstm.executeQuery();
            if (rs2.next()){
              ArrayList<MembershipFunction> mfList = DaoUtils.getMFList(rs2.getString("value"));
              for(int j =0;j<mfList.size();j++){
                MembershipFunction m = mfList.get(j);
                if(m.getCodeMF().equals(codeMF)){
//                  String nameMF = m.getNameMF();
//                  String paramValueMF = m.getParamValueMF();
//
//                  MembershipFunction mf = new MembershipFunction(nameMF,codeMF,paramValueMF);

                  String nameVariable = rs2.getString("name");
                  Condition condition = new Condition(idVariable,nameVariable);
                  condition.setMembershipFunction(m);
                  thenMap.put(nameVariable,condition);
                }
              }
            }
          }
        }
        rule.setIFConditionMap(ifMap);
        rule.setANDConditionMap(andMap);
        rule.setTHENConditionMap(thenMap);
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
        Rule rule = new Rule(rs.getInt("id"), rs.getString("value"),rs.getBoolean("isactive"));
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

  public static void deleteRule(int id) throws SQLException {
    String query = "DELETE FROM cvdata.bmstu.rules WHERE id = ?";
    try (PreparedStatement pstmt = PostgreSQLConnection.getConnection().prepareStatement(query)) {
      int i = 0;
      pstmt.setInt(++i,id);
      pstmt.executeUpdate();
    }
  }

  public static ArrayList<Project> getProjectsList(){
    ArrayList<Project> listProjects = new ArrayList<>();
    try(PreparedStatement statement = PostgreSQLConnection.getConnection().prepareStatement("select id, name, descr from cvdata.bmstu.projects")) {
      ResultSet rs = statement.executeQuery();
      while (rs.next()){
        Project project = new Project(rs.getInt("id"), rs.getString("name"),rs.getString("descr"));
        listProjects.add(project);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return listProjects;
  }

  public static ArrayList<Category> getCategoriesList() throws ParseException {
    ArrayList<Category> listCategories = new ArrayList<>();
    org.json.simple.parser.JSONParser parser = new org.json.simple.parser.JSONParser();
    Map<Variable,String> map = new LinkedHashMap<>();
    try(PreparedStatement statement = PostgreSQLConnection.getConnection().prepareStatement("select id, name, VALUE from cvdata.bmstu.categories")) {
      ResultSet rs = statement.executeQuery();
      while (rs.next()){
        Category category = new Category(rs.getInt("id"), rs.getString("name"),rs.getString("value"));
        listCategories.add(category);

        //Парсинг JSON
        Object obj = parser.parse(category.getValue());
        JSONObject jsonObject = (JSONObject) obj;
        System.out.println("Парсинг JSON: " + jsonObject.toJSONString());

        JSONArray array = (JSONArray) jsonObject.get("values");
        //System.out.println("values: " + array);
        for(int i =0;i<array.size();i++){
          JSONObject param =  (JSONObject) array.get(i);
          String idVariable = param.get("idvariable").toString();
          String minValue = param.get("minValue").toString();
          String maxValue = param.get("maxValue").toString();
          //map.put(Variable.getVariableByName())

        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }



    return listCategories;
  }


  //Список резюме
  public static ArrayList<Vacancy> getVacanciesList(int idProject) throws ParseException {
    ArrayList<Vacancy> listVacancies = new ArrayList<>();
    org.json.simple.parser.JSONParser parser = new org.json.simple.parser.JSONParser();
    try(PreparedStatement statement = PostgreSQLConnection.getConnection().prepareStatement("select id, name, wages, value, " +
        " freeamount as busyamount, totalamount from cvdata.bmstu.vacancies WHERE idproject = ?;")) {
      statement.setInt(1,idProject);
      ResultSet rs = statement.executeQuery();

      while (rs.next()){
        Vacancy vacancy = new Vacancy(rs.getInt("id"), idProject,  rs.getString("name"),rs.getInt("wages"),
            rs.getString("value"),  rs.getInt("totalamount"),rs.getInt("busyamount"));

        //Парсинг JSON
        Object obj = parser.parse(vacancy.getValue());
        JSONObject jsonObject = (JSONObject) obj;
        System.out.println("Парсинг JSON: " + jsonObject.toJSONString());
        vacancy.setNameCategory(jsonObject.get("nameCategory").toString());

        listVacancies.add(vacancy);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return listVacancies;
  }

  public static ArrayList<CV> getCVList(String positionName) throws SQLException {

    ArrayList<CV> list = new ArrayList<>();
    StringBuilder query = new StringBuilder("SELECT q.graduateyear,id, positionname, salary, experience,busytype,idowner FROM cvdata.bmstu.cv c,\n" +
        "LATERAL (select max(e.graduateyear) as graduateyear from cvdata.bmstu.education e where e.idowner = c.idowner and e.type = 'Education') q  ");
    StringBuilder where = new StringBuilder("");

    if (where.toString().isEmpty()) {
      where.append(" where ");
    }
    where.append(" positionname ilike '%" + positionName + "%'");
    where.append(" and locality = '7700000000000'");

    if (!where.toString().isEmpty()) {
      query.append(where.toString());
    }

    query.append("order by id;");
    System.out.println(query.toString());
    try (PreparedStatement statement = PostgreSQLConnection.getConnection().prepareStatement(query.toString())) {
      ResultSet rs = statement.executeQuery();
      while (rs.next()) {
        CV cv = new CV();
        cv.setId(rs.getInt("id"));
        int graduateYear = rs.getInt("graduateyear");
        cv.setAge(2018 - graduateYear + 18 + rs.getInt("experience"));
        String position = rs.getString("positionname");
        cv.setPositionname(position);
        if (position.toLowerCase().indexOf("директор") > 0) {
          cv.setPosition(0.9);
        } else {
          if (position.toLowerCase().indexOf("начальник") > 0) {
            cv.setPosition(0.8);
          } else {
            if (position.toLowerCase().indexOf("руководитель") > 0) {
              cv.setPosition(0.7);
            } else {
              cv.setPosition(0.5);
            }
          }
        }

        cv.setSalary(rs.getInt("salary"));
        cv.setExperience(rs.getInt("experience"));
        String idOwner = rs.getString("idowner");
        String busyType = rs.getString("busytype");
        switch (busyType) {
          case "Частичная занятость":
            cv.setBusytype(0.15);
            break;
          case "Временная":
            cv.setBusytype(0.25);
            break;
          case "Полная занятость":
            cv.setBusytype(0.7);
            break;
          case "Сезонная":
          case "Стажировка":
          case "Удаленная":
            cv.setBusytype(0.1);
        }
        if (graduateYear > 0) {
          String eduQueryH = "SELECT 1 as exist FROM cvdata.bmstu.education where " +
              "(legalname ILIKE ('%университет%') or legalname ILIKE ('%институт%') or legalname ILIKE ('%академия%')) and idowner = '" + idOwner + "' limit 1;";

          try (PreparedStatement stmt2 = PostgreSQLConnection.getConnection().prepareStatement(eduQueryH.toString())) {
            ResultSet rs2 = stmt2.executeQuery();
            if (rs2.next()) {
              cv.setEducation(0.85);
            } else {
              String eduQueryM = "SELECT 1 as exist FROM cvdata.bmstu.education where " +
                  "(legalname ILIKE ('%техникум%') or legalname ILIKE ('%колледж%') or legalname ILIKE ('%сред%проф%')) and idowner = '" + idOwner + "' limit 1;";
              try (PreparedStatement stmt3 = PostgreSQLConnection.getConnection().prepareStatement(eduQueryH.toString())) {
                ResultSet rs3 = stmt3.executeQuery();
                if (rs3.next()) {
                  cv.setEducation(0.5);
                } else {
                  cv.setEducation(0.175);
                }
              }
            }
          }
        }
        list.add(cv);
      }

    }
    return list;
  }

}




