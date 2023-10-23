import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

class Player {
    String name;
    int score;

    public Player(String name) {
        this.name = name;
        this.score = 0;
    }
}

class Game {
    Player player1;
    Player player2;
    Player winner;
    String gameType;

    public Game(Player player1, Player player2, String gameType) {
        this.player1 = player1;
        this.player2 = player2;
        this.winner = null;
        this.gameType = gameType;
    }
}

class Tournament {
    List<Player> participants;
    List<Game> games;
    List<Player> winners; // Keep track of winners for each round
    String tournamentType;

    public Tournament(String tournamentType) {
        this.tournamentType = tournamentType;
        participants = new ArrayList<>();
        games = new ArrayList<>();
        winners = new ArrayList<>();
    }
}

public class TournamentManager {
    public static void main(String[] args) {
        List<Player> players = new ArrayList<>();
        List<Tournament> tournaments = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("1. Add Player");
            System.out.println("2. Create Game (Chess)");
            System.out.println("3. Create Game (Tic-Tac-Toe)");
            System.out.println("4. Create Tournament (Semifinals)");
            System.out.println("5. Create Tournament (Finals)");
            System.out.println("6. Log Game Result");
            System.out.println("7. Exit");

            String choice = scanner.nextLine();

            if (choice.equals("1")) {
                System.out.print("Enter player name: ");
                String playerName = scanner.nextLine();
                Player player = new Player(playerName);
                players.add(player);
            } else if (choice.equals("2") || choice.equals("3")) {
                if (players.size() < 2) {
                    System.out.println("There must be at least two players to create a game.");
                } else {
                    String gameType = (choice.equals("2")) ? "Chess" : "Tic-Tac-Toe";

                    System.out.println("Select two players for the " + gameType + " game:");
                    for (int i = 0; i < players.size(); i++) {
                        System.out.println((i + 1) + ". " + players.get(i).name);
                    }
                    int player1Index = Integer.parseInt(scanner.nextLine()) - 1;
                    int player2Index;
                    do {
                        player2Index = Integer.parseInt(scanner.nextLine()) - 1;
                    } while (player2Index == player1Index);

                    Player player1 = players.get(player1Index);
                    Player player2 = players.get(player2Index);

                    Game game = new Game(player1, player2, gameType);
                    if (!tournaments.isEmpty()) {
                        tournaments.get(tournaments.size() - 1).games.add(game);
                    } else {
                        System.out.println("No active tournament to add the game to.");
                    }
                }
            } else if (choice.equals("4") || choice.equals("5")) {
                if (players.size() < 2) {
                    System.out.println("There must be at least two players to create a tournament.");
                } else {
                    String tournamentType = (choice.equals("4")) ? "Semifinals" : "Finals";

                    Tournament tournament = new Tournament(tournamentType);
                    Random rand = new Random();
                    List<Player> shuffledPlayers = new ArrayList<>(players);
                    for (int i = 0; i < players.size(); i++) {
                        int j = rand.nextInt(i + 1);
                        Player temp = shuffledPlayers.get(i);
                        shuffledPlayers.set(i, shuffledPlayers.get(j));
                        shuffledPlayers.set(j, temp);
                    }
                    for (int i = 0; i < Math.min(2, players.size()); i++) {
                        tournament.participants.add(shuffledPlayers.get(i));
                    }
                    tournaments.add(tournament);
                }
            } else if (choice.equals("6")) {
                if (tournaments.isEmpty() || tournaments.get(tournaments.size() - 1).games.isEmpty()) {
                    System.out.println("No games to log.");
                } else {
                    Tournament tournament = tournaments.get(tournaments.size() - 1);
                    Game game = tournament.games.get(tournament.games.size() - 1);
                    if (game.winner != null) {
                        System.out.println("Game already logged. The winner is: " + game.winner.name);
                    } else {
                        System.out.print("Enter the winner (1 for " + game.player1.name + " or 2 for " + game.player2.name + "): ");
                        int winnerIndex = Integer.parseInt(scanner.nextLine());
                        if (winnerIndex == 1) {
                            game.winner = game.player1;
                        } else if (winnerIndex == 2) {
                            game.winner = game.player2;
                        } else {
                            System.out.println("Invalid winner selection.");
                        }

                        if (game.winner != null) {
                            game.winner.score++;
                            System.out.println("Game result logged. The winner is: " + game.winner.name);

                            if (tournament.tournamentType.equals("Semifinals")) {
                                // Store the winner of the semifinals for the finals
                                tournament.winners.add(game.winner);
                            }
                        }
                    }
                }
            } else if (choice.equals("7")) {
                System.out.println("Exiting the Tournament Manager.");
                break;
            } else {
                System.out.println("Invalid choice. Please select a valid option.");
            }
        }

        scanner.close();
    }
}
