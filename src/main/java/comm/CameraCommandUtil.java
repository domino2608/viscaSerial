package comm;

import java.util.Arrays;

/**
 * Created by Domino on 2016-12-10.
 */
public abstract class CameraCommandUtil {

    public static final int PAN_SPEED_INDEX = 4;
    public static final int TILT_SPEED_INDEX = 5;

    public static final int[] CLEAR_BUFFER = {0x88, 0x01, 0x00, 0x01, 0xff};

    public static final int[] HOME = {0x81, 0x01, 0x06, 0x04, 0xff};

    public static final int[] STOP = {0x81, 0x01, 0x06, 0x01, 0x01, 0x01, 0x03, 0x03, 0xff};

    public static final int[] MOVE_UP = {0x81, 0x01, 0x06, 0x01, 0x01, 0x01, 0x03, 0x01, 0xff};

    public static final int[] MOVE_DOWN = {0x81, 0x01, 0x06, 0x01, 0x01, 0x01, 0x03, 0x02, 0xff};

    public static final int[] MOVE_LEFT = {0x81, 0x01, 0x06, 0x01, 0x01, 0x01, 0x01, 0x03, 0xff};

    public static final int[] MOVE_RIGHT = {0x81, 0x01, 0x06, 0x01, 0x01, 0x01, 0x02, 0x03, 0xff};

    public static final int[] MOVE_UP_LEFT = {0x81, 0x01, 0x06, 0x01, 0x01, 0x01, 0x01, 0x01, 0xff};

    public static final int[] MOVE_UP_RIGHT = {0x81, 0x01, 0x06, 0x01, 0x01, 0x01, 0x02, 0x01, 0xff};

    public static final int[] MOVE_DOWN_LEFT = {0x81, 0x01, 0x06, 0x01, 0x01, 0x01, 0x01, 0x02, 0xff};

    public static final int[] MOVE_DOWN_RIGHT = {0x81, 0x01, 0x06, 0x01, 0x01, 0x01, 0x02, 0x02, 0xff};

    public static int[] getCommandCopyWithChangedSpeed(int[] command, int panSpeed, int tiltSpeed) {
        int[] commandCopy = command.clone();

        commandCopy[PAN_SPEED_INDEX] = panSpeed;
        commandCopy[TILT_SPEED_INDEX] = tiltSpeed;

        return commandCopy;
    }
}
