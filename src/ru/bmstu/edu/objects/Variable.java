package ru.bmstu.edu.objects;

public enum Variable {

  SALARY ("Размер заработной платы"),
  WORK_EXPERIENCE("Стаж работы"),
  RANK("РАНГ");


  private String name;

  Variable(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }


  public static Variable getVariableByName(String name) {
    for (Variable variable : Variable.values()) {
      if (variable.name.equalsIgnoreCase(name)) {
        return variable;
      }
    }
    return null;
  }

}
