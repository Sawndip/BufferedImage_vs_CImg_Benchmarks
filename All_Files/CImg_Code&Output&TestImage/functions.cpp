#include <iostream>
#include <string>
#include <fstream>
#include <chrono>
#include <vector>
#include <algorithm>
#include <random>
#include <numeric>
#include <typeinfo>
#include "CImg.h"


//WORKING
template <typename T>
double stdDev(std::vector<T>& v, boolean isPopulationStandardDev) {
	std::vector<double> d(v.size());

	double mean = std::accumulate(v.begin(), v.end(), 0) / (v.size() + 0.0);

	for (int i = 0; i < v.size(); i++) {
		d[i] =(double) ((v.at(i) - mean))*((v.at(i) - mean));
	}

	
	double sumOfMeanDifferences = 0.0;

	for (int i = 0; i < v.size(); i++) {
		sumOfMeanDifferences += (double(v.at(i)) - mean)*(double(v.at(i)) - mean);
	}


	//std::cout << std::accumulate(d.begin(), d.end(), 0) / (d.size()+.00) << std::endl;

	return std::sqrt(sumOfMeanDifferences/(v.size()-isPopulationStandardDev));
		
}

//WORKING
template <typename T>
void writeStatisticsToFile_1D(std::vector<long long>& listOfTimes,
							unsigned long long totalBenchmarkTime,
							cimg_library::CImg<T> image,
							std::string benchmarkName,
							std::string outFileName){

	std::cout << "Start Writing " << outFileName << std::endl;
	//std::cout << "inStatistics_1D" << std::endl;

	std::fstream file;
	file.open(outFileName, std::fstream::out);

	file << benchmarkName << std::endl;
	file << "----------------------" << std::endl;
	file << "Image Dimensions(WxH):  " << image.width() << " * " << image.height() << std::endl;
	file << "Total Pixel CountL " << (image.width()*image.height()) << std::endl;
	file << std::endl;
	std::cout << "-\tWrote Image Data Statistics." << std::endl;

	file << "Times are given in (NS)/(MS)" << std::endl;
	file << "Total time for Test (loop evals included):  " 
		 << totalBenchmarkTime  << "/ " << totalBenchmarkTime / unsigned long long(1000000) << std::endl;
	file << "Total funtion calls: " << listOfTimes.size() << std::endl;
	file << "Total function call Time (calls only):  "
		 << std::accumulate(listOfTimes.begin(), listOfTimes.end(), 0) << "/ "
		 << std::accumulate(listOfTimes.begin(), listOfTimes.end(), 0) / unsigned long long(1000000) << std::endl;
	file << "Avg function call time:  " 
		 << std::accumulate(listOfTimes.begin(), listOfTimes.end(), 0) / unsigned long long(listOfTimes.size()) << "/ "
		 << std::accumulate(listOfTimes.begin(), listOfTimes.end(), 0) / unsigned long long(listOfTimes.size()) / double(1000000) << std::endl;
	file << "Max function call time:  "
		 << (*std::max_element(listOfTimes.begin(), listOfTimes.end())) << "/ "
		 << (*std::max_element(listOfTimes.begin(), listOfTimes.end())) / double(1000000) <<std::endl;
	file << "Min function call time:  "
		 << (*std::min_element(listOfTimes.begin(), listOfTimes.end())) << "/ "
		 << (*std::min_element(listOfTimes.begin(), listOfTimes.end())) / double(1000000) <<std::endl;

	std::nth_element(
		listOfTimes.begin(),
		(listOfTimes.begin() + listOfTimes.size() / 2),
		listOfTimes.end());
	file << "Median function call time:  "
		 << *(listOfTimes.begin() + listOfTimes.size() / 2) << "/ "
		 << *(listOfTimes.begin() + listOfTimes.size() / 2) / double(1000000) << std::endl;
	file << "stdDev of times () " << stdDev(listOfTimes, true) << "/ "
		 << stdDev(listOfTimes, true) / double(1000000) << std::endl;
	file << std::endl << std::endl << std::endl;

	std::cout << "-\tWrote Overall Statistics in" << outFileName << std::endl;
	std::cout << "-\tWriting individual pass times: " << std::endl;
	file << "INDIVIDUAL PASS TIMES (NS/MS): " << std::endl;
	for (int i = 0; i < listOfTimes.size(); i++){
		file << (i + 1) << ".  " << listOfTimes[i] << "/" << listOfTimes[i]/double(1000000) << std::endl;
		std::cout << "-\t\tPass " << i + 1 << " Written" << std::endl;
	}

	file.close();
	std::cout << "END Writing " << outFileName << std::endl;
}


