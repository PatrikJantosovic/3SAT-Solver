package com.satsolver.kop;

import java.util.List;

public class SATInstance {
  public Integer NumberOfVariables;
  public Integer NumberOfClauses;
  public List<Clause> Clauses;
  public List<Integer> Weights;
  public String ID;

  public Integer SolutionWeight = 0;
  public Integer MaximumWeight = 0;

  public SATInstance(List<Integer> weights, List<Clause> clauses, String id, int maximumWeight){
    this.Clauses = clauses;
    this.Weights = weights;
    this.NumberOfClauses = clauses.size();
    this.NumberOfVariables = weights.size();
    this.ID = id;
    this.MaximumWeight = maximumWeight;
  }

  @Override
  public String toString() {
    String output = "Instance " + ID + " of " + NumberOfClauses + " clauses and " + NumberOfVariables + " variables.\n";
    output += "Weights: ";
    for(Integer w : Weights){
      output += w + " , ";
    }
    output += "\n";
    for (Clause clause : Clauses) {
      output += clause.toString();
    }
    return output;
  }

}
