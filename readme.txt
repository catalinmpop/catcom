Transferring data using sockets in Java. Two methods were used: Go Back N, Stop and Wait. Two text files were used: Small and Large to test different sizes. Testing was done to determine the speed of each algorithm. 


GO BACK N

COMMAND LINE:

javac Sender.java
javac Reciever.java
java Receiver <hostname> <rec port> <send port> <output file name>
java Sender <hostname> <send port> <rec port> <RN> <window size> <file to send name>

GO BACK N TESTS

WN SIZE: 10		RN = 0		RN = 10	RM = 100
FILE: small.txt
TIME (ms): 		225ms		222ms		221ms
FILE: large.txt
TIME (ms):		690ms		109147ms	11435ms
TIMEOUT: 200

WN SIZE: 40
FILE: small.txt
TIME (ms): 		221ms		22ms		220ms
FILE: large.txt
TIME (ms):		685ms		28022ms	11461ms
TIMEOUT: 200

WN SIZE: 80
FILE: small.txt
TIME (ms): 		223ms		223ms		221ms
FILE: large.txt
TIME (ms):		679ms		14481ms	11605ms
TIMEOUT: 200




STOP AND WAIT

COMMAND LINE:

javac Sender.java
javac Reciever.java
java Reciever <hostname> <rec port> <send port> <RN> <output file name>
java Sender <hostname> <send port> <rec port> <file to send name>


STOP AND WAIT TESTS

FILE: small.txt	RN = 0		RN = 10	RM = 100
TIME (ms): 		17ms		15ms		15ms
FILE: large.txt
TIME (ms):		295ms		13777ms	1534ms
TIMEOUT: 200

FILE: small.txt	RN = 0		RN = 10	RM = 100
TIME (ms): 		15ms		15ms		16ms
FILE: large.txt
TIME (ms):		8202ms	20398ms	2114ms
TIMEOUT: 300

