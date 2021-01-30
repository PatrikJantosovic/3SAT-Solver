package com.satsolver.kop;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    static void getSolution(SATInstance instace, String filename){
        String fileID = instace.ID+" ";
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line = br.readLine();
            while (line != null && !line.startsWith(fileID))
            {
                //bordel na zaciatku fily
                line = br.readLine();
            }
            Matcher m = Pattern.compile("\\d+").matcher(line);
            List<Integer> numbers = new ArrayList<>();
            while(m.find()) {
                numbers.add(Integer.parseInt(m.group()));
            }
            instace.SolutionWeight = numbers.get(2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static SATInstance readInstance(String filename){
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line = br.readLine();
            while (line != null && line.startsWith("c"))
            {
              //bordel na zaciatku fily
                line = br.readLine();
            }
            int variableCount = 1;
            int clausesCount = 1;
            int sumWeight = 0;
            if (line.startsWith("p mwcnf")) {
                Matcher m = Pattern.compile("\\d+").matcher(line);
                List<Integer> numbers = new ArrayList<>();
                while(m.find()) {
                    numbers.add(Integer.parseInt(m.group()));
                }
                variableCount = numbers.get(0);
                clausesCount = numbers.get(1);
            }
            line = br.readLine();
            while (line != null && line.startsWith("c"))
            {
                //bordel na zaciatku fily
                line = br.readLine();
            }
            List<Integer> weights = new ArrayList<>(variableCount);
            if (line != null && line.startsWith("w")) {
                Matcher m = Pattern.compile("\\d+").matcher(line);
                while(m.find()) {
                    int w = Integer.parseInt(m.group());
                    sumWeight+=w;
                    if(w != 0) weights.add(w);
                }
            }
            line = br.readLine();
            while (line != null && line.startsWith("c"))
            {
                //bordel na zaciatku fily
                line = br.readLine();
            }
            List<Clause> clauses = new ArrayList<>(clausesCount);
            for(int i = 0; i < clausesCount -1; i++){
                // nacitaj jeden riadok po 3 inty
                List<Integer> literals = new ArrayList<>(3);
                Matcher m = Pattern.compile("-?\\d+").matcher(line);
                while(m.find()) {
                    int literal = Integer.parseInt(m.group());
                    if(literal != 0) literals.add(literal);
                }
                Clause clause = new Clause(literals);
                clauses.add(clause);
                line = br.readLine();
                if(line == null) break;
            }

            return new SATInstance(weights, clauses, filename.substring(1).substring(0, filename.indexOf(".") - 1),sumWeight);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static void main(String[] args) {
        for(int i = 1; i<=10; i++) {
            SATInstance instance = readInstance("wuf20-0"+i+".mwcnf");
            getSolution(instance, "wuf20-78-N-opt.dat");
            SASolver solver = new SASolver(0.1d, instance.MaximumWeight/10d, 0.95, instance.NumberOfVariables*3);
            Solution solution = solver.solve(instance);
            //System.out.println(solution.toString());
            System.out.println(solution.PrintSolution());
            solution.PrintLog();
        }
        //50 fily:
        List<String> dataFiles = new ArrayList<>();
        dataFiles.add("wuf50-012.mwcnf");
        dataFiles.add("wuf50-0123.mwcnf");
        dataFiles.add("wuf50-0414.mwcnf");
        dataFiles.add("wuf50-0430.mwcnf");
        dataFiles.add("wuf50-0434.mwcnf");
        dataFiles.add("wuf50-0441.mwcnf");
        dataFiles.add("wuf50-0452.mwcnf");
        dataFiles.add("wuf50-0470.mwcnf");
        for(String file : dataFiles) {
            SATInstance instance = readInstance(file);
            getSolution(instance, "wuf50-201-N-opt.dat");
            SASolver solver = new SASolver(0.1d, instance.MaximumWeight/10d, 0.95, instance.NumberOfVariables*3);
            Solution solution = solver.solve(instance);
            //System.out.println(solution.toString());
            System.out.println(solution.PrintSolution());
            solution.PrintLog();
        }
    }
}
