package actors;

import java.util.ArrayList;
import java.util.List;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class Neuron extends UntypedActor {
  private LoggingAdapter log = Logging.getLogger(getContext().system(), this);
  private List<Link> links = new ArrayList<>();
  
  public static Props props() {
    return Props.create(Neuron.class, () -> new Neuron());
  }
  
  @Override
  public void onReceive(Object message) throws Exception {
    if (message instanceof ActorRef) {
      links.add(new Link((ActorRef) message));
      log.info("Added a link");
    } else if ("fire".equals(message)) {
      log.info("fired");
      for (Link l : links) {
        l.target.tell("fire", getSelf());
      }
    } else {
      unhandled(message);
    }
  }
  
  private class Link {
    //float strength; // [-1, 1]
    //float delay
    ActorRef target;
    
    Link(ActorRef target) {
      this.target = target;
    }
  }

}
