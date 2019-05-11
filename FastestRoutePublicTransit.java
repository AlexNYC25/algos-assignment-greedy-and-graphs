/**
 * Public Transit
 * Author: Alexis Montes and Carolyn Yao
 * Does this compile? Y
 */

/**
 * This class contains solutions to the Public, Public Transit problem in the
 * shortestTimeToTravelTo method. There is an existing implementation of a
 * shortest-paths algorithm. As it is, you can run this class and get the solutions
 * from the existing shortest-path algorithm.
 */
public class FastestRoutePublicTransit {

  /**
   * The algorithm that could solve for shortest travel time from a station S
   * to a station T given various tables of information about each edge (u,v)
   *
   * @param S the s th vertex/station in the transit map, start From
   * @param T the t th vertex/station in the transit map, end at
   * @param startTime the start time in terms of number of minutes from 5:30am
   * @param lengths lengths[u][v] The time it takes for a train to get between two adjacent stations u and v
   * @param first first[u][v] The time of the first train that stops at u on its way to v, int in minutes from 5:30am
   * @param freq freq[u][v] How frequently is the train that stops at u on its way to v
   * @return shortest travel time between S and T
   */

  /*
      Alg:
        find the shortestPath Arr:
          * Using a modified version of shortest time that was part of originally code
            set arrays of size of colloumns and fill in values according to values
            of the passed 2d array
              -One arr for the array to hold the final path
              - one to hold the initial train times
              -- one to hold a record of what stations have been visited
            return path of shortest length
        go through array from biggest to smallest location value
          find the location of Source location in shortestPathLocation
        check to see if while the train arival time is within our time constranint
          add the train time to arrival time
          update values
        update values for the next possible values for next itteration
        
  */

  // not working unkown error is continouly returing 0
  public int myShortestTravelTime(
    int S,
    int T,
    int startTime,
    int[][] lengths,
    int[][] first,
    int[][] freq
  ) {
    // Your code along with comments here. Feel free to borrow code from any
    // of the existing method. You can also make new helper methods.


    // uses modified shortestTime
    // get to betermine the best path
    int thePath[] = myShortestTime(lengths, S, T);
    // get the row size for traversals
    int rowSize = first[0].length - 1;
    // starting time variable
    int possibleShortestTime = 0;

    // time variables
    int nextTrainTime;
    int theCurrentTime;

    // starting from the largest possible value we keep going backwards until we found the index of the start
    while( (thePath[rowSize] != S) &&  (rowSize > 0) ) {
      rowSize--;
    }

    theCurrentTime = startTime;

    for (int x = rowSize; x >= 1; x--){
      int currentStationNumber = thePath[x];
      int nextStationNumber = thePath[x-1];
      int theTrainArivalTime = first[currentStationNumber][nextStationNumber];
    
      int temp = 0;
      while (theTrainArivalTime < theCurrentTime){
        theTrainArivalTime = first[currentStationNumber][nextStationNumber] + ( temp * freq[currentStationNumber][nextStationNumber]);
        temp++;
      }

      // update variables
      nextTrainTime = theTrainArivalTime + lengths[currentStationNumber][nextStationNumber];
      // update the possible time
      possibleShortestTime = (possibleShortestTime + (nextTrainTime - theCurrentTime));
      theCurrentTime = nextTrainTime;
      
    }

    return possibleShortestTime;
  }


  public int[] myShortestTime(int[][] graph, int S, int T){
    // holds what times are available for trains
    int[] possibleTimes = initialPossibleTimes(graph[0].length);
    // first train arrives at 0 for initial start
    possibleTimes[S] = 0;
    // list of train stations visited 
    int[] stationsVisited = new int[graph[0].length];
    stationsVisited[S] = -1;
    // array of stations in path, to be returned
    int[] resultPath = initialPathVar(graph[0].length);
    // keep track of what is included in the possible values
    Boolean[] includedInPath = initialBoolValues(graph[0].length);

    // initial value filling in 
    for (int x = 0; x < graph[0].length - 1; x++){
      // uses originally method
      int temp = findNextToProcess(possibleTimes, includedInPath);
      // check that were including it
      includedInPath[x] = true;

      for (int y = 0; y < graph[0].length; y++){
        // conditons to test 
        boolean isIncludedInPath = includedInPath[y];
        int pathEdgeIsUnvailable = graph[temp][y];
        boolean valueSet =  possibleTimes[temp] != Integer.MAX_VALUE;
        boolean valueSmaller = possibleTimes[temp] + graph[temp][y] < possibleTimes[temp];

        // actual testing of conditions
        if (!isIncludedInPath && pathEdgeIsUnvailable != 0 && valueSet && valueSmaller){
          possibleTimes[y] = possibleTimes[temp] + graph[temp][y];
          stationsVisited[y] = temp;
        }

      }
    }

    return findFinalPath(resultPath, stationsVisited, S,T);

  }

