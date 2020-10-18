/*
 This program runs the game CodeBreaker
 By Amanda and Collin
 March 25, 2019
 */

import java.util.*;
import java.io.*;

public class codeBreaker1 { 
  
  static Scanner sc = new Scanner(System.in);
  static Random rand = new Random();
  static final int SIZE = 4; 
  static final int TRIES = 10;
  static final String VALID_CHARS = "GRBYOP";
  static int counter = 0;
  
  
  public static void main(String [] args) { 
    String choice;
    String playAgain;
    String key;
    
    System.out.println ("Welcome to CodeBreaker!");
    System.out.println ("");
    System.out.println ("INSTRUCTIONS");
    System.out.println ("You will be playing against the computer in this game.");
    System.out.print ("One player will create a code using four colours given the options ");
    System.out.println ("green, red, blue, yellow, orange, and purple. ");
    System.out.println ("Each colour will be represented by the first letter of each colour. ");
    System.out.println ("If you enter a colour not given in these options, you will be asked to re-enter.");
    System.out.print ("The other player will attempt to guess the code in under ten tries using clues ");
    System.out.print ("given where black ('b') means one correct colour is in the correct position, ");
    System.out.println ("and white ('w') means one of the colours is correct, but not in the correct position.");
    System.out.println ("Guess all colours and positions correctly in 10 guesses or under to win the game!");
    
    
    System.out.println ("");
    
    System.out.println("Enter any key to continue.");
    key = sc.nextLine();
    
    System.out.println ("");
    
    
    do { //do-while loop to ask user for input and validate response 
      System.out.println ("Would you like to be the codebreaker (1) or codemaker (2) first?"); 
      choice = sc.nextLine(); 
    } while (!choice.equals("1") && !choice.equals("2")); 
    
    do { 
      String [][] guess = new String [TRIES][SIZE];
      String [][] clues = new String [TRIES][SIZE]; 
      String [] code = new String[SIZE];
      counter = 0;
      
      if (choice.equals("1")) { //user chooses to be the codebreaker
        choice = "2"; //user's status will change to codemaker after the current round 
        codeBreakerSequence(guess, clues, VALID_CHARS); 
      } else { //user chooses to be the codemaker
        choice = "1"; //user's status will change to codebreaker after the current round 
        codeMakerSequence(guess, clues, VALID_CHARS); 
      }
      do { //do-while loop to ask user for input and validate response 
        System.out.println("Do you want to play again? You and the computer will switch positions. (1 - Yes; 2 - No)"); 
        playAgain = sc.next(); 
      } while (playAgain.equals("1") && playAgain.equals("2")); 
    } while (playAgain.equals("1"));
  }  
  
  
  
  public static void codeBreakerSequence (String [][] guess, String [][] clues, String colours) {
    boolean win = false;
    String[] code = createCode (colours, SIZE) ;
    
    for (int i = 0; i < TRIES; i++) { 
      do {
        
        System.out.println ("Guess #"+(i+1));
        
        for (int j = 0; j < SIZE; j++) {
          System.out.print("Enter your guess for spot #"+(j+1)+": ");
          guess [i][j] = sc.next();
        }
        
        System.out.println("");
        
        if (valid (guess[i], colours, SIZE)) {
          
          findFullyCorrect(code, guess[counter]).size();
          findColourCorrect(guess[counter], code).size();
          
          if (findFullyCorrect(code, guess[counter]).size() == SIZE) {
            System.out.println ("You have guessed the code! You win the game!");
            System.out.println("");
            win = true;
          } 
          else {
            
            getClues(clues, findFullyCorrect(code, guess[counter]), findColourCorrect(guess[counter], code), counter);
            displayGame(guess, clues); 
            
            counter++;
          }
        }
        
      } while (!valid(guess [i], colours, SIZE) && win == false) ;
      
      
      if (win == true) {
        break;
      }
      
    }
    
    if (win == false) {
      
      System.out.print ("You lost the game! The code was ") ;
      for (int c = 0; c < SIZE; c++) {
        System.out.print (code[c]);
      }
      System.out.println (".");
      System.out.println("");
    }
    
  }
  
  
  
  
  
  
  /**
   * Executes the procedures of the game when the user is the code maker.
   *
   * @param guess     a 2D String array that stores all the respective guess from each round 
   * @param clues     a 2D String array that stores all the respective clues from each round 
   * @param colours   a String variable that contains the first letter of the name of all available colours
   */
  
