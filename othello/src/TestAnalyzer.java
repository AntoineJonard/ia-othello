
public class TestAnalyzer {

	public static void main(String[] args) {
		/*
		Analyzer analyzer = new Analyzer(4,7);
		analyzer.analyze();
		analyzer.displayResult();
		*/
		
		RandomAnalyzer randomAnalyzer = new RandomAnalyzer(500, 6);
		randomAnalyzer.analyze();
		randomAnalyzer.displayResult();

	}

}
