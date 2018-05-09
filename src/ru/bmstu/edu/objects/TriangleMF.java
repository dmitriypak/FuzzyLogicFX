package ru.bmstu.edu.objects;

import java.util.ArrayList;

public class TriangleMF {

  //Определение принадлежности
  public static MembershipFunction getMF(ArrayList<MembershipFunction>listMF, Double value){
    MembershipFunction mf = new MembershipFunction();
    double VAL1 = 0;
    double VAL2 = 0;
    for(MembershipFunction m:listMF){
      String values[] = m.getParamValueMF().split(" ");

      double val1 = Double.valueOf(values[0]);
      double val2 = Double.valueOf(values[1]);
      double val3 = Double.valueOf(values[2]);
        if(value>=val1 && value<=val2 ){
          VAL1 = getValueMFA(val1,val2,value);
          System.out.println("VAL1: " + VAL1);
        }
        if(value>=val2 && value<=val3){
          System.out.println("VAL2: " + VAL2);
        }

    }


    return mf;
  }
  //Получение значение по формуле для треугольной функции
  private static double getValueMFA(double a, double b, double x){
    return 1-(b-x)/(b-a);
  }
  private static double getValueMFB(double b, double c, double x){
    return 1-(x-b)/(c-b);
  }

}
