# Function for nth Fibonacci number
def Fibonacci(n):

    # Check if input is 0 then it will
    # print incorrect input
    if n < 0:
        print("Incorrect input")

    # Check if n is 0
    # then it will return 0
    elif n == 0:
        return 0

    # Check if n is 1,2
    # it will return 1
    elif n == 1 or n == 2:
        return 1

    else:
        return Fibonacci(n-1) + Fibonacci(n-2)


def readInputFile():
    f = open("input.txt", "r")
    #consider that each input file contains one input
    return int(f.readline())

def writeOutputFile(data):
    f = open("output.txt", "w")
    f.write(str(data))

if __name__ == "__main__":
    x=readInputFile()
    print(Fibonacci(x))
    writeOutputFile(Fibonacci(x))