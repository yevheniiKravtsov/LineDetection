import java.nio.file.Paths;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class HoughLinesAnalize {

	public void run(String[] args) {
		
		String filePath = "myRoad5.jpg";
        if (!Paths.get(filePath).toFile().exists()){
             System.out.println("File " + filePath + " does not exist!");
             return;
        }
        
        Mat src = Imgcodecs.imread(filePath);
        Imgproc.resize(src, src, new Size(550, 660));
        Mat mGray=new Mat();
        Imgproc.cvtColor(src,mGray,Imgproc.COLOR_BGR2GRAY);
        Mat dst=new Mat();
        Imgproc.Canny(mGray, dst, 50, 200, 3, false);
        Mat cdst=new Mat();
        Imgproc.cvtColor(mGray, cdst, Imgproc.COLOR_GRAY2BGR);
        Rect rectCrop = new Rect(0, 200, 520, 450);
        Mat imageROI = new Mat(dst,rectCrop);
        //drawLinesHoughLines(imageROI,src);
        drawLinesHoughLinesP(imageROI,src);
        HighGui.imshow("HoughLines", src);
        HighGui.waitKey();
        System.exit(0);
	}
	public void drawLinesHoughLinesP(Mat dst, Mat src){
		
		 Mat linesP = new Mat();
         Imgproc.HoughLinesP(dst, linesP, 1, Math.PI/180, 50, 50, 10);
         for (int x = 0; x < linesP.rows(); x++) {
             double[] l = linesP.get(x, 0);   
             if(l[1]<l[3]) {
	             if((((l[1]+100)>=300 && (l[3]+100)<600)) || l[3]-l[1]<70) {
	            	 Imgproc.line(src, new Point(l[0], l[1]+200), new Point(l[2], l[3]+200),new Scalar(255, 0, 0), 3, Imgproc.LINE_AA, 0);          	
	             }else if(l[2]<400){
	            	 Imgproc.line(src, new Point(l[0], l[1]+200), new Point(l[2], l[3]+200), new Scalar(0, 0, 255), 3, Imgproc.LINE_AA, 0);
	             }else {
	            	 Imgproc.line(src, new Point(l[0], l[1]+200), new Point(l[2], l[3]+200),new Scalar(0, 255, 0), 3, Imgproc.LINE_AA, 0);
	             }
             }
             else {
            	 if((((l[1]+100)<600 && (l[3]+100)>=300)) ||l[1]-l[3]<70) {
 	                Imgproc.line(src, new Point(l[0], l[1]+200), new Point(l[2], l[3]+200), new Scalar(255, 0, 0), 3, Imgproc.LINE_AA, 0);
 	            }else if(l[0]>100){
 	            	Imgproc.line(src, new Point(l[0], l[1]+200), new Point(l[2], l[3]+200), new Scalar(0, 0, 255), 3, Imgproc.LINE_AA, 0);
 	            }else {
 	            	Imgproc.line(src, new Point(l[0], l[1]+200), new Point(l[2], l[3]+200), new Scalar(0, 255, 0), 3, Imgproc.LINE_AA, 0);
 	            	
 	            }
             }
             //Imgproc.line(src, new Point(l[0], l[1]+200), new Point(l[2], l[3]+200), new Scalar(0, 255, 0), 3, Imgproc.LINE_AA, 0);
         }
	}
	
	public void drawLinesHoughLines(Mat dst, Mat src) {
		Mat lines = new Mat(); 
        Imgproc.HoughLines(dst, lines, 1, Math.PI/180, 150); 
        // Draw the lines
        for (int x = 0; x < lines.rows(); x++) {       	
            double rho = lines.get(x, 0)[0],
                    theta = lines.get(x, 0)[1];
            double a = Math.cos(theta), b = Math.sin(theta);
            double x0 = a*rho, y0 = b*rho;
            Point pt1 = new Point(Math.round(x0 + 1000*(-b)),Math.round(y0 + 1000*(a)+200));
            Point pt2 = new Point(Math.round(x0 - 1000*(-b)),Math.round(y0 - 1000*(a)+200));
           	Imgproc.line(src, pt1, pt2, new Scalar(50, 205, 50), 3, Imgproc.LINE_AA, 0);
        }
	}
	
}
	
	class HoughLinesStart {
	    public static void main(String[] args) {
	        // Load the native library.
	        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	        new HoughLinesAnalize().run(args);
	    }
}
