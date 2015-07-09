import java.util.*;
import java.io.*;
//final const int USER_TYPES;

public class PasswordCrack {

	public static void main(String[] args) {
		String dict_file = args[0];
		String pass_file = args[1];

		ArrayList<String> dictionary = readFile(dict_file);
		ArrayList<String> passwords  = readFile(pass_file);

		ArrayList<Map> token_maps = new ArrayList<Map>();
		for (int i = 0; i < passwords.size(); ++i) {
			Map temp = new HashMap();
			if (!passwords.get(i).isEmpty()) {
				temp = parse_users(passwords.get(i));
				token_maps.add(temp);
			}
		}
		ArrayList<String> mangles = new ArrayList<String>();
		ArrayList<String> double_mangles = new ArrayList<String>();

		boolean found = false;

		int idx = 0;
		int didx = 0;
		int count = 0;

		while (idx < 200*(3+dictionary.size())) {
			boolean flag = true;

			for (Map h : token_maps) {
				String guess = "";
				String[] guesses = new String[200];

				if (idx < 200)
					guesses = get_permutation((String)h.get("fname"), true);
				else if (idx < 400)
					guesses = get_permutation((String)h.get("lname"), true);
				else if (idx < 600)
					guesses = get_permutation((String)h.get("username"), true);
				else //dictionary
					guesses = get_permutation(dictionary.get(didx), true);

				
				if (h.get("solved_pw") == null) {

					String result = "";
					guess = guesses[idx%200];

					// Cache each mangled element
					if (flag) {
						mangles.add(guess);
						flag = false;
					}

					result = check_password(guess, (String)h.get("passwd"));
					h.put("solved_pw", result);
					found = (result != null);

					if (found) {

					//add to "solved" structure of some kind: username, password, encrypted password
						count++;
						System.out.println("Found " + (String)h.get("username") + "'s pwd: \t" + guess);
						found = false;
					}
				}
			}
			if (idx++ > 600 && (idx % 200 == 0))
				didx++;
		}

		idx = 0;
		didx = 0;
		while (idx < 200*(mangles.size())) {
			boolean flag = true;
			String[] guesses = new String[200];
			guesses = get_permutation(mangles.get(didx % mangles.size()), false);

			for (Map h : token_maps) {
				if (h.get("solved_pw") != null)
					continue;
				String guess = "";

				if (h.get("solved_pw") == null) {
					String result = "";
					guess = guesses[idx%200];
					result = check_password(guess, (String)h.get("passwd"));
					h.put("solved_pw", result);
					found = (result != null);

					if (flag) {
						double_mangles.add(guess);
						flag = false;
					}

					if (found) {
						count++;
						System.out.println("Found " + (String)h.get("username") + "'s pwd: \t" + guess);
						found = false;
					}
				}
			}
			if (idx++ % 200 == 0)
				didx++;
		}

		// for (String s : double_mangles)
		//  	System.out.println(s);
		System.out.println("entering next loop: " + double_mangles.size());

		idx = 0;
		didx = 0;
		while (idx < 200*(double_mangles.size())) {
			String[] guesses = new String[200];
			guesses = get_permutation(double_mangles.get(didx % double_mangles.size()), false);

			for (Map h : token_maps) {
				if (h.get("solved_pw") != null)
					continue;
				String guess = "";
				System.out.println("trying: " + guesses[idx%200]);

				if (h.get("solved_pw") == null) {
					String result = "";
					guess = guesses[idx%200];
					result = check_password(guess, (String)h.get("passwd"));
					h.put("solved_pw", result);
					found = (result != null);

					if (found) {
						count++;
						System.out.println("Found " + (String)h.get("username") + "'s pwd: \t" + guess);
						found = false;
					}
				}
			}
			if (idx++ % 200 == 0)
				didx++;
		}		


		System.out.println("Found " + count + " passwords");
	}

