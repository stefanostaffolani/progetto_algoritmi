JC = javac
JR = java

RM = rm

OPTIONS = -cp

MNK = 15 15 5

run:
	$(JR) $(OPTIONS) ".." mnkgame.MNKGame $(MNK) mnkgame.TryHeuristicPlayer

build: 
	$(JC) $(OPTIONS) ".." *.java

clean:
	$(RM) -rf *.class


