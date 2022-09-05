package Server;



import java.util.*;

public class Ball {
    private final Map<Integer, PlayerSession> players = new TreeMap<>();
    private List<Integer> allPlayers = new ArrayList<>();

    public void createPlayerSession(int id, boolean hasBall){
        PlayerSession playerSession = new PlayerSession(id, hasBall);
        players.put(id, playerSession);
        allPlayers.add(id);

    }

    public void giveBall(int from, int to){
        synchronized (players){
            if (from != to){
                if (from == 0){
                    players.get(to).setHasBall(true);
                }
                else{
                players.get(from).setHasBall(false);
                players.get(to).setHasBall(true);
                }
            }
            else{
                players.get(from).setHasBall(true);
            }
        }
    }

    public boolean hasBall(int id){
        return players.get(id).getHasBall();
    }


    public int assignId(){
        int newId = 1;

        synchronized (players){
            for (Map.Entry<Integer, PlayerSession> entry: players.entrySet()){
                newId = Math.max(newId, entry.getValue().getId()) + 1;
            }
            if (allPlayers.contains(newId)){
                newId = allPlayers.size() + 1;
            }

            createPlayerSession(newId, false);
            return newId;
        }

    }

    public List<PlayerSession> getListOfPlayers(){
        List<PlayerSession> listOfPlayers = new ArrayList<>();
        synchronized (listOfPlayers){
        if (!players.isEmpty()){
            for (PlayerSession playerSession : players.values()){
                listOfPlayers.add(playerSession);
            }
            return listOfPlayers;
        }
        }
        return null;
    }

    public void removePlayer(int playerId){
            players.remove(playerId);
    }


}
