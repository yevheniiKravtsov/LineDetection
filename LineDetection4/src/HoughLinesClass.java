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
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.VideoWriter;
import org.opencv.videoio.Videoio;
class HoughLinesClass {
    public void run(String[] args) {
    	
    		
            String filePath = "videoRoad5.mov";
            if (!Paths.get(filePath).toFile().exists()){
                 System.out.println("File " + filePath + " does not exist!");
                 return;
            }
            Mat src = Imgcodecs.imread(filePath);
            
            VideoCapture camera = new VideoCapture(filePath);

            if (!camera.isOpened()) {
                System.out.println("Error! Camera can't be opened!");
                return;
            }
            int totalFrameNumber = (int) camera.get(Videoio.CAP_PROP_FRAME_COUNT);
            Mat[] framesArray=new Mat[totalFrameNumber];
            camera.set(Videoio.CV_CAP_PROP_POS_FRAMES, 100);
            System.out.println("\n"+totalFrameNumber);
            for(int i=0;i<100;i++) {
	            Mat frame = new Mat();
	            camera.read(frame); 
	            Mat dst= new Mat();
	            Core.rotate(frame, dst, Core.ROTATE_90_CLOCKWISE);
	            framesArray[i]=detectLines(dst);
	            camera.grab();
            }
            int fourcc = VideoWriter.fourcc('m','j','p','g');
            Size frameSize = new Size(550,660);
      
            VideoWriter videoWriter = new VideoWriter("testfile2.avi", fourcc, 20, frameSize, true);
            for(int i=0;i<100;i++) {
            	videoWriter.write(framesArray[i]);
            }
            videoWriter.release();
            camera.release();
            
            HighGui.waitKey();
        }
    Mat detectLines(Mat src){
        
    	// Declare the output variables
        Mat dst = new Mat(), cdst = new Mat(), cdstP;
        Imgproc.resize(src, src, new Size(550, 660));
        // Check if image is loaded fine
        Mat mGray=new Mat();
        Imgproc.cvtColor(src,mGray,Imgproc.COLOR_BGR2GRAY);
        // Edge detection
        Imgproc.Canny(mGray, dst, 50, 200, 3, false);
        // Copy edges to the images that will display the results in BGR
        
        Imgproc.cvtColor(dst, cdst, Imgproc.COLOR_GRAY2BGR);        
        cdstP = cdst.clone();
        Rect rectCrop = new Rect(0, 200, 520, 450);
        Mat imageROI = new Mat(dst,rectCrop);
        
        // Standard Hough Line Transform
        Mat lines = new Mat(); // will hold the results of the detection
        Imgproc.HoughLines(imageROI, lines, 1, Math.PI/180, 150); // runs the actual detection
        // Draw the lines
        // Probabilistic Line Transform
        Mat linesP = new Mat(); // will hold the results of the detection
        Imgproc.HoughLinesP(imageROI, linesP, 1, Math.PI/180, 50, 50, 10); // runs the actual detection
        // Draw the lines
        for (int x = 0; x < linesP.rows(); x++) {
            double[] l = linesP.get(x, 0);
            if(l[1]<l[3]) {
	             if((((l[1]+100)>=300 && (l[3]+100)<600)) || l[3]-l[1]<100) {
	            	 //Imgproc.line(src, new Point(l[0], l[1]+200), new Point(l[2], l[3]+200),new Scalar(255, 0, 0), 3, Imgproc.LINE_AA, 0);          	
	             }else if(l[2]<400){
	            	 Imgproc.line(src, new Point(l[0], l[1]+200), new Point(l[2], l[3]+200), new Scalar(0, 0, 255), 3, Imgproc.LINE_AA, 0);
	             }else {
	            	 Imgproc.line(src, new Point(l[0], l[1]+200), new Point(l[2], l[3]+200),new Scalar(0, 255, 0), 3, Imgproc.LINE_AA, 0);
	             }
            }
            else {
           	 if((((l[1]+100)<600 && (l[3]+100)>=300)) ||l[1]-l[3]<70) {
	                //Imgproc.line(src, new Point(l[0], l[1]+200), new Point(l[2], l[3]+200), new Scalar(255, 0, 0), 3, Imgproc.LINE_AA, 0);
	            }else if(l[0]>100){
	            	Imgproc.line(src, new Point(l[0], l[1]+200), new Point(l[2], l[3]+200), new Scalar(0, 0, 255), 3, Imgproc.LINE_AA, 0);
	            }else {
	            	Imgproc.line(src, new Point(l[0], l[1]+200), new Point(l[2], l[3]+200), new Scalar(0, 255, 0), 3, Imgproc.LINE_AA, 0);
	            	
	            }
            }
        
        }  
        return src;

}
    	
}
 class HoughLines {
    public static void main(String[] args) {
        // Load the native library.
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        new HoughLinesClass().run(args);
    }
}
