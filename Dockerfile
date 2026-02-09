# Multi-stage build for IIQ application
FROM tomcat:9.0-jdk17 AS builder

# Build arguments
ARG IIQ_VERSION
ARG BUILD_DATE
ARG VCS_REF

# Labels
LABEL org.opencontainers.image.title="IdentityIQ Application"
LABEL org.opencontainers.image.description="SailPoint IdentityIQ Docker Image"
LABEL org.opencontainers.image.version="${IIQ_VERSION}"
LABEL org.opencontainers.image.created="${BUILD_DATE}"
LABEL org.opencontainers.image.revision="${VCS_REF}"

# Copy built WAR file from Maven build
COPY iiq-distribution/target/*.war /tmp/identityiq.war

# Extract WAR to webapps
RUN mkdir -p /usr/local/tomcat/webapps/identityiq && \
    cd /usr/local/tomcat/webapps/identityiq && \
    jar -xf /tmp/identityiq.war && \
    rm /tmp/identityiq.war

# Final stage
FROM tomcat:9.0-jdk17

# Install required tools
RUN apt-get update && \
    apt-get install -y --no-install-recommends \
    curl \
    vim \
    && rm -rf /var/lib/apt/lists/*

# Copy IIQ application from builder
COPY --from=builder /usr/local/tomcat/webapps/identityiq /usr/local/tomcat/webapps/identityiq

# Remove default Tomcat webapps
RUN rm -rf /usr/local/tomcat/webapps/ROOT \
           /usr/local/tomcat/webapps/docs \
           /usr/local/tomcat/webapps/examples \
           /usr/local/tomcat/webapps/host-manager \
           /usr/local/tomcat/webapps/manager

# Create IIQ directories
RUN mkdir -p /opt/identityiq/config \
             /opt/identityiq/logs \
             /opt/identityiq/uploads

# Set environment variables
ENV CATALINA_OPTS="-Xmx2048m -Xms1024m -XX:+UseG1GC"
ENV JAVA_OPTS="-Diiq.home=/opt/identityiq"

# Expose Tomcat port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:8080/identityiq/health.jsf || exit 1

# Start Tomcat
CMD ["catalina.sh", "run"]
