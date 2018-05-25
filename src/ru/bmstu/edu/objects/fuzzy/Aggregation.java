package ru.bmstu.edu.objects.fuzzy;

public class Aggregation {

  public static double getAggregationResult(double mas[]){
    double minValue = 1;
    for(int i = 0;i<mas.length;i++){
      if(minValue>mas[i]){
        minValue = mas[i];
      }
    }
    return minValue;
  }

}
