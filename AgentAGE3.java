

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.StringTokenizer;


public class AgentAGE3 {
    public static void main(String[] args) throws IOException {
        //al principio
        Scanner scanner = new Scanner(System.in);
        int rad2 = Integer.parseInt(scanner.nextLine());
        int rad1 = Integer.parseInt(scanner.nextLine());
        int impulse = Integer.parseInt(scanner.nextLine());
        int cars = Integer.parseInt(scanner.nextLine());

        BufferedReader in = new BufferedReader(new FileReader("/Users/vir/Downloads/Desafio_Final_CODE/src/test/java/code.txt"));
        String line2 = in.readLine(); // <-- read whole line
        String[] line3 = line2.split(",");
        line3[0] = line3[0].replace("[", "");
        line3[line3.length - 1] = line3[line3.length - 1].replace("]", "");
        System.err.println(Arrays.toString(line3));


        System.err.println("RAD0:" + rad2 + " RAD1:" + rad1 + " IMPULSE: " + impulse);
        while (true) {
            //cada turno
            int myscore = Integer.parseInt(scanner.nextLine());
            int enemyScore = Integer.parseInt(scanner.nextLine());
            int fitness = myscore - enemyScore;
            System.err.println(fitness);
            FileWriter fileWriter = new FileWriter("/Users/vir/Downloads/Desafio_Final_CODE/src/test/java/score.txt");
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.print(fitness);
            printWriter.close();
            int currentWinner = Integer.parseInt(scanner.nextLine());
            int entities = Integer.parseInt(scanner.nextLine());
            System.err.println("SCORES: " + myscore + " - " + enemyScore);
            System.err.println("WINNER: " + currentWinner);
            int thrust = 0;
            ArrayList<Car> carPoses = new ArrayList<>();
            ArrayList<Ball> balls = new ArrayList<>();
            ArrayList<Car> rivalCars = new ArrayList<>();
            // MY CARS
            for (int i = 0; i < entities; i++) {
                String s = scanner.nextLine(); // //{car.id, type, (int)car.x, (int)car.y, (int)car.vx, (int)car.vy, angle, ballId};
                String[] line = s.split(" ");
                System.err.println("INPUT: " + s);
                int type = Integer.parseInt(line[1]);
                int x = Integer.parseInt(line[2]);
                int y = Integer.parseInt(line[3]);
                int vx = Integer.parseInt(line[4]);
                int vy = Integer.parseInt(line[5]);
                int angle = Integer.parseInt(line[6]);
                int id = Integer.parseInt(line[7]);
                if (type == 0)
                    carPoses.add(new Car(x, y, vx, vy, angle, id));
                if (type == 1)
                    rivalCars.add(new Car(x, y, vx, vy, angle, id));
                if (type == 2)
                    balls.add(new Ball(x, y, vx, vy, id));
            }

            Car car1 = carPoses.get(0);
            Car car2 = carPoses.get(1);
            String robotA = 0 + " " + 0 + " " + 50 + " BotAGE_1a", robotB = 0 + " " + 0 + " " + 50 + " BotAGE_1b";

            if (balls.size() > 0) {

                Ball targetBall = balls.get(findClosestBall(car1, balls));
                //calcular posicion aprox en 50ms

                double distance = getDistanceCB(car1, targetBall);

                int quince = Integer.parseInt(line3[15].replace(" ", ""));
                int sixteen = Integer.parseInt(line3[16].replace(" ", ""));
                int seventeen = Integer.parseInt(line3[17].replace(" ", ""));


                if (car1.VX > quince && car1.VY > sixteen) {
                    thrust = Integer.parseInt(line3[10].replace(" ", ""));
                } else {
                    thrust = Integer.parseInt(line3[11].replace(" ", ""));
                }

                if (distance < seventeen) {
                    thrust = Integer.parseInt(line3[12].replace(" ", ""));
                }

                int nextX = targetBall.X + targetBall.VX + Integer.parseInt(line3[13].replace(" ", ""));
                int nextY = targetBall.Y + targetBall.VY + Integer.parseInt(line3[14].replace(" ", ""));
                robotA = nextX + " " + nextY + " " + thrust + " BotAGE_1a";
                robotB = nextX + " " + nextY + " " + thrust + " BotAGE_1b";
                System.err.println("E. Ir Bola");


                if (balls.size() > 1) {
                    int closestBall2 = findClosestBall(car2, balls);
                    Ball targetBall2 = balls.get(closestBall2);
                    if (targetBall2 == targetBall) {
                        balls.remove(targetBall2);
                        targetBall2 = balls.get(0);
                    }
                    int thrust2 = Integer.parseInt(line3[0].replace(" ", ""));
                    if (car2.VX > quince && car2.VY > sixteen) {
                        thrust2 = Integer.parseInt(line3[1].replace(" ", ""));
                    }
                    if (distance < seventeen) {
                        thrust2 = Integer.parseInt(line3[2].replace(" ", ""));
                    }

                    int nextX2 = targetBall2.X + targetBall2.VX + Integer.parseInt(line3[13].replace(" ", ""));
                    int nextY2 = targetBall2.Y + targetBall2.VY + Integer.parseInt(line3[14].replace(" ", ""));
                    robotB = nextX2 + " " + nextY2 + " " + thrust2 + " BotAGE_1b";
                    System.err.println("E. Ir Bola2 C2");


                } else {

                    int thrust2 = Integer.parseInt(line3[3].replace(" ", ""));
                    if (car2.VX > quince && car2.VY > sixteen) {
                        thrust2 = Integer.parseInt(line3[4].replace(" ", ""));
                    }
                    if (distance < seventeen) {
                        thrust2 = Integer.parseInt(line3[5].replace(" ", ""));
                    }

                    //una pelota enemigo

                    Car attackCar = rivalCars.get(whoHasBall(rivalCars));
                    int nextXAC = attackCar.X + attackCar.VX + Integer.parseInt(line3[10].replace(" ", ""));
                    int nextYAC = attackCar.Y + attackCar.VY + Integer.parseInt(line3[11].replace(" ", ""));
                    if (car1.carID == -1) {
                        robotB = nextXAC + " " + nextYAC + " " + thrust2 + " BotAGE_1b";
                        System.err.println("E. Ir Enemigo C2");
                    }
                }

            } else {

                //ellos tienen las dos

                Car attackCar = rivalCars.get(whoHasBall(rivalCars));
                int nextXAC = attackCar.X + attackCar.VX + Integer.parseInt(line3[13].replace(" ", ""));
                int nextYAC = attackCar.Y + attackCar.VY + Integer.parseInt(line3[14].replace(" ", ""));
                robotA = nextXAC + " " + nextYAC + " " + Integer.parseInt(line3[6].replace(" ", "")) + " BotAGE_1a";
                robotB = nextXAC + " " + nextYAC + " " + Integer.parseInt(line3[7].replace(" ", "")) + " BotAGE_1b";
                System.err.println("A por ellos.");

            }

            if (car1.carID != -1) {
                System.err.println("E. Marcar C1");
                robotA = 0 + " " + 0 + " " + Integer.parseInt(line3[8].replace(" ", "")) + " BotAGE_1b";
            }


            if (car2.carID != -1) {
                System.err.println("E. Marcar C2");
                robotB = 0 + " " + 0 + " " + Integer.parseInt(line3[9].replace(" ", "")) + " BotAGE_1b";
            }


            System.out.println(robotA);
            System.out.println(robotB);

        }
    }