//WORKING
template <typename T>
void writeStatisticsToFile_2D(std::vector<std::vector<long long>> & listOfTimes,
							unsigned long long totalBenchmarkTime,
							cimg_library::CImg<T> image,
							std::string benchmarkName,
							std::string outFileName) {

	std::cout << "Start Writing " << outFileName << std::endl;
	//std::cout << "inStatistics_2D" << std::endl;

	std::fstream file;
	file.open(outFileName, std::fstream::out);

	std::vector<long long> allData(0);

	for (int i = 0; i < listOfTimes.size(); i++) {
		allData.insert(allData.end(), listOfTimes[i].begin(), listOfTimes[i].end());
	}
	std::cout << "-\tGot all data as one list." << std::endl;

	file << benchmarkName << std::endl;
	file << "----------------------" << std::endl;
	file << "Image Dimensions(WxH):  " << image.width() << " * " << image.height() << std::endl;
	file << "Total Pixel CountL " << (image.width()*image.height()) << std::endl;
	file << std::endl;
	std::cout << "-\tWrote Image Data Statistics." << std::endl;

	file << "Times are given in (NS/MS): " << std::endl;
	file << "Time for 50 benches:  " << totalBenchmarkTime << "/ "
									 << totalBenchmarkTime / double(1000000) << std::endl;
	file << "Total Function calls: " << allData.size() << std::endl;

	long long totalCallTime = std::accumulate(allData.begin(), allData.end(), 0);
	file << "Total function call Time (calls only):  "
		 << totalCallTime << "/ " 
		 << totalCallTime / double(1000000) << std::endl;

	long long avgCallTime = std::accumulate(allData.begin(), allData.end(), 0) / double(allData.size());
	file << "Avg function call time (NS):  "
		 << avgCallTime
		 << avgCallTime / double(allData.size()) /double(1000000) << std::endl;

	long long maxCallTime = (*std::max_element(allData.begin(), allData.end()));
	file << "Max function call time (NS):  "
		 << maxCallTime <<"/ "
		 << maxCallTime / double(1000000) << std::endl;

	long long minCallTime = (*std::min_element(allData.begin(), allData.end()));
	file << "Min function call time (NS):  "
		 << minCallTime << "/ "
		 << minCallTime / double(1000000) << std::endl;

	std::nth_element(
		allData.begin(),
		(allData.begin() + allData.size() / 2),
		allData.end());


	long long medianCallTime = *(allData.begin() + allData.size() / 2);
	file << "Median function call time (NS):  "
		<< medianCallTime << "/ "
		<< medianCallTime / double(1000000) << std::endl;

	double standardDevNanos = stdDev(allData, true);
	file << "stdDev of times " << standardDevNanos << "/ "
		 << standardDevNanos / double(1000000) << std::endl;
	file << std::endl << std::endl << std::endl;

	std::cout << "-\tWrote Overall Statistics in" << outFileName << std::endl;
	file << "INDIVIDUAL PASS STATS MS" << std::endl;
	file << "average/M: median/ U: max/ L: min/ S; stDev/ T: timeMS" << std::endl;

	double average[50] = {0.0};
	double median[50] = { 0.0 };
	double max[50] = { 0.0 };
	double min[50] = { 0.0 };
	double stDev[50] = { 0.0 };
	double timeNS[50] = { 0.0 };

	//Turns out all these calculations are pretty expensive. So, 
	std::cout << "-\tCalculating individual pass stats:" << std::endl;
	for (int i = 0; i < listOfTimes.size(); i++) {
			std::nth_element(
				listOfTimes[i].begin(),
				(listOfTimes[i].begin() + listOfTimes[i].size() / 2),
				listOfTimes[i].end());
			average[i] = std::accumulate(allData.begin(), allData.end(), 0) / double(allData.size());
			median[i] = (*(listOfTimes[i].begin() + listOfTimes[i].size() / 2));
			max[i] = (*std::max_element(listOfTimes[i].begin(), listOfTimes[i].end()));
			min[i] = (*std::min_element(listOfTimes[i].begin(), listOfTimes[i].end()));
			stDev[i] = stdDev(listOfTimes[i], true);
			timeNS[i] = std::accumulate(listOfTimes[i].begin(), listOfTimes[i].end(), 0);
			std::cout << "-\t\tPass " << i + 1 << " calculated." << std::endl;
	}

	double nanoToMilliRatio = 1000000.0;
	for (int i = 0; i < listOfTimes.size(); i++) {
		file << i + 1 << ". "
			<< average[i] / nanoToMilliRatio << "/ "
			<< median[i] / nanoToMilliRatio << "/ "
			<< max[i] / nanoToMilliRatio << "/ "
			<< min[i] / nanoToMilliRatio << "/ "
			<< stDev[i] / nanoToMilliRatio << "/ "
			<< timeNS[i] / nanoToMilliRatio << " |"
			<< std::endl;
		std::cout << "-\t\tPass " << i + 1 << " outputted millisecondsData." << std::endl;
	}
	file << std::endl;

	file << "INDIVIDUAL PASS STATS NS" << std::endl;
	file << "average/M: median/ U: max/ L: min/ S; stDev/ T: timeNS" << std::endl;
	for (int i = 0; i < listOfTimes.size(); i++) {
		file << i + 1 << ". "
			<< average[i] << "/ "
			<< median[i] << "/ "
			<< max[i] << "/ "
			<< min[i]  << "/ "
			<< stDev[i]  << "/ "
			<< timeNS[i] << " |"
			<< std::endl;
		std::cout << "-\t\tPass " << i + 1 << " outputted nanosecondsData." << std::endl;
	}

	file.close();
	std::cout << "END Writing " << outFileName << std::endl;

}

