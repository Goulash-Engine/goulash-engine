java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005 \
 -jar ./build/libs/prosper-engine-0.1-all.jar
