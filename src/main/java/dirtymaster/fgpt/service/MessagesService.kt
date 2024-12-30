package dirtymaster.fgpt.service;

import dirtymaster.fgpt.model.Message;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class MessagesService {
    private final List<Message> messages = new ArrayList<>();

    public void addMessage(Message message) {
        messages.add(message);
    }

    public void clearMessages() {
        messages.clear();
    }
}
