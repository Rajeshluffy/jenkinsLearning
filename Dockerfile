cat > Dockerfile << 'EOF'
FROM maven:3.9-eclipse-temurin-17
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
COPY testng.xml .
CMD ["mvn", "clean", "test", "-B"]
EOF