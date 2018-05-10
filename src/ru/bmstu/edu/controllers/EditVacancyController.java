package ru.bmstu.edu.controllers;

import javafx.fxml.FXML;
import org.controlsfx.control.textfield.CustomTextField;
import ru.bmstu.edu.objects.Vacancy;

public class EditVacancyController {

  @FXML
  private CustomTextField txtVacancy;
  @FXML
  private CustomTextField txtWages;

  private Vacancy vacancy;

  public Vacancy getVacancy(){
    return vacancy;
  }
  public void setVacancy(Vacancy vacancy){
    if(vacancy==null){
      return;
    }
    System.out.println("Получена вакансия id: " + vacancy.getId());
    this.vacancy=vacancy;

    if(vacancy.getId()!=0){
      txtVacancy.setText(vacancy.getName());
      txtWages.setText(String.valueOf(vacancy.getWages()));
    }else{
      txtWages.clear();
      txtVacancy.clear();
    }

  }
}
