
//Package
package pkg1;

import java.awt.List;
//Imports
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

import javax.imageio.ImageIO;
import java.awt.image.DataBufferInt;
import java.awt.image.WritableRaster;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.stat.descriptive.rank.Median;

//Benchmarking suite
//Do each for s = 10, 100, 1000, 10000
public class Test {
	
	//Gets benchmark statistics, and writes it to the file fileName
	private static void writeStatisticsToFile(double[] data,
											  long totalLoopTime,
											  BufferedImage b,
											  String benmarkName,
											  String fileName) throws FileNotFoundException{
		
		DescriptiveStatistics stats = new DescriptiveStatistics(data);
		Median statsMedian = new Median();

		PrintWriter fileOut = new PrintWriter(new FileOutputStream(fileName));
		
		fileOut.println("getPixel Benchmark");
		fileOut.println("__________________");
		fileOut.println("Image Dimensions (WxH):  " + b.getWidth() + " * " + b.getHeight());
		fileOut.println("Total Pixel Count:  " + (b.getWidth()*b.getHeight()));
		fileOut.println();
		
		fileOut.println("Times are given in nanoseconds");
		fileOut.println("Total Time for Test (loop evals included):  " + totalLoopTime);
		fileOut.println("Total Function Calls:  " + stats.getN());
		fileOut.println("Total function call time (function calls only):  " + stats.getSum());
		fileOut.println("Avg function call Time:  " + stats.getMean());
		fileOut.println("Max function call Time:  " + stats.getMax());
		fileOut.println("Min function call Time:  " + stats.getMin());
		fileOut.println("Median function call time:  " + statsMedian.evaluate(data, 50.0));
		fileOut.println("stdDev of times: " + stats.getStandardDeviation());
		
		fileOut.close();
		fileOut = null;
		
	}
	
	//ArrayList<ArrayList<Long>> listOfTimesAllBenches = new ArrayList<ArrayList<Long>>(b.getWidth()*b.getHeight()*50);
	//ArrayList<Long> = inidividualBenchTimes
	//listOfTimesAllBenches.get(benchCount).add(singleCallTime);
	//System.out.println(singleCallTime);
	
