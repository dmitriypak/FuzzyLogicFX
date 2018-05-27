package ru.bmstu.edu.objects.fuzzy;

import java.util.ArrayList;

public class Mamdani {

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




}
