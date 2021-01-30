package com.satsolver.kop;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Arrays;

public class Solution {
  public State Best;
  public double Time;
  public SATInstance Instance;
  public String Log;
  public double RelativeError;

  public Solution(State state, long time, SATInstance instance, String log){
    this.Best = state;
    this.Time = time / 1000000d; //ms
    this.Instance = instance;
    this.Log = log;
    this.RelativeError = calcError();
  }

  private double calcError(){
    if(this.Instance.SolutionWeight==this.Best.SumWeight) return 0d;
    double error = 0d;
    error = Math.abs(this.Instance.SolutionWeight - this.Best.SumWeight);
    error = error / this.Instance.SolutionWeight;
    return error;
  }

  public void PrintLog(){
    try (PrintWriter out = new PrintWriter("log_"+Instance.ID+".txt")) {
      out.println(Log);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }

  public String PrintSolution() {
    String output = Instance.ID +";" + Instance.SolutionWeight +";" + Best.SumWeight + ";" + Time + ";" + RelativeError;
    return output;
  }

  @Override
  public String toString() {
    String output = "Instance: " + Instance.ID +" Expected Weight: " + Instance.SolutionWeight +" Weight: " + Best.SumWeight + " Time: " + Time + " Error: "+ RelativeError+"\n";
    output+= " Variables: "+ Arrays.toString(Best.Variables);
    return output;
  }

}