//WORKING
template <typename T>
void benchmark_getPixel(cimg_library::CImg<T> image, bool use_atXY) {

	auto benchmarkStartTime = std::chrono::high_resolution_clock::now();
	auto benchmarkEndTime = std::chrono::high_resolution_clock::now();

	auto singleCallStart = std::chrono::high_resolution_clock::now();
	auto singleCallEnd = std::chrono::high_resolution_clock::now();

	//std::chrono::duration<long, std::ratio<1, 1000000000>>;

	std::cout << "START benchmark_getPixel" 
		      << (use_atXY? "_using_atXY." : "_using_Operator().") << std::endl;
	std::vector<std::vector<long long>> listOfCallDurations(50);

	if (use_atXY) {
		benchmarkStartTime = std::chrono::high_resolution_clock::now();
		for (int benchCount = 0; benchCount < 50; benchCount++) {

			cimg_forXY(image, x, y) {
				singleCallStart = std::chrono::high_resolution_clock::now();
				//image(x,y) is much faster
				image._atXY(x, y, 0, 0);
				image._atXY(x, y, 0, 1);
				image._atXY(x, y, 0, 2);
				/*image(x, y, 0, 1);
				image(x, y, 0, 2);*/
				singleCallEnd = std::chrono::high_resolution_clock::now();
				listOfCallDurations[benchCount]
					.push_back(
						std::chrono::duration_cast<std::chrono::nanoseconds>(singleCallEnd - singleCallStart)
						.count()
					);
			}

			//std::cout << "bench " << benchCount + 1 << " complete" << std::endl;
		}

		benchmarkEndTime = std::chrono::high_resolution_clock::now();
	}
	else {
		benchmarkStartTime = std::chrono::high_resolution_clock::now();
		for (int benchCount = 0; benchCount < 50; benchCount++) {

			cimg_forXY(image, x, y) {
				singleCallStart = std::chrono::high_resolution_clock::now();
				//image(x,y) is much faster
				image(x, y, 0, 0);
				image(x, y, 0, 1);
				image(x, y, 0, 2);
				singleCallEnd = std::chrono::high_resolution_clock::now();
				listOfCallDurations[benchCount]
					.push_back(
						std::chrono::duration_cast<std::chrono::nanoseconds>(singleCallEnd - singleCallStart)
						.count()
					);
			}

			//std::cout << "bench " << benchCount + 1 << " complete" << std::endl;
		}

		benchmarkEndTime = std::chrono::high_resolution_clock::now();
	}
	
	unsigned long long time = std::chrono::duration_cast<std::chrono::nanoseconds>(benchmarkEndTime - benchmarkStartTime).count();

	std::cout << "End benchmark_getPixel"
		<< (use_atXY ? "_using_atXY." : "_using_Operator().") << std::endl;
	
	std::string benchmarkName = std::string("benchmark_getPixel") 
							  + std::string((use_atXY? "_using_atXY": "_using_Operator()"));
	std::string outFileName = benchmarkName + ".txt";

	writeStatisticsToFile_2D(listOfCallDurations,
		time,
		image,
		benchmarkName,
		outFileName);


}

