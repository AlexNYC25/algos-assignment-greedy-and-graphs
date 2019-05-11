
/**
 * Physics Experiment
 * Author: Alexis Montes and Carolyn Yao
 * Does this compile or finish running within 5 seconds?  YES and YES
 */

/**
 * This class implements a greedy scheduler to assign students
 * to steps in a physics experiment. There are three test cases in the main
 * method. Please read through the whole file before attempting to code the
 * solution.
 *
 * You will only be graded on code you add to the scheduleExperiments method.
 * Do not mess with the existing formatting and identation.
 * You don't need to use the helper methods, but if they come in handy setting
 * up a custom test case, feel free to use them.
 */
public class PhysicsExperiment {

  private int currentPerson = 0;

  public void resetPerson(){
    currentPerson = 0;
  }

  public void newPerson(int x){
    currentPerson = x;
  }

  public int getPerson(){
    return currentPerson;
  }

  public boolean isPersonValid(){
    if(currentPerson != 0){
      return true;
    }

    return false;
  }

  
  public int howManyCandidates(int[] finalCandidatesarr){
    int count = 0;
    for(int x = 1; x < finalCandidatesarr.length; x++){
        if(finalCandidatesarr[x] == 1){
            count++;
        }
    }
    //System.out.println("candidate count used " + count);
    return count;
}

  public int onlyValue(int[] arr){
    int location = -1 ;
    //System.out.println("only value used");
    for(int x = 0; x < arr.length; x++){
        if(arr[x] == 1){
            location = x;
            return location;
        }
    }

    return location+1;
}


public int findBestCandidate(int[] finalCandidates){
  int candidate = 0;

  // find if there is only one positive value in final candidate then return one 
  if(finalCandidates[getPerson()] == 1){
      return getPerson();
  }
  /* 
  if(howManyCandidates(finalCandidates) == 1 ){
      System.out.println("only one candidate available");
      candidate = onlyValue(finalCandidates);
      newPerson(candidate);
      return candidate;
  }
  */
  else {
      for(int x = 0; x < finalCandidates.length; x++){
         
          if(finalCandidates[x] > finalCandidates[candidate]){
              candidate = x;
          }
      }

      newPerson(candidate);
      return candidate;
  }
}

    // first number equals student second is the job
    // col to find is the jon
    public int[] getCertainCol(int colToFind, int[][] toSearch){
      int[] arr = new int[toSearch.length];
      int numberOfStudents = toSearch.length;

      // set size of arr

      // main code in here
      for(int x = 1; x < numberOfStudents ; x++ ){
          arr[x] = toSearch[x][colToFind];
      }

      return arr;
  }

  public int[] findPotential(int[] candidates, int[][] toSearch, int currentjob){
    int[] arr = new int[candidates.length];

    // itterate through array
            // if the values is one then find the number of potential jobs
            // assign value at posioon in new arr

    for(int x = 1; x < candidates.length; x++){
        if(candidates[x] == 1){
            int counter = 0;
            for(int y = currentjob; y < toSearch[0].length; y++ ){
                if(toSearch[x][y] == 1){
                    counter++;
                }
                
            }
            arr[x] = counter;
        }
    }

    return arr;
}

  /*
    findPossiblePeople(int stepNeededtoBeDone, int[][] tableOfSignups){
      itterate through int[x][step] 
        if person x signed up mark as so in 1d arr
    }
  */
  // finds the list of students that are signed up for that possible step



  /**
   * The actual greedy scheduler you will be implementing!
   * @param numStudents The number of students who can participate, m
   * @param numSteps The number of steps in the experiment, n
   * @param signUpTable An easy lookup tool, signUpTable[x][Y] = student X signed up or did not sign up for step Y.
   *      Example:
          signUpTable[1][3] = 1 if Student 1 signed up for Step 3
          signUpTable[1][3] = 0 if Student 1 didn't sign up for Step 3
   * @return scheduleTable: a table similar to the signUpTable where scheduleTable[X][Y] = 1 means
   *     student X is assigned to step Y in an optimal schedule
   */
  public int[][] scheduleExperiments(
    int numStudents,
    int numSteps,
    int[][] signUpTable
  ) {
    // Your scheduleTable is initialized as all 0's so far. Your code will put 1's
    // in the table in the right places based on the return description
    int[][] scheduleTable = new int[numStudents + 1][numSteps + 1];

    // Your code goes here
    for (int step = 0; step < numSteps+1; step++){
        int bestCandidate = findBestCandidate(findPotential(getCertainCol(step, signUpTable), signUpTable, step));
      
        scheduleTable[bestCandidate][step] = 1;
     
        resetPerson();
    }
    

    return scheduleTable;
  }


