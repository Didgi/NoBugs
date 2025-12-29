package cleancode.fixcode.eighth;

public class NotificationService {
    ISenderType iSenderType;

    public NotificationService(ISenderType iSenderType) {
        this.iSenderType = iSenderType;
    }

    public void sendNotification(String message) {
        iSenderType.sendEmail(message);
    }
}
