
import java.util.ArrayList;
import java.util.Scanner;



public class Agent_AGV {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int rad2 = Integer.parseInt(scanner.nextLine());
        int rad1 = Integer.parseInt(scanner.nextLine());
        int impulse = Integer.parseInt(scanner.nextLine());
        int cars = Integer.parseInt(scanner.nextLine());
        System.err.println("RAD0:" + rad2 + " RAD1:" + rad1 + " IMPULSE: " + impulse);
        while (true) {
            int myscore = Integer.parseInt(scanner.nextLine());
            int enemyScore = Integer.parseInt(scanner.nextLine());
            int currentWinner = Integer.parseInt(scanner.nextLine());
            int entities = Integer.parseInt(scanner.nextLine());
            System.err.println("SCORES: " + myscore + " - " + enemyScore);
            System.err.println("WINNER: " + currentWinner);

            ArrayList<Car> carPoses = new ArrayList<>();
            ArrayList<Ball> balls = new ArrayList<>();
            ArrayList<Car> rival = new ArrayList<>();
            // MY CARS
            for(int i = 0; i < entities; i++){
                String s = scanner.nextLine(); // //{car.id, type, (int)car.x, (int)car.y, (int)car.vx, (int)car.vy, angle, ballId};
                String[] line = s.split(" ");
                System.err.println("INPUT: " + s);
                int type = Integer.parseInt(line[1]);
                int x = Integer.parseInt(line[2]);
                int y = Integer.parseInt(line[3]);
                int vx = Integer.parseInt(line[4]);
                int vy = Integer.parseInt(line[5]);
                int angle = Integer.parseInt(line[6]);
                int ballId = Integer.parseInt(line[7]);
                if(type == 0)
                    carPoses.add(new Car(x, y, ballId, vx, vy));
                if(type == 1)
                    rival.add(new Car(x, y, ballId, vx, vy));
                if(type == 2)
                    balls.add(new Ball(x, y, ballId, vx, vy));
            }

 /*           Car target = new Car(0,0,0);
            if(balls.size()>0) target = balls.get(0);
            for(int i = 0; i < cars; i++){
                Car car = carPoses.get(i);
                if(car.ballId != -1) target = new Car(0,0,0);
                int thrust = 150;
                String s = i == 0? "BotAGE_1a" : "BotAGE_1b";
                if(target.distance(car) < 550) thrust = 10;
                System.out.println(target.X + " " + target.Y + " " + thrust + " " + s);
            }
            */
            
            for(int a=0; a<rival.size(); a++) {
            	
            	int [] b = {0, 0};
            	carPoses.get(a).setRival(rival.get(a).ballId);
            	carPoses.get(a).setObjetive(b);
            	carPoses.get(a).setThrust(75);
            	
            }
            
            	for(int x=0; x<rival.size(); x++) {
            		if(rival.get(x).ballId!=-1) {
            			for(int y=0; y<carPoses.size(); y++) {
            				if(carPoses.get(y).getRival()==rival.get(x).ballId) {
            					if(rival.get(x).distanceC(carPoses.get(y))<500) {
            						int riv[] = {rival.get(x).getX(), rival.get(x).getY()};
            						carPoses.get(y).setThrust(250);
            						carPoses.get(y).setObjetive(riv);
            					}
            				}
            			}
            		}
            	
            }
            
            for(int i=0; i<carPoses.size(); i++) {
            	String s = i == 0? "BotAGE_1a" : "BotAGE_1b";
            	System.out.println(carPoses.get(i).getObjetive()[0] + " " + carPoses.get(i).getObjetive()[1] + " "
            	+ carPoses.get(i).getThrust() + " " + s);
            }
            
        }
         
    }

    public static class Car{
        public int X, Y, ballId, VX, VY, rival, thrust;
        public int [] objetive = new int[2];
        public Car(int x, int y, int vx, int vy, int ballId){
            X = x;
            Y = y;
            VX = vx;
            VY = vy;
            this.ballId = ballId;
        }
        
		public int getThrust() {
			return thrust;
		}
		public void setThrust(int thrust) {
			this.thrust = thrust;
		}
		 public int getRival() {
			return rival;
		}

		public void setRival(int rival) {
			this.rival = rival;
		}

		public int[] getObjetive() {
			return objetive;
		}

		public void setObjetive(int[] objetive) {
			this.objetive = objetive;
		}

		public double distanceC(Car c2){
             return Math.sqrt(Math.pow(c2.X-X, 2) + Math.pow(c2.Y-Y, 2));
         }

		public int getX() {
			return X;
		}

		public int getY() {
			return Y;
		}
          
    }
    
    public static class Ball{
        public int X, Y, ballId, VX, VY;
        public Ball(int x, int y, int vx, int vy, int ballId){
            X = x;
            Y = y;
            VX = vx;
            VY = vy;
            this.ballId = ballId;
        }
    }
}