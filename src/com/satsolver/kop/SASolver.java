package com.satsolver.kop;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class SASolver {
  private double FreezingTemp;
  private double Cooling;
  private int Steps;
  private double CurrentTemp;
  private SATInstance satInstance;
  private StringBuilder Log;

  public SASolver(double freezingTemp, double initTemp, double cooling, int steps) {
    this.FreezingTemp = freezingTemp;
    this.CurrentTemp = initTemp;
    this.Cooling = cooling;
    this.Steps = steps;
    this.Log = new StringBuilder();
  }

  private State tryNext(State state){
    State neighbour = getNeighborState(state);
    int delta = neighbour.SumWeight - state.SumWeight;
    if (delta > 0) return neighbour;
    Random random = new Random();
    double r = Math.random(); // vracia double medzi 0 a 1
    if (r < Math.exp(delta / this.CurrentTemp)) return neighbour;
    return state;
  }

  private State better(State best, State neighbor){
    if(neighbor.SumWeight > best.SumWeight && neighbor.Satisfied) return neighbor;
    return best;
  }

  private int calculateWeight(boolean[] configuration){
    int sumOfWeight = 0;
    for(int i = 0; i<configuration.length; i++){
      if(configuration[i]){
        sumOfWeight += this.satInstance.Weights.get(i);
      }
    }
    return sumOfWeight;
  }

  private void evaluate(State state){
    List<Integer> remainingVariables = new ArrayList<>();
    for (Clause clause : this.satInstance.Clauses) {
      clause.evaluate(state.Variables);
      if(!clause.Satisfied){
        remainingVariables.addAll(clause.Variables);
      }
    }
    state.RemainingVariables = remainingVariables;
    state.Satisfied = state.RemainingVariables.isEmpty();
    state.SumWeight = calculateWeight(state.Variables);
  }

  public Solution solve(SATInstance instance) {
    this.satInstance = instance;
    long startTime = System.nanoTime();
    State current = new State(instance.NumberOfVariables);
    State best = new State(instance.NumberOfVariables);
    while (CurrentTemp > FreezingTemp) {
      for (int i = 0; i < Steps; i++) {
        current = tryNext(current);
        best = better(best, current);
        // Logovanie je hrozne pomale
        Log.append(current.SumWeight+"\n");
      }
      CurrentTemp *= Cooling;
    }
    long endTime = System.nanoTime();
    return new Solution(best, endTime - startTime, instance, Log.toString());
  }

  private State getNeighborState(State state) {
    State newState = new State(state);
    int index;
    boolean[] configuration = Arrays.copyOf(state.Variables, state.Variables.length);
    Random random = new Random();
    if (!state.RemainingVariables.isEmpty()) {
      int randomPosition = random.nextInt(state.RemainingVariables.size());
      index = Math.abs(state.RemainingVariables.get(randomPosition)) - 1;
    } else {
      index = random.nextInt(state.Variables.length);
    }
    configuration[index] = !configuration[index];
    newState.Variables = configuration;
    evaluate(newState);
    return newState;
  }

}
