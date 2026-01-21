package cleancode.patterns.structural.adapter.first;

public class PdfToDocAdapter implements Process {
    Pdf pdfFile;
    public PdfToDocAdapter(Pdf pdfFile){
        this.pdfFile = pdfFile;
    }
    @Override
    public void process() {
        Doc convertedFile = new Doc();
        System.out.println("Конвертация файла из формата " + pdfFile  + " в " + convertedFile );
        convertedFile.process();
    }
}
