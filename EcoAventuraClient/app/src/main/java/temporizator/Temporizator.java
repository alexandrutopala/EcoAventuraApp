package temporizator;

/**
 * Created by Alexandru on 14.04.2017.
 */

public class Temporizator extends Thread {
    public final static int MILISECONDS = 1;
    public final static int SECONDS = 1000;
    public final static int MINUTES = 60000;
    private long timeUnit;
    private Event event;
    private int unit;
    private boolean isWaitingForEvent;
    private boolean cancelEvent;

    public Temporizator (Event event, long timeUnit, int unit) {
        this.event = event;
        this.timeUnit = timeUnit;
        this.unit = unit;
    }

    public void doScheduledEvent () {
        isWaitingForEvent = true;
        cancelEvent = false;
        start();
    }

    public void doEventNow () {
        isWaitingForEvent = false;
        cancelEvent = false;
        event.doAction();
    }

    public boolean isWaitingForEvent() {
        return isWaitingForEvent;
    }

    public void cancelEvent () {
        cancelEvent = true;
    }

    @Override
    public void run () {
        isWaitingForEvent = true;
        try {
            Thread.sleep(timeUnit * unit);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (cancelEvent) return;
        try {
            event.doAction();
        }catch (Exception e) {}
        isWaitingForEvent = false;
    }
}