//WORKING
template <typename T>
void benchmark_setPixel(cimg_library::CImg<T> image) {
	
	std::cout << "START benchmark_setPixel." << std::endl;

	unsigned long long totalBenchTime = 0;
	auto benchmarkStartTime = std::chrono::high_resolution_clock::now();
	auto benchmarkEndTime = std::chrono::high_resolution_clock::now();

	auto singleCallStart = std::chrono::high_resolution_clock::now();
	auto singleCallEnd = std::chrono::high_resolution_clock::now();

	std::vector<std::vector<T>> randPixels (image.width(),std::vector<T>(image.height(),0));
	int pixelValue = 0;

	std::vector<std::vector<long long>> listOfCallDurations(50);

	for (int benchCount = 0; benchCount < 50; benchCount++) {

		for (int x = 0; x < 50; x++) {
			for (int y = 0; y < 50; y++) {
				randPixels[x][y] = static_cast<T>(rand());
			}
		}

		benchmarkStartTime = std::chrono::high_resolution_clock::now();
		cimg_forXY(image, x, y){
				pixelValue = randPixels[x][y];
				singleCallStart = std::chrono::high_resolution_clock::now();

				image(x, y, 0 ,0) = pixelValue;
				image(x, y, 0, 1) = pixelValue;
				image(x, y, 0, 1) = pixelValue;

				singleCallEnd = std::chrono::high_resolution_clock::now();

				listOfCallDurations[benchCount]
					.push_back(
						std::chrono::duration_cast<std::chrono::nanoseconds>(singleCallEnd - singleCallStart)
						.count()
					);
			}

		benchmarkEndTime = std::chrono::high_resolution_clock::now();
		totalBenchTime += std::chrono::duration_cast<std::chrono::nanoseconds>(benchmarkEndTime - benchmarkStartTime).count();

		std::cout << "-\tBench " << benchCount + 1 << " complete" << std::endl;
	}

	std::cout << "End benchmark_setPixel.";
	//std::cout << "totalBenchTime(NS): " << totalBenchTime << std::endl;

	writeStatisticsToFile_2D(listOfCallDurations,
		totalBenchTime,
		image,
		"benchmark_setPixel",
		"benchmark_setPixel.txt");

}