  /**
   * Makes the convenient lookup table based on the steps each student says they can do
   * @param numSteps the number of steps in the experiment
   * @param studentSignUps student sign ups ex: {{1, 2, 4}, {3, 5}, {6, 7}}
   * @return a lookup table so if we want to know if student x can do step y,
      lookupTable[x][y] = 1 if student x can do step y
      lookupTable[x][y] = 0 if student x cannot do step y
   */
  public int[][] makeSignUpLookup(int numSteps, int[][] studentSignUps) {
    int numStudents = studentSignUps.length;
    int[][] lookupTable = new int[numStudents+1][numSteps + 1];
    for (int student = 1; student <= numStudents; student++) {
      int[] signedUpSteps = studentSignUps[student-1];
      for (int i = 0; i < signedUpSteps.length; i++) {
        lookupTable[student][signedUpSteps[i]] = 1;
      }
    }
    return lookupTable;
  }

  /**
   * Prints the optimal schedule by listing which steps each student will do
   * Example output is Student 1: 1, 3, 4
   * @param schedule The table of 0's and 1's of the optimal schedule, where
   *   schedule[x][y] means whether in the optimal schedule student x is doing step y
   */
  public void printResults(int[][] schedule) {
    for (int student = 1; student < schedule.length; student++) {
      int[] curStudentSchedule = schedule[student];
      System.out.print("Student " + student + ": ");
      for (int step = 1; step < curStudentSchedule.length; step++) {
        if (curStudentSchedule[step] == 1) {
          System.out.print(step + " ");
        }
      }
      System.out.println("");
    }
  }

  /**
   * This validates the input data about the experiment step sign-ups.
   * @param numStudents the number of students
   * @param numSteps the number of steps
   * @param signUps the data given about which steps each student can do
   * @return true or false whether the input sign-ups match the given number of
   *    students and steps, and whether all the steps are guaranteed at least
   *    one student.
   */
  public boolean inputsValid(int numStudents, int numSteps, int signUps[][]) {
    int studentSignUps = signUps.length;

    // Check if there are any students or signups
    if (numStudents < 1 || studentSignUps < 1) {
      System.out.println("You either did not put in any student or any signups");
      return false;
    }

    // Check if the number of students and sign-up rows matches
    if (numStudents != studentSignUps) {
      System.out.println("You input " + numStudents + " students but your signup suggests " + signUps.length);
      return false;
    }

    // Check that all steps are guaranteed in the signups
    int[] stepsGuaranteed = new int[numSteps + 1];
    for (int i = 0; i < studentSignUps; i++) {
      for (int j = 0; j < signUps[i].length; j++) {
        stepsGuaranteed[signUps[i][j]] = 1;
      }
    }
    for (int step = 1; step <= numSteps; step++) {
      if (stepsGuaranteed[step] != 1) {
        System.out.println("Your signup is incomplete because not all steps are guaranteed.");
        return false;
      }
    }

    return true;
  }

  /**
   * This sets up the scheduling test case and calls the scheduling method.
   * @param numStudents the number of students
   * @param numSteps the number of steps
   * @param signUps which steps each student can do, in order of students and steps
   */
  public void makeExperimentAndSchedule(int experimentNum, int numStudents, int numSteps, int[][] signUps) {
    System.out.println("----Experiment " + experimentNum + "----");
    if (!inputsValid(numStudents, numSteps, signUps)) {
      System.out.println("Experiment signup info is invalid");
      return;
    }
    int[][] signUpsLookup = makeSignUpLookup(numSteps, signUps);
    int[][] schedule = scheduleExperiments(numStudents, numSteps, signUpsLookup);
    printResults(schedule);
    System.out.println("");
  }

  /**
   * You can make additional test cases using the same format. In fact the helper functions
   * I've provided will even check your test case is set up correctly. Do not touch any of
   * of the existing lines. Just make sure to comment out or delete any of your own test cases
   * when you submit. The three experiment test cases existing in this main method should be
   * the only output when running this file.
   */
  public static void main(String args[]){
    PhysicsExperiment pe = new PhysicsExperiment();

    // Experiment 1: Example 1 from README, 3 students, 6 steps:
    int[][] signUpsExperiment1 = {{1, 2, 3, 5}, {2, 3, 4}, {1, 4, 5, 6}};
    /*
        Signup table lools like:
              |0|1|2|3|4|5|6|
             0|0|0|0|0|0|0|0|
             1|0|1|1|1|0|1|0|
             2|0|0|1|1|1|0|0|
             3|0|1|0|0|1|1|1|
    */
    pe.makeExperimentAndSchedule(1, 3, 6, signUpsExperiment1);

    // Experiment 2: Example 2 from README, 4 students, 8 steps
    int[][] signUpsExperiment2 = {{5, 7, 8}, {2, 3, 4, 5, 6}, {1, 5, 7, 8}, {1, 3, 4, 8}};
    pe.makeExperimentAndSchedule(2, 4, 8, signUpsExperiment2);

    // Experiment 3: Another test case, 5 students, 11 steps
    int[][] signUpsExperiment3 = {{7, 10, 11}, {8, 9, 10}, {2, 3, 4, 5, 7}, {1, 5, 6, 7, 8}, {1, 3, 4, 8}};
    pe.makeExperimentAndSchedule(3, 5, 11, signUpsExperiment3);
  }
}
