## Purpose

This repository implements a chat-system, with the following features:

- Choose a pseudo,
- Connect with other active users on a same broadcast network, and display their pseudo in the form of a button which can open a conversation with them;
- Display a conversation window which automatically appends messages and offers the possibility to chat;
- At any given moment, the user can change pseudo, change discussion, or disconnect;
- User is always open to receiving messages from the start of the app to the disconnection, even if not on the conversation interface;
- Old messages are kept so that past conversations are displayed when entering the chat interface;
- Old conversations can be cleared.

Left to check:

- **code organization**: code is organized in loosely coupled packages. For instance, the packages `chatsystem.network` and `chatsystem.users` are completely independent of each other and only combined in the main class.
- **error handling**: custom error types (e.g. `chatsystem.users.ContactALreadyExists`), and explicit handling of unrecoverable error (error raised higher the chain of responsibility) or recoverable error (handled locally and continue processing)

## Usage

Building the program from source requires `maven` to be installed.

```sh
# compile 
mvn compile
# run tests
mvn test
# Run main program (which will simply wait for connection messages)
mvn exec:java -Dexec.mainClass="chatsystem.Main" 
```