//WORKING
template <typename T>
void benchmark_get5x5Matrix(cimg_library::CImg<T> image, bool makeCopy) {

	long long totalBenchTime = 0;
	auto benchmarkStartTime = std::chrono::high_resolution_clock::now();
	auto benchmarkEndTime = std::chrono::high_resolution_clock::now();

	auto singleCallStart = std::chrono::high_resolution_clock::now();
	auto singleCallEnd = std::chrono::high_resolution_clock::now();

	int matrixArr[25] = { 0 };

	//std::vector<std::vector<T>> randPixels(image.width(), std::vector<T>(image.height(), 0));
	//int pixelValue = 0;
	std::cout << "START benchmark_get5x5Matrix." << std::endl;
	std::vector<std::vector<long long>> listOfCallDurations(50);

	if (makeCopy) {
		T* copyData = new T[25];

		benchmarkStartTime = std::chrono::high_resolution_clock::now();
		for (int benchCount = 0; benchCount < 50; benchCount++) {

			//std::cout << image[0] << std::endl;
			CImg_5x5(I, T);
			cimg_forC(image, k)
				//This does allocations to I apparently
				cimg_for5x5(image, x, y, 0, k, I, T) {
					singleCallStart = std::chrono::high_resolution_clock::now();
					for (int dataIndex = 0; dataIndex < 25; dataIndex++) {
						copyData[dataIndex] = I[dataIndex];
					}
					singleCallEnd = std::chrono::high_resolution_clock::now();
					listOfCallDurations[benchCount]
						.push_back(
							std::chrono::duration_cast<std::chrono::nanoseconds>(singleCallEnd - singleCallStart)
							.count()
					);
				}
			std::cout << "-\tBench " << benchCount + 1 << " complete" << std::endl;
		}
		benchmarkEndTime = std::chrono::high_resolution_clock::now();
	}
	else {

		benchmarkStartTime = std::chrono::high_resolution_clock::now();
		for (int benchCount = 0; benchCount < 50; benchCount++) {

			//std::cout << image[0] << std::endl;
			CImg_5x5(I, T);
			cimg_forC(image, k)
				//This does allocations to I apparently, you can get your values from here
				cimg_for5x5(image, x, y, 0, k, I, T) {
				//Data would be accessable here via I[index]
			}
			std::cout << "-\tBench " << benchCount + 1 << " complete" << std::endl;
		}
		benchmarkEndTime = std::chrono::high_resolution_clock::now();
	}

	auto time = std::chrono::duration_cast<std::chrono::milliseconds>(benchmarkEndTime - benchmarkStartTime).count();
	//std::cout << time / 1.0 << std::endl;

	std::cout << "End benchmark_get5x5Matrix." << std::endl;

	writeStatisticsToFile_2D(listOfCallDurations,
		time,
		image,
		"benchmark_get5x5Matrix",
		"benchmark_get5x5Matrix.txt");
}

