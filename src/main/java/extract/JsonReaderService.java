package extract;

import java.util.Scanner;

public class JsonReaderService {

	public String getValueOfFirstKeyAppearance(String key, Scanner scanner) {
		String line = "";
		String value = "";
		while (scanner.hasNextLine()) {
			line = scanner.nextLine();
			if (line.contains(key)) {
				value = line.split(":\\s+")[1].split(",")[0].replace("\"", "");
				break;
			}
		}
		return value;
	}

}
