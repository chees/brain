package brain;

import java.util.HashMap;
import java.util.Map;

import actors.Neuron;
import actors.Output;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;

public class Brain {
  private Map<Character, ActorRef> inputs = new HashMap<>();
  
  public Brain(ActorRef output) {
    ActorSystem system = ActorSystem.create();
    
    for (char c = 'a'; c <= 'z'; c++) {
      
      ActorRef out = system.actorOf(Output.props(output, c));
      
      ActorRef in = system.actorOf(Neuron.props());
      inputs.put(c, in);
      
      in.tell(out, null);
    }
  }
  
  public void input(char c) {
    c = Character.toLowerCase(c);
    if (c < 'a' || c > 'z')
      return;
    inputs.get(c).tell("fire", ActorRef.noSender());
  }
}
