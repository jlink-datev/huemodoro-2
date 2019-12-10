IF "%1" equ "" (
java -jar web/target/web-1.0-SNAPSHOT.jar --server.port=0
) ELSE (
java -jar web/target/web-1.0-SNAPSHOT.jar --server.port=%1
)
