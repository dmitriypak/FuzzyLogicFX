package ru.bmstu.edu.controllers;

import javafx.fxml.FXML;
import org.controlsfx.control.textfield.CustomTextField;
import ru.bmstu.edu.objects.Category;
import ru.bmstu.edu.objects.LinguisticVariable;
import ru.bmstu.edu.objects.utils.DaoUtils;

import java.util.ArrayList;

public class EditCategoryController {

  @FXML
  private CustomTextField txtNameCategory;

  private Category category;



  public Category getCategory(){
    return category;
  }
  public void setCategory(Category category){
    if(category==null){
      return;
    }
    System.out.println("Получена категория id: " + category.getId());
    this.category=category;

    if(category.getId()!=0){
      txtNameCategory.setText(category.getName());
    }else{
      txtNameCategory.clear();
    }

  }
}
