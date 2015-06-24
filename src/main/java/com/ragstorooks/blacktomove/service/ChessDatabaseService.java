package com.ragstorooks.blacktomove.service;

import com.ragstorooks.blacktomove.chess.PGNParser;
import com.ragstorooks.blacktomove.database.Game;
import com.ragstorooks.blacktomove.database.GameBuilder;
import com.ragstorooks.blacktomove.database.GameDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Path("game")
public class ChessDatabaseService {
    private static final String ERROR_MESSAGE = "Unable to save game to database";

    private static final Logger logger = LoggerFactory.getLogger(PGNParser.class);

    private PGNParser pgnParser;
    private GameDAO gameDAO;

    public ChessDatabaseService(PGNParser pgnParser, GameDAO gameDAO) {
        this.pgnParser = pgnParser;
        this.gameDAO = gameDAO;
    }

    @POST
    @Path("/pgn")
    @Consumes(MediaType.TEXT_PLAIN)
    public Response saveGamesWithPgn(String pgn) {
        logger.info("Parsing pgn and saving games");
        Map<String, com.ragstorooks.blacktomove.chess.Game> games = pgnParser.parseMultiGamePGN(pgn);
        games.entrySet().stream().forEach(game -> {
            try {
                gameDAO.saveGame(convertToDatabaseObject(game.getKey(), game.getValue()));
            } catch (ExecutionException e) {
                logger.error(ERROR_MESSAGE, e);
            } catch (InterruptedException e) {
                logger.error(ERROR_MESSAGE, e);
            }
        });

        return null;
    }

    @GET
    @Path("/position/{position}")
    @Produces(MediaType.APPLICATION_JSON)
    public GameList findGamesWithPosition(@PathParam("position") String position) {
        logger.info("Finding games with position {}", position);
        List<String> pgns = new ArrayList<>();
        List<Game> games = gameDAO.findGamesWithPosition(position);
        games.forEach(game -> pgns.add(game.getFullPgn()));

        logger.info("Found {} games with position {}", pgns.size(), position);
        return new GameList(pgns);
    }

    private Game convertToDatabaseObject(String pgn, com.ragstorooks.blacktomove.chess.Game game) {
        Map<String, String> gameMetadata = game.getMetadata();
        GameBuilder gameBuilder = new GameBuilder(
                getAndRemove(gameMetadata, "Event"),
                getAndRemove(gameMetadata, "Site"),
                getAndRemove(gameMetadata, "Date"),
                getAndRemove(gameMetadata, "Round"),
                getAndRemove(gameMetadata, "White"),
                getAndRemove(gameMetadata, "Black"),
                getAndRemove(gameMetadata, "Result")
        ).setFullPgn(pgn);

        gameMetadata.entrySet().forEach(entry -> gameBuilder.addAdditionalInfo(entry.getKey(), entry.getValue()));
        game.getPositions().forEach(position -> gameBuilder.addPosition(position));

        return gameBuilder.getGame();
    }

    private String getAndRemove(Map<String, String> gameMetadata, String key) {
        String result = gameMetadata.get(key);
        gameMetadata.remove(key);
        return result;
    }
}
