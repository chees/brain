package controllers;

import actors.Connection;
import play.*;
import play.mvc.*;
import views.html.*;

public class Application extends Controller {

  public static Result index() {
    return ok(index.render("TODO"));
  }

  public static WebSocket<String> websocket() {
    Logger.info("New websocket: " + request().remoteAddress());
    return WebSocket.withActor(Connection::props);
  }
}
