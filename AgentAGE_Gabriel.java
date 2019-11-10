
import java.util.ArrayList;
import java.util.Scanner;

public class AgentAGE_Gabriel {
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
			ArrayList<Car> carOpos = new ArrayList<>();// Coches del oponente
			ArrayList<Car> balls = new ArrayList<>();

			for (int i = 0; i < entities; i++) {
				String s = scanner.nextLine(); // //{car.id, type, (int)car.x, (int)car.y, (int)car.vx, (int)car.vy, angle, ballId};
				String[] line = s.split(" ");
				System.err.println("INPUT: " + s);
				int type = Integer.parseInt(line[1]);
				int x = Integer.parseInt(line[2]);
				int y = Integer.parseInt(line[3]);
				int vx = Integer.parseInt(line[4]);
				int vy = Integer.parseInt(line[5]);

				int ballId = Integer.parseInt(line[7]);
				if (type == 0)
					carPoses.add(new Car(x, y, vx, vy, ballId));
				if (type == 1)
					carOpos.add(new Car(x, y, vx, vy, ballId));
				if (type == 2)
					balls.add(new Car(x, y, vx, vy, ballId));
			}

			int carVsBall[] = new int[cars];
			double distanceB[] = new double[cars];

			int carVsOponent[] = new int[cars];
			double distanceC[] = new double[cars];

			for (int i = 0; i < cars; i++) {
				carVsBall[i] = -1;
				carVsOponent[i] = -1;
			}

			for (int i = 0; i < balls.size(); i++) {// Asignamos objetivo para cada coche de entre los prisioneros libres o los coches oponentes con prisionero
				for (int j = 0; j < cars; j++) {
					if (carPoses.get(j).ballId == -1) {
						if (carVsBall[i] == -1) {// buscando prisionero
							carVsBall[i] = i;
							distanceB[i] = carPoses.get(j).distance(balls.get(i));
						} else if (carPoses.get(j).distance(balls.get(i)) < distanceB[i]) {
							carVsBall[i] = i;
							distanceB[i] = carPoses.get(j).distance(balls.get(i));
						}
					}
				}
			}

			for (int i = 0; i < cars; i++) {
				for (int j = 0; j < cars; j++) {
					if (carPoses.get(j).ballId == -1 && carOpos.get(i).ballId!=-1) {
						if (carVsOponent[i] == -1) {// buscando coche oponente
							carVsOponent[i] = i;
							distanceC[i] = carPoses.get(j).distance(carOpos.get(i));
						} else if (carPoses.get(j).distance(carOpos.get(i)) < distanceC[i]) {
							carVsOponent[i] = i;
							distanceC[i] = carPoses.get(j).distance(carOpos.get(i));
						}
					}
				}
			}

			for (int i = 0; i < cars; i++) {
				Car car = carPoses.get(i);

				Car target = new Car(0, 0, 0, 0, 0);

				int posX = 0, posY = 0;// modificacion de la direccion en la que ir para compensar la direccion en la que el prisionero o coche oponente esta yendo
				double aumentoX = 0.0, aumentoY = 0.0; // Parametros a modificar por un AGE

				int if_A_Usar = -1;
				
				if(car.ballId == -1) {
					if (carVsBall[i] != -1 && carVsOponent[i] != -1) {
						if (distanceB[i] < distanceC[i]) {
							if_A_Usar = 0;// Dependiendo de que esta mas cerca, coche oponente o prisionero, iremos a por uno u otro
						}else {
							if_A_Usar = 1;
						}
					} else if (carVsBall[i] == -1 && carVsOponent[i] != -1) {
						if_A_Usar = 1;
					}else if (carVsBall[i] != -1 && carVsOponent[i] == -1) {
						if_A_Usar = 0;
					}
				}

				if (if_A_Usar == 0) { // Tenemos un prisionero asignado
					target = balls.get(carVsBall[i]);
					posX = (int) (target.X + target.VX * aumentoX);
					posY = (int) (target.Y + target.VY * aumentoY);

				} else if (if_A_Usar == 1) {// No llevamos prisionero ni tenemos asignado uno por lo que iremos a por un jugador del otro equipo
					target = carOpos.get(carVsOponent[i]);
					posX = (int) (target.X + target.VX * aumentoX);
					posY = (int) (target.Y + target.VY * aumentoY);
				}

				int thrust = 150;
				if (target.distance(car) < 550) thrust = 10;

				String s = i == 0 ? "G_1a:"+posX+":"+posY : "G_1b:"+posX +":"+posY;

				System.out.println(posX + " " + posY + " " + thrust + " " + s);
			}
		}
	}

	public static class Car {
		public int X, Y, VX, VY, ballId, objetivo;

		public Car(int x, int y, int vx, int vy, int ballId) {
			X = x;
			Y = y;
			VX = vx;
			VY = vy;
			this.ballId = ballId;
		}

		public double distance(Car c2) {
			return Math.sqrt(Math.pow(c2.X - X, 2) + Math.pow(c2.Y - Y, 2));
		}
	}
}
