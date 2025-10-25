#!/bin/bash

# Health check script for E-Salle application
set -e

# Check if Tomcat is running
if ! pgrep -f "catalina" > /dev/null; then
    echo "Tomcat is not running"
    exit 1
fi

# Check if the application is responding
if ! curl -f -s http://localhost:8080/ > /dev/null; then
    echo "Application is not responding"
    exit 1
fi

# Check database connectivity (if PostgreSQL client is available)
if command -v psql > /dev/null; then
    if ! psql -h postgres -U postgres -d esalle_ensaa -c "SELECT 1;" > /dev/null 2>&1; then
        echo "Database is not accessible"
        exit 1
    fi
fi

echo "Application is healthy"
exit 0
