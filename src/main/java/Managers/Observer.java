package Managers;

import Models.Message;

public interface Observer {
    void update(Message mess);
}