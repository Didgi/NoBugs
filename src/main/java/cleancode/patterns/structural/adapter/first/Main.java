package cleancode.patterns.structural.adapter.first;

public class Main {
    /*
    У нас есть два типа файлов:
PDF-файл, который должен обрабатываться системой, но она работает только с DOC.
Необходимо создать адаптер, который позволит работать с файлами обоих форматов,
конвертируя их при необходимости.
Задача — создать интерфейс, который будет принимать файлы с расширением PDF или DOC,
а также адаптер, который будет конвертировать один формат в другой.
     */

    public static void main(String[] args) {
        Pdf pdf = new Pdf();
        PdfToDocAdapter pdfToDocAdapter = new PdfToDocAdapter(pdf);
        pdfToDocAdapter.process();

    }
}
