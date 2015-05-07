package actors;

import brain.FireMsg;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;

public class Output extends UntypedActor {

  private ActorRef out;
  private char c;
  private float value;
  
  Output(ActorRef out, char c) {
    this.out = out;
    this.c = c;
  }

  public static Props props(ActorRef out, char c) {
    return Props.create(Output.class, () -> new Output(out, c));
  }

  @Override
  public void onReceive(Object msg) throws Exception {
    if (msg instanceof FireMsg) {
      value += ((FireMsg) msg).strength;
      if (value >= 1) {
        out.tell("" + c, null);
        value = 0;
      } else if (value < -1)
        value = -1;
    }
    else unhandled(msg);
  }
}
