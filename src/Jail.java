import akka.actor.UntypedActor;

import java.util.ArrayList;
import java.util.List;

public class Jail extends UntypedActor {

    private final int totalLines;
    private final List<Passenger> passengers;
    private int linesClosed;

    public Jail(int totalLines) {
        this.totalLines = totalLines;
        passengers = new ArrayList<>();
        linesClosed = 0;
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof Passenger) {
            System.out.println("Jail received passenger "+((Passenger)message).getId()+".");
            passengers.add(((Passenger) message));
        } else if (message instanceof Close) {
            System.out.println("Jail received closing signal.");
            linesClosed++;
            if (linesClosed == totalLines) {
                System.out.println("All stations have closed; sending all detainees to permanent detention.");
                for (Passenger passenger : passengers) {
                    System.out.println("Sending passenger "+passenger.getId()+" to permanent detention.");
                }
                System.out.println("Jail closed.");
                context().system().stop(self());
                System.exit(0);
            }
        }
    }
}
