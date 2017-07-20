package pkg1;

import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.awt.image.DataBufferUShort;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.stat.descriptive.rank.Median;

public class CompleteMethods {
	
	//Gets benchmark statistics, and writes it to the file fileName

	private static void outputImage(BufferedImage b){
		JFrame frame = new JFrame();
		frame.getContentPane().setLayout(new FlowLayout());
		frame.getContentPane().add(new JLabel(new ImageIcon(b)));
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
	}
	
	private static void writeStatisticsToFile_1D(ArrayList<Long> listOfTimes,
											  	 long benchMarkTime,
											  	 BufferedImage b,
											  	 String benchmarkName,
											  	 String fileName) throws FileNotFoundException{
		
		System.out.println("Start Writing " + fileName);
		
		double[] data = new double[listOfTimes.size()];
		
		for(int i = 0; i < listOfTimes.size(); i++){
			data[i] = listOfTimes.get(i).doubleValue();
		}
		
		DescriptiveStatistics stats = new DescriptiveStatistics(data);
		Median statsMedian = new Median();
		
		PrintWriter fileOut = new PrintWriter(new FileOutputStream(fileName));
		
		fileOut.println(benchmarkName);
		fileOut.println("__________________");
		fileOut.println("Image Dimensions (WxH):  " + b.getWidth() + " * " + b.getHeight());
		fileOut.println("Total Pixel Count:  " + (b.getWidth()*b.getHeight()));
		fileOut.println();
		System.out.println("-\tWrote Image Dimension Data.");
		
		fileOut.println("Times are given in nanoseconds");
		fileOut.println("Total Time for Test (loop evals included):  " + benchMarkTime
				+ "/ " + benchMarkTime / 1000000);
		fileOut.println("Total Function Calls:  " + stats.getN());
		fileOut.println("Total function call time (function calls only):  " + stats.getSum()
				+ "/ " + stats.getSum() /1000000);
		fileOut.println("Avg function call Time:  " + stats.getMean()
				+ "/ " + stats.getMean() / 1000000);
		fileOut.println("Max function call Time:  " + stats.getMax()
				+ "/ " + stats.getMax() / 1000000);
		fileOut.println("Min function call Time:  " + stats.getMin()
				+ "/ " + stats.getMin() / 1000000);
		fileOut.println("Median function call time:  " + statsMedian.evaluate(data, 50.0)
				+ "/ " + statsMedian.evaluate(data, 50.0) / 1000000);
		fileOut.println("stdDev of times: " + stats.getStandardDeviation()
				+ "/ " + stats.getStandardDeviation() / 1000000);
		System.out.println("-\tWrote Overall Statistics Data.");
		
		System.out.println("-\tWriting Individual Pass Data:");
		fileOut.println("\nINDIVIDUAL PASS TIMES (NS/MS):");
		for(int i = 0; i < data.length; i++){
			fileOut.println((i+1) + ". " + data[i] + "/" + data[i]/1000000);
			System.out.println("-\t\tPass " + (i+1) + " data written.");
			
		}
		
		fileOut.close();
		fileOut = null;
		
		stats = null;
		statsMedian = null;
		
		System.out.println("END writing" + fileName);
		System.gc();
		
	}
	