  public static void codeMakerSequence(String [][] guess, String [][] clues, String colours) { 
    boolean win = false;
    String [] code = new String [SIZE];
    
    do {
      for (int i = 0; i<SIZE; i++){
        System.out.print("Enter #"+(i+1)+" colour of your code: ");
        code[i] = sc.next();
      }
    } while (!valid(code, colours, SIZE));
    do {
      int b = 0;
      for (int i = 0; i<guess[counter].length; i++){ 
        guess[counter][i] = computerGuess(colours)[i]; 
        System.out.println(guess[counter][i]);
      }
      getClues(clues, findFullyCorrect(code, guess[counter]), findColourCorrect(guess[counter], code), counter);
      displayGame(guess, clues); 
      
      for (int i = 0; i<clues[counter].length; i++){
        if (clues[counter][i].equals("b")){
          b++;
        }
      }
      if (b==SIZE) { 
        System.out.println("Unfortunately, the computer has guessed your code."); 
        for (int i =0; i<code.length; i++) { 
          System.out.print(code[i] + " ");
        }
        win = true;
      } else if (b!=SIZE && counter==(TRIES-1)) { 
        System.out.println("Good job! The computer couldn't guess your code.");
      }
      counter++; 
    } while (win == false && counter < TRIES); 
    
  }
  
  
  /**
   * Returns a 1D String array that contains a randomized guess from the computer based on the given colours
   * and length of the code.
   * 
   * @param list        a 1D String array that stores the sequence that is being validated 
   * @param colours     a String variable that stores all the characters that will only be in the 1D String array 
   * @param SIZE        an int variable that contains the length of the sequence
   * @return            a boolean value indicating if the inputted 1D String array is the same length as
   *                    the int variable and contains only characters from the specified String variable.
   */
  public static String [] computerGuess (String colours){
    String [] comGuess = new String [SIZE]; 
    for (int i = 0; i< SIZE; i++) {
      int index = rand.nextInt(SIZE);
      comGuess[i] = Character.toString(colours.charAt(index));
    }
    return comGuess;
  }
  
  
  
  
  
  public static String [] createCode (String VALID_CHARS , int SIZE) {
    String[] code = new String[SIZE] ;
    for (int i = 0; i < SIZE; i++) {
      int rand = (int)(Math.random()*SIZE) ; //random
      code[i] = Character.toString(VALID_CHARS.charAt(rand)) ;
    }
    return code;
  }
  
  
  
  
  
  /**
   * Returns a boolean value indicating if the inputted 1D String array is the same length as
   * the int variable and contains only characters from the specified String variable.
   *
   * @param list        a 1D String array that stores the sequence that is being validated 
   * @param colours     a String variable that stores all the characters that will only be in the 1D String array 
   * @param SIZE        an int variable that contains the length of the sequence
   * @return            a boolean value indicating if the inputted 1D String array is the same length as
   *                    the int variable and contains only characters from the specified String variable.
   */
  
  public static boolean valid(String [] list, String VALID_CHARS, int SIZE) { 
    boolean same = true; 
    int yes = 0; 
    if (list.length == SIZE) { 
      for (int  i = 0; i<list.length; i++) {
        for (int k = 0; k<VALID_CHARS.length(); k++){
          if (list[i].equalsIgnoreCase(Character.toString(VALID_CHARS.charAt(k)))){
            yes++;
          }
        }
      }
    } 
    if (yes == SIZE) { 
      same = true;
    } else { 
      same = false;
    }
    return same; 
  }
  
  
  
  
  
