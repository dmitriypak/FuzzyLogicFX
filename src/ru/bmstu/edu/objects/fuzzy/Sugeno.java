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


  public static double getCenterOfGravitySingletons(Map<Integer,Rule> mapRules){
    double result = 0;
    double sumY = 0;
    double sumXY = 0;


    for(Map.Entry<Integer,Rule> r:mapRules.entrySet()){
      Rule outputRule = r.getValue();

      double y = outputRule.getValueOutput();
      if(y>0){
        Map<String,Condition> mapTHEN = outputRule.getTHENConditionMap();
        for(Map.Entry<String, Condition> entry:mapTHEN.entrySet()) {
          Condition condition = entry.getValue();
          MembershipFunction mfOut = condition.getMembershipFunction();
          Double x = mfOut.getConstantSugeno();
          sumY += y;
          sumXY+=x*y;
        }
      }
    }

    //System.out.println("SUM XY " + sumXY + "|" + "SUMY " + sumY);
    result = sumXY/sumY;

    return result;
  }

//  public static double getCenterOfGravitySingletons(Map<MembershipFunction,Double> mapGraph){
//    double result = 0;
//    double sumY = 0;
//    double sumXY = 0;
//    for(Map.Entry<MembershipFunction,Double> m:mapGraph.entrySet()){
//      double y = m.getValue();
//      //Правило активное
//      if(y>0){
//         MembershipFunction mf = m.getKey();
//         Double x = mf.getConstantSugeno();
//         sumY += y;
//         sumXY+=x*y;
//      }
//    }
//    //System.out.println("SUM XY " + sumXY + "|" + "SUMY " + sumY);
//    result = sumXY/sumY;
//
//    return result;
//  }


}


