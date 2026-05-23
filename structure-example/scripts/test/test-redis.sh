#!/bin/bash

# Redis Example Test Script
# Tests Redis functionality including basic operations and distributed locks

RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(cd "$SCRIPT_DIR/../.." && pwd)"
JAVA_HOME="${JAVA_HOME:-/Users/chuck/.sdkman/candidates/java/17.0.12-oracle}"
MODULE_NAME="structure-redis-example"
APP_PORT=16007
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
        print_info "Please run './build-examples.sh redis' first"
        return 1
    fi
}

start_redis_example() {
    print_info "Starting Redis Example Application..."
    
    # Check if already running
    if lsof -i:$APP_PORT >/dev/null 2>&1; then
        print_warning "Application is already running on port $APP_PORT"
        return 0
    fi
    
    cd "$PROJECT_ROOT/$MODULE_NAME"
    
    nohup java -jar "$JAR_FILE" > "$LOG_DIR/redis-example.log" 2>&1 &
    APP_PID=$!
    
    print_info "Application started with PID: $APP_PID"
    print_info "Waiting for startup (10 seconds)..."
    sleep 10
    
    # Check if started successfully
    if curl -s http://localhost:$APP_PORT/redis/getKey?key=test >/dev/null 2>&1; then
        print_success "Redis Example started successfully on port $APP_PORT"
        return 0
    else
        print_error "Failed to start Redis Example"
        print_error "Check log: $LOG_DIR/redis-example.log"
        return 1
    fi
}

stop_redis_example() {
    print_info "Stopping Redis Example..."
    
    PID=$(lsof -t -i:$APP_PORT)
    if [ -n "$PID" ]; then
        kill $PID
        sleep 2
        print_success "Redis Example stopped"
    else
        print_warning "Redis Example not running"
    fi
}

test_redis_operations() {
    print_info "Testing Redis operations..."
    
    # Test 1: Set and get key-value
    print_info "Test 1: Set and get key-value"
    RESPONSE=$(curl -s "http://localhost:$APP_PORT/redis/getKey?key=testkey")
    if echo "$RESPONSE" | grep -q "testkey"; then
        print_success "✓ Redis set/get operation successful: $RESPONSE"
    else
        print_error "✗ Redis set/get operation failed"
        return 1
    fi
    
    # Test 2: Multiple operations
    print_info "Test 2: Multiple key-value operations"
    for i in {1..3}; do
        curl -s "http://localhost:$APP_PORT/redis/getKey?key=key$i" > /dev/null
        if [ $? -eq 0 ]; then
            print_success "✓ Operation $i successful"
        else
            print_error "✗ Operation $i failed"
        fi
    done
    
    return 0
}

test_redis_lock() {
    print_info "Testing Redis distributed lock..."
    
    # Test 1: Simple lock
    print_info "Test 1: Simple distributed lock"
    RESPONSE=$(curl -s "http://localhost:$APP_PORT/redis/redisLock?key=lock1")
    if echo "$RESPONSE" | grep -q "lock1"; then
        print_success "✓ Simple lock acquired: $RESPONSE"
    else
        print_warning "Lock test response: $RESPONSE"
    fi
    
    # Test 2: Lock with custom object
    print_info "Test 2: Lock with custom object"
    RESPONSE=$(curl -s "http://localhost:$APP_PORT/redis/redisLock1?key=lock2")
    if echo "$RESPONSE" | grep -q "lock2"; then
        print_success "✓ Object lock acquired: $RESPONSE"
    else
        print_warning "Object lock test response: $RESPONSE"
    fi
    
    return 0
}

run_all_tests() {
    print_info "========================================="
    print_info "  Redis Example - All Tests"
    print_info "========================================="
    
    if ! check_java; then
        exit 1
    fi
    
    if ! check_jar; then
        exit 1
    fi
    
    # Start application
    if ! start_redis_example; then
        exit 1
    fi
    
    # Run tests
    test_redis_operations
    test_result1=$?
    
    test_redis_lock
    test_result2=$?
    
    # Stop application
    echo ""
    read -p "Do you want to stop the application? [Y/n]: " -n 1 -r
    echo
    if [[ $REPLY =~ ^[Yy]$ ]] || [ -z $REPLY ]; then
        stop_redis_example
    fi
    
    # Summary
    echo ""
    print_info "========================================="
    if [ $test_result1 -eq 0 ] && [ $test_result2 -eq 0 ]; then
        print_success "All tests passed!"
        return 0
    else
        print_error "Some tests failed"
        return 1
    fi
}

# Parse command line arguments
case "$1" in
    start)
        check_java && check_jar && start_redis_example
        ;;
    stop)
        stop_redis_example
        ;;
    test)
        run_all_tests
        ;;
    *)
        echo "Usage: $0 {start|stop|test}"
        echo ""
        echo "Commands:"
        echo "  start - Start Redis Example application"
        echo "  stop  - Stop Redis Example application"
        echo "  test  - Run all tests (start app, test, stop app)"
        echo ""
        echo "Examples:"
        echo "  $0 start      # Start the application"
        echo "  $0 test      # Run all tests"
        exit 1
        ;;
esac
