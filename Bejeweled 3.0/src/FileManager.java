import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileManager {
	
	BufferedWriter writer;
	BufferedReader reader;
	
	File file;
	
	FileManager(String fileName) {
		file = new File(fileName);
		if (!file.exists() || file.isDirectory()) makeNewFile();
	}
	
	private void makeNewFile() {
		String[] timeModes = {"1MIN", "2MIN", "5MIN", "10MIN", "30MIN"};
		try {
			FileWriter write = new FileWriter(file);
			writer = new BufferedWriter(write);
			
			for (int i=0; i<5; i++) {
			writer.write(timeModes[i]);
			writer.newLine();
			writer.write("1.John-->5000");
			writer.newLine();
			writer.write("2.Ben-->4000");
			writer.newLine();
			writer.write("3.Carl-->3000");
			writer.newLine();
			writer.write("4.Peter-->2000");
			writer.newLine();
			writer.write("5.Garry-->1000");
			writer.newLine();
			}
			
			writer.close();
		} catch(IOException e) {
			System.out.println("Kïûda veidojot failu " + file.getName());
		}
	}
	
	public int checkNewScore(int score, String timeMode) {
		String[] content = getContent(timeMode);
		
		for (int i=0; i<5; i++) {
			int index = content[i].indexOf("-->") + 3;
			int lineScore = Integer.valueOf(content[i].substring(index, content[i].length()));
			if (score >= lineScore) {
				if (i==0) return 2;
				else return 1;
			}
		}
		
		return 0;
		
		/*try {
			FileReader read = new FileReader(file);
			reader = new BufferedReader(read);
			
			String line;
			while((line = reader.readLine()) != "") {
				if (line.equals(timeMode)) {
					for (int i=0; i<5; i++) {
						line = reader.readLine();
						int index = line.indexOf("-->") + 3;
						System.out.println(line.substring(index, line.length()));
						int lineScore = Integer.valueOf(line.substring(index, line.length()));
						if (score >= lineScore) return true;
					}
					break;
				}
			}
			
			reader.close();
		} catch(IOException e) {
			System.out.println("Kïûda nolasot failu!");
		}
		
		return false;*/
	}
	
	public void writeNewScore(String name, int score, String timeMode) {
		timeMode += "MIN";
		int n = 0;
		/*String[] content = getContent(timeMode);
		
		for (int i=0; i<5; i++) {
			int scoreIndex = content[i].indexOf("-->") + 3;
			int lineScore = Integer.valueOf(content[i].substring(scoreIndex, content[i].length()));
			if (score >= lineScore) {
				content[i] = (i+1) + "." + name + "-->" + score;
				break;
			}
		}*/
		
		try {
			FileReader read = new FileReader(file);
			reader = new BufferedReader(read);
			
			String line;
			while((line = reader.readLine()) != null) {
				n++;
				System.out.println(line);
			}
			
			reader.close();
		} catch(IOException e) {
			System.out.println("Neizdevâs ierakstît failu");
		}
		//Saskaita cik rindiòu ir failâ
		
		String[] fileText = new String[n];
		
		try {
			FileReader read = new FileReader(file);
			reader = new BufferedReader(read);
			
			String line;
			int i=0;
			while((line = reader.readLine()) != null) {
				fileText[i] = line;
				i++;
			}
			
			reader.close();
		} catch(IOException e) {
			System.out.println("Neizdevâs ierakstît failu");
		}
		//Visas rindiòas nolasa un saglabâ masîvâ
		
		String tempLine = "";
		boolean scoreChanged = false;
		for (int i=0; i<n; i++) {
			if (fileText[i].equals(timeMode)) {
				for (int j=0; j<5; j++) {
					int index = fileText[i+j+1].indexOf("-->") + 3;
					int lineScore = Integer.valueOf(fileText[i+j+1].substring(index, fileText[i+j+1].length()));
					if (score >= lineScore && !scoreChanged) {
						tempLine = fileText[i+j+1];
						fileText[i+j+1] = (j+1) + "." + name + "-->" + score;
						scoreChanged = true;
					} else if (scoreChanged) {
						String memo = fileText[i+j+1];
						fileText[i+j+1] = tempLine;
						tempLine = memo;
					}
				}
			}
		}
		//Nomaina masîvâ punktus pret jaunajiem
		
		/*boolean needChangeScore = true;
		
		try {
			FileReader read = new FileReader(file);
			reader = new BufferedReader(read);
			
			String line;
			while((line = reader.readLine()) != "") {
				if (line.equals(timeMode)) {
					for (int i=0; i<5; i++) {
						
					}
				} 
			}
			
			for (int i=0; i<n; i++) {
				fileText[i] = reader.readLine();
				
				int scoreIndex = fileText[i].indexOf("-->") + 3;
				
				System.out.println(fileText[i].substring(scoreIndex, fileText[i].length()));
				int lineScore = Integer.valueOf(fileText[i].substring(scoreIndex, fileText[i].length()));
				if (score >= lineScore && needChangeScore) {
					fileText[i] = (i+1) + "." + name + "-->" + score;
					needChangeScore = false;
				}
			}
			
			reader.close();
		} catch(IOException e) {
			System.out.println("Kïûda nolasot failu!");
		}*/
		
		try {
			FileWriter write = new FileWriter(file);
			writer = new BufferedWriter(write);
			
			for (int i=0; i<n; i++) {
				writer.write(fileText[i]);
				writer.newLine();
			}
			
			writer.close();
		} catch(IOException e) {
			System.out.println("Kïûda nolasot failu " + file);
		}
		//Ievada masîvu failâ
	}
	
	public String[] getContent(String timeMode) {
		timeMode += "MIN";
		String[] result = new String[5];
		
		try {
			FileReader read = new FileReader(file);
			reader = new BufferedReader(read);
			
			String line;
			while((line=reader.readLine()) != "") {
				System.out.println(line + "; " + timeMode);
				if (line.equals(timeMode)) {
					for (int i=0; i<5; i++)
						result[i] = reader.readLine();
					break;
				}
			}
			
			reader.close();
		} catch(IOException e) {
			System.out.println("Kïûda nolasot failu!");
		}
		
		return result;
	}
}
