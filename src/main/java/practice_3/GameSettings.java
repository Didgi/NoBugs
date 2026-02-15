package practice_3;

public class GameSettings {

    static int maxPlayers;
    final String gameName;
    int currentPlayers;

    GameSettings(String gameName, int currentPlayers){
        this.gameName = gameName;
        this.currentPlayers = currentPlayers;
    }

    static void setMaxPlayers(int numberPlayers){
        maxPlayers = numberPlayers;
    }

    void addPlayer(){
        if (this.currentPlayers < maxPlayers){
            this.currentPlayers ++;
        }
        else {
            System.out.println("Превышен лимит игроков в игре " + this.gameName);
        }
    }

    void printGameStatus(){
        System.out.println("В игру " + this.gameName + " сейчас играет " + this.currentPlayers +
                " игроков из максимально доступного количества " + maxPlayers);
    }
}
