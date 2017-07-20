#include <iostream>
#include <fstream>
#include <chrono>
#include <cMath>
#include <typeinfo>
#include <vector>
#define cimg_use_jpg
#include "CImg.h"
#include "functions.cpp"

int main(int argc, char **argv) {

	std::vector<int> v;
	std::string filename = "knv.jpg";

	//const char* file_i = cimg_option("-i", "lena512.bmp", "Input image");
	const char* file_i = cimg_option("-i", "RGB_Color_Bars.BMP", "Input image");
	//cimg_library::cimg::graphicsmagick_path("C:\Program Files\GraphicsMagick - 1.3.26 - Q16");
	cimg_library::CImg<unsigned char> img(file_i);
	std::cout << "Image Read Successfully. Starting Benchmarks" << std::endl;
	std::cout << "START ALL.*************************" << std::endl;

	//FINAL SUITE
	auto benchmarkStartTime = std::chrono::high_resolution_clock::now();
	benchmark_getPixel(img, true);
	benchmark_getPixel(img, false);
	benchmark_setPixel(img);
	benchmark_set5x5Matrix(img);
	benchmark_get5x5Matrix(img, true);
	benchmark_setAllData(img, true);
	benchmark_getAllData(img, false);
	benchmark_getAllData(img, true);
	auto benchmarkEndTime = std::chrono::high_resolution_clock::now();
	std::cout << "END ALL*************************" << std::endl;
	auto testTimeMillis = std::chrono::duration_cast<std::chrono::milliseconds>(benchmarkEndTime - benchmarkStartTime).count();
	std::cout << "Time (ms):  " << testTimeMillis << std::endl;
	std::cout << "Time (s):  " << testTimeMillis / double(1000) << std::endl;
	std::cout << "Time (minutes): " << testTimeMillis / double(1000) / double(60) << std::endl;

	return 0;
}
