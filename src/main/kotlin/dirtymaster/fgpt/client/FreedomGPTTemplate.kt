package dirtymaster.fgpt.client

import dirtymaster.fgpt.api.FreedomGptApi
import dirtymaster.fgpt.config.CompletionConfiguration
import dirtymaster.fgpt.model.ChatCompletionRequest
import dirtymaster.fgpt.model.ChatCompletionResponse
import dirtymaster.fgpt.model.Message
import dirtymaster.fgpt.model.Model
import dirtymaster.fgpt.service.MessagesService
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

class FreedomGPTTemplate(var model: Model, apiKey: String) {
    var rememberMessages = false
    var completionConfiguration = CompletionConfiguration()

    private val messagesService = MessagesService()
    private val freedomGptApi: FreedomGptApi

    init {
        val httpClient = OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS) // Устанавливаем readTimeout
            .connectTimeout(60, TimeUnit.SECONDS) // Устанавливаем connectTimeout
            .addInterceptor { chain ->
                val originalRequest = chain.request()
                val requestWithAuthorization = originalRequest.newBuilder()
                    .addHeader("Authorization", "Bearer $apiKey") // Добавляем заголовок Authorization
                    .build()
                chain.proceed(requestWithAuthorization)
            }
            .build()

        this.freedomGptApi = FreedomGptApi(client = httpClient, basePath = "https://chat.freedomgpt.com")

    }

    fun getCompletion(newMessageContent: String?): String {
        val newMessage = Message(content = newMessageContent, role = Message.Role.user)
        val request = ChatCompletionRequest(
            model = model,
            messages = if (rememberMessages) messagesService.messages else listOf(newMessage),
            stream = false,
            maxTokens = completionConfiguration.maxTokens,
            temperature = completionConfiguration.temperature,
            topK = completionConfiguration.topK,
            topP = completionConfiguration.topP
        )

        if (rememberMessages) {
            messagesService.addMessage(newMessage)
        } else {
            messagesService.clearMessages()
        }

        val response: ChatCompletionResponse = freedomGptApi.getChatCompletions(request)
        val responseMessage: Message? = response.choices?.first()?.message
        if (responseMessage?.content == null) {
            throw RuntimeException("Response message content is null")
        }
        if (rememberMessages) {
            messagesService.addMessage(responseMessage)
        }
        return responseMessage.content
    }

    fun getAllMessages(): List<Message> {
        return messagesService.messages
    }

    fun clearMessages() {
        messagesService.clearMessages()
    }
}