  // filter method to find the appropriate path
  public int[] findFinalPath(int[] path, int[] visited, int S, int T){
    int[] finalPath = path;
    int t = T;
    for (int x = 0; x < T; x++){
      if(t != S){
        finalPath[x++] = t;
        t = visited[t];
      }
      finalPath[x] = t;
    }
    
    return finalPath;
  }

  // initializer method for poosible times
  public int[] initialPossibleTimes(int size){
    int[] result = new int[size];
    result[0] = 0;
    for (int x = 1; x < size; x++){
      result[x] = Integer.MAX_VALUE;
    }
    return result;
  }

  // initial method for path array
  public int[] initialPathVar(int size){
    int[] result = new int[size];

    for(int x = 0; x < size; x++){
      result[x] = -1;
    }

    return result;
  }

  // initial values for array of if value is checked
  public Boolean[] initialBoolValues(int size){
    Boolean[] result = new Boolean[size];
    for(int x = 0; x < size; x++){
      result[x] = false;
    }

    return result;
  }



  /**
   * Finds the vertex with the minimum time from the source that has not been
   * processed yet.
   * @param times The shortest times from the source
   * @param processed boolean array tells you which vertices have been fully processed
   * @return the index of the vertex that is next vertex to process
   */
  public int findNextToProcess(int[] times, Boolean[] processed) {
    int min = Integer.MAX_VALUE;
    int minIndex = -1;

    for (int i = 0; i < times.length; i++) {
      if (processed[i] == false && times[i] <= min) {
        min = times[i];
        minIndex = i;
      }
    }
    return minIndex;
  }

  public void printShortestTimes(int times[]) {
    System.out.println("Vertex Distances (time) from Source");
    for (int i = 0; i < times.length; i++)
        System.out.println(i + ": " + times[i] + " minutes");
  }

  /**
   * Given an adjacency matrix of a graph, implements
   * @param graph The connected, directed graph in an adjacency matrix where
   *              if graph[i][j] != 0 there is an edge with the weight graph[i][j]
   * @param source The starting vertex
   */
  public void shortestTime(int graph[][], int source) {
    int numVertices = graph[0].length;

    // This is the array where we'll store all the final shortest times
    int[] times = new int[numVertices];

    // processed[i] will true if vertex i's shortest time is already finalized
    Boolean[] processed = new Boolean[numVertices];

    // Initialize all distances as INFINITE and processed[] as false
    for (int v = 0; v < numVertices; v++) {
      times[v] = Integer.MAX_VALUE;
      processed[v] = false;
    }

    // Distance of source vertex from itself is always 0
    times[source] = 0;

    // Find shortest path to all the vertices
    for (int count = 0; count < numVertices - 1 ; count++) {
      // Pick the minimum distance vertex from the set of vertices not yet processed.
      // u is always equal to source in first iteration.
      // Mark u as processed.
      int u = findNextToProcess(times, processed);
      processed[u] = true;

      // Update time value of all the adjacent vertices of the picked vertex.
      for (int v = 0; v < numVertices; v++) {
        // Update time[v] only if is not processed yet, there is an edge from u to v,
        // and total weight of path from source to v through u is smaller than current value of time[v]
        if (!processed[v] && graph[u][v]!=0 && times[u] != Integer.MAX_VALUE && times[u]+graph[u][v] < times[v]) {
          times[v] = times[u] + graph[u][v];
        }
      }
    }

    printShortestTimes(times);
  }

  public static void main (String[] args) {
    /* length(e) */
    int lengthTimeGraph[][] = new int[][]{
      {0, 4, 0, 0, 0, 0, 0, 8, 0},
      {4, 0, 8, 0, 0, 0, 0, 11, 0},
      {0, 8, 0, 7, 0, 4, 0, 0, 2},
      {0, 0, 7, 0, 9, 14, 0, 0, 0},
      {0, 0, 0, 9, 0, 10, 0, 0, 0},
      {0, 0, 4, 14, 10, 0, 2, 0, 0},
      {0, 0, 0, 0, 0, 2, 0, 1, 6},
      {8, 11, 0, 0, 0, 0, 1, 0, 7},
      {0, 0, 2, 0, 0, 0, 6, 7, 0}
    };
    FastestRoutePublicTransit t = new FastestRoutePublicTransit();
    t.shortestTime(lengthTimeGraph, 0);


    // You can create a test case for your implemented method for extra credit below
  }
}
