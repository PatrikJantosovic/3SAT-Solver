package com.satsolver.kop;

import java.util.ArrayList;
import java.util.List;

public class State {
  public int SumWeight;
  public boolean[] Variables;
  public boolean Satisfied;
  public List<Integer> RemainingVariables;

  public State(int numberOfVariables){
    this.Satisfied = false;
    this.Variables = new boolean[numberOfVariables];
    this.SumWeight = 0;
    this.RemainingVariables = new ArrayList<>();
    for (int i = 1; i <= numberOfVariables; i++) {
      RemainingVariables.add(i);
    }
  }

  public State(State state){
    this.Satisfied = state.Satisfied;
    this.Variables = state.Variables.clone();
    this.SumWeight = state.SumWeight;
    this.RemainingVariables = new ArrayList<>();
    this.RemainingVariables.addAll(state.RemainingVariables);
  }

}
