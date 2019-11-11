import java.util.ArrayList;
import java.util.Scanner;

public class AgentAGE_Jorge {
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

			// Lista de los coches propios y las bolas
			ArrayList<Car> carPoses = new ArrayList<>();
			ArrayList<Car> balls = new ArrayList<>();

			// Para cada entidad que se lee, se debe anadir a la lista correspondiente
			for (int i = 0; i < entities; i++) {
				String s = scanner.nextLine();
				// {car.id, type, (int)car.x, (int)car.y, (int)car.vx, (int)car.vy, angle,
				// ballId};
				String[] line = s.split(" ");
				System.err.println("INPUT: " + s);
				int type = Integer.parseInt(line[1]);
				int x = Integer.parseInt(line[2]);
				int y = Integer.parseInt(line[3]);
				int ballId = Integer.parseInt(line[7]);
				if (type == 0)
					carPoses.add(new Car(x, y, ballId));
				if (type == 2)
					balls.add(new Car(x, y, ballId));
			}

			// distancia de los coches del equipo a la bola
			double distCocheBola[][] = new double[carPoses.size()][balls.size()];

			// Aquellos coches que no tienen una bola, actualizaran su distancia a la bola
			// mas cercana
			for (int i = 0; i < distCocheBola.length; i++) {
				for (int j = 0; j < distCocheBola[i].length; j++) {
					if (carPoses.get(i).ballId == -1) {// coches sin bola
						distCocheBola[i][j] = carPoses.get(i).distance(balls.get(j));
					}
				}
			}

			int bolaminima = 0;
			// asignacion del objetivo
			for (int i = 0; i < carPoses.size(); i++) {
				Car car = carPoses.get(i);
				Car target = new Car(0, 0, 0);
				if (car.ballId == -1) {// Si el coche no tiene una bola, busca la bola mas cercana
					for (int j = 0; j < distCocheBola[i].length; j++) {
						if (j == 0) {
							bolaminima = j;
							target = balls.get(j);
						} else {
							if (distCocheBola[i][bolaminima] > distCocheBola[i][j]) {
								bolaminima = j;
								target = balls.get(j);
							}
						}
					}
				}

				int thrust = 150;
				String s = i == 0 ? "BotAGE_1a" : "BotAGE_1b";
				if (target.distance(car) < 550)
					thrust = 10;
				System.out.println(target.X + " " + target.Y + " " + thrust + " " + s);
			}
		}
	}

	public static class Car {
		public int X, Y, ballId;

		public Car(int x, int y, int ballId) {
			X = x;
			Y = y;
			this.ballId = ballId;
		}

		public double distance(Car c2) {
			return Math.sqrt(Math.pow(c2.X - X, 2) + Math.pow(c2.Y - Y, 2));
		}
	}
}