import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

public class Project {

	static String[] opCode = {"AND" , "ADD" , "LD" , "ST" , "ANDI" , "ADDI" , "CMP" , "JUMP" , "JE" , "JA" , "JB" , "JBE" , "JAE"};
	

	public static void main(String[] args) throws Exception {

		try {
			ArrayList<String> result = new ArrayList<>();

			File file = new File("input.txt");
			if (!file.exists()) {
				System.out.println("File does not exist...");
			}

			else {

				BufferedReader read = new BufferedReader(new FileReader(file));
				
				String input;
				while ((input = read.readLine()) != null) {
					result.add(parsing(input));
					
				}
				FileWriter writer = new FileWriter("dld_output.hex");
				writer.write("v2.0 raw \n");
				for(String str: result) {
				  writer.write(str + System.lineSeparator());
				}
				writer.close();
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static int bin2Dec(String bin) {
		return Integer.parseInt(bin, 2);
	}

	public static String decToBin(int dec, int len, boolean twosComplement) {
		dec = (twosComplement) ? -dec: dec;
      
		String bin = Integer.toBinaryString(dec);
		bin = bin == "0" ? "": bin;

		int x = len - bin.length();
		String prefix = "";
		
		if(dec < 0) {
			for(int i = 0; i < x; i++) {
				prefix+="1";
			}
		} else {
			for(int i = 0; i < x; i++) {
				prefix+="0";
			}
		}

		return prefix + bin;
	}

	public static String decToHex(int dec, int len, boolean twosComplement) {
		String hex = Integer.toHexString(dec);
		hex = hex == "0" ? "": hex;

		int x = len - hex.length();
		String prefix = "";

		if(dec < 0) {
			for(int i = 0; i < x; i++) {
				prefix+="1";
			}
		} else {
			for(int i = 0; i < x; i++) {
				prefix+="0";
			}
		}

		return prefix + hex;
	}
	
	public static String parsing(String input) {
		String input2 = input.replaceAll("R", "");
		//split for blank character
		String splitForBlank[] = input2.split(" ");
		//split for comma
		String splitForComma[] = splitForBlank[1].split(",");
		
		//after split operations, parse the string array to int
		int size = splitForComma.length;
		int[] afterParsing = new int[size];
		for(int i = 0; i < size; i++) {
			afterParsing[i] = Integer.parseInt(splitForComma[i]);
		}
		
		String opHexCode = splitForBlank[0];
		int i;
		for(i = 0; i < opCode.length; i++) {
			if(opHexCode.equals(opCode[i])) {
				opHexCode = decToHex(i, 1, false);
				break;
			}
		}
		return  parseParameters(i, afterParsing) ;
	}

	public static String parseParameters(int op, int[] p) {
		StringBuilder sb = new StringBuilder();

		String instruction = opCode[op];
		
        switch(instruction){
            case "AND":
            	sb.append("0000");
			    sb.append(decToBin(p[0], 3, false)); //DST
                sb.append(decToBin(p[1], 3, false)); //SRC1
                sb.append(decToBin(p[2], 3, false)); //SRC2
                sb.append("000");

				break;
            case "ADD":
            	sb.append("1000");
                sb.append(decToBin(p[0], 3, false)); //DST
                sb.append(decToBin(p[1], 3, false)); //SRC1
                sb.append(decToBin(p[2], 3, false)); //SRC2
				sb.append("000");
                break;
            case "ANDI":
            	if(p[2]>=0) {
            		sb.append(decToBin(p[2], 7, false)); //IMM
            	}
            	else {
                    String bin;
                    bin = Integer.toBinaryString(p[2]);
                    bin = bin.substring(bin.length()-7,bin.length());
                    sb.append(bin);
            	}
            		
                sb.append(decToBin(p[1], 3, false)); //DST
                sb.append(decToBin(p[0], 3, false)); //SRC1
                sb.append("001");
                break;
				
            case "ADDI":
            	if(p[2]>=0) {
            		sb.append(decToBin(p[2], 7, false)); //IMM
            	}
            	else {
                    String bin;
                    bin = Integer.toBinaryString(p[2]);
                    bin = bin.substring(bin.length()-7,bin.length());
                    sb.append(bin);
            	}
            		
                sb.append(decToBin(p[1], 3, false)); //DST
                sb.append(decToBin(p[0], 3, false)); //SRC1
                sb.append("010");
                break;
            case "CMP":
            	sb.append("0000000");
                sb.append(decToBin(p[0], 3, false)); //SRC1
                sb.append(decToBin(p[1], 3, false)); //SRC2
                sb.append("011");

                break;
            case "LD":
            	
            	sb.append(decToBin(p[1], 10, false)); //ADDRESS
                sb.append(decToBin(p[0], 3, false)); //DST
                sb.append("100");

                break;
            case "ST":
            	sb.append(decToBin(p[1], 10, false)); //ADDRESS
                sb.append(decToBin(p[0], 3, false)); //SRC
                sb.append("101");

                break;
            case "JUMP":
            	
            	sb.append(decToBin(p[0], 10, false)); //ADDRESS
                sb.append("000");
                sb.append("110");
                break;
                
            case "JA":
            	sb.append(decToBin(p[0], 10, false)); //ADDRESS
                sb.append("100");
                sb.append("111");
                break;
                
            case "JB":
            	sb.append(decToBin(p[0], 10, false)); //ADDRESS
                sb.append("101");
                sb.append("111");
                break;
            	
            case "JE":
            	sb.append(decToBin(p[0], 10, false)); //ADDRESS
                sb.append("110");
                sb.append("111");
                break;
                
            case "JBE":
            	sb.append(decToBin(p[0], 10, false)); //ADDRESS
                sb.append("111");
                sb.append("111");
                break;
                
            case "JAE":
            	sb.append(decToBin(p[0], 10, false)); //ADDRESS
                sb.append("000");
                sb.append("111");
                break;
        }

		return decToHex(bin2Dec(sb.toString()), 4, false);
	}
	
}