	private static void writeStatisticsToFile_2D(ArrayList<ArrayList<Long>> listOfTimes,
												 long benchMarkTime,
												 BufferedImage b,
												 String benchmarkName,
												 String fileName) throws FileNotFoundException{
		
		System.out.println("Start Writing" + fileName);
		
		double[] allDataCollection = new double[listOfTimes.size()*listOfTimes.get(0).size()];
		double[][] dataLists = new double[listOfTimes.size()][listOfTimes.get(0).size()];
		
		for(int i = 0; i < listOfTimes.size(); i++){
			for(int j = 0; j < listOfTimes.get(0).size(); j++){
				dataLists[i][j] = listOfTimes.get(i).get(j).doubleValue();
				allDataCollection[j + i*listOfTimes.get(0).size()] = listOfTimes.get(i).get(j).doubleValue();
			}
		}
		System.out.println("-\tGot All Data as one Doubles list");
		
		DescriptiveStatistics overallStats = new DescriptiveStatistics(allDataCollection);
		Median overallStatsMedian = new Median();
		
		PrintWriter fileOut = new PrintWriter(new FileOutputStream(fileName));
		
		fileOut.println(benchmarkName);
		fileOut.println("__________________");
		fileOut.println("Image Dimensions (WxH):  " + b.getWidth() + " * " + b.getHeight());
		fileOut.println("Total Pixel Count:  " + (b.getWidth()*b.getHeight()));
		fileOut.println();
		System.out.println("-\tWrote image dimension data.");
		
		fileOut.println("Time for 50 benches:  " + benchMarkTime);
		//fileOut.println("Average Time for Test (loop evals included):  " + benchMarkTime);
		fileOut.println("Total Function Calls:  " + overallStats.getN());
		fileOut.println("Total function call time (function calls only):  " + overallStats.getSum()
				+ "/ " + (overallStats.getSum()/1000000));
		fileOut.println("Avg function call Time:  " + overallStats.getMean()
				+ "/ " + overallStats.getMean()/1000000);
		fileOut.println("Max function call Time:  " + overallStats.getMax()
				+ "/ " + overallStats.getMax()/1000000);
		fileOut.println("Min function call Time:  " + overallStats.getMin()
				+ "/ " + overallStats.getMin()/1000000);
		fileOut.println("Median function call time:  " + overallStatsMedian.evaluate(allDataCollection, 50.0)
				+ "/ " + overallStatsMedian.evaluate(allDataCollection, 50.0)/1000000);
		fileOut.println("stdDev of call times: " + overallStats.getStandardDeviation()
				+ "/ " + overallStatsMedian.evaluate(allDataCollection, 50.0)/1000000);
		System.out.println("-\tWrote Overall data.");
		
		fileOut.println("\n\nINDIVIDUAL PASS STATS:");
		fileOut.println("average/ M: Median/ U:max/ L: min/ S: stDev/ T: timeNS/ timeMS");
		Median individualMedian = new Median();
		

		for(int i = 0; i < dataLists.length; i++){
			DescriptiveStatistics indivStat = new DescriptiveStatistics(dataLists[i]);
			
			fileOut.println(  (i+1) + ". "
							+ indivStat.getMean() / 1000000+" /"
							+ individualMedian.evaluate(dataLists[i], 50.0) / 1000000 + " /"
							+ indivStat.getMax() / 1000000 + " /"
							+ indivStat.getMin() / 1000000 + " /"
							+ indivStat.getStandardDeviation() / 1000000 + " /"
							+ indivStat.getSum() / 1000000 + " |"
							);
			
			System.out.println("-\t\tPass " + (i+1) + "calculated and outputted (Milliseconds)" );
			
		}
		
		fileOut.println();
		fileOut.println("INDIVIDUAL STATS NANOSECONDS:");
		for(int i = 0; i < dataLists.length; i++){
			DescriptiveStatistics indivStat = new DescriptiveStatistics(dataLists[i]);
			
			fileOut.println(  (i+1) + ". "
							+ indivStat.getMean()+" /"
							+ individualMedian.evaluate(dataLists[i], 50.0) + " /"
							+ indivStat.getMax() + " /"
							+ indivStat.getMin() + " /"
							+ indivStat.getStandardDeviation() + " /"
							+ indivStat.getSum() + " |"
							);
			
			System.out.println("-\t\tPass " + (i+1) + "calculated and outputted (Nanoseconds)" );
			
		}
		
		fileOut.close();
		fileOut = null;
		
		overallStats = null;
		overallStatsMedian = null;
		individualMedian = null;
		
		System.out.println("END Writing" + fileName);
		
		System.gc();

	}
	
	private static void benchmark_getPixel(BufferedImage b) throws FileNotFoundException{
		
		System.out.println("START benchmark_getPixel.");
		
		long totalBenchmarkTime = 0l;
		long totalLoopTime = 0;				
		long singleCallTime = 0l;
		
		ArrayList<ArrayList<Long>> listOfCallTimes = new ArrayList<ArrayList<Long>>(0);
		
		
		totalBenchmarkTime = System.currentTimeMillis();
		for(int benchCount = 0; benchCount < 50; benchCount++){//For 50 passes
			
			listOfCallTimes.add(new ArrayList<Long>(0));
			
			for(int y = 0; y < b.getHeight(); y++){
				for(int x = 0; x < b.getWidth(); x++){
					singleCallTime = System.nanoTime();
					b.getRGB(x,y);
					singleCallTime = System.nanoTime() - singleCallTime;
					listOfCallTimes.get(benchCount).add(singleCallTime);
				}
			}
			
		}
		totalBenchmarkTime = System.currentTimeMillis() - totalBenchmarkTime;
		
		
		writeStatisticsToFile_2D(listOfCallTimes,
								totalBenchmarkTime,
								b,
								"Java_benchmark_getPixel",
								"Java_benchmark_getPixel.txt"
								);
		
		listOfCallTimes = null;
		System.out.println("END benchmark_getPixel.");
		System.gc();
		//writeStatsToFile2d
		
		
	}
	
