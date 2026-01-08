package commands;

import restaurantclient.model.Message;

public interface ICommandHandler {
    Message execute(Object data);
}