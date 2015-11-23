import akka.actor.ActorRef;
import akka.actor.UntypedActor;

import java.util.List;

public class DocumentCheck extends UntypedActor {

    private final List<ActorRef> passengerScanner;
    private final List<ActorRef> baggageScanner;
    private final ActorRef jail;
    private int nextLine;

    public DocumentCheck(List<ActorRef> passengerScanner, List<ActorRef> baggageScanner, ActorRef jail) {
        this.passengerScanner = passengerScanner;
        this.baggageScanner = baggageScanner;
        this.jail = jail;
        this.nextLine = 0;
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof Passenger) {
            Passenger passenger = ((Passenger) message);
            System.out.println("Document Check receives passenger "+passenger.getId()+".");
            if (Math.random() > .2) {
                System.out.println("Passenger "+passenger.getId()+" fails document check and is sent to jail.");
                jail.tell(passenger, self());
            } else {
                System.out.println("Passenger "+passenger.getId()+" passes document check and enters the queue for line "+nextLine+".");
                passengerScanner.get(nextLine % passengerScanner.size()).tell(new CheckPassenger(passenger), self());
                baggageScanner.get(nextLine % baggageScanner.size()).tell(new CheckBaggage(passenger), self());
                nextLine++;
            }
        } else if (message instanceof Close) {
            System.out.println("Document Check station closing.");
            for (int i = 0; i < passengerScanner.size(); i++) {
                System.out.println("Document Check sending close notification to line "+i+".");
                passengerScanner.get(i).tell(message, self());
                baggageScanner.get(i).tell(message, self());
            }
            System.out.println("Document Check station closed.");
            getContext().stop(self());
        }
    }
}
