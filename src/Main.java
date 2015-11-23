import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

public class Main {

    public static void main(String[] args) {
        int lines = 10;

        ActorSystem sys = ActorSystem.create("tsa");

        ActorRef jail = sys.actorOf(Props.create(Jail.class, lines));
    }
}
