package pl.polsl.piechota.michal.memoryforblind.controller.listeners;

import android.view.MotionEvent;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Created by michalp on 2017-01-07.
 */
@RunWith(MockitoJUnitRunner.class)
public class GestureListenerTest {
    @Mock
    private GestureListener testee;

    @Mock
    private MotionEvent e1mock;

    @Mock
    private MotionEvent e2mock;

    @Before
    public void setUp() {
        testee = new GestureListener();
    }

    @Test
    public void testOnSwipeDown() throws Exception {
        Mockito.when(e1mock.getX()).thenReturn(0.0f);
        Mockito.when(e2mock.getX()).thenReturn(0.0f);

        Mockito.when(e1mock.getY()).thenReturn(0.0f);
        Mockito.when(e2mock.getY()).thenReturn(110.0f);

        testee.onFling(e1mock, e2mock, 101.0f, 101.0f);

        Mockito.verify(testee, Mockito.atLeastOnce()).onSwipeDown();
    }

    @Test
    public void onSwipeUp() throws Exception {
        Mockito.when(e1mock.getX()).thenReturn(0.0f);
        Mockito.when(e2mock.getX()).thenReturn(0.0f);

        Mockito.when(e1mock.getY()).thenReturn(110.0f);
        Mockito.when(e2mock.getY()).thenReturn(0.0f);

        testee.onFling(e1mock, e2mock, 101.0f, 101.0f);

        Mockito.verify(testee, Mockito.atLeastOnce()).onSwipeUp();
    }

    @Test
    public void onSwipeRight() throws Exception {
        Mockito.when(e1mock.getX()).thenReturn(110.0f);
        Mockito.when(e2mock.getX()).thenReturn(0.0f);

        Mockito.when(e1mock.getY()).thenReturn(0.0f);
        Mockito.when(e2mock.getY()).thenReturn(00.0f);

        testee.onFling(e1mock, e2mock, 101.0f, 101.0f);

        Mockito.verify(testee, Mockito.atLeastOnce()).onSwipeRight();
    }

    @Test
    public void onSwipeLeft() throws Exception {
        Mockito.when(e1mock.getX()).thenReturn(0.0f);
        Mockito.when(e2mock.getX()).thenReturn(110.0f);

        Mockito.when(e1mock.getY()).thenReturn(0.0f);
        Mockito.when(e2mock.getY()).thenReturn(0.0f);

        testee.onFling(e1mock, e2mock, 101.0f, 101.0f);

        Mockito.verify(testee, Mockito.atLeastOnce()).onSwipeLeft();
    }

}