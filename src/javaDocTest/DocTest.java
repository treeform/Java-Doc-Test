package javaDocTest;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import bsh.EvalError;
import bsh.Interpreter;

public class DocTest {
	
	private ArrayList<TestRun> testRuns;
	private long passed = 0, failed = 0;
	
	public DocTest(){
		testRuns = new ArrayList<TestRun>();
	}
	
	public void runDocTests(String fileName) throws FileNotFoundException, EvalError{
		Interpreter interpreter = new Interpreter();
		// read the file
		Scanner scanner;
		scanner = new Scanner(new File(fileName)).useDelimiter("\\Z");
		String contents = scanner.next();
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
				Object result = interpreter.eval(question); 
				//System.out.println(result);
				if( answer != null){
					Object neededResult = interpreter.eval(answer); 
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
		
		DocTest dt = new DocTest();
		
		try {
			dt.runDocTests("src/Example.java");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (EvalError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
