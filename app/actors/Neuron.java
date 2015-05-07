package actors;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import brain.FireMsg;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class Neuron extends UntypedActor {
  private LoggingAdapter log = Logging.getLogger(getContext().system(), this);
  private List<Link> links = new ArrayList<>();
  private Random random = new Random();
  private float value;
  
  public static Props props() {
    return Props.create(Neuron.class, () -> new Neuron());
  }
  
  @Override
  public void onReceive(Object message) throws Exception {
    if (message instanceof ActorRef) {
      links.add(new Link((ActorRef) message));
      log.info("Added a link");
    }
    else if (message instanceof FireMsg) {
      value += ((FireMsg) message).strength;
      if (value >= 1) {
        //log.info("fired");
        for (Link l : links) {
          l.target.tell(new FireMsg(l.strength), getSelf());
        }
        value = 0;
      }
      else if (value < -1)
        value = -1;
    }
    else unhandled(message);
  }
  
  private class Link {
    float strength; // [-1, 1]
    //float delay;
    ActorRef target;
    
    Link(ActorRef target) {
      this.target = target;
      strength = (random.nextFloat() * 2 - 1) / 2;
    }
  }
}