//WORKING
template <typename T> 
void benchmark_set5x5Matrix(cimg_library::CImg<T> image) {

	long long totalBenchTime = 0;
	auto benchmarkStartTime = std::chrono::high_resolution_clock::now();
	auto benchmarkEndTime = std::chrono::high_resolution_clock::now();

	auto singleCallStart = std::chrono::high_resolution_clock::now();
	auto singleCallEnd = std::chrono::high_resolution_clock::now();


	//std::vector<std::vector<T>> randPixels(image.width(), std::vector<T>(image.height(), 0));
	//int pixelValue = 0;
	std::cout << "START benchmark_set5x5Matrix." << std::endl;

	std::vector<std::vector<long long>> listOfCallDurations(50);

	T* randData = new T[25];

	benchmarkStartTime = std::chrono::high_resolution_clock::now();
	for (int benchCount = 0; benchCount < 50; benchCount++) {

		std::vector<T> receiveMatrix(0);

		for (int randDataIndex = 0; randDataIndex < 25; randDataIndex++) {
			randData[randDataIndex] = static_cast<T>(rand());
		}

		//std::cout << image[0] << std::endl;
		CImg_5x5(I, T);
		cimg_forC(image, k)
			//This does allocations to I apparently
			cimg_for5x5(image, x, y, 0, k, I, T) {
			singleCallStart = std::chrono::high_resolution_clock::now();
			//receiveMatrix.assign(I, I + 25);
			for (int dataIndex = 0; dataIndex < 25; dataIndex++) {
				I[dataIndex] = randData[dataIndex];
			}
			singleCallEnd = std::chrono::high_resolution_clock::now();
			listOfCallDurations[benchCount]
				.push_back(
					std::chrono::duration_cast<std::chrono::nanoseconds>(singleCallEnd - singleCallStart)
					.count()
				);
			}
			std::cout << "-\tBench " << benchCount + 1 << " complete" << std::endl;
	}
	benchmarkEndTime = std::chrono::high_resolution_clock::now();
	
	auto time = std::chrono::duration_cast<std::chrono::milliseconds>(benchmarkEndTime - benchmarkStartTime).count();
	//std::cout << time / 1.0 << std::endl;

	std::cout << "End benchmark_set5x5Matrix." << std::endl;
	writeStatisticsToFile_2D(listOfCallDurations,
		time,
		image,
		"benchmark_set5x5Matrix",
		"benchmark_set5x5Matrix.txt");
}

//WORKING
template <typename T>
void benchmark_getAllData(cimg_library::CImg<T> image, bool pointerOnly) {

	auto benchmarkStartTime = std::chrono::high_resolution_clock::now();
	auto benchmarkEndTime = std::chrono::high_resolution_clock::now();

	auto singleCallStart = std::chrono::high_resolution_clock::now();
	auto singleCallEnd = std::chrono::high_resolution_clock::now();

	//std::chrono::duration<long, std::ratio<1, 1000000000>>;

	std::cout << "in func." << std::endl;
	std::vector<long long> listOfCallDurations(50);

	//cimg_library::CImg<T> randImage;
	if (!pointerOnly) {

		T* dataArray = new T[image.size()];

		benchmarkStartTime = std::chrono::high_resolution_clock::now();
		for (int benchCount = 0; benchCount < 50; benchCount++) {

			singleCallStart = std::chrono::high_resolution_clock::now();
			int i = 0;
			for (auto it = image.begin(); it != image.end(); it++, i++) {
				dataArray[i] = *it;
			}
			singleCallEnd = std::chrono::high_resolution_clock::now();
			listOfCallDurations[benchCount] =
				std::chrono::duration_cast<std::chrono::nanoseconds>(singleCallEnd - singleCallStart)
				.count()
				;

			//std::cout << "bench " << benchCount + 1 << " complete" << std::endl;
		}
		benchmarkEndTime = std::chrono::high_resolution_clock::now();
	}
	else {

		T* dataArray = new T[image.size()];

		benchmarkStartTime = std::chrono::high_resolution_clock::now();

		for (int benchCount = 0; benchCount < 50; benchCount++) {

			singleCallStart = std::chrono::high_resolution_clock::now();
			dataArray = image.data();
			singleCallEnd = std::chrono::high_resolution_clock::now();
			listOfCallDurations[benchCount] =
				std::chrono::duration_cast<std::chrono::nanoseconds>(singleCallEnd - singleCallStart)
				.count()
				;

			//std::cout << "bench " << benchCount + 1 << " complete" << std::endl;
		}

		benchmarkEndTime = std::chrono::high_resolution_clock::now();
	}
	

	

	auto time = std::chrono::duration_cast<std::chrono::milliseconds>(benchmarkEndTime - benchmarkStartTime).count();
	/*std::cout << time / 1.0 << std::endl;
	std::cout << listOfCallDurations.size() << std::endl;*/

	std::cout << std::string("benchmark_getAllData Complete") + (pointerOnly? "_pointerOnly":"") << std::endl;

	writeStatisticsToFile_1D(listOfCallDurations,
		time,
		image,
		(std::string ("benchmark_getAllData") + (pointerOnly? "_pointerOnly" : "")),
		std::string("benchmark_getAllData") + (pointerOnly? "_pointerOnly":"") + std::string(".txt")
		);

}

