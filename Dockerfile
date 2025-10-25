# Multi-stage Dockerfile for E-Salle ENSAA
FROM maven:3.8.6-openjdk-11-slim AS build

# Set working directory
WORKDIR /app

# Copy pom.xml and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Runtime stage
FROM tomcat:9.0-jdk11-openjdk-slim

# Install PostgreSQL client for health checks
RUN apt-get update && apt-get install -y postgresql-client && rm -rf /var/lib/apt/lists/*

# Set environment variables
ENV CATALINA_HOME=/usr/local/tomcat
ENV PATH=$CATALINA_HOME/bin:$PATH
ENV JAVA_OPTS="-Xmx512m -Xms256m"

# Create tomcat user
RUN groupadd -r tomcat && useradd -r -g tomcat tomcat

# Copy the WAR file from build stage
COPY --from=build /app/target/e-salle-ensaa.war $CATALINA_HOME/webapps/ROOT.war

# Create logs directory
RUN mkdir -p $CATALINA_HOME/logs && chown -R tomcat:tomcat $CATALINA_HOME

# Copy custom server configuration
COPY docker/tomcat/server.xml $CATALINA_HOME/conf/server.xml
COPY docker/tomcat/logging.properties $CATALINA_HOME/conf/logging.properties

# Copy application properties
COPY docker/application.properties $CATALINA_HOME/conf/

# Create health check script
COPY docker/healthcheck.sh /usr/local/bin/healthcheck.sh
RUN chmod +x /usr/local/bin/healthcheck.sh

# Expose port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD /usr/local/bin/healthcheck.sh

# Switch to tomcat user
USER tomcat

# Start Tomcat
CMD ["catalina.sh", "run"]
