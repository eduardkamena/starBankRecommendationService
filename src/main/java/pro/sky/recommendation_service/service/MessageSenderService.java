package pro.sky.recommendation_service.service;

public interface MessageSenderService {

    void sendMessage(Long chatID, String messageText);

}
