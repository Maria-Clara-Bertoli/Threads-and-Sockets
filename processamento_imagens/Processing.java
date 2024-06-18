package processamento_imagens;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

public class Processing implements Runnable{
	
private String path;
	
	public Processing(String path) {
		this.path = path;
	}
	
	static {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}
	
	public void faceDetection() throws InterruptedException{
		Mat src = Imgcodecs.imread(this.path);
		String xmlFile = "C:/Users/User/eclipse-workspace/Imagens/src/processamento_imagens/haarcascade.xml";
        CascadeClassifier classifier = new CascadeClassifier(xmlFile);
        MatOfRect faceDetections = new MatOfRect();
        classifier.detectMultiScale(src, faceDetections);
        for (Rect rect : faceDetections.toArray()) {
            Imgproc.rectangle(src, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 0, 255), 25);
        }
        Imgcodecs.imwrite(this.path, src);
    }
	
	public void histogramEqualization() throws InterruptedException{
		
		Mat imagePath = Imgcodecs.imread(this.path);
		Mat imagePathEqualized = new Mat();
		Imgproc.cvtColor(imagePath, imagePathEqualized, Imgproc.COLOR_RGB2GRAY);
		Imgproc.equalizeHist(imagePathEqualized, imagePathEqualized);
		Imgcodecs.imwrite(this.path, imagePathEqualized);
		
	}
	
	public void run() {
		
		try {
			System.out.println(Thread.currentThread().getName());
			System.out.println();
			faceDetection();
			} 
		catch (InterruptedException exception){
			exception.printStackTrace();
			Thread.currentThread().interrupt();
		}
	}
}