//WORKING
template <typename T>
void benchmark_setAllData(cimg_library::CImg<T> image, bool withAssign) {

	auto benchmarkStartTime = std::chrono::high_resolution_clock::now();
	auto benchmarkEndTime = std::chrono::high_resolution_clock::now();

	auto singleCallStart = std::chrono::high_resolution_clock::now();
	auto singleCallEnd = std::chrono::high_resolution_clock::now();

	//std::chrono::duration<long, std::ratio<1, 1000000000>>;

	std::cout << "START benchmark_setAllData" << std::endl;
	std::vector<long long> listOfCallDurations(50);

	cimg_library::CImg<T> randImage;

	if(withAssign)
	{
		benchmarkStartTime = std::chrono::high_resolution_clock::now();
		for (int benchCount = 0; benchCount < 50; benchCount++) {

			randImage = image.get_rand(static_cast<T>(FLT_MIN), static_cast<T>(FLT_MAX));

			singleCallStart = std::chrono::high_resolution_clock::now();
			image.assign(randImage);
			singleCallEnd = std::chrono::high_resolution_clock::now();
			listOfCallDurations[benchCount] = 
					std::chrono::duration_cast<std::chrono::nanoseconds>(singleCallEnd - singleCallStart)
					.count()
					;

			//std::cout << "bench " << benchCount + 1 << " complete" << std::endl;
			}
		benchmarkEndTime = std::chrono::high_resolution_clock::now();
	}
	else
	{
		benchmarkStartTime = std::chrono::high_resolution_clock::now();
		for (int benchCount = 0; benchCount < 50; benchCount++) {

			randImage = image.get_rand(static_cast<T>(FLT_MIN), static_cast<T>(FLT_MAX));

			singleCallStart = std::chrono::high_resolution_clock::now();
			for (auto it = image.begin(), randItr = randImage.begin(); it != image.end(); it++, randItr++) {
				*it = *randItr;
			}
			singleCallEnd = std::chrono::high_resolution_clock::now();

			/*cimg_forXYC(image, x, y, c) {
				singleCallStart = std::chrono::high_resolution_clock::now();
				*(image.data(x, y, 0, c)) = *(randImage.data(x, y, 0, c));
				singleCallEnd = std::chrono::high_resolution_clock::now();
			}
			*/
			listOfCallDurations[benchCount] =
				std::chrono::duration_cast<std::chrono::nanoseconds>(singleCallEnd - singleCallStart)
				.count()
				;

			//std::cout << "bench " << benchCount + 1 << " complete" << std::endl;
		}
		benchmarkEndTime = std::chrono::high_resolution_clock::now();
	}
		auto time = std::chrono::duration_cast<std::chrono::milliseconds>(benchmarkEndTime - benchmarkStartTime).count();

		std::cout << "End benchmark_setAllData" << std::endl;
		/*std::cout << time / 1.0 << std::endl;
		std::cout << listOfCallDurations.size() << std::endl;
		std::cout << "benchend" << std::endl;*/

		if (withAssign) {
			std::cout << "benchmark_setAllData_Assign complete." << std::endl;
			writeStatisticsToFile_1D(listOfCallDurations,
				time,
				image,
				"benchmark_setAllData_Assign",
				"benchmark_setAllData_Assign.txt");
		}
		else {
			std::cout << "benchmark_setAllData_Iterator complete." << std::endl;
			writeStatisticsToFile_1D(listOfCallDurations,
				time,
				image,
				"benchmark_setAllData_Iterator",
				"benchmark_setAllData_Iterator.txt");
		}
	
}