	private static void benchmark_setPixel(BufferedImage b) throws FileNotFoundException{
		
		System.out.println("START benchmark_setPixel.");
		long totalBenchmarkTime = 0l;
		long totalLoopTime = 0;				
		long singleCallTime = 0l;
		Random rand = new Random();
		int pixelValue = 0;
		
		int[][] randPixels = new int[b.getWidth()][b.getHeight()];			
		
		ArrayList<ArrayList<Long>> listOfCallTimes = new ArrayList<ArrayList<Long>>(0);
		
		for(int benchCount = 0; benchCount < 50; benchCount++){//For 50 passes
			
			listOfCallTimes.add(new ArrayList<Long>(0));
			
			for(int y = 0; y < b.getHeight(); y++){
				for(int x = 0; x < b.getWidth(); x++){
					randPixels[x][y] = rand.nextInt();
				}
			}
			
			totalLoopTime = System.nanoTime();
			for(int y = 0; y < b.getHeight(); y++){
				for(int x = 0; x < b.getWidth(); x++){
					pixelValue = randPixels[x][y];
					singleCallTime = System.nanoTime();
					b.setRGB(x,y, pixelValue);
					singleCallTime = System.nanoTime() - singleCallTime;
					listOfCallTimes.get(benchCount).add(singleCallTime);
				}
			}
			totalLoopTime = System.nanoTime() - totalLoopTime;
			totalBenchmarkTime += totalLoopTime;
		}
		
		writeStatisticsToFile_2D(listOfCallTimes,
								totalBenchmarkTime,
								b,
								"Java_benchmark_setPixel",
								"Java_benchmark_setPixel.txt"
								);

		listOfCallTimes = null;
		randPixels = null;
		rand = null;
		
		System.out.println("END benchmark_setPixel.");
		
		System.gc();
		
	}
	
	private static void benchmark_get5x5Matrix(BufferedImage b) throws FileNotFoundException{
		
		System.out.println("START benchmark_get5x5Matrix.");
		
		long totalBenchmarkTime = 0l;
		long totalLoopTime = 0;				
		long singleCallTime = 0l;
		
		ArrayList<ArrayList<Long>> listOfCallTimes = new ArrayList<ArrayList<Long>>(0);
		
		
		totalBenchmarkTime = System.currentTimeMillis();
		for(int benchCount = 0; benchCount < 50; benchCount++){//For 50 passes
			listOfCallTimes.add(new ArrayList<Long>(0));
			for(int y = 0; y < b.getHeight()-5; y++){
				for(int x = 0; x < b.getWidth()-5; x++){
					singleCallTime = System.nanoTime();
					b.getRGB(x, y, 5, 5, null, 0, b.getWidth());
					singleCallTime = System.nanoTime() - singleCallTime;
					listOfCallTimes.get(benchCount).add(singleCallTime);
				}
			}
			
		}
		totalBenchmarkTime = System.currentTimeMillis() - totalBenchmarkTime;
		
		writeStatisticsToFile_2D(listOfCallTimes,
				totalBenchmarkTime,
				b,
				"Java_benchmark_get5x5Matrix",
				"Java_benchmark_get5x5Matrx.txt"
				);

		listOfCallTimes = null;
		
		System.out.println("END benchmark_get5x5Matrix.");
		System.gc();
		
	}

