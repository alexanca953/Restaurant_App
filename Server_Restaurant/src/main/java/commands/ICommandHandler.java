package commands;

import restaurantproject.model.Message;

public interface ICommandHandler {
    Message execute(Object data);
}