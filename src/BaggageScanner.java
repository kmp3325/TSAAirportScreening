import akka.actor.UntypedActor;
import akka.actor.ActorRef;

public class BaggageScanner extends UntypedActor {

    private final int line;
    private final ActorRef security;

    public BaggageScanner(int line, ActorRef security) {
        this.line = line;
        this.security = security;
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof CheckBaggage) {
            Passenger toCheck = ((CheckBaggage) message).getPassenger();
            System.out.println("Line "+line+"'s baggage scanner receives passenger "+toCheck.getId()+"'s bags.");
            boolean passed = Math.random() > .2;
            System.out.println("Line "+line+"'s baggage scanner reports to security that passenger "+toCheck.getId()+"'s bags have "+ (passed ? "passed." : "failed."));
            security.tell(new BaggageReport(toCheck, passed), self());
        } else if (message instanceof Close) {
            System.out.println("Line "+line+"'s baggage scanner is closing.");
            System.out.println("Line "+line+"'s baggage scanner tells its security station that it is closed.");
            security.tell(message, self());
            context().system().stop(self());
        }
    }
}
