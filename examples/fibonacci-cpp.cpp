#include <iostream>
#include <fstream>
#include <string>

int calFibonacci(long long x){
    if (x ==0)
        return 0;
    else if (x==1 || x==2)
        return 1;
    else
        return calFibonacci(x-1) + calFibonacci(x-2);
}

long long readInput(){
    std::ifstream file ("input.txt");
    std::string line;
    long long input;
    if (file.is_open())
    {
        getline (file,line);
        input= stoll (line, nullptr, 10);
        file.close();
    }
    return input;
}

void writeOutput(long long x){
    std::ofstream file ("output.txt");
    if (file.is_open())
    {
        file << std::to_string(x);
        file.close();
    }
}

int main() {
    long long x= readInput();
    long long result =calFibonacci(x);
    std::cout << result << std::endl;
    writeOutput(result);
    return 0;
}
