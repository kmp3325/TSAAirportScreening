import akka.actor.UntypedActor;
import akka.actor.ActorRef;

public class PassengerScanner extends UntypedActor {

    private final int line;
    private final ActorRef security;

    public PassengerScanner(int line, ActorRef security) {
        this.line = line;
        this.security = security;
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof CheckPassenger) {
            Passenger toCheck = ((CheckPassenger) message).getPassenger();
            System.out.println("Line "+line+"'s body scanner receives passenger "+toCheck.getId()+".");
            boolean passed = Math.random() > .2;
            System.out.println("Line "+line+"'s body scanner reports to security that passenger "+toCheck.getId()+" has "+ (passed ? "passed." : "failed."));
            security.tell(new PassengerReport(toCheck, passed), self());
        } else if (message instanceof Close) {
            System.out.println("Line "+line+"'s body scanner is closing.");
            System.out.println("Line "+line+"'s body scanner tells its security station that it is closed.");
            security.tell(message, self());
            context().system().stop(self());
        }
    }
}
