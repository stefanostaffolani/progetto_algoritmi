JC = javac
JR = java

RM = rm

OPTIONS = -cp

MNK = 3 3 3

build: 
	$(JC) $(OPTIONS) ".." *.java

clean:
	$(RM) -rf *.class

run:
	$(JR) $(OPTIONS) ".." mnkgame.MNKGame $(MNK) mnkgame.BrunoPlayer


