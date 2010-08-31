package javaDocTest;

public class TestRun {

	private boolean passed;
	private String question;
	private String answer;
	private String result;
	private String needed;

	public TestRun(boolean passed, String question, String answer,
			String result, String needed) {
		super();
		this.passed = passed;
		this.question = question;
		this.answer = answer;
		this.result = result;
		this.needed = needed;
	}

	public boolean isPassed() {
		return passed;
	}

	public String getQuestion() {
		return question;
	}

	public String getAnswer() {
		return answer;
	}

	public String getResult() {
		return result;
	}

	public String getNeeded() {
		return needed;
	}

}
