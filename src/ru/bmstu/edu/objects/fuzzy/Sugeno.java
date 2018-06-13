package ru.bmstu.edu.objects.fuzzy;

import ru.bmstu.edu.objects.MembershipFunction;

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

  public static double getCenterOfGravitySingletons(Map<MembershipFunction,Double> mapGraph){
    double result = 0;
    double sumY = 0;
    double sumXY = 0;
    for(Map.Entry<MembershipFunction,Double> m:mapGraph.entrySet()){
      double y = m.getValue();
      //Правило активное
      if(y>0){
         MembershipFunction mf = m.getKey();
         Double x = mf.getConstantSugeno();
         sumY += y;
         sumXY+=x*y;
      }
    }
    //System.out.println("SUM XY " + sumXY + "|" + "SUMY " + sumY);
    result = sumXY/sumY;

    return result;
  }


}


