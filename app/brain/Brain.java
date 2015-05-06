package brain;

import java.util.HashMap;
import java.util.Map;

import actors.Neuron;
import actors.Output;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;

public class Brain {
  private ActorSystem system = ActorSystem.create();
  private Map<Character, ActorRef> inputs = new HashMap<>();
  
  public Brain(ActorRef output) {
    for (char c = 'a'; c <= 'z'; c++) {
      createInputOutputLink(c, output);
    }
    createInputOutputLink(' ', output);
  }
  
  private void createInputOutputLink(char c, ActorRef output) {
    ActorRef out = system.actorOf(Output.props(output, c));
    
    ActorRef in = system.actorOf(Neuron.props());
    inputs.put(c, in);
    
    // For now just link inputs directly to outputs:
    in.tell(out, null);
  }
  
  public void input(char c) {
    c = Character.toLowerCase(c);
    if (c != ' ' && (c < 'a' || c > 'z'))
      return;
    inputs.get(c).tell("fire", ActorRef.noSender());
  }
}
