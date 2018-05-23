package ru.bmstu.edu.objects;

import java.util.ArrayList;

public class MFType {

  //Определение принадлежности
  public static double getTriangleMF(ArrayList<MembershipFunction>listMF, Double value,String codeMF){
    MembershipFunction mf = new MembershipFunction();
    double result = 100;
    double VAL1 = 100;
    double VAL2 = 100;
    for(MembershipFunction m:listMF){
      String values[] = m.getParamValueMF().split(" ");
      if(m.getCodeMF().equals(codeMF)){
        double a = Double.valueOf(values[0]);
        double b = Double.valueOf(values[1]);
        double c = Double.valueOf(values[2]);
        if(value>=a && value<=b ){
          VAL1 = getValueMFA(a,b,value);
          result = VAL1;
          //System.out.println("VAL1: " + VAL1);
        }else{
          if(value>=b && value<=c){
            VAL2 = getValueMFB(b,c,value);
            result = VAL2;
            //System.out.println("VAL2: " + VAL2);
          }else{
            result = 0;
          }
        }
      }
    }
    return result;
  }

  public static MembershipFunction getTrapMF(ArrayList<MembershipFunction>listMF, Double value){
    MembershipFunction mf = new MembershipFunction();
    double VAL1 = 0;
    double VAL2 = 0;
    for(MembershipFunction m:listMF){
      String values[] = m.getParamValueMF().split(" ");
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
