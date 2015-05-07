package brain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import actors.Neuron;
import actors.Output;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

public class Brain {
  private final static ImmutableList<Character> ALPHABET = Lists.charactersOf("abcdefghijklmnopqrstuvwxyz ");
  
  private ActorSystem system = ActorSystem.create();
  private Map<Character, ActorRef> inputs = new HashMap<>();
  private List<ActorRef> middles = new ArrayList<>();
  private List<ActorRef> outputs = new ArrayList<>();
  
  public Brain(ActorRef output) {
    
    for (char c : ALPHABET) {
      inputs.put(c, system.actorOf(Neuron.props()));
      middles.add(system.actorOf(Neuron.props()));
      outputs.add(system.actorOf(Output.props(output, c)));
    }
    
    // Connect inputs to middles:
    for (ActorRef in : inputs.values())
      for (ActorRef m : middles)
        in.tell(m, null);
    
    // Connect middles to outputs:
    for (ActorRef m : middles)
      for (ActorRef out : outputs)
        m.tell(out, null);
  }
  
  public void input(char c) {
    c = Character.toLowerCase(c);
    if (!ALPHABET.contains(c))
      return;
    inputs.get(c).tell(new FireMsg(1), ActorRef.noSender());
  }
}
