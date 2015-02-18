import re

"""Reads the files and puts each line into an a 2-d Array. 
   Deletes unnecessary lines and most importantly, puts relevant data into an array. """
def parseToArray():
    data = []
    with open('weather.dat') as f:
        for line in f:
            data.append(splitData(line)[1:4])
        for unnecessaryLine in range(8):
            del(data[0])
        del(data[len(data)-1])
        del(data[len(data)-1])
    return data

""" Uses Regex to replace any amount of spaces within each line with one space, to utilize .split().
    Replaces the foreign character '*'. """
def splitData(line):
    return re.sub('\s+', ' ', line).replace('*','').split(' ')

"""Stores the value of the min temperature spread,
   Checks the rest of the column to find the min temperature spread.
   Returns the day with the least tempSpread. """
def getSmallestTempSpreadDay():
    finalData = parseToArray()
    # Makes each element an int.
    for i in range(len(finalData)):
      for j in range(3):
        finalData[i][j] = int(finalData[i][j])
    # Arbitrarily sets minValue to the value of the first day.
    minValue = finalData[0][1] - finalData[0][2]
    # Arbitrarily sets dayOfTemp to the first Day.
    dayOfTemp = finalData[0][0]
    for i in range(len(finalData)):
        maxTemp = finalData[i][1]
        minTemp = finalData[i][2]
        if (maxTemp - minTemp < minValue):
            minValue = maxTemp - minTemp
            dayOfTemp = finalData[i][0]
    print(str(dayOfTemp) + " " + "is the day with the smallest temperature spread.")

if __name__ == "__main__":
    getSmallestTempSpreadDay()
