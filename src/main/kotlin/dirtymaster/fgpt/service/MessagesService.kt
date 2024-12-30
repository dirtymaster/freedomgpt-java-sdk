package dirtymaster.fgpt.service

import dirtymaster.fgpt.model.Message

class MessagesService {
    val messages: MutableList<Message> = ArrayList()

    fun addMessage(message: Message) {
        messages.add(message)
    }

    fun clearMessages() {
        messages.clear()
    }
}
