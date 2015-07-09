
1test:
	javac *.java
	java PasswordCrack dictionary users1
2test:
	javac *.java
	java PasswordCrack dictionary users2
clean:
	rm -f *.class
	rm -f *.enc
	rm -f *.dec
