javac -classpath "akka/*" src/*.java
# jar cmvf META-INF/MANIFEST.MF TSAAirportScreening.jar src/*.class

cd src
java -classpath ".;../akka/*" Main