    public static double getDistanceCB(Car a1, Ball a2) {
        return Math.sqrt(Math.pow(a1.X - a2.X, 2) + Math.pow(a1.Y - a2.Y, 2));
    }

    public static int whoHasBall(ArrayList<Car> rivalCars) {
        for (int i = 0; i < rivalCars.size(); i++) {
            Car rivalCar = rivalCars.get(i);
            if (rivalCar.carID != 1) return i;
        }
        return -1;
    }


    public static int findClosestBall(Car car1, ArrayList<Ball> balls) {
        double min = getDistanceCB(car1, balls.get(0));

        int lowestVal = 0;
        for (int i = 1; i < balls.size(); i++) {
            double newVal = getDistanceCB(car1, balls.get(i));

            if (newVal < min) {
                lowestVal = i;
            }

        }

        return lowestVal;
    }

    public static class Car {

        public int X, Y, VX, VY, angle, carID;

        public Car(int x, int y, int vx, int vy, int angle, int carID) {
            X = x;
            Y = y;
            VX = vx;
            VY = vy;
            this.angle = angle;
            this.carID = carID;
        }


    }

    public static class Ball {
        public int X, Y, VX, VY, ballId;

        public Ball(int x, int y, int vx, int vy, int ballId) {
            X = x;
            Y = y;
            VX = vx;
            VY = vy;
            this.ballId = ballId;
        }


    }
}
