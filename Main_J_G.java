import com.codingame.gameengine.runner.MultiplayerGameRunner;
import com.codingame.gameengine.runner.dto.GameResult;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;


public class Main_J_G {


    public static void main(String[] args) throws IOException, CloneNotSupportedException {

        File f = new File("/Users/vir/Downloads/Desafio_Final_CODE/src/test/java/results.txt");
        f.delete();

        final int INDIVIDUALS = 10;
        final int N_CYCLES = 10;



        /* Multiplayer Game */

        ArrayList<Individual> population = new ArrayList<>();

        Random random = new Random();
        //Inicializacion
        for (int x = 0; x < INDIVIDUALS; x++) {


            Individual individual = new Individual();

            for (int i = 0; i < 2; i++) {
                individual.code.add(random.nextDouble());
            }

            individual.code.add(random.nextDouble() * 200);

            individual.code.add(random.nextDouble() * 1000);

            individual.code.add(random.nextDouble() * 100);

            for (int i = 0; i < individual.code.size(); i++) {
                individual.variances.add(random.nextDouble() * 10);
            }


            String a = "/Users/vir/Downloads/Desafio_Final_CODE/src/test/java/code" + x + ".txt";

            FileWriter fileWriter = new FileWriter(a);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.print(individual.code.toString());
            printWriter.close();

            individual.fittnessVal = getFitness(a);
            population.add(individual);

        }


        //listo para ejecutar algoritmo

        for (int i = 0; i < N_CYCLES; i++) {
            int best = getBest(population);
            PrintWriter pw = new PrintWriter(new FileOutputStream(new File("/Users/vir/Downloads/Desafio_Final_CODE/src/test/java/results.txt"), true));
            PrintWriter pw2 = new PrintWriter(new FileOutputStream(new File("/Users/vir/Downloads/Desafio_Final_CODE/src/test/java/output.txt")));
            pw2.println(i + "- " + best + " Best this Cycle : " + population.get(best).fittnessVal + " with code:" + population.get(best).code.toString());
            pw2.close();
            //mutacion

            ArrayList<Individual> oldPopulation = new ArrayList<>();

            for (int j = 0; j < population.size(); j++) {
                oldPopulation.add(population.get(j).clone());
            }


            for (int j = 0; j < population.size(); j++) {

                double freq = population.get(j).getImpFreq();

                for (int k = 0; k < population.get(j).code.size(); k++) {

                    double nextVal = (random.nextGaussian() * population.get(j).variances.get(k));
                    double lastVal = population.get(j).code.get(k);
                    lastVal += nextVal;
                    if (lastVal < 0) {
                        lastVal = 0;
                    } else if ((k == 2 && lastVal > 200) || (k == 4 && lastVal > 200)) {
                        lastVal = 200;
                    }

                    population.get(j).code.set(k, lastVal);

                    if (freq > 0.2) {
                        population.get(j).variances.set(k, (population.get(j).variances.get(k)) * 1.18);
                    } else if (freq < 0.2) {
                        population.get(j).variances.set(k, (population.get(j).variances.get(k)) * 0.82);
                    }

                }


            }

            //cruce

            for (int j = 0; j < population.size(); j++) {

                String a = "/Users/vir/Downloads/Desafio_Final_CODE/src/test/java/code" + j + ".txt";

                FileWriter fileWriter = new FileWriter(a);
                PrintWriter printWriter = new PrintWriter(fileWriter);
                printWriter.print(population.get(j).code.toString());
                printWriter.close();

                population.get(j).fittnessVal = getFitness(a);
            }

            for (int j = 0; j < population.size(); j++) {

                if (oldPopulation.get(j).fittnessVal > population.get(j).fittnessVal) {
                    population.set(j, oldPopulation.get(j));
                    population.get(j).addMiss();
                } else {
                    population.get(j).addHit();
                }

            }

            printFitnessToFile(population, pw);
            pw.close();
            for (int j = 0; j < INDIVIDUALS; j++) {
                File fd = new File("/Users/vir/Downloads/Desafio_Final_CODE/src/test/java/code" + j + ".txt");
                fd.delete();
            }

        }


        File fa = new File("/Users/vir/Downloads/Desafio_Final_CODE/src/test/java/code.txt");
        fa.delete();
        File fb = new File("/Users/vir/Downloads/Desafio_Final_CODE/src/test/java/score.txt");
        fb.delete();

    }