	static String check_password(String guess, String password) {
		// Do encryption here
		
		String salt = password.substring(0,2);
		String encrypted_guess = jcrypt.crypt(salt, guess);
		String solved_pw = null;
		
		boolean found = encrypted_guess.equals(password);
		if (found) {
			solved_pw = guess;
		}
		return solved_pw;
	}
	static boolean check_dict(Map hash) {
		return false;
	}
	static String toggle(String s, int len) {
		//check if uppercase
		int i = 0;

		char[] cs = s.toCharArray();
		while(i<len) {
			if (cs[i] < 97) 
				cs[i] = cs[i] += 32;
			else
				cs[i] = cs[i] -= 32;
			i+=2;
		}
		return new String(cs);
	}

	static String[] get_permutation(String s, boolean lower) {
		// Size of array is 12 listed permutations, plus appending or prepending any ascii
		// character from 33 -> 126. = 94.  Double for appending/prepending, 188 + 12 = 200.

		String[] guesses = new String[200];  
		int len = s.length();
		if(lower)
			s = s.toLowerCase();
		String S = s.toUpperCase();
		int i = 0;
		for (; i < 94; ++i) {  // append a character to the string, e.g., string9;
			guesses[i] = s + (char)('!' + i);
		}
		for (; i < 188; ++i) { // prepend a character to the string, e.g., @string;
			guesses[i] = (char)('!' + i - 94) + s;
		}
		guesses[i++] = s.substring(1); //delete the first character from the string, e.g., tring;
		guesses[i++] = s.substring(0,len - 1); //delete the last character from the string, e.g., strin;
		String rev = new StringBuilder(s).reverse().toString();
		guesses[i++] = rev; //reverse the string, e.g., gnirts;
		guesses[i++] = s+s; //duplicate the string, e.g., stringstring;
		guesses[i++] = s + rev; //reflect the string, e.g., stringgnirts or gnirtsstring;
		guesses[i++] = rev + s;

		guesses[i++] = s; //lowercase the string, e.g., string;
		guesses[i++] = S; //uppercase the string, e.g., STRING;
		guesses[i++] = Character.toUpperCase(s.charAt(0)) + s.substring(1); //capitalize the string, e.g., String;
		guesses[i++] = s.charAt(0) +s.substring(1).toUpperCase(); //ncapitalize the string, e.g., sTRING;
		guesses[i++] = toggle(s, len); //toggle case of the string, e.g., StRiNg or sTrInG;
		guesses[i++] = toggle(S, len);

		// if(s.equals("preset")) {
		// 	System.out.println("finding preset");
		// 	for(int q = 0; q < 200; q++)
		// 		System.out.println(guesses[q]);
		// }
		return guesses;
	}

	// Test function
	static void print_hash(Map hash) {
		Iterator it = hash.entrySet().iterator();
		System.out.print("\n**************\nElement: \n");
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry)it.next();
			System.out.println(pair.getKey() + " = " + pair.getValue());
		}
	
	}

	static Map parse_users(String line) {
		Map token_map = new HashMap();
		String[] tokens = line.split(":");

		String[] name = tokens[4].split(" ");

		token_map.put("username",tokens[0]);
		token_map.put("passwd",tokens[1]);
		token_map.put("userid",tokens[2]);
		token_map.put("fname",name[0]);
		token_map.put("lname",name[1]);
		token_map.put("solved_pw", null);
		
		return token_map;
	}


	static ArrayList<String> readFile(String fileName) {
		ArrayList<String> result = new ArrayList<String>();

		try {
			BufferedReader br = new BufferedReader(new FileReader (fileName));

			String line = "";
			while ((line = br.readLine()) != null && !line.isEmpty()) { 
				result.add(line);
			}
		}
		catch(IOException e) {
			System.out.println(e);
		}

		return result;
	}

	static void writeFile(ArrayList<String> message, String fileName) {
		try {
			File file;
			file = new File(fileName + ".cpy");
			if (!file.exists())
				file.createNewFile();
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			for (int i = 0; i < message.size(); ++i)
				bw.write(message.get(i) + "\n");
			bw.write("\n");
			bw.close();
		}
		catch (IOException e) {
			System.out.println(e);
		}
	}  
}
