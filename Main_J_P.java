import com.codingame.gameengine.runner.MultiplayerGameRunner;
import com.codingame.gameengine.runner.dto.GameResult;

import java.util.ArrayList;

public class Main2 {
    public static void main(String[] args) {

        /* Multiplayer Game */
//        MultiplayerGameRunner gameRunner = new MultiplayerGameRunner();
//        gameRunner.setLeagueLevel(4);
//
//        gameRunner.addAgent(AgentAGE4.class);
//        gameRunner.addAgent(AgentAGE_Jorge.class);

        //gameRunner.simulate(); //Sirve para simular partidas
        //gameRunner.setSeed(1L);
        //gameRunner.start(); //Sirve para arrancar el servidor

        ArrayList<Double> results = new ArrayList<>();

        for (int i = 0; i < 197; i++) {
            System.out.println("\n\n---------------------Cycle N-" + i + "------------------\n\n");
            MultiplayerGameRunner gameRunner = new MultiplayerGameRunner();
            gameRunner.setLeagueLevel(4);

            gameRunner.addAgent(AgentAGE4.class);
            gameRunner.addAgent(AgentAGE_Jorge.class);
            GameResult result = gameRunner.simulate();
            Integer myScore = result.scores.get(0);
            Integer rivalScore = result.scores.get(1);
            if (myScore > rivalScore) results.add(0.0);
            else if (rivalScore > myScore) results.add(1.0);
            else results.add(2.0);
        }
        double player0 = 0, player1 = 0, draw = 0;
        for (int i = 0; i < results.size(); i++) {
            if (results.get(i) == 0) player0++;
            else if (results.get(i) == 1) player1++;
            else if (results.get(i) == 2) draw++;
        }

        double player0freq = player0 / results.size();
        double player1freq = player1 / results.size();
        double drawfreq = draw / results.size();

        System.out.println("-----------------------------------------\nNumber of Matches: " + results.size());
        System.out.println("Player 0 won " + player0freq * 100 + "% of the matches.");
        System.out.println("Player 1 won " + player1freq * 100 + "% of the matches.");
        System.out.println("The match ended in draw " + drawfreq * 100 + "% of the matches.");


    }
}
