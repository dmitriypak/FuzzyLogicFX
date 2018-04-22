package ru.bmstu.edu.controllers;

import javafx.beans.property.ObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.controlsfx.control.textfield.CustomTextField;
import org.controlsfx.control.textfield.TextFields;
import ru.bmstu.edu.objects.MembershipFunction;

import java.lang.reflect.Method;


public class EditMembershipFunctionController {

  @FXML
  private CustomTextField txtNameMF;
  @FXML
  private CustomTextField txtParamMF;

  private MembershipFunction membershipFunction;


  public void initialize() {
    setupClearButtonField(txtNameMF);
    setupClearButtonField(txtParamMF);

  }


  public void setupClearButtonField(CustomTextField txtFunction){
    try {
      Method m = TextFields.class.getDeclaredMethod("setupClearButtonField", TextField.class, ObjectProperty.class);
      m.setAccessible(true);
      m.invoke(null, txtFunction, txtFunction.rightProperty());
    }catch (Exception e){
      e.printStackTrace();
    }
  }

  public void setMF(MembershipFunction membershipFunction){
    if(membershipFunction==null){
      return;
    }
    this.membershipFunction=membershipFunction;
    txtNameMF.setText(membershipFunction.getMFname());
    txtParamMF.setText(membershipFunction.getMFParamValue());
  }

  public MembershipFunction getMF(){
    return membershipFunction;
  }

  public void actionClose(ActionEvent actionEvent) {
    Node source = (Node) actionEvent.getSource();
    Stage stage = (Stage) source.getScene().getWindow();
    stage.hide();
  }



}
