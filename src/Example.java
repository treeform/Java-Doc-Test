/**
 * This shows a simple use of Java Doc Test 
 *
 * % Example e = new Example()
 * 
 */
public class Example {
	
	/**
	 * @return the answer to life the universe and everything
	 * 
	 * % e.getAnswerToLifeUniverseEverything()
	 * > 42
	 */
	public int getAnswerToLifeUniverseEverything(){
		return 42;
	}
	
	/**
	 * @return adds 3 numbers
	 * 
	 * % e.add(1, 2, 3)
	 * > 6
	 * % e.add(1, 7, 3)
	 * > 11
	 * % e.add(21, 7, 14)
	 * > e.getAnswerToLifeUniverseEverything()
	 */
	public int add(int a, int b, int c){
		return a + b + c;
	}
	
	/**
	 * @return toString representation
	 * 
	 * % e.toString()
	 * > "Super Example Class"
	 */
	public String toString(){
		return "Super Example Class";
	}
	
}
