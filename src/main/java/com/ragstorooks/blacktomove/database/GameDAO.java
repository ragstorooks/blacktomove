package com.ragstorooks.blacktomove.database;


import java.util.List;
import java.util.concurrent.ExecutionException;

public interface GameDAO {
    Long saveGame(Game game) throws ExecutionException, InterruptedException;

    List<Game> findGamesWithPosition(String position);

    Game findGameById(String id);
}