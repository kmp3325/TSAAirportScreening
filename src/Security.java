import akka.actor.ActorRef;
import akka.actor.UntypedActor;

import java.util.HashMap;
import java.util.Map;

public class Security extends UntypedActor {

    private final int line;
    private final ActorRef jail;
    private final Map<Passenger, Boolean> passengers;
    private int scannersClosed;

    public Security(int line, ActorRef jail) {
        this.line = line;
        this.jail = jail;
        passengers = new HashMap<>();
        scannersClosed = 0;
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof PassengerReport) {
            PassengerReport report = (PassengerReport) message;
            System.out.println("Passenger "+report.getPassenger().getId()+" has arrived at security station "+line+".");
            if (passengers.keySet().contains(report.getPassenger())) {
                if (passengers.get(report.getPassenger()) && report.getPassed()) {
                    System.out.println("Passenger "+ report.getPassenger().getId()+" has passed security and boards the plane!");
                } else {
                    System.out.println("Passenger "+ report.getPassenger().getId()+" failed security and goes to jail!");
                    jail.tell(report.getPassenger(), self());
                }
            } else {
                passengers.put(report.getPassenger(), report.getPassed());
            }
        } else if (message instanceof BaggageReport) {
            BaggageReport report = (BaggageReport) message;
            System.out.println("Passenger "+report.getPassenger().getId()+"'s bags have arrived at security station "+line+".");
            if (passengers.keySet().contains(report.getPassenger())) {
                if (passengers.get(report.getPassenger()) && report.getPassed()) {
                    System.out.println("Passenger "+ report.getPassenger().getId()+" has passed security and boards the plane!");
                } else {
                    System.out.println("Passenger "+ report.getPassenger().getId()+" failed security and goes to jail!");
                    jail.tell(report.getPassenger(), self());
                }
            } else {
                passengers.put(report.getPassenger(), report.getPassed());
            }
        } else if (message instanceof Close) {
            System.out.println("Security Station "+line+" received closing signal.");
            scannersClosed++;
            if (scannersClosed == 2) {
                System.out.println("Body and baggage scanners for Security Station "+line+" have closed.  Sending close signal to jail.");
                jail.tell(message, self());
                System.out.println("Security Station "+line+" closed.");
                getContext().stop(self());
            }
        }
    }
}
