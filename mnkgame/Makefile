JC = javac
JR = java

RM = rm

OPTIONS = -cp

MNK = 6 5 4

run:
	$(JR) $(OPTIONS) ".." mnkgame.MNKGame $(MNK) mnkgame.TryHeuristicPlayer

build: 
	$(JC) $(OPTIONS) ".." *.java

clean:
	$(RM) -rf *.class


