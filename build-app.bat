cd frontend
call npm run lint
call npm run test
call npm run build
cd ..
call mvn clean package