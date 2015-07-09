
1test:
	javac *.java
	java PasswordCrack dict1 test1
2test:
	javac *.java
	java PasswordCrack dict1 test2
3test:
	javac *.java
	java PasswordCrack smaller_dictionary test1  
4test:
	javac *.java
	time java PasswordCrack orig_dict test2 > test4.out &
clean:
	rm -f *.class
	rm -f *.enc
	rm -f *.dec
