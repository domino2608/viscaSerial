package comm;

import java.util.Arrays;

/**
 * Created by Domino on 2016-12-10.
 */
public abstract class CameraCommandUtil {

    public static final int HORIZONTAL_CHANGE = 144;

    public static final int VERTICAL_CHANGE = 60;


    public static final int POSITIVE_START = 0; //hex = 0000

    public static final int NEGATIVE_START = 65535; //hex = FFFF


    public static final int PAN_SPEED_INDEX = 4;

    public static final int TILT_SPEED_INDEX = 5;

    public static final int POSITION_PAN_1_INDEX = 6;

    public static final int POSITION_PAN_2_INDEX = 7;

    public static final int POSITION_PAN_3_INDEX = 8;

    public static final int POSITION_PAN_4_INDEX = 9;


    public static final int POSITION_TILT_1_INDEX = 10;

    public static final int POSITION_TILT_2_INDEX = 11;

    public static final int POSITION_TILT_3_INDEX = 12;

    public static final int POSITION_TILT_4_INDEX = 13;


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

    public static final int[] ABSOLUTE_POSITION = {0x81, 0x01, 0x06, 0x02, 0x10, 0x10, 0x00, 0x00, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x00, 0xff};

    public static final int[] RELATIVE_POSITION = {0x81, 0x01, 0x06, 0x03, 0x10, 0x10, 0x00, 0x00, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x00, 0xff};

    public static int[] getCommandCopyWithChangedSpeed(int[] command, int panSpeed, int tiltSpeed) {
        NewIndexValueCommandHolder[] newValues = {new NewIndexValueCommandHolder(PAN_SPEED_INDEX, panSpeed),
                new NewIndexValueCommandHolder(TILT_SPEED_INDEX, tiltSpeed)};

        return getCommandCopyWithChangedValueAtIndex(command, newValues);
    }

    public static int[] getCommandCopyWithChangedValueAtIndex(int[] command, NewIndexValueCommandHolder[] newValues) {
        int[] commandCopy = Arrays.copyOf(command, command.length);

        for (NewIndexValueCommandHolder newCmd : newValues) {
            commandCopy[newCmd.index] = newCmd.newValue;
        }

        return commandCopy;
    }

    public static class NewIndexValueCommandHolder {
        private int index;
        private int newValue;

        public NewIndexValueCommandHolder(int index, int newValue) {
            this.index = index;
            this.newValue = newValue;
        }
    }
}
