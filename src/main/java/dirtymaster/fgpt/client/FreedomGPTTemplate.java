package dirtymaster.fgpt.client;

import dirtymaster.fgpt.ApiClient;
import dirtymaster.fgpt.ApiException;
import dirtymaster.fgpt.api.FreedomGptApi;
import dirtymaster.fgpt.config.CompletionConfiguration;
import dirtymaster.fgpt.model.ChatCompletionRequest;
import dirtymaster.fgpt.model.ChatCompletionResponse;
import dirtymaster.fgpt.model.Message;
import dirtymaster.fgpt.model.Model;
import dirtymaster.fgpt.service.MessagesService;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class FreedomGPTTemplate {
    @Setter
    private Model model;
    @Setter
    private boolean rememberMessages = false;
    @Setter
    @Getter
    private CompletionConfiguration completionConfiguration;

    private final MessagesService messagesService;
    private final ApiClient apiClient;
    private final FreedomGptApi freedomGptApi;

    public FreedomGPTTemplate(Model model, String apiKey) {
        this.model = model;
        this.completionConfiguration = new CompletionConfiguration();
        this.messagesService = new MessagesService();

        this.apiClient = new ApiClient();
        apiClient.setBasePath("https://chat.freedomgpt.com");
        apiClient.addDefaultHeader("Authorization", "BEARER " + apiKey);
        apiClient.setConnectTimeout(60 * 1000);
        apiClient.setReadTimeout(60 * 1000);
        this.freedomGptApi = new FreedomGptApi(apiClient);
    }

    public void setUrl(String url) {
        this.apiClient.setBasePath(url);
        this.freedomGptApi.setApiClient(this.apiClient);
    }

    public void setApiKey(String apiKey) {
        this.apiClient.addDefaultHeader("Authorization", "BEARER " + apiKey);
    }

    public void setConnectTimeout(int connectTimeout) {
        this.apiClient.setConnectTimeout(connectTimeout);
        this.freedomGptApi.setApiClient(this.apiClient);
    }

    public void setReadTimeout(int readTimeout) {
        this.apiClient.setReadTimeout(readTimeout);
        this.freedomGptApi.setApiClient(this.apiClient);
    }

    public String getCompletion(String newMessageContent) {
        ChatCompletionRequest request = new ChatCompletionRequest()
                .model(model)
                .messages(messagesService.getMessages())
                .stream(false)
                .maxTokens(this.completionConfiguration.getMaxTokens())
                .temperature(this.completionConfiguration.getTemperature())
                .topK(this.completionConfiguration.getTopK())
                .topP(this.completionConfiguration.getTopP());

        Message newMessage = new Message().content(newMessageContent).role(Message.RoleEnum.USER);
        if (rememberMessages) {
            messagesService.addMessage(newMessage);
            request.setMessages(messagesService.getMessages());
        } else {
            messagesService.clearMessages();
            request.setMessages(List.of(newMessage));
        }

        ChatCompletionResponse response;
        try {
            response = freedomGptApi.getChatCompletions(request);
        } catch (ApiException ex) {
            throw new RuntimeException(ex);
        }
        Message responseMessage = response.getChoices().getFirst().getMessage();
        if (rememberMessages) {
            messagesService.addMessage(responseMessage);
        }
        return responseMessage.getContent();
    }
}