	private static void benchmark_set5x5Matrix(BufferedImage b) throws FileNotFoundException{
		
		System.out.println("START benchmark_set5x5Matrix.");
		
		long totalBenchmarkTime = 0l;
		long totalLoopTime = 0;				
		long singleCallTime = 0l;
		Random rand = new Random();
		int[] pixelMatrix = new int[25];
		
		int[][][] randPixelMatricies = new int[b.getWidth()][b.getHeight()][25];
		
		ArrayList<ArrayList<Long>> listOfCallTimes = new ArrayList<ArrayList<Long>>(0);
		
		for(int benchCount = 0; benchCount < 50; benchCount++){//For 50 passes
			
			listOfCallTimes.add(new ArrayList<Long>(0));
			
			for(int y = 0; y < b.getHeight(); y++){
				for(int x = 0; x < b.getWidth(); x++){
					for(int k = 0; k < randPixelMatricies[x][y].length; k++){
						randPixelMatricies[x][y][k] = rand.nextInt();
					}
				}
			}
			
//			for(int i = 0; i < randPixelMatricies[0][0].length; i++){
//				System.out.print(randPixelMatricies[0][0][i] + ", ");
//			}
			
			totalLoopTime = System.nanoTime();
			for(int y = 0; y < b.getHeight()-5; y++){
				for(int x = 0; x < b.getWidth()-5; x++){
					pixelMatrix = randPixelMatricies[x][y];
					//System.out.println(pixelMatrix.length);
					singleCallTime = System.nanoTime();
					//b.setRGB(x, y, 5, 5, pixelMatrix, 0, b.getWidth());
					b.setRGB(x, y, 5, 5, pixelMatrix, 0, 3);
					singleCallTime = System.nanoTime() - singleCallTime;
					listOfCallTimes.get(benchCount).add(singleCallTime);
				}
			}
			
			//outputImage(b);
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			totalLoopTime = System.nanoTime() - totalLoopTime;
			totalBenchmarkTime += totalLoopTime;
		}
		
		writeStatisticsToFile_2D(listOfCallTimes,
								totalBenchmarkTime,
								b,
								"Java_benchmark_set5x5Matrix",
								"Java_benchmark_set5x5Matrix.txt"
								);

		listOfCallTimes = null;
		randPixelMatricies = null;
		rand = null;
		
		System.out.println("END benchmark_set5x5Matrix.");
		
		System.gc();
		
		
	}
	
	private static void benchmark_setAllData(BufferedImage b,
												  boolean withRaster
												  ) throws FileNotFoundException{
		
		System.out.println("START benchmark_setAllData"
				+ (withRaster? "_using_setRaster": "_usingSetDataElements"));
		
		if(withRaster == true){//uses setData(Raster r)

			long totalBenchmarkTime = 0l;	
			long totalLoopTime = 0l;
			long singleCallTime = 0l;
			Random rand = new Random();
			int[] randData = new int[b.getWidth()*b.getHeight()];
			
			ArrayList<Long> listOfTimes = new ArrayList<Long>(50);
		
			BufferedImage randImg = new BufferedImage(b.getColorModel(),
													b.getRaster(),
													b.isAlphaPremultiplied(),
													getBufferedImageProperties(b));
			
			totalBenchmarkTime = System.nanoTime();
		
			for(int benchCount = 0; benchCount < 50; benchCount++){
				
				for(int i = 0; i < randImg.getHeight()*randImg.getWidth(); i++){
					randData[i] = rand.nextInt();
				}
				randImg.setRGB(0, 0, randImg.getWidth(), randImg.getHeight(), randData, 0, b.getWidth());
				WritableRaster rasterToSetTo = randImg.getRaster();
				
				totalLoopTime = System.nanoTime();
				
				singleCallTime = System.nanoTime();
				b.setData(rasterToSetTo);
				singleCallTime = System.nanoTime() - singleCallTime;
				listOfTimes.add(singleCallTime);
				
				totalLoopTime = System.nanoTime() - singleCallTime;
				totalBenchmarkTime += totalLoopTime;
				
				
			}
			//totalBenchmarkTime = System.nanoTime() - totalBenchmarkTime;
			writeStatisticsToFile_1D(listOfTimes,
									totalBenchmarkTime,
									b,
									"Java_benchmark_setAllPixelData_setRaster",
									"Java_benchmark_setAllPixelData_setRaster.txt"
									);
			
			randData = null;
			rand = null;
			listOfTimes = null;
			
			System.gc();
			
		}
		else{//Uses b.getRaster().setDataElements();
			long totalBenchmarkTime = 0l;	
			long totalLoopTime = 0l;
			long singleCallTime = 0l;
			Random rand = new Random();
			int[] randData = new int[b.getWidth()*b.getHeight()];
			
			ArrayList<Long> listOfTimes = new ArrayList<Long>(50);
		
			BufferedImage randImg = new BufferedImage(b.getColorModel(),
													b.getRaster(),
													b.isAlphaPremultiplied(),
													getBufferedImageProperties(b));
			
			totalBenchmarkTime = System.nanoTime();
		
			for(int benchCount = 0; benchCount < 50; benchCount++){
				
				for(int i = 0; i < randImg.getHeight()*randImg.getWidth(); i++){
					randData[i] = rand.nextInt();
				}
				randImg.setRGB(0, 0, randImg.getWidth(), randImg.getHeight(), randData, 0, b.getWidth());
				WritableRaster rasterToSetTo = randImg.getRaster();
			
				totalLoopTime = System.nanoTime();
				WritableRaster bRaster = b.getRaster();
				
				for(int y = 0; y < b.getHeight() - 1; y++){
					for(int x = 0; x < b.getWidth() - 1; x++){
						singleCallTime = System.nanoTime();
						bRaster.setDataElements(x, y, rasterToSetTo);
						singleCallTime = System.nanoTime() - singleCallTime;
						listOfTimes.add(singleCallTime);
					}
				}
				
				
				
				totalLoopTime = System.nanoTime() - singleCallTime;
				totalBenchmarkTime += totalLoopTime;
			
			}
			
			//totalBenchmarkTime = System.nanoTime() - totalBenchmarkTime;
			writeStatisticsToFile_1D(listOfTimes,
									totalBenchmarkTime,
									b,
									"Java_benchmark_setAllPixelData_setDataElements",
									"Java_benchmark_setAllPixelData_setDataElements.txt"
									);
			

			randData = null;
			rand = null;
			listOfTimes = null;
			
			
		}
		
		System.out.println("END benchmark_setAllData"
				+ (withRaster? "_using_setRaster": "_usingSetDataElements"));
		
		System.gc();
	}
	
