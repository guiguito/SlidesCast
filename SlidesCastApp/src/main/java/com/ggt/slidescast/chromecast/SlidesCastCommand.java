package com.ggt.slidescast.chromecast;

/**
 * Represents a command to the slidescast chromecast app receiver.
 *
 * @author guiguito
 */
public class SlidesCastCommand {

    public enum Command {
        RELOAD("RELOAD"), START_CAST("START_CAST"), LEFT("LEFT"), RIGHT("RIGHT"), FIRST("FIRST"), LAST("LAST"), JUMPTO("JUMPTO"), FINISH("FINISH");

        private final String command;

        /**
         * @param text
         */
        private Command(final String command) {
            this.command = command;
        }

        /*
         * (non-Javadoc)
         *
         * @see java.lang.Enum#toString()
         */
        @Override
        public String toString() {
            return command;
        }
    }

    Command command;
    String castType;
    String value;

    public Command getCommand() {
        return command;
    }

    public void setCommand(Command command) {
        this.command = command;
    }

    public String getCastType() {
        return castType;
    }

    public void setCastType(String castType) {
        this.castType = castType;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
