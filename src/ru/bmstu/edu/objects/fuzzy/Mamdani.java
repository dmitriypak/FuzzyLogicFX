package ru.bmstu.edu.objects.fuzzy;

public class Mamdani {

  public static double getAggregationResult(double[] mas){
    double minValue = 1;
    for(int i = 0;i<mas.length;i++){
      if(minValue>mas[i]){
        minValue = mas[i];
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
