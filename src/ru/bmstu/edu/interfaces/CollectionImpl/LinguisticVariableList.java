package ru.bmstu.edu.interfaces.CollectionImpl;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ru.bmstu.edu.interfaces.Collections.LinguisticVariableCollection;
import ru.bmstu.edu.objects.LinguisticVariable;


public class LinguisticVariableList implements LinguisticVariableCollection{

  public static ObservableList<LinguisticVariable> variablesList = FXCollections.observableArrayList();

  @Override
  public LinguisticVariable get(int index) {
    return variablesList.get(index);
  }

  @Override
  public void add(LinguisticVariable linguisticVariable) {
    variablesList.add(linguisticVariable);
  }

  @Override
  public void delete(LinguisticVariable linguisticVariable) {
    variablesList.remove(linguisticVariable);
  }

  @Override
  public void update(int index, LinguisticVariable linguisticVariable) {
    variablesList.set(index,linguisticVariable);
  }

  public ObservableList<LinguisticVariable> getVariablesList() {
    return variablesList;
  }

  @Override
  public void clear() {
    variablesList.clear();
  }

}