	private static void benchmark_getAllData(BufferedImage b) throws FileNotFoundException{
		
		System.out.println("START benchmark_getAllData");
		
		long totalBenchmarkTime = 0l;
		//long totalLoopTime = 0;				
		long singleCallTime = 0l;
		long singleCallEnd = 0l;
		short x[] = new short[0];
		
		ArrayList<Long> listOfTimes = new ArrayList<Long>(b.getWidth()*b.getHeight());
		
		totalBenchmarkTime = System.nanoTime();
		
		for(int benchCount = 0; benchCount < 50; benchCount++){
			singleCallTime = System.nanoTime();
			x = ((DataBufferUShort) b.getRaster().getDataBuffer()).getData();
			singleCallEnd = System.nanoTime() ;
			listOfTimes.add(singleCallEnd - singleCallTime);
		}
		
		totalBenchmarkTime = System.nanoTime() - totalBenchmarkTime;
		

		writeStatisticsToFile_1D(listOfTimes,
								totalBenchmarkTime,
								b,
								"Java_benchmark_getAllData",
								"Java_benchmark_getAllData.txt"
								);
		
		listOfTimes = null;
		
		System.out.println("END benchmark_getAllData");
		
		System.gc();
		
	}
	
	private static Hashtable<?,?> getBufferedImageProperties(BufferedImage b){
		String[] bPropertyNames = b.getPropertyNames();
		Hashtable<String,Object> bProperties = new Hashtable<String, Object>();
		
		if (bPropertyNames != null){
			 for (int i = 0; i < bPropertyNames.length; i++) {
		            bProperties.put(bPropertyNames[i], b.getProperty(bPropertyNames[i]));
		        }
		}
		
		return bProperties;
	}

	public static void main(String args[]){
		long benchStartTime = System.nanoTime();
		System.out.println("BENCHMARK SUITE START****************************");
		try {
			BufferedImage bufferedImg = ImageIO.read(new File("RGB_Color_Bars.BMP"));
			System.out.println(bufferedImg.getColorModel().getClass().getCanonicalName());
			System.out.println(bufferedImg.getRaster().getSampleModel().getClass().getCanonicalName());
			
			//benchmark_setAllData(bufferedImg, true);
			//benchmark_getAllData(bufferedImg);
			//benchmark_get5x5Matrix(bufferedImg);
			//benchmark_set5x5Matrix(bufferedImg);
			//benchmark_getPixel(bufferedImg);
			//benchmark_setPixel(bufferedImg);

			
			
					} catch (IOException e) {
			
			System.out.println("Reading image in failed. Benchmarks Halted.");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//benchmark_setAllData(bufferedImg, false);
		
		long benchEndTime = System.nanoTime();
		System.out.println("BENCHMARKING END****************************");
		System.out.println("Time (ms):  " + ((benchEndTime - benchStartTime) / 1000000));
		System.out.println("Time (s):  " + ((benchEndTime - benchStartTime) / 1000000/1000));
		System.out.println("Time (minutes):  " + ((benchEndTime - benchStartTime) / 1000000/1000/60) );
	}
	
	
}
