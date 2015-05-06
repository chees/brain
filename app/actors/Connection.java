package actors;

import java.util.concurrent.TimeUnit;

import scala.concurrent.duration.Duration;
import akka.actor.ActorRef;
import akka.actor.Cancellable;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import brain.Brain;

public class Connection extends UntypedActor {
  private LoggingAdapter log = Logging.getLogger(getContext().system(), this);
  private final Cancellable tick = getContext().system().scheduler().schedule(
      Duration.create(0, TimeUnit.MILLISECONDS),
      Duration.create(1000, TimeUnit.MILLISECONDS),
      getSelf(), new Tick(), getContext().dispatcher(), null);
  
  public static Props props(ActorRef out) {
    return Props.create(Connection.class, out);
  }

  private Brain brain;
  private char lastReceived = ' ';
  
  public Connection(ActorRef out) {
    brain = new Brain(out);
  }
  
  @Override
  public void onReceive(Object message) throws Exception {
    if (message instanceof Tick) {
      log.info("tick");
      brain.input(lastReceived);
      lastReceived = ' ';
    } else if (message instanceof String) {
      log.info("received: " + message);
      lastReceived = ((String) message).charAt(0);
    } else {
      unhandled(message);
    }
  }

  @Override
  public void postStop() {
    tick.cancel();
  }
  
  private class Tick {}
}
