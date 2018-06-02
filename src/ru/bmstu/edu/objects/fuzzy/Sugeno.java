package ru.bmstu.edu.objects.fuzzy;

import ru.bmstu.edu.objects.Condition;
import ru.bmstu.edu.objects.MembershipFunction;
import ru.bmstu.edu.objects.Rule;

import java.util.ArrayList;
import java.util.Map;

public class Sugeno {

  public static double getAggregationResult(ArrayList<Double> list){
    double minValue = 1;
    for(int i = 0;i<list.size();i++){
      if(minValue>list.get(i)){
        minValue = list.get(i);
      }
    }
    return minValue;
  }


  public static double getAccumulationResult(double[] mas){
    double maxValue = 0;
    for(int i = 0;i<mas.length;i++){
      if(maxValue<mas[i]){
        maxValue = mas[i];
      }
    }
    return maxValue;
  }

  public static double getCenterOfGravityResult(Map<Integer,Rule> mapRules){
    double result = 0;
    double dy = 0;
    double ydy = 0;
    for(Map.Entry<Integer,Rule> r:mapRules.entrySet()){

      Rule outputRule = r.getValue();
      double x = outputRule.getValueOutput();
      //Правило активное
      if(x>0){
        Map<String,Condition> conditionMap = outputRule.getTHENConditionMap();
        double minValue = 0;
        double maxValue = 0;
        for(Map.Entry<String,Condition> c:conditionMap.entrySet()) {
          MembershipFunction mf = c.getValue().getMembershipFunction();
          String values[] = mf.getParamValueMF().split(" ");
          switch (values.length){
            case 3:
              minValue = Double.valueOf(values[0]);
              maxValue = Double.valueOf(values[2]);
              break;
            case 4:
              minValue = Double.valueOf(values[0]);
              maxValue = Double.valueOf(values[3]);
              break;

          }
        }
        dy+= Sugeno.getIntegralDY(minValue,maxValue,x);
        ydy+=Sugeno.getIntegralYDY(minValue,maxValue,x);

      }

    }

    result = ydy/dy;

    return result;
  }

  private static double getIntegralDY(double a, double b, double x){
    double result = 0;
    result = (b*x-a*x);
    return result;
  }

  private static double getIntegralYDY(double a, double b, double x){
    double result = 0;
    result = (b*b/2)*x-(a*a/2)*x;
    return result;
  }


}
