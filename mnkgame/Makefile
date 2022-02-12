JC = javac
JR = java

RM = rm

OPTIONS = -cp

MNK = 6 6 4

build: 
	$(JC) $(OPTIONS) ".." *.java

clean:
	$(RM) -rf *.class

run:
	$(JR) $(OPTIONS) ".." mnkgame.MNKGame $(MNK) mnkgame.BrunoPlayer


