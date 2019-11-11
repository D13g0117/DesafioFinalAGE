

import java.util.ArrayList;
import java.util.Scanner;

public class AgentAGE1 {
    public static void main(String[] args) {
        //al principio
        Scanner scanner = new Scanner(System.in);
        int rad2 = Integer.parseInt(scanner.nextLine());
        int rad1 = Integer.parseInt(scanner.nextLine());
        int impulse = Integer.parseInt(scanner.nextLine());
        int cars = Integer.parseInt(scanner.nextLine());
        System.err.println("RAD0:" + rad2 + " RAD1:" + rad1 + " IMPULSE: " + impulse);
        while (true) {
            //cada turno
            int myscore = Integer.parseInt(scanner.nextLine());
            int enemyScore = Integer.parseInt(scanner.nextLine());
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
            String robotA = "", robotB = "";
            boolean printedA = false, printedB = false;
            //codigo coche 1

            if (car1.carID != -1) {
                System.err.println("q1");
                if (!printedA) {
                    robotA = 0 + " " + 0 + " " + 50 + " BotAGE_1a";
                    printedA = true;
                }

            }


            if (car2.carID != -1) {
                System.err.println("q2");
                if (!printedB) {
                    robotB = 0 + " " + 0 + " " + 50 + " BotAGE_1b";
                    printedB = true;
                }

            }

            if (balls.size() > 0) {
                //hay bolas libres por el mapa

                Ball targetBall = balls.get(findClosestBall(car1, balls));
                //calcular posicion aprox en 50ms


                double distance = getDistanceCB(car1, targetBall);
                thrust = 100;
                if (distance < 550) {
                    thrust = 20;
                }
                System.err.println(targetBall.X + " Speed X: " + targetBall.VX + " " + targetBall.Y + " Speed Y: " + targetBall.VY);
                int nextX = targetBall.X + targetBall.VX ;
                int nextY = targetBall.Y + targetBall.VY ;
                if (!printedA) {
                    robotA = nextX + " " + nextY + " " + thrust + " BotAGE_1a";
                    printedA = true;
                }

                String toDo = spinBot(car2, rad1, 40);
                toDo += " BotAGE_1b";
                if (!printedB) {
                    robotB = toDo;
                    printedB = true;
                }

            } else {
                // no quedan bolas libres
                if (car1.carID == -1 && car2.carID == -1) {
                    //ellos tienen todas las bolas
                    //estrategia quitar bola y defender porteria
                    //coche 1 quita bola
                    System.err.println("Both Have Balls");
                    int index = whoHasBall(rivalCars);
                    if (index >= 0) {
                        thrust = 120;
                        if (!printedA) {
                            robotA = rivalCars.get(index).X + " " + rivalCars.get(index).Y + " " + thrust + " BotAGE_1a";
                            printedA = true;
                        }

                    }
                    //coche 2 defiende porteria
                    String toDo = spinBot(car2, rad1, 40);
                    toDo += " BotAGE_1b";
                    if (!printedB) {
                        robotB = toDo;
                        printedB = true;
                    }


                } else if (car1.carID != -1) {
                    //car1 tiene bola
                    //car2 va a por el enemigo
                    System.err.println("h1");
                    int index = whoHasBall(rivalCars);
                    System.err.println(index);
                    if (index >= 0) {
                        thrust = 100;
                        if (!printedB) {
                            robotB = rivalCars.get(index).X + " " + rivalCars.get(index).Y + " " + thrust + " BotAGE_1b";
                            printedB = true;
                        }
                    }
                } else {
                    //car2 tiene bola
                    //car1 va a por el enemigo
                    System.err.println("h2");
                    int index = whoHasBall(rivalCars);
                    if (index >= 0) {
                        thrust = 100;
                        if (!printedA) {
                            robotA = rivalCars.get(index).X + " " + rivalCars.get(index).Y + " " + thrust + " BotAGE_1a";
                            printedA = true;
                        }
                    }
                }
            }

            System.out.println(robotA);
            System.out.println(robotB);

        }
    }

    public static String spinBot(Car a1, int radius, int speed) {
        String result = "";
        if (a1.X > radius || a1.Y > radius) {
            result += "0 0 " + speed + " ";
        } else if (a1.X <= radius && a1.Y <= radius) {
            result += "0 0 " + speed + " ";
            //inside radius, go around
        }
        return result;
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
        System.err.println("Distance to ball: " + min);
        int lowestVal = 0;
        for (int i = 1; i < balls.size(); i++) {
            double newVal = getDistanceCB(car1, balls.get(i));
            System.err.println("Distance to ball: " + newVal);
            if (newVal < min) {
                lowestVal = i;
            }

        }
        System.err.println("Closest ball is: " + balls.get(lowestVal).X + " " + balls.get(lowestVal).Y);
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
