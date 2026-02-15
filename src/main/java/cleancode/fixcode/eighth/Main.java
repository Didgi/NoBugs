package cleancode.fixcode.eighth;

public class Main {

    public static void main(String[] args) {
        EmailSender emailSender = new EmailSender();
        NotificationService notificationService = new NotificationService(emailSender);
        notificationService.sendNotification("тест");
    }
}
