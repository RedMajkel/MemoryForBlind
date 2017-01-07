package pl.polsl.piechota.michal.memoryforblind.controller.services;

import org.junit.Before;
import org.junit.Test;

import pl.polsl.piechota.michal.memoryforblind.model.BoardModel;

import static org.junit.Assert.assertEquals;
import static pl.polsl.piechota.michal.memoryforblind.model.utils.Const.HEIGHT;
import static pl.polsl.piechota.michal.memoryforblind.model.utils.Const.WIDTH;

/**
 * Created by michalp on 2017-01-07.
 */
public class InGameServiceTest {
    private InGameService testee;

    @Before
    public void setUp() {
        testee = InGameService.getInstance();
    }

    @Test
    public void createBoard() throws Exception {
        BoardModel testeeBoard = testee.createBoard(4, 4);

        assertEquals(4, testeeBoard.getHeight());
        assertEquals(4, testeeBoard.getWidth());
    }

    @Test
    public void calculateBoard() throws Exception {
        testee.calculateBoard(8);

        assertEquals(4, WIDTH);
        assertEquals(4, HEIGHT);
    }

}