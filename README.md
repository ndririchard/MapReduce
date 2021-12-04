# MapReduce
The main purpose of this projet is to manage data flow within a distributed application.
To achieve this goal, we will build a word counter application (like map reduce) without using the library Hadoop.<br>

Our application works with 10 remote machines allocated as follows:
![image](https://user-images.githubusercontent.com/94222983/144715384-794ae929-daed-4c7d-83f6-3530531d8ec7.png)

* 1 manager : the manager is the machine that manages all requests comming from the client.<br>
Indeed, it manage the map reduce process as follows :
  * Step 1 : compute the number of mapper according to the length of the data.
  * step 2 : split the data. 
  * step 3 : send each chrunk to one mapper and sure that all mappers are running atbthe same time (we did this by using multithreading).
  * step 4 : compute the number of reducer according to the number of mapper.
  * step 5 : once the mapping process is finish each reducer will take the result to mappers that that are attributed to it.
  * step 6 : concate the result of each reducer and send it to the client.
 
* 6 mappers : the mapper is the remote machine that do the mapping process.
* 3 reducers : the reducer is the remote machine that do the recducing process.

Follow this [link](https://www.ibm.com/topics/mapreduce) to know more about Map Reduce process.<br>

## Installation
The project is made with [java RMI](https://docs.oracle.com/javase/tutorial/rmi/index.html).<br>
Follow these steps to run it on your computer:
```
>>> git clone 
>>> cd (here you put the path of the clonning file)
```

## Usage
Now that your are in the working directory, open two terminals.<br>

**Terminal 1**
```
>>> javac client/*.java manager/*.java mapper/*.java reducer/*.java mapperThread/*.java reducerThread/*.java
>>> java manager/Manager
```

**Terminal 2**
```
>>> javac client/*.java manager/*.java mapper/*.java reducer/*.java mapperThread/*.java reducerThread/*.java
>>> java client/MapRedFrame
```





