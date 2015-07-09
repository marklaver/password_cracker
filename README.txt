UTEID: mcl267; mw23845;
FIRSTNAME: Mark; Marguerite;
LASTNAME: Laver; West-Driga;
CSACCOUNT: mcl267; mwestdri;
EMAIL: crusherven@yahoo.com; mwestdri@gmail.com;

[Program 5]
[Description]
There are 2 java files: jcrypt.java is the provided file from the assignment webpage.  We use it only to call the jcrypt.crypt() function.  The main method is in the PasswordCrack.java file.  We read in the dictionary and the passwd file as arraylists, then parse and store the user information into a nested hashmap.  Then we iterate through each user, trying 200 different possibilities for each word.  The words are derived from the username, first name, last name, and provided dictionary.  If the password is not found, we take the cached guesses from the first pass and mangle each of them further.

To compile our program:
javac *.java
java PasswordCrack dict1 test1

[Finish]
We completely finished this assignment.

[Test Cases]
[Input of test 1]
https://www.cs.utexas.edu/~byoung/cs361/passwd1

[Output of test 1]
We can crack 18 cases.
List of cracked: 
liagiba
michael
Salizar
abort6
amazing
eeffoc
rdoctor
doorrood
enoggone
Impact
keyskeys
sATCHEL
squadro
THIRTY
icious
obliqu3
teserP
sHREWDq


We did not crack 4.
List of uncracked:
litpeR
hI6d$pC2

[Input of test 2]
https://www.cs.utexas.edu/~byoung/cs361/passwd2

[Output of test 2]
nosral
cOnNeLlY
^bribed
dIAMETER
enchant$
eltneg
INDIGNITY
Saxon
tremors
ellows
zoossooz
soozzoos
Lacque
Swine3
uPLIFTr

We did not crack 5.
e4EyEMhNzYLJU
w@FxBZv.d0y/U
KenK1CTDGr/7k
!cSaQELm/EBV.
T8U5jQaDVv/o2