  public static ArrayList<String> findFullyCorrect (String[] code, String[] guess) {
    
    ArrayList <String> b = new ArrayList <String> () ;
    
    for (int i = 0; i < SIZE; i++) {
      if (code [i].equalsIgnoreCase(guess [i])) {
        b.add("b");
      }
    }
    return b;
  }
  
  
  
  
 /**
  * Returns an ArrayList that contains only of elements that are of colours that are not correctly positioned.
  *
  * @param guesses     a 2D String array that stores all the respective guesses from each round 
  * @param clues       a 2D String array that stores all the respective clues from each round
  * @return            an ArrayList that contains only of elements that are of colours that are not correctly positioned. 
  */
  public static ArrayList <String> removeFullyCorrect(String [] guess, String [] code){
    ArrayList<String> result = new ArrayList<String> ();
    for (int i = 0; i<guess.length; i++){
      if (!(guess[i].equalsIgnoreCase(code[i]))){
        result.add(guess[i]);
      }
    }
    return result;
  }
  
  
  
  /*
   * 
   public static ArrayList <String> findColourCorrect (String[] guess, String[] code) {
   ArrayList <String> w = new ArrayList <String> () ;
   
   for (int i = 0; i < code.length; i++) {
   
   if (removeFullyCorrect(guess, code).contains (code[i])) {
   w.add("w");
   }
   
   }
   return w; 
   }
   
   
   */
  
  
  public static ArrayList <String> findColourCorrect (String[] guess, String[] code) {
    ArrayList <String> w = new ArrayList <String> () ;
    
    for (int i = 0; i <removeFullyCorrect(guess, code).size(); i++) {
      for (int k = 0; k<code.length; k++){
        if (((String)removeFullyCorrect(guess, code).get(i)).equalsIgnoreCase(code[k])) {
          w.add("w");
          break;
        }
      }
      
    }
    return w; 
  }
  
  
  
  /**
   * Displays the results of each round by printing the guesses and clues like below:
   *  Guess    Clues
   * ***************
   *  O O O O   b
   *  R R R R   b b
   *
   * @param guesses      a 2D String array that stores all the respective guesses from each round 
   * @param clues        a 2D String array that stores all the respective clues from each round
   */
  
  public static void displayGame(String [][] guess, String [][] clues){
    System.out.println("Guess\tClues"); 
    System.out.println("****************");
    for (int i = 0; i<counter+1; i++) {
      for (int j =0; j<guess[i].length; j++){
        System.out.print(guess[i][j]+" ");
        if (j==(guess[i].length)-1){
          System.out.print("\t");
        }
      }
      for (int k =0; k<clues[i].length; k++){
        System.out.print(clues[i][k]+" ");
        if (k==(clues[i].length)-1){
          System.out.print("\n");
        }
      }
    }
    
    System.out.println("");
    
  }
  
  
  
  /**
   * Appends the clues from two ArrayLists into a 2D String array.
   *
   * @param clues      a 2D String array that stores all the respective clues from each round 
   * @param bClues     an ArrayList that stores the clues for the correctly positioned elements at a given round
   * @param wClues     an ArrayList that stores the clues for the correct colour but incorrectly positioned elements
   *                   at a given round
   * @param counter    an int variable that contains the round number 
   */
  
  public static void getClues (String [][] clues, ArrayList bClues, ArrayList wClues, int counter) {
    for (int i = 0; i<(bClues.size()); i++) { //for loop to insert clues from 
      clues[counter][i] = (String)bClues.get(i);
    }
    int k = 0;
    for (int i = bClues.size(); i < bClues.size()+wClues.size(); i++) { 
      clues[counter][i] = (String)wClues.get(k);
      k++;
    }
    for (int g = 0; g<clues[counter].length; g++){
      if (clues[counter][g]==null){
        clues[counter][g] = " ";
      }
    }
  }
  
  
}