	private static void benchmark_gtPixel(BufferedImage b, String outFile) throws FileNotFoundException{
		
		long totalBenchmarkTime = 0l;
		long totalLoopTime = 0;				
		long singleCallTime = 0l;
		
		ArrayList<ArrayList<Long>> listOfCallTimes = new ArrayList<ArrayList<Long>>(b.getWidth()*b.getHeight());
		
		
		totalBenchmarkTime = System.currentTimeMillis();
		for(int benchCount = 0; benchCount < 50; benchCount++){//For 50 passes
			
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
		
		//writeStatsToFile2d
		
		
	}
	
	private static void benchmark_getPixel(BufferedImage b, String outFile) throws FileNotFoundException{
		
		//long totalTime = 0l;
		//Variable to keep track of execution times
		long totalBenchmarkTime = 0l;
		long totalLoopTime = 0;				
		long singleCallTime = 0l;
		
		//List to store individual call times
		
		ArrayList<Long> listOfTimes = new ArrayList<Long>(b.getWidth()*b.getHeight());
		
		/* Begin Gathering Data */
		totalBenchmarkTime = 0l;
		
		totalLoopTime = System.nanoTime();
		
			for(int x = 0; x < b.getWidth(); x++){
				for(int y = 0; y < b.getHeight(); y++){
					singleCallTime = System.nanoTime();
					b.getRGB(x,y);
					singleCallTime = System.nanoTime() - singleCallTime;
					
					listOfTimes.add(singleCallTime);
				}
			}
			
		//int[] x = ((DataBufferInt) b.getRaster().getDataBuffer()).getData();
		
		//b.getRaster().getDataBuffer().getData();

		totalLoopTime = System.nanoTime() - totalLoopTime;
		/*End Data Gathering*/
		
		//Get the list of times as an array of primitive doubles for statistics analysis.
		double[] data = new double[listOfTimes.size()];
		
		for(int i = 0; i < listOfTimes.size(); i++){
			data[i] = listOfTimes.get(i).doubleValue();
		}
		
		writeStatisticsToFile(data, totalLoopTime, b, "getPixel_Benchmark", outFile);
			
	}
	
	private static void benchMark_stPixel(BufferedImage b, String outFile) throws FileNotFoundException{
		
		long totalBenchmarkTime = 0l;
		long totalLoopTime = 0;				
		long singleCallTime = 0l;
		Random rand = new Random();
		int pixelValue = 0;
		
		int[][] randPixels = new int[b.getWidth()][b.getHeight()];			
		
		ArrayList<ArrayList<Long>> listOfCallTimes = new ArrayList<ArrayList<Long>>(b.getWidth()*b.getHeight());
		
		for(int benchCount = 0; benchCount < 50; benchCount++){//For 50 passes
			
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
		
		//writeStats_2d
	}
	
	private static void benchmark_setPixel(BufferedImage b, String outFile) throws FileNotFoundException{
		
		//long totalTime = 0l;
		//Variable to keep track of execution times
		long totalBenchmarkTime = 0l;
		long totalLoopTime = 0;				
		long singleCallTime = 0l;
		
				
		//List to store individual call times
				
		ArrayList<Long> listOfTimes = new ArrayList<Long>(b.getWidth()*b.getHeight());
				
		
		/* Begin Gathering Data */
		totalBenchmarkTime = 0l;
				
		totalLoopTime = System.nanoTime();
				
		for(int x = 0; x < b.getWidth(); x++){
			for(int y = 0; y < b.getHeight(); y++){
				singleCallTime = System.nanoTime();
				//b.setRGB(x, y, greenPixel);
				singleCallTime = System.nanoTime() - singleCallTime;
							
				listOfTimes.add(singleCallTime);
			}
		}

		totalLoopTime = System.nanoTime() - totalLoopTime;
		/*End Data Gathering*/
				
		//Get the list of times as an array of primitive doubles for statistics analysis.
		double[] data = new double[listOfTimes.size()];
				
		for(int i = 0; i < listOfTimes.size(); i++){
			data[i] = listOfTimes.get(i).doubleValue();
		}
				
		writeStatisticsToFile(data, totalLoopTime, b, "setPixel_Benchmark", outFile);
					
	}
	
	private static void benchmark_get5x5Matrix(BufferedImage b, String outFile) throws FileNotFoundException{
		long totalBenchmarkTime = 0l;
		long totalLoopTime = 0;				
		long singleCallTime = 0l;
		
		ArrayList<ArrayList<Long>> listOfCallTimes = new ArrayList<ArrayList<Long>>(b.getWidth()*b.getHeight());
		
		
		totalBenchmarkTime = System.currentTimeMillis();
		for(int benchCount = 0; benchCount < 50; benchCount++){//For 50 passes
			
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
		
	}

	private static void benchmark_set5x5Matrix(BufferedImage b, String outFile) throws FileNotFoundException{
		
		long totalBenchmarkTime = 0l;
		long totalLoopTime = 0;				
		long singleCallTime = 0l;
		Random rand = new Random();
		int[] pixelMatrix = new int[25];
		
		int[][][] randPixelMatricies = new int[b.getWidth()][b.getHeight()][25];
		
		ArrayList<ArrayList<Long>> listOfCallTimes = new ArrayList<ArrayList<Long>>(b.getWidth()*b.getHeight());
		
		for(int benchCount = 0; benchCount < 50; benchCount++){//For 50 passes
			
			for(int y = 0; y < b.getHeight(); y++){
				for(int x = 0; x < b.getWidth(); x++){
					for(int k = 0; k < randPixelMatricies[x][y].length; k++){
						randPixelMatricies[x][y][k] = rand.nextInt();
					}
				}
			}
			
			totalLoopTime = System.nanoTime();
			for(int y = 0; y < b.getHeight(); y++){
				for(int x = 0; x < b.getWidth(); x++){
					pixelMatrix = randPixelMatricies[x][y];
					singleCallTime = System.nanoTime();
					b.setRGB(x, y, 5, 5, pixelMatrix, 0, b.getWidth());
					singleCallTime = System.nanoTime() - singleCallTime;
					listOfCallTimes.get(benchCount).add(singleCallTime);
				}
			}
			totalLoopTime = System.nanoTime() - totalLoopTime;
			totalBenchmarkTime += totalLoopTime;
		}
		
		
	}
	
	
	private static void benchmark_getPixelGroup(BufferedImage b,
												String outFile) throws FileNotFoundException{
		
		//long totalTime = 0l;
		//Variable to keep track of execution times
		long totalBenchmarkTime = 0l;
		long totalLoopTime = 0;				
		long singleCallTime = 0l;
				
		//List to store individual call times
				
		ArrayList<Long> listOfTimes = new ArrayList<Long>(b.getWidth()*b.getHeight());
				
		/* Begin Gathering Data */
		totalBenchmarkTime = 0l;
				
		totalLoopTime = System.nanoTime();
				
		for(int x = 0; x < b.getWidth()-5; x++){
			for(int y = 0; y < b.getHeight()-5; y++){
				singleCallTime = System.nanoTime();
				b.getRGB(x, y, 5, 5, null, 0, b.getWidth());
				singleCallTime = System.nanoTime() - singleCallTime;
	
				listOfTimes.add(singleCallTime);
			}
		}
					
				//int[] x = ((DataBufferInt) b.getRaster().getDataBuffer()).getData();
				
				//b.getRaster().getDataBuffer().getData();

		totalLoopTime = System.nanoTime() - totalLoopTime;
		/*End Data Gathering*/
				
		//Get the list of times as an array of primitive doubles for statistics analysis.
		double[] data = new double[listOfTimes.size()];
				
		for(int i = 0; i < listOfTimes.size(); i++){
			data[i] = listOfTimes.get(i).doubleValue();
		}
				
		writeStatisticsToFile(data, totalLoopTime, b, "get5x5Matrix_Benchmark", outFile);
					
		
	}
	
	private static void benchmark_setPixelGroup(BufferedImage b, String outFile) throws FileNotFoundException{
		
		//long totalTime = 0l;
		//Variable to keep track of execution times
		long totalBenchmarkTime = 0l;
		long totalLoopTime = 0;				
		long singleCallTime = 0l;
		final int greenPixel = 0xFF00FF00;
		
		int[] rgbArray = new int[25];
		for(int i = 0; i < rgbArray.length; i++){
			rgbArray[i] = greenPixel;
		}
				
		//List to store individual call times
		ArrayList<Long> listOfTimes = new ArrayList<Long>(b.getWidth()*b.getHeight());
				
		/* Begin Gathering Data */
		totalBenchmarkTime = 0l;
				
		totalLoopTime = System.nanoTime();
				
		for(int x = 0; x < b.getWidth()-5; x++){
			for(int y = 0; y < b.getHeight()-5; y++){
				singleCallTime = System.nanoTime();
				b.setRGB(x, y, 5, 5, rgbArray, 0, b.getWidth());
				singleCallTime = System.nanoTime() - singleCallTime;
							
				listOfTimes.add(singleCallTime);
			}
		}
					
				//int[] x = ((DataBufferInt) b.getRaster().getDataBuffer()).getData();
				
				//b.getRaster().getDataBuffer().getData();

		totalLoopTime = System.nanoTime() - totalLoopTime;
		/*End Data Gathering*/
				
		//Get the list of times as an array of primitive doubles for statistics analysis.
		double[] data = new double[listOfTimes.size()];
				
		for(int i = 0; i < listOfTimes.size(); i++){
			data[i] = listOfTimes.get(i).doubleValue();
		}
				
		writeStatisticsToFile(data, totalLoopTime, b, "set5x5Matrix_Benchmark", outFile);
				
	}
	
	
	
	private static void benchmark_setAllPixelData(BufferedImage b,
												  WritableRaster r,
												  String outFile) throws FileNotFoundException{
		
		long totalBenchmarkTime = 0l;
		//long totalLoopTime = 0;				
		long singleCallTime = 0l;
		
		ArrayList<Long> listOfTimes = new ArrayList<Long>(b.getWidth()*b.getHeight());
		
		for(int benchCount = 0; benchCount < 50; benchCount++){
			
		}
		
	}

	public static void main(String[] args){
		
//		final String parentPath;
//		
//		long s = 0l;
//		
//		System.out.println(Long.MAX_VALUE);
//		System.out.println(Double.MAX_VALUE);
//		try {
//			benchmark_getPixel(ImageIO.read(new File("jpgTest2.jpg")), "testOut.txt");
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

		
		double[] d = {1,2,3};
		DescriptiveStatistics s = new DescriptiveStatistics(d);
		System.out.println(s.getStandardDeviation());
		
		
	}
	
}
