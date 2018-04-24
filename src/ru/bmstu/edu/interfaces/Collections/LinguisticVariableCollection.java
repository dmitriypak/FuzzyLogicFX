package ru.bmstu.edu.interfaces.Collections;

import javafx.collections.ObservableList;
import ru.bmstu.edu.objects.LinguisticVariable;

public interface LinguisticVariableCollection {

  LinguisticVariable get(int index);
  void add(LinguisticVariable linguisticVariable);
  void delete(LinguisticVariable linguisticVariable);
  void update(int index, LinguisticVariable linguisticVariable);
  ObservableList getVariablesList();
  void clear();
}
