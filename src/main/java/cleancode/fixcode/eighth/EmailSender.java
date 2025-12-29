package cleancode.fixcode.eighth;

class EmailSender implements ISenderType {
    @Override
    public void sendEmail(String message) {
        System.out.println("Отправка email: " + message);
    }
}

//Задача: Используйте интерфейсы и внедрение зависимостей, чтобы ослабить связь между классами.
