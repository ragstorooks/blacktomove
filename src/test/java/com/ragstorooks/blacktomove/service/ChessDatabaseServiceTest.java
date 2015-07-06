package com.ragstorooks.blacktomove.service;

import com.ragstorooks.blacktomove.chess.PGNParser;
import com.ragstorooks.blacktomove.database.Game;
import com.ragstorooks.blacktomove.database.GameBuilder;
import com.ragstorooks.blacktomove.database.GameDAO;
import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.util.Arrays;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ChessDatabaseServiceTest {
    private static final String ONE = "one";
    private static final String TWO = "two";
    private static final String THREE = "three";

    private Long id = 1L;

    @Mock
    private PGNParser pgnParser;

    @Mock
    private GameDAO gameDAO;
    @InjectMocks
    private ChessDatabaseService chessDatabaseService;

    @Test
    public void testThatGameIsSavedWithPgn() throws Exception {
        // setup
        when(gameDAO.saveGame(isA(Game.class))).thenReturn(id);

        String pgn = "test pgn";
        when(pgnParser.parsePGN(pgn)).thenReturn(new com.ragstorooks.blacktomove.chess.Game());

        // act
        Response response = chessDatabaseService.savePgn(pgn);

        // verify
        verify(gameDAO).saveGame(isA(Game.class));
        assertThat(response.getStatus(), equalTo(Status.CREATED.getStatusCode()));
        assertThat(response.getLocation().toString(), equalTo("game/id/1"));
    }

    @Test
    public void testThatPgnsAreReturnedForAGivenPosition() {
        // setup
        String position = "test position";
        when(gameDAO.findGamesWithPosition(position)).thenReturn(Arrays.asList(buildGame(ONE), buildGame(TWO),
                buildGame(THREE)));

        // act
        GameList result = chessDatabaseService.findGamesWithPosition(position);

        // verify
        assertThat(result.games.size(), equalTo(3));
        assertTrue(CollectionUtils.isEqualCollection(result.games, Arrays.asList(ONE, TWO, THREE)));
    }

    @Test
    public void testThatPgnIsReturnedForAGivenGameId() {
        // setup
        String id = "test id";
        when(gameDAO.findGameById(id)).thenReturn(buildGame(ONE));

        // act
        String result = chessDatabaseService.findGameById(id);

        // verify
        assertThat(result, equalTo(ONE));
    }

    private Game buildGame(String pgn) {
        return new GameBuilder(null, null, null, null, null, null, null).setFullPgn(pgn).getGame();
    }
}