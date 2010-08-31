package javaDocTest;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import bsh.EvalError;
import bsh.Interpreter;

/**
 * This is a simple implementation of Doc Tests for Java using Bean Shell. 
 * Doc Tests offer 4 main advantages:
 *  tests as example code in the documentation 
 *  tests that are very close to the code its testing
 *  no need for heavy test set-up
 *  tests will never get build and shipped to production
 */
public class DocTest {
	
	private ArrayList<TestRun> testRuns;
	private long passed = 0, failed = 0;
	
	public DocTest(){
		testRuns = new ArrayList<TestRun>();
	}
	
	public void runDocTests(String fileName) throws FileNotFoundException {
		Interpreter interpreter = new Interpreter();
		// read the file
		Scanner scanner;
		scanner = new Scanner(new File(fileName)).useDelimiter("\\Z");
		String contents = scanner.next();
		
		// run the source file first
		try {
			interpreter.eval(contents);
		} catch (EvalError ee) {
			ee.printStackTrace();
			System.out.println("Error running file");
			System.exit(2);
		}
		
		// split the file
		String[] lines = contents.split("\n");
		// for each line figure it if its a doc test line		
		for(int i=0; i<lines.length; i++){
			Matcher matcher = Pattern.compile("\\*\\s\\%(.*)").matcher(lines[i]);
			if (matcher.find()) {
				// found a question or command
				String question = matcher.group(1).trim();
				//System.out.println("% " + question);
				// see if it has an answer
				Matcher matcherAnswer = Pattern.compile("\\*\\s\\>(.*)").matcher(lines[i+1]);
				String answer;
				if (matcherAnswer.find()) {
					// has answer
					answer = matcherAnswer.group(1).trim();
					//System.out.println("> " + answer);
				} else {
					// no answer its just a command
					answer = null;
				}
				// do interpretation
				Object result;
				try {
					result = interpreter.eval(question);
				} catch (EvalError e) {
					// if it errors out lets use the error as result
					result = e.toString();
				} 
				//System.out.println(result);
				if( answer != null){
					Object neededResult;
					try {
						neededResult = interpreter.eval(answer);
					} catch (EvalError e) {
						// if it errors out lets use the error as result
						neededResult = e.toString();
					} 
					//System.out.println(neededResult);
					// do testing
					if( result.toString().equals(neededResult.toString())){
						testPassed(question, answer, result.toString());
					} else {
						testFaield(question, answer, result.toString(), neededResult.toString());
					}
				}
			}
		}
	}
	
	
	
	private void testFaield(String question, String answer,
			String result, String needed) {
		System.out.println(question+" -> "+answer+" : \""+result+"\" != \""+needed+"\"");
		testRuns.add( new TestRun(false, question, answer, result, needed));
	}



	private void testPassed(String question, String answer, String result) {
		testRuns.add( new TestRun(true, question, answer, result, result));
	}

	public void printFooter(){
		for( TestRun test: testRuns ){
			if (test.isPassed()){
				passed ++;
			} else {
				failed ++;
			}
		}
		System.out.println("JavaDocTests: passed:"+passed+" failed:"+failed);
	}

	public static void main(String[] args) {
		
		if (args.length != 1) {
			System.out.println("Expected DocTest.java path/to/source.java");
			System.exit(1);
		}
		
		DocTest dt = new DocTest();
		
		try {
			dt.runDocTests(args[0]);
		} catch (FileNotFoundException e) {
			System.out.println("File "+args[0]+"not found");
			System.exit(1);
		}
		
		
		dt.printFooter();
		
		if (dt.getFailed() == 0){
			System.exit(0);
		} else {
			System.exit(1);
		}
	}

	public ArrayList<TestRun> getTestRuns() {
		return testRuns;
	}

	public long getPassed() {
		return passed;
	}

	public long getFailed() {
		return failed;
	}
	
	
}
