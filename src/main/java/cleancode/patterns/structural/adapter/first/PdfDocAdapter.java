package cleancode.patterns.structural.adapter.first;

public class PdfDocAdapter implements Process {
    Process fileType;
    public PdfDocAdapter(Process fileType){
        this.fileType = fileType;
    }
    @Override
    public void process() {
        Process processedFile = fileType instanceof Pdf ? new Doc() : new Pdf();
        System.out.println("Конвертация файла из формата " + fileType  + " в " + processedFile );
        processedFile.process();
    }
}
