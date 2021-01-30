package com.satsolver.kop;

import java.util.List;

public class Clause {
  public List<Integer> Variables;
  public boolean Satisfied;

  public Clause(List<Integer> variables){
    this.Satisfied = false;
    this.Variables = variables;
  }

  public void evaluate(boolean[] configuration){
    // ak je jedno z nich true tak je to true
    for(Integer variable : this.Variables){
      int index = Math.abs(variable) - 1;
      if((variable>0 && configuration[index]) || (variable<0 && !configuration[index])){
        this.Satisfied = true;
        return;
      }
    }
    this.Satisfied = false;
  }

  @Override
  public String toString() {
    String output = "";
    for (Integer literal : Variables) {
      output += literal.toString();
      output += " ";
    }
    output += "\n";
    return output;
  }

}
