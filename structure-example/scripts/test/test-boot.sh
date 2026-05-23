#!/bin/bash

# Boot Example Test Script
# Tests basic boot functionality

RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(cd "$SCRIPT_DIR/../.." && pwd)"
JAVA_HOME="${JAVA_HOME:-/Users/chuck/.sdkman/candidates/java/17.0.12-oracle}"
MODULE_NAME="structure-boot-example"
APP_PORT=16001
LOG_DIR="$SCRIPT_DIR/logs"
JAR_FILE="$PROJECT_ROOT/$MODULE_NAME/target/${MODULE_NAME}-1.0.1.jar"

print_info() { echo -e "${BLUE}[INFO]${NC} $1"; }
print_success() { echo -e "${GREEN}[SUCCESS]${NC} $1"; }
print_warning() { echo -e "${YELLOW}[WARNING]${NC} $1"; }
print_error() { echo -e "${RED}[ERROR]${NC} $1"; }

check_java() {
    if [ -d "$JAVA_HOME" ]; then
        export JAVA_HOME
        export PATH="$JAVA_HOME/bin:$PATH"
        return 0
    fi
    print_error "Java 17 not found"
    return 1
}

check_jar() {
    if [ -f "$JAR_FILE" ]; then
        return 0
    else
        print_error "JAR file not found: $JAR_FILE"
        print_info "Please run './build-examples.sh boot' first"
        return 1
    fi
}

start_app() {
    print_info "Starting Boot Example..."
    
    if lsof -i:$APP_PORT >/dev/null 2>&1; then
        print_warning "Application is already running on port $APP_PORT"
        return 0
    fi
    
    cd "$PROJECT_ROOT/$MODULE_NAME"
    nohup java -jar "$JAR_FILE" > "$LOG_DIR/boot-example.log" 2>&1 &
    APP_PID=$!
    
    print_info "Application started with PID: $APP_PID"
    print_info "Waiting for startup (10 seconds)..."
    sleep 10
    
    if curl -s http://localhost:$APP_PORT/test >/dev/null 2>&1; then
        print_success "Boot Example started successfully on port $APP_PORT"
        return 0
    else
        print_error "Failed to start application"
        print_error "Check log: $LOG_DIR/boot-example.log"
        return 1
    fi
}

stop_app() {
    print_info "Stopping Boot Example..."
    
    PID=$(lsof -t -i:$APP_PORT)
    if [ -n "$PID" ]; then
        kill $PID
        sleep 2
        print_success "Application stopped"
    else
        print_warning "Application not running"
    fi
}

test_basic_api() {
    print_info "Testing basic API..."
    
    # Test 1: Health check
    print_info "Test 1: Health check endpoint"
    RESPONSE=$(curl -s http://localhost:$APP_PORT/test)
    if echo "$RESPONSE" | grep -q "ok"; then
        print_success "✓ Health check successful: $RESPONSE"
    else
        print_error "✗ Health check failed"
        return 1
    fi
    
    return 0
}

run_all_tests() {
    print_info "========================================="
    print_info "  Boot Example - All Tests"
    print_info "========================================="
    
    if ! check_java; then
        exit 1
    fi
    
    if ! check_jar; then
        exit 1
    fi
    
    if ! start_app; then
        exit 1
    fi
    
    test_basic_api
    test_result=$?
    
    echo ""
    read -p "Do you want to stop the application? [Y/n]: " -n 1 -r
    echo
    if [[ $REPLY =~ ^[Yy]$ ]] || [ -z $REPLY ]; then
        stop_app
    fi
    
    if [ $test_result -eq 0 ]; then
        print_success "All tests passed!"
        return 0
    else
        print_error "Some tests failed"
        return 1
    fi
}

case "$1" in
    start)
        check_java && check_jar && start_app
        ;;
    stop)
        stop_app
        ;;
    test)
        run_all_tests
        ;;
    *)
        echo "Usage: $0 {start|stop|test}"
        exit 1
        ;;
esac
