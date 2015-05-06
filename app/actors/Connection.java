package actors;

import brain.Brain;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class Connection extends UntypedActor {
  LoggingAdapter log = Logging.getLogger(getContext().system(), this);
  
  public static Props props(ActorRef out) {
    return Props.create(Connection.class, out);
  }

  private final ActorRef out;
  private Brain brain;
  
  public Connection(ActorRef out) {
    this.out = out;
    brain = new Brain(out);
  }
  
  @Override
  public void onReceive(Object message) throws Exception {
    log.info("received: " + message);
    
    String msg = (String) message;
    brain.input(msg.charAt(0));
    
    out.tell("OK", getSelf());
    //else unhandled(message);
  }
}
