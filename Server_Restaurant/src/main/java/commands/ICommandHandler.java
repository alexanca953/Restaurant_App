package commands;

import model.Message;

public interface ICommandHandler {
    Message execute(Object data);
}