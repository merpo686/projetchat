## Purpose

This repository implements a chat-system, with the following features:

- Choose a pseudo,
- Connect with other active users on a same broadcast network, and display their pseudo in the form of a button which can open a conversation with them;
- Display a conversation window which automatically appends messages and offers the possibility to chat;
- At any given moment, the user can change pseudo, change discussion, or disconnect;
- User is always open to receiving messages from the start of the app to the disconnection, even if not on the conversation interface;
- Old messages are kept so that past conversations are displayed when entering the chat interface;
- Old conversations can be cleared.

Some issues yet to be solved:

- Clear conversation button in chat window doesn't clear the database - query doesn't seem to work
- When disconnection occurs while chatting, and the user after reconnects, he can't see the former one in his active list - the former one seems to not send his pseudo ?
- ActiveUserList and Self are used in other packages, and ConversationManager too. it could certainly be solved with again some handler/observers, but would it be worth it ?

## Usage

Building the program from source requires `maven` to be installed.

```sh
# compile 
mvn compile
# run tests
mvn test
# Run main program (which will simply wait for connection messages)
mvn exec:java -Dexec.mainClass="chatsystem.Main" 
# Clear database
mvn exec:java -Dexec.clearDatabaseClass=""
```

