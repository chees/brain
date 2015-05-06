package actors;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;

public class Output extends UntypedActor {

  private ActorRef out;
  private char c;
  
  Output(ActorRef out, char c) {
    this.out = out;
    this.c = c;
  }

  public static Props props(ActorRef out, char c) {
    return Props.create(Output.class, () -> new Output(out, c));
  }

  @Override
  public void onReceive(Object msg) throws Exception {
    out.tell("" + c, null);
  }
}