    public static void printFitnessToFile(ArrayList<Individual> population, PrintWriter pw) {
        for (int i = 0; i < population.size(); i++) {
            if (i == population.size() - 1) {
                pw.print(population.get(i).fittnessVal + "\n");
            } else {
                pw.print(population.get(i).fittnessVal + ", ");
            }
        }
    }

    public static int getBest(ArrayList<Individual> population) {
        double maximum = population.get(0).fittnessVal;
        int index = 0;
        for (int i = 1; i < population.size(); i++) {
            Double a = population.get(i).fittnessVal;
            if (a > maximum) {
                maximum = a;
                index = i;
            }
        }
        return index;
    }


    public static double getFitness(String fname) throws IOException {

        BufferedReader in = new BufferedReader(new FileReader(fname));
        String line2 = in.readLine();
        String a = "/Users/vir/Downloads/Desafio_Final_CODE/src/test/java/code.txt";
        FileWriter fileWriter = new FileWriter(a);
        PrintWriter printWriter = new PrintWriter(fileWriter);
        printWriter.print(line2);
        printWriter.close();

        MultiplayerGameRunner gameRunner = new MultiplayerGameRunner();
        gameRunner.setLeagueLevel(4);

        gameRunner.addAgent(AgentAGE_Gabriel_G.class);
        gameRunner.addAgent(AgentAGE_Gabriel.class);
        GameResult result = gameRunner.simulate();
        Integer myScore = result.scores.get(0);
        Integer rivalScore = result.scores.get(1);
        double fitness = myScore - rivalScore;

        for (int i = 1; i < 5; i++) {
            MultiplayerGameRunner gameRunner2 = new MultiplayerGameRunner();
            gameRunner2.setLeagueLevel(4);

            gameRunner2.addAgent(AgentAGE_Gabriel_G.class);
            gameRunner2.addAgent(AgentAGE_Gabriel.class);
            GameResult result2 = gameRunner2.simulate();
            Integer myScore2 = result2.scores.get(0);
            Integer rivalScore2 = result2.scores.get(1);
            double fitness2 = myScore2 - rivalScore2;
            fitness = (fitness + fitness2) / 2;
        }

        return fitness;
    }

    public static class Individual implements Cloneable {

        final int EVAL_SIZE = 10;

        public ArrayList<Double> code;
        public ArrayList<Double> variances;
        public ArrayList<Integer> improval;
        public double fittnessVal;

        public Individual() {
            code = new ArrayList<>();
            variances = new ArrayList<>();
            improval = new ArrayList<>();
        }

        @Override
        protected Individual clone() throws CloneNotSupportedException {
            Individual clone = null;
            clone = (Individual) super.clone();
            clone.fittnessVal = this.fittnessVal;
            clone.variances = (ArrayList<Double>) this.variances.clone();
            clone.code = (ArrayList<Double>) this.code.clone();
            clone.improval = (ArrayList<Integer>) this.improval.clone();
            return clone;
        }

        public void addMiss() {
            if (improval.size() >= EVAL_SIZE) {
                improval.remove(0);
                improval.add(improval.size() - 1, 0);
            } else if (improval.size() != 0) {
                improval.add(improval.size() - 1, 0);
            } else {
                improval.add(0);
            }
        }

        public void addHit() {
            if (improval.size() >= EVAL_SIZE) {
                improval.remove(0);
                improval.add(improval.size() - 1, 1);
            } else if (improval.size() != 0) {
                improval.add(improval.size() - 1, 1);
            } else {
                improval.add(1);
            }
        }

        public double getImpFreq() {
            double sum = 0.0;
            for (int i = 0; i < improval.size(); i++) {
                sum += improval.get(i);
            }
            sum /= improval.size();
            return sum;
        }


    }

}
