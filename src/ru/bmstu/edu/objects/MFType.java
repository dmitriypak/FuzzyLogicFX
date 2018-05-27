package ru.bmstu.edu.objects;

import java.util.ArrayList;

public class MFType {

  //Определение принадлежности
  public static double getTriangleMF(ArrayList<MembershipFunction>listMF, Double value,String codeMF){
    double result = 100;
    for(MembershipFunction m:listMF){
      String values[] = m.getParamValueMF().split(" ");
      if(m.getCodeMF().equals(codeMF)){
        double a = Double.valueOf(values[0]);
        double b = Double.valueOf(values[1]);
        double c = Double.valueOf(values[2]);
        if(value>=a && value<=b ){
          result = getValueMFA(a,b,value);
        }else{
          if(value>=b && value<=c){
            result = getValueMFB(b,c,value);
          }else{
            result = 0;
          }
        }
      }
    }
    return result;
  }

  public static double getTrapMF(ArrayList<MembershipFunction>listMF, Double value,String codeMF){
    double result = 100;

    for(MembershipFunction m:listMF){
      String values[] = m.getParamValueMF().split(" ");
      if(m.getCodeMF().equals(codeMF)){
        double a = Double.valueOf(values[0]);
        double b = Double.valueOf(values[1]);
        double c = Double.valueOf(values[2]);
        double d = Double.valueOf(values[3]);
        if(value>=a && value<=b ){
          result = getValueMFA(a,b,value);
        }else{
          if(value>=b && value<=c){
            result = 1;
          }else{
            if(value>=c&&value<=d){
              result = getValueMFB(c,d,value);
            }else {
              result = 0;
            }
          }
        }
      }
    }
    return result;
  }

  private static double getValueMFA(double a, double b, double x){
    if(b==a){
      return 1-(b-x)/1;
    }else{
      return 1-(b-x)/(b-a);
    }

  }
  private static double getValueMFB(double b, double c, double x){
    return 1-(x-b)/(c-b);
  }


  private static double getValueMFC(double c, double d, double x){

    return 1-(x-c)/(d-x);

  }
}
