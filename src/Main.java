import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

import java.util.ArrayList;
import java.util.List;

public class Main {

    static final int LINES = 3;
    static final int NUMBER_OF_PASSENGERS = 20;

    public static void main(String[] args) {

        ActorSystem sys = ActorSystem.create("tsa");

        ActorRef jail = sys.actorOf(Props.create(Jail.class, LINES));
        List<ActorRef> securityStations = new ArrayList<>();
        List<ActorRef> baggageScanners = new ArrayList<>();
        List<ActorRef> bodyScanners = new ArrayList<>();
        for (int i = 0; i < LINES; i++) {
            securityStations.add(sys.actorOf(Props.create(Security.class, i, jail)));
            baggageScanners.add(sys.actorOf(Props.create(BaggageScanner.class, i, securityStations.get(i))));
            bodyScanners.add(sys.actorOf(Props.create(PassengerScanner.class, i, securityStations.get(i))));
        }

        ActorRef documentCheck = sys.actorOf(Props.create(DocumentCheck.class, bodyScanners, baggageScanners, jail));

        for (int i = 0; i < NUMBER_OF_PASSENGERS; i++) {
            System.out.println("Passenger "+i+" arrives at the airport and is sent to Document Check.");
            documentCheck.tell(new Passenger(i), null);
        }
        System.out.println("The airport is closing; sending notification to Document Check.");
        documentCheck.tell(new Close(), null);
    }
}
