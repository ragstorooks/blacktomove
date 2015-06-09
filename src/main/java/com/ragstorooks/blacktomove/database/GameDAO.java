package com.ragstorooks.blacktomove.database;


import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public interface GameDAO {
    Map saveGame(Game game) throws ExecutionException, InterruptedException;

    List<Game> findGamesWithPosition(String position);
}