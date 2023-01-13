package Managers;

import Models.Message;

/**
 * Observer interfaces, mainly to notify reception of messages to other classes
 */
public interface Observer {
    /**
     * Observer for the reception of messages (as in conversation messages)
     * @param mess
     */
    void update(Message mess);

    /**
     * Observer to notify the disconnection of a user when chatting with him
     */
    void